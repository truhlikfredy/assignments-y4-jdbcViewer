package antonkrug.eu;

/**
 * Common interface for all databases implementations
 * 
 * @author   Anton Krug
 * @date     26.9.2016
 * @version  1
 */

public interface DataAccess {
	
	public Pair<Boolean,String> addEmployee();
	
	public Pair<Boolean,String> connect();
	
	public void firstEmployee();
	
	public Employee getCurrentEmployee();
	
	public Integer getCurrentPossition();
	
	public Pair<Boolean, String> getEmployees();
	
	public Integer getSize();
	
	public void lastEmployee();
	
	public void nextEmployee();
	
	public void previousEmployee();
	
	public void removeEmployee(Integer Ssn);
	

}
