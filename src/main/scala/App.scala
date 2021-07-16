import java.sql.DriverManager
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLTimeoutException

object App {

  var connection:Connection = null

  def main(args: Array[String]): Unit = {
    //Test Database connection
    val connection_status = test_mysql_connection()
    if(connection_status == 1) {
      //Continue with program
      print(Console.YELLOW)
      print("Program Running...")
      print(Console.RESET + "\n")
      //Close the connection
      connection.close()
    }
    
    print(Console.BLUE)
    print("Closing program")
    print(Console.RESET + "\n")
  }

  def test_mysql_connection(): Int = {

    try {// connect to the database named "demodatabase" on the localhost
      
      val driver = "com.mysql.cj.jdbc.Driver"
      // make the connection
      Class.forName(driver).newInstance()
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demodatabase?" + "user=root&password=Ro93Jo98@")

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT response FROM connection_test")

      while ( resultSet.next() ) {
        val status = resultSet.getString("response")
        print(Console.GREEN)
        print("Connection status = " + status)
        print(Console.RESET + "\n")
      }

      //Return a status code of 1 for successful connection
      return 1;

    } 
    catch {
      case a: ClassNotFoundException => a.printStackTrace(); print_connection_failure_message(); return 0;
      case b: SQLException => b.printStackTrace(); print_connection_failure_message(); return 0;
      case c: SQLTimeoutException => c.printStackTrace(); print_connection_failure_message(); return 0;
    } 
    finally {
      // Scala wants return statement within catch block
    }
    
  }// End of test_mysql_connection

  def print_connection_failure_message(): Unit = {
    print(Console.RED)
    print("Connection status = Failure")
    print(Console.RESET + "\n")
  }// End of print_connection_failure_message


}// EOF