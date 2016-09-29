package antonkrug.eu;

/**
 * Common interface for all persistent storage implementations
 * 
 * @author  Anton Krug
 * @date    26.9.2016
 * @version 1
 */

public interface DataAccess {

  public Pair<Boolean, String> addEmployee(Employee employee);

  
  public Pair<Boolean, String> addEmployeeRandom();

  
  public Pair<Boolean, String> connect();


  public Pair<Boolean, String> executeQuery(String query);


  public void firstEmployee();


  public Employee getCurrentEmployee();


  public Integer getCurrentPossition();


  public Pair<Boolean, String> getEmployees();


  public int getSize();


  public void lastEmployee();


  public void nextEmployee();


  public void previousEmployee();


  public Pair<Boolean, String> removeEmployee(Integer Ssn);

}
