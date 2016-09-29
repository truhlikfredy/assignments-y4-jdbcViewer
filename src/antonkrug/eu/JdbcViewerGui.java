package antonkrug.eu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * The main UI and the app which is orchestrating all other classes and methods. 
 * Contains the main method and the main JFrame app. 
 * For more details read the README.md file
 * 
 * @author  Anton Krug
 * @date    26.9.2016
 * @version 1
 */
public class JdbcViewerGui extends JFrame implements ActionListener {

  private static final long     serialVersionUID = 1630825216656378020L;
  
  private static final boolean  SHOW_IMAGES      = true;
  
  private static final int      APP_WIDTH        = 650; 
  private static final int      APP_HEIGHT       = 600;

  private static final int      LABELS_WIDTH     = 120; 
  private static final int      LABELS_HEIGHT    = 220;
  
  private static final int      FIELDS_WIDTH     = 250; 
  private static final int      FIELDS_HEIGHT    = LABELS_HEIGHT;
    
  
  public static void main(String[] args) {
    JdbcViewerGui app = new JdbcViewerGui();
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //System.exit(0);
  }
  
  
  private JLabel                countLabel;
  private JPanel                buttons;
  private JFrame                frame;
  private JPanel                fields;
  private JPanel                prevnext;
  private DataAccess            dao;
  private Map<String, Runnable> actions;
  
   
  /**
   * Constructor will setup UI layout and DB connection.
   */
  public JdbcViewerGui() {
    setupUi();
    connectToDb();
  }
  
  
  /**
   * Will add employee, read all employees back and display last one
   */
  private void actionAdd() {
    Pair<Boolean, String> status = dao.addEmployeeRandom();
    
    JOptionPane.showMessageDialog(frame, status.getSecond());
    
    dao.getEmployees();
    actionLast();
  }
  
  /**
   * Creates new employee object from populated fields
   * 
   * @return
   */
  private Employee getEmployeeFromFields() {
    Employee ret = new Employee();
    
    try {
      ret.setSsn(      Integer.parseInt(getField(0)));
      ret.setName(     getField(1)                  );
      ret.setAddress(  getField(2)                  );
      ret.setSalary(   Integer.parseInt(getField(3)));
      ret.setSex(      getField(4)                  );  
      
      //test if the date was parsed correctly
      if (!ret.setDob(getField(5))) {
        throw new Exception("Wrong date format");
      }
      
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, Messages.getString("ERROR_FORM") + "\n"+ e.toString());
    }
    
