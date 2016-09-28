package antonkrug.eu;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 650x180
 * 
 * @author Anton Krug
 * @date 26.9.2016
 * @version 1
 */
public class JdbcViewer extends JFrame implements ActionListener {

  private static final long     serialVersionUID = 1630825216656378020L;
  
  private static final boolean  SHOW_IMAGES      = true;
  
  private static final int      APP_WIDTH        = 650; 
  private static final int      APP_HEIGHT       = 600;

  private static final int      LABELS_WIDTH     = 120; 
  private static final int      LABELS_HEIGHT    = 220;
  
  private static final int      FIELDS_WIDTH     = 250; 
  private static final int      FIELDS_HEIGHT    = LABELS_HEIGHT;
    
  private JLabel                countLabel;
  private JFrame                frame;
  private JPanel                fields;
  private DataAccessJdbc        dao;
  private Map<String, Runnable> actions;

  
  public JdbcViewer() {
    setupUi();
    connectToDb();
  }
  
   
  /**
   * Will setup all the UI elements and layout
   */
  private void setupUi() {
    frame = new JFrame();
    actions = new HashMap<>();

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
      field.setEditable(false);
      fields.add(field);
    }
    fields.setPreferredSize(new Dimension(FIELDS_WIDTH, FIELDS_HEIGHT));
    

    //Populate buttons    
    JPanel buttons = new JPanel(new GridLayout(5, 1));
    JPanel prevnext = new JPanel(new GridLayout(1,4));

    countLabel = new JLabel("-", SwingConstants.CENTER);  //Counter label
    
    //add buttons and map a runnable to them
    buttons.add(new JLabel("Employee record: ", SwingConstants.CENTER));
    buttons.add(countLabel);
    buttons.add(getButton("Delete",     this::actionDelete,   "delete"));
    buttons.add(getButton("Add random", this::actionAdd,      "add"));
    
    prevnext.add(getButton("First",     this::actionFirst,    "first"));
    prevnext.add(getButton("Previous",  this::actionPrevious, "previous"));
    prevnext.add(getButton("Next",      this::actionNext,     "next"));
    prevnext.add(getButton("Last",      this::actionLast,     "last"));
    
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
  }
  
  
  /**
   * Will delete employee, read all employees back and display first one
   */
  private void actionDelete() {
    dao.addEmployee();
    dao.getEmployees();
    actionFirst();
  }
  
  
  /**
   * Will add employee, read all employees back and display last one
   */
  private void actionAdd() {
    dao.addEmployee();
    dao.getEmployees();
    actionLast();
  }
  
  
  private void actionFirst() {
    dao.firstEmployee();
    populateEmployeeFields();    
  }
  
  
  private void actionPrevious() {
    dao.previousEmployee();
    populateEmployeeFields();        
  }
  
  
  private void actionNext() {
    dao.nextEmployee();
    populateEmployeeFields();
  }
  
  
  private void actionLast() {
//    dao.lastEmployee();
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
   * Will connect to database
   */
  private void connectToDb() {
    dao = new DataAccessJdbc();
    dao.connect();
    dao.getEmployees();
    
    //get the first record displayed in the UI
    populateEmployeeFields();
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
   * Update in the UI one text field
   * 
   * @param possition
   * @param value
   */
  private void setField(int possition, Integer value) {
    setField(possition, value.toString());
  }
  

  /**
   * Will fetch employee from current position and populate all the fields in the UI
   */
  private void populateEmployeeFields() {
    Employee currentEmployee = dao.getCurrentEmployee();
    
    setField(0, currentEmployee.getSsn()    );
    setField(1, currentEmployee.getName()   );
    setField(2, currentEmployee.getAddress());
    setField(3, currentEmployee.getSalary() );
    setField(4, currentEmployee.getSex()    );
    setField(5, currentEmployee.getDob()    );
    
    countLabel.setText(dao.getCurrentPossition() +" of "+ dao.getSize());
  }
  
  
  /**
   * Will trigger on all mouse events and will distinguish which event handler to call
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    
    if (actions.containsKey(action)) actions.get(action).run();
  }


  public static void main(String[] args) {
    JdbcViewer app = new JdbcViewer();
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  
}
