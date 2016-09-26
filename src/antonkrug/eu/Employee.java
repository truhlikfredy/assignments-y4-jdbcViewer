package antonkrug.eu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple class holding information about one employee
 * 
 * @author   Anton Krug
 * @date     26.9.2016
 * @version  1
 *
 */
public class Employee {
	
	private Integer ssn;
	private String  name;
	private String  address;
	private Integer salary;
	private String  sex;
	private String  dob;
	
	
	/**
	 * Will populate employee with the strings given
	 * 
	 * @param ssn
	 * @param name
	 * @param address
	 * @param salary
	 * @param sex
	 * @param dob
	 */
  public Employee(Integer ssn, String name, String address, Integer salary, String sex,
      String dob) {

    this.ssn     = ssn;
    this.name    = name;
    this.address = address;
    this.salary  = salary;
    this.sex     = sex;
    this.dob     = dob;
  }

  
  /**
   * Will populate the employee from the given ResultSet
   * 
   * @param result
   */
  public Employee(ResultSet result) {    
    try {
      this.ssn     = result.getInt("Ssn");
      this.name    = result.getString("Name");
      this.address = result.getString("Address");
      this.salary  = result.getInt("Salary");;
      this.sex     = result.getString("Sex");
      
      setDob(result.getDate("Bdate"));
      
    } catch (SQLException e) {
      System.out.println(Messages.getString("ERROR_FIELDS"));
      //e.printStackTrace();
    }
  }

  
  /**
   * Will create employee with empty / default fields
   */
  public Employee() {
    this.ssn     = 0;
    this.name    = Messages.getString("NO_EMPLOYEE");
    this.address = Messages.getString("N/A");
    this.salary  = 0;
    this.sex     = Messages.getString("N/A");
    this.dob     = Messages.getString("N/A");
  }  
  
  
  public Integer getSsn() {
    return ssn;
  }


  public void setSsn(Integer ssn) {
    this.ssn = ssn;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getAddress() {
    return address;
  }


  public void setAdress(String address) {
    this.address = address;
  }


  public Integer getSalary() {
    return salary;
  }


  /**
   * Will accept only positive or zero salary
   * 
   * @param salary
   */
  public void setSalary(Integer salary) {
    if (salary >= 0) {
      this.salary = salary;
    }
  }


  public String getSex() {
    return sex.toUpperCase();
  }


  /**
   * Only M or F can be set as sex
   * 
   * @param sex
   */
  public void setSex(String sex) {
    sex = sex.toUpperCase();
    if (sex.equals("M") || sex.equals("F")) {
      this.sex = sex;
    }
  }


  public String getDob() {
    return dob;
  }


  public void setDob(String dob) {    
    this.dob = dob;
  }
  
  
  /**
   * Will format date object into a string
   * 
   * @param date
   */
  public void setDob(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    this.dob = sdf.format(new Date());
  }

  
}
