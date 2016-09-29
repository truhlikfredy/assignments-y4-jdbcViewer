package antonkrug.eu;

import java.sql.*;
import java.util.Properties;


/**
 * JDBC implementation of DAO
 * 
 * @author  Anton Krug
 * @date    26.9.2016
 * @version 1
 */
public class DataAccessJdbc implements DataAccess {

  // if set true will output on console more verbose information
  private static final boolean DEBUG = true;

  private Connection con         = null;
  private ResultSet  rs          = null;
  private Properties cfg         = null;
  private boolean    resultError = false;
//  private int        size        = 0;
  
  
  /**
   * Will load the config.properties settings
   */
  public DataAccessJdbc() {
    cfg = new Properties();
    try {
      cfg.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
      
    } catch (Exception e) {
      if (DEBUG) e.printStackTrace();
    }    
  }


  /**
   * Will connect to MySQL database with the credentials from config.properties
   */
  @Override
  public Pair<Boolean, String> connect() {

    // try to connect, for each error respond accordingly or return DB_CONNECTED
    // on success
    try {
      
      // use the config.properties file to set the database access credentials
      Class.forName(cfg.getProperty("driver"));
     
      //use the config.properties file for the configuration
      con = DriverManager.getConnection(
        cfg.getProperty("provider") +           // jdbc:mysql://
        cfg.getProperty("servername") + ":" +   // localhost
        cfg.getProperty("port") + "/" +         // 3306
        cfg.getProperty("database"),            // Assignment1
        cfg);                                   // provide username and password from configuration

    } catch (ClassNotFoundException e) {
      if (DEBUG) e.printStackTrace();
      return new Pair<Boolean, String>(false, Messages.getString("ERROR_CLASS"));

    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      return new Pair<Boolean, String>(false, Messages.getString("ERROR_CON"));
    }

    // everything went fine, return DB_CONNECTED
    if (DEBUG) System.out.println(Messages.getString("DB_CONNECTED") + ": " + con.toString());
    return new Pair<Boolean, String>(true, Messages.getString("DB_CONNECTED"));
  }

  
  /**
   * Will fetch all employees with SQL query
   */
  @Override
  public Pair<Boolean, String> getEmployees() {

    //if not connected to database return a error
    if (con == null) {
      if (DEBUG) System.err.println(Messages.getString("ERROR_CON"));
      return new Pair<Boolean, String>(false, Messages.getString("ERROR_CON"));
    }
    
    //the SQL query only if it's connected to DB
    try {
      Statement st = con.createStatement();
      rs = st.executeQuery("select * from Employee");

    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      return new Pair<Boolean, String>(false, Messages.getString("ERROR_CLASS"));
    }
    
    nextEmployee(); // jump to first employee
//    size = getSize();
    
    if (DEBUG) System.out.println(Messages.getString("SQL_OK") + ": " + rs.toString());
    return new Pair<Boolean, String>(true, Messages.getString("SQL_OK"));
  }

  
  /**
   * Will fetch employee from current possition
   */
  @Override
  public Employee getCurrentEmployee() {
    
    if (resultError) {
      //if there was problem with result set return default / empty employee
      return new Employee();
    }
    else {
      //if everything is ok return employee populated from resultset
      return new Employee(rs);
    }
  }


  /**
   * Get next record. If that fails, roll over to the first record, if that
   * fails as well set result error.
   */
  @Override
  public void nextEmployee() {
    try {
      if (!rs.next()) {
        resultError = !rs.first();
      }

    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      resultError = true;
    }
  }


  /**
   * Get previous record. If that fails, roll over to the last record, if that
   * fails as well set result error.
   */
  @Override
  public void previousEmployee() {
    try {
      if (!rs.previous()) {
        resultError = !rs.last();
      }
      
    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      resultError = true;
    }
  }

  
  @Override
  public void firstEmployee() {
    try {
      resultError = !rs.first();
      
    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      resultError = true;
    }
  }


  @Override
  public void lastEmployee() {
    try {
      resultError = !rs.last();
      
    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      resultError = true;
    }    
  }
  
  
  /**
   * Will create SQL query from given employee and save it to the database.
   * 
   * @param employee
   * @return
   */
  private Pair<Boolean,String> saveEmployee(Employee employee) {
    System.out.println(employee.toString());
    
  //convert util.Date to sql.Date
    java.sql.Date sqlDate = new java.sql.Date(employee.getDobDate().getTime()); 
    
    PreparedStatement s;
    int count;
    try {
      s = con.prepareStatement ("INSERT INTO Employee "+
                                "(Bdate, Name, Address, Salary, Sex, Works_For) "+
                                "VALUES(?,?,?,?,?,?)");
      
      s.setDate(1,   sqlDate              );
      s.setString(2, employee.getName()   );
      s.setString(3, employee.getAddress());
      s.setInt(4,    employee.getSalary() );
      s.setString(5, employee.getSex()    );
  
      //populating Works_For with any number, because it can't be null
      s.setInt(6,    1                    );  
      
      count = s.executeUpdate ();
      System.out.println(s);
      s.close ();
      
    } catch (SQLException e) {
      if (DEBUG) e.printStackTrace();
      return new Pair<Boolean, String>(false, Messages.getString("ERROR_SQL"+ e.toString()));
    }

    final String msg = count + " " + Messages.getString("ROWS_AFFECTED") + " " + employee.toStringLite();
    if (DEBUG) System.out.println(msg);
    return new Pair<Boolean, String>(true, msg);
  }


  @Override
  public Pair<Boolean,String> addEmployee() {
    
    return saveEmployee(Employee.getRandomizedEmployee());
  }


  @Override
  public void removeEmployee(Integer Ssn) {
    // TODO Auto-generated method stub

  }


  /**
   * Return current entry position, if there is any problem return 0
   */
  @Override
  public Integer getCurrentPossition() {
    if (rs == null) {
      return 0;
    }
    else {
      
      try {
        return rs.getRow();
        
      } catch (SQLException e) {
        if (DEBUG) e.printStackTrace();
        return 0;
      }
      
    }
  }


  /**
   * Return size of the result set, if there is any problem return 0
   */
  @Override
  public Integer getSize() {
    if (rs == null) {
      return 0;
    }
    else {
      
      try {
        int originalPosition = rs.getRow(); //save original position
        rs.last();                          //jump to last entry
        int lastPosition = rs.getRow();     //get position of the last entry
        rs.absolute(originalPosition);      //return cursor to original position before we started this measurement 
        return lastPosition;
        
      } catch (SQLException e) {
        if (DEBUG) e.printStackTrace();
        return 0;
      }
      
    }
  }

  
}