    return ret;
  }

  
  /**
   * Will add employee, read all employees back and display last one
   */
  private void actionAddNew() {
    Employee employee = getEmployeeFromFields(); 
    
    Pair<Boolean, String> status = dao.addEmployee(employee);
    
    JOptionPane.showMessageDialog(frame, status.getSecond());
    
    dao.getEmployees();
    actionLast();
  }

  
  /**
   * Will delete employee, read all employees back and display first one
   */
  private void actionDelete() {
    dao.removeEmployee(dao.getCurrentEmployee().getSsn());
    dao.getEmployees();
    actionFirst();
  }
  
  /**
   * Event handler for "First" button
   */
  private void actionFirst() {
    dao.firstEmployee();
    populateEmployeeFields();    
  }
  
  
  /**
   * Event handler for "Last" button
   */
  private void actionLast() {
    dao.lastEmployee();
    populateEmployeeFields();
  }
  
  /**
   * Event handler for "Next" button
   */
  private void actionNext() {
    dao.nextEmployee();
    populateEmployeeFields();
  }
  
  
  /**
   * Will trigger on all mouse events and will distinguish which event handler to call
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    
    if (actions.containsKey(action)) actions.get(action).run();
  }
  
  /**
   * Event handler for "Previous" button
   */
  private void actionPrevious() {
    dao.previousEmployee();
    populateEmployeeFields();        
  }


  /**
   * Connect to JDBC database
   */
  private void connectToDb() {
    dao = new DataAccessJdbc();
    Pair<Boolean,String> ret = dao.connect();
    
    if (!ret.getFirst()) {
      System.err.println(ret.getSecond());
      JOptionPane.showMessageDialog(frame, ret.getSecond());
      
      System.exit(1);
    }
    
    dao.getEmployees();
   
    //get the first record displayed in the UI
    populateEmployeeFields();
  }
  
  
  /**
   * Will construct button object, attach event listener to it and map runnable as onClick handler
   * 
   * @param label
   * @return
   */
  private JButton getButton(String label, Runnable actionPerformed, String iconName) {
    Icon icon = new ImageIcon(getClass().getResource("/resources/icon_"+iconName+".png"));
    
    JButton ret;
    
    if (SHOW_IMAGES) {
      ret = new JButton(label,icon);
    }
    else {
      ret = new JButton(label);
    }
    
    ret.addActionListener(this);
    actions.put(label, actionPerformed);
    return ret;
  }
  
  
  /**
   * Disables or enables Next/Prev buttons, use when then there are no employees to iterate
   */
  private void buttonsAreEnabled(boolean areEnabled) {
    //change the first, prev, next and last buttons
    Arrays.asList(prevnext.getComponents()).stream().forEach((item)->item.setEnabled(areEnabled));
    
    //do same with delete button
    buttons.getComponent(2).setEnabled(areEnabled);
  }
  
  
  /**
   * Fetch employee from current position and populate all the fields in the UI
   */
  private void populateEmployeeFields() {
    Employee currentEmployee = dao.getCurrentEmployee();
    
    setField(0, currentEmployee.getSsn()      );
    setField(1, currentEmployee.getName()     );
    setField(2, currentEmployee.getAddress()  );
    setField(3, currentEmployee.getSalary()   );
    setField(4, currentEmployee.getSex()      );
    setField(5, currentEmployee.getDobString());
    
    //update the counter "1 of 9" etc...
    int count = dao.getSize();
    countLabel.setText(dao.getCurrentPossition() +" of "+ count);
    
    //if no employes then disable the Next / Prev buttons and enable them back when there are Employees
    buttonsAreEnabled( (count > 0) ? true : false );
  }
  

  /**
   * Update in the UI one text field
   * 
   * @param possition
   * @param value
   */
  private void setField(int possition, Integer value) {
    setField(possition, value.toString());
  }
  
  
  /**
   * Update in the UI one text field
   * 
   * @param possition
   * @param value
   */
  private void setField(int possition, String value) {
    Component  item = fields.getComponent(possition);
    JTextField text = (JTextField) item;
    text.setText(value);    
  }


  /**
   * Get back one field
   * 
   * @param possition
   */
  private String getField(int possition) {
    Component  item = fields.getComponent(possition);
    JTextField text = (JTextField) item;
    return text.getText();    
  }
  
  
  /**
   * Will setup all the UI elements and layout
   */
  private void setupUi() {
    frame = new JFrame();
    actions = new HashMap<>();
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Populate text labels
    JPanel labels = new JPanel(new GridLayout(6, 1));
    labels.add(new JLabel(Messages.getString("ID")));      //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("NAME")));    //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("ADDRESS"))); //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("SALARY")));  //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("SEX")));     //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("DOB")));     //$NON-NLS-1$
    //minimizePanelToFit(labels);
    //labels.setBounds(10,10,20,20);
    labels.setPreferredSize(new Dimension(LABELS_WIDTH,LABELS_HEIGHT));
    
    //Populate fields
    fields = new JPanel(new GridLayout(6, 1));
    for (int i=0;i<6;i++) {
      JTextField field = new JTextField();
      if (i==0) field.setEditable(false);
      fields.add(field);
    }
    fields.setPreferredSize(new Dimension(FIELDS_WIDTH, FIELDS_HEIGHT));
    

    //Populate buttons    
    buttons = new JPanel(new GridLayout(5, 1));
    prevnext = new JPanel(new GridLayout(1,4));

    countLabel = new JLabel("-", SwingConstants.CENTER);  //Counter label
    
    //add buttons and map a runnable to them
    buttons.add(new JLabel(Messages.getString("EMPLOYEE_COUNT"), SwingConstants.CENTER));
    buttons.add(countLabel);
    buttons.add(getButton(Messages.getString("DELETE"),      this::actionDelete,   "delete"));
    buttons.add(getButton(Messages.getString("ADD_RANDOM"),  this::actionAdd,      "add"));
    buttons.add(getButton(Messages.getString("ADD_CURRENT"), this::actionAddNew,   "add"));
    
    prevnext.add(getButton(Messages.getString("FIRST"),      this::actionFirst,    "first"));
    prevnext.add(getButton(Messages.getString("PREVIOUS"),   this::actionPrevious, "previous"));
    prevnext.add(getButton(Messages.getString("NEXT"),       this::actionNext,     "next"));
    prevnext.add(getButton(Messages.getString("LAST"),       this::actionLast,     "last"));
    
    //buttons.add(prevnext);   

    //Layout the text labels, fields and buttons
    
    JPanel bottomPanel = new JPanel(new FlowLayout());  
    bottomPanel.add(labels);
    bottomPanel.add(fields);
    bottomPanel.add(buttons);
    bottomPanel.add(prevnext);
    
    
    JPanel layout;
    if (SHOW_IMAGES) {
      layout = new JPanel(new GridLayout(2, 1));
      layout.add(new JLabel(new ImageIcon(getClass().getResource("/resources/logo.jpg"))));
    }
    else {
      layout = new JPanel(new GridLayout(1, 1));      
    }
    layout.add(bottomPanel);      

    //Auto - pack paddings and show window
    
    frame.add(layout);
    frame.pack();
    frame.setResizable(false); // keep the window in fixed resolution
    frame.setSize(new Dimension(APP_WIDTH, APP_HEIGHT));
    frame.setVisible(true);
    
    //shutdown everything after closing the window
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    
  }

  
}
