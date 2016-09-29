package antonkrug.eu;

import io.bloco.faker.Faker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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

  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
  
	private Integer ssn;
	private String  name;
	private String  address;
	private Integer salary;
	private String  sex;
	private Date    dob;
	
	
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
      Date dob) {

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
    this.dob     = new Date();
  }  

  
  /**
   * Will populate employee fields with random values
   */
  public static Employee getRandomizedEmployee() {
    Faker faker = new Faker();

    LocalDate bod = LocalDate.of(1946+(int)(Math.random()*52), // age 18-70 years old 
        1+(int)(Math.random()*11),    // any month 1-12
        1+(int)(Math.random()*27));   // only in safe days 1-28
       
    Employee employee = new Employee();
    employee.setAdress( faker.address.streetAddress() );
    employee.setDob(    Date.from(bod.atStartOfDay(ZoneId.systemDefault()).toInstant())); //age 18-70
    employee.setName(   faker.name.name()             );
    employee.setSalary( 190+(int)(Math.random() * 750));  //give it salary between 190 and 940
    employee.setSex(    Math.random()                 );  //randomly distribute genders
    
    return employee;
  }
  
  
  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public Integer getSsn() {
    return ssn;
  }


  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public void setSsn(Integer ssn) {
    this.ssn = ssn;
  }


  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public String getName() {
    return name;
  }


  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public String getAddress() {
    return address;
  }


  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public void setAdress(String address) {
    this.address = address;
  }


  /**
   * These are generic getters and setters for the fields
   * @return
   */
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


  /**
   * Returned gender should be M or F
   * @return
   */
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

  
  /**
   * Set gender based from floating point (good with randomizers)
   * @return
   */  
  public void setSex(double sex) {
    setSex((sex>0.5)?"M":"F");
  }
  
  
  /**
   * Formated birthday
   * @return
   */
  public String getDobString() {
    return sdf.format(dob);
  }

  
  /**
   * These are generic getters and setters for the fields
   * @return
   */
  public Date getDobDate() {
    return dob;
  }
 
  
  /**
   * Will format date object into a string
   * 
   * @param date
   */
  public void setDob(Date date) {
    this.dob = date;
  }


  /**
   * Full toString each field
   * @return
   */
  @Override
  public String toString() {
    return "Employee [ssn=" + ssn + ", name=" + name + ", address=" + address + ", salary="
        + salary + ", sex=" + sex + ", dob=" + dob + "]";
  }

  
  /**
   * Shorter toString which returns more human readable strings and less fields.
   * @return
   */
  public String toStringLite() {
    return "Employee " + name + ", address=" + address + ", salary="
        + salary + ", sex=" + sex + ", dob=" + getDobString() + ".";
  }
  
  
}
