package antonkrug.eu.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DataAccessJdbcTest {
  
  private void wipeDb() {
    
  }

  /**
   * Warning this will wipe the database and start clean one from scratch, be careful.
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    wipeDb();
  }


  @Test
  public void test() {
    fail("Not yet implemented");
  }

}
