package antonkrug.eu;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

  private static final long serialVersionUID = 1630825216656378020L;
  
  private JLabel                countLabel;
  private JFrame                frame;
  private JPanel                fields;
  private DataAccessJdbc        dao;
  private Map<String, Runnable> actions;

  
  public JdbcViewer() {
    setupUi();
    connectToDb();
  }
  
  
  private void minimizePanelToFit(JPanel panel) {
    
    //find the biggest container from whole panel
    int max = 0;
    for (int i=0; i< panel.getComponents().length; i++) {
      if (max < panel.getComponent(i).getPreferredSize().width) {
        max = panel.getComponent(i).getPreferredSize().width;
      }
    }
    
    //and set it to whole panel
    for (int i=0; i< panel.getComponents().length; i++) {
//      panel.getComponent(i).setSize(max, panel.getComponent(i).getHeight());
      panel.getComponent(i).setBounds(10, 10, max, 10);
    }
    
    panel.setSize(max, panel.getHeight());
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
    minimizePanelToFit(labels);
    labels.setBounds(10,10,20,20);
    
    //Populate fields
    fields = new JPanel(new GridLayout(6, 1));
    for (int i=0;i<6;i++) {
      fields.add(new JTextField(""));
      System.out.println(labels.getComponent(i).getPreferredSize());
    }

    //Populate buttons    
    JPanel buttons = new JPanel(new GridLayout(4, 1));
    JPanel prevnext = new JPanel(new GridLayout(1,4));

    countLabel = new JLabel("-/-", SwingConstants.CENTER);  //Counter label
    
    //add buttons and map a runnable to them
    buttons.add(getButton("Delete",    this::actionDelete));
    buttons.add(getButton("Add random",this::actionAdd));
    buttons.add(countLabel);
    
    prevnext.add(getButton("F",       this::actionFirst));
    prevnext.add(getButton("P",        this::actionPrevious));
    prevnext.add(getButton("N",        this::actionNext));
    prevnext.add(getButton("L",       this::actionLast));
    
    buttons.add(prevnext);   

    //Layout the text labels, fields and buttons
    
    JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
    bottomPanel.add(labels);
    bottomPanel.add(fields);
    bottomPanel.add(buttons);
    
    
    JPanel layout = new JPanel(new GridLayout(2, 1));
    layout.add(new JLabel(new ImageIcon(getClass().getResource("/resources/logo.jpg"))));
    layout.add(bottomPanel);

    //Auto - pack paddings and show window
    frame.add(layout);
    frame.setVisible(true);
    frame.pack();
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
    dao.lastEmployee();
    populateEmployeeFields();
  }
  
  
  /**
   * Will construct button object, attach event listener to it and map runnable as onClick handler
   * 
   * @param label
   * @return
   */
  private JButton getButton(String label, Runnable actionPerformed) {
    Icon icon = new ImageIcon(getClass().getResource("/resources/icon_clock.png"));
    JButton ret = new JButton(label,icon);
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
    
    countLabel.setText(dao.getCurrentPossition()+"/"+dao.getSize());
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
