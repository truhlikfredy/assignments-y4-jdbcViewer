package antonkrug.eu;

/**
 * Common interface for all databases implementations
 * 
 * @author   Anton Krug
 * @date     26.9.2016
 * @version  1
 */

public interface DataAccess {
	
	public Pair<Boolean,String> connect();
	
	public Pair<Boolean, String> getEmployees();
	
	public Employee getCurrentEmployee();
	
	public void nextEmployee();
	
	public void previousEmployee();
	
	public void addEmployee();
	
	public void removeEmployee(Integer Ssn);
	
	public Integer getCurrentPossition();
	
	public Integer getSize();
	

}
