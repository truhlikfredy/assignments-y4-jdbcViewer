package antonkrug.eu;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;

/**
 * 
 * 
 * @author Anton Krug
 * @date 26.9.2016
 * @version 1
 */
public class JdbcViewer extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1630825216656378020L;
  
  private JLabel         countLabel;
  private JFrame         frame;
  private JButton        button;
  private JPanel         fields;
  private DataAccessJdbc dao;

  
  public JdbcViewer() {
    setupUi();
    connectToDb();
  }

  
  private void setupUi() {
    frame = new JFrame();

    //Populate text labels
    JPanel labels = new JPanel(new GridLayout(6, 1));
    labels.add(new JLabel(Messages.getString("ID")));      //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("NAME")));    //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("ADDRESS"))); //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("SALARY")));  //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("SEX")));     //$NON-NLS-1$
    labels.add(new JLabel(Messages.getString("DOB")));     //$NON-NLS-1$
    
    //Populate fields
    fields = new JPanel(new GridLayout(6, 1));
    for (int i=0;i<6;i++) {
      fields.add(new JTextField(""));
    }

    //Populate buttons    
    JPanel buttons = new JPanel(new GridLayout(3, 1));
    buttons.add(new Button("Delete"));
    buttons.add(new Button("Add random"));
    
    JPanel prevnext = new JPanel(new GridLayout(1,5));
    prevnext.add(new Button("|<"));
    prevnext.add(new Button("<"));
    
    countLabel = new JLabel("-/-", SwingConstants.CENTER);  //Counter label
    prevnext.add(countLabel);
    
    prevnext.add(new Button(">"));
    prevnext.add(new Button(">|"));
    buttons.add(prevnext);
    

    //Layout the text labels, fields and buttons
    JPanel panel = new JPanel(new GridLayout(1, 3));
    panel.add(labels);
    panel.add(fields);
    panel.add(buttons);

    //Auto - pack paddings and show window
    frame.add(panel);
    frame.setVisible(true);
    frame.pack();
  }

  
  private void connectToDb() {
    dao = new DataAccessJdbc();
    dao.connect();
    dao.getEmployees();
    System.out.println(dao.getSize());
    populateFields();
  }
  
  
  private void setField(int possition, String value) {
    Component  item = fields.getComponent(possition);
    JTextField text = (JTextField) item;
    text.setText(value);    
  }

  
  private void setField(int possition, Integer value) {
    setField(possition, value.toString());
  }
  

  /**
   * Will populate all the fields in the UI
   */
  private void populateFields() {
    Employee currentEmployee = dao.getCurrentEmployee();
    
    setField(0, currentEmployee.getSsn());
    setField(1, currentEmployee.getName());
    setField(2, currentEmployee.getAddress());
    setField(3, currentEmployee.getSalary());
    setField(4, currentEmployee.getSex());
    setField(5, currentEmployee.getDob());
  }
  
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }


  public static void main(String[] args) {
    JdbcViewer app = new JdbcViewer();
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  
}
