import java.sql.DriverManager
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLTimeoutException
import java.sql.PreparedStatement
import java.io.FileReader
import java.io.BufferedReader
import java.io.FileNotFoundException
import scala.io.StdIn

object App {

  val username = "admin"
  val password = "password"

  var connection:Connection = null

  def main(args: Array[String]): Unit = {
    //This application is a demo so the data is loaded into the database before user input is allowed for the simulation.

    //Test Database connection
    val connection_status = test_mysql_connection()
    if(connection_status == 1) {
      //Continue with program

      //Load the data if empty
      //load_books_to_db()

      //Test connection with a simple query
      //queryForBooksTest()

      var finished = false

      LibraryPrompter.printLoginPrompt()
      do {
        SpecialPrint.cprint("username>: ", Console.CYAN)
        var input1 = StdIn.readLine()
        SpecialPrint.cprint("password>: ", Console.CYAN)
        var input2 = StdIn.readLine()
        if(input1 == username && password == input2) {
          finished = true
        }
        else {
          SpecialPrint.cprintln("Invalid Credentials. Try again.", Console.RED)
        }

      } while(!finished)

      finished = false

      LibraryPrompter.printWelcomeMessage()
      LibraryPrompter.printHelp()
      do {
        SpecialPrint.cprint(">: ", Console.CYAN)
        var input = StdIn.readLine()
        
        input match {
          case "help" => LibraryPrompter.printHelp()
          case "search" => searchMenu();
          case "quit" => finished = true
          case _ => SpecialPrint.cprintln("Unexpected command: Type \"help\" to relist possible commands", Console.YELLOW)
        }

      } while(!finished)

      //Close the connection
      connection.close()
    }
    SpecialPrint.cprintln("Closing program", Console.BLUE)
  }

  def searchMenu(): Unit = {
    LibraryPrompter.printSearchMenuPrompt()
    var finished = false
    do {
        SpecialPrint.cprint("search_attribute>: ", Console.MAGENTA)
        var input = StdIn.readLine()
        
        input match {
          case "title" => queryDB("title")
          case "author" => queryDB("author")
          case "genre" => queryDB("genre")
          case "publisher" => queryDB("publisher")
          case "help" => LibraryPrompter.printSearchMenuHelp()
          case "back" => finished = true
          case _ => SpecialPrint.cprintln("Unexpected command: Type \"help\" to relist possible commands", Console.YELLOW)
        }

      } while(!finished)

  }

  def queryDB(attr:String): Unit = {
    SpecialPrint.cprint(attr + ">: ", Console.MAGENTA)
    var input = StdIn.readLine()

    try {
      val sql = "SELECT * FROM books WHERE " + attr + " LIKE \"%" + input + "%\""

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(sql)

      if(resultSet.next() == false) {
        SpecialPrint.cprintln("No results found.", Console.YELLOW)
      }
      else {
        do {
          var title = resultSet.getString("title")
          var author = resultSet.getString("author")
          var genre = resultSet.getString("genre")
          var pages = resultSet.getString("pages")
          var publisher = resultSet.getString("publisher")

          println("Title: " + title + " | Author: " + author + " | Genre: " + genre + " | Pages: " + pages + " | Publisher: " + publisher)
        } while ( resultSet.next() )
      }

    }
    catch {
      case a: SQLException => try {connection.rollback()} catch {case e: SQLException => e.printStackTrace()}
    } 

  }

  def queryForBooksTest(): Unit = {
    try {
      val sql = "SELECT * FROM books LIMIT 10"

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(sql)

      while ( resultSet.next() ) {
        var title = resultSet.getString("title")
        var author = resultSet.getString("author")
        var genre = resultSet.getString("genre")
        var pages = resultSet.getString("pages")
        var publisher = resultSet.getString("publisher")
        println("Title: " + title + " | Author: " + author + " | Genre: " + genre + " | Pages: " + pages + " | Publisher: " + publisher)
      }
    }
    catch {
      case a: SQLException => try {connection.rollback()} catch {case e: SQLException => e.printStackTrace()}
    }
  }

  def test_mysql_connection(): Int = {

    try {// connect to the database named "demodatabase" on the localhost
      
      val driver = "com.mysql.cj.jdbc.Driver"
      // make the connection
      Class.forName(driver).newInstance()
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demodatabase?" + "user=root&password=Ro93Jo98@")
      connection.setAutoCommit(false);

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT response FROM connection_test")

      while ( resultSet.next() ) {
        val status = resultSet.getString("response")
        SpecialPrint.cprintln("Database Connection status = " + status, Console.GREEN)
      }

      //Return a status code of 1 for successful connection
      return 1;

    } 
    catch {
      case a: ClassNotFoundException => a.printStackTrace(); print_failure_message("Connection status = Failure"); return 0;
      case b: SQLException => b.printStackTrace(); print_failure_message("Connection status = Failure"); return 0;
      case c: SQLTimeoutException => c.printStackTrace(); print_failure_message("Connection status = Failure"); return 0;
    } 
    finally {
      // Scala wants return statement within catch block
    }
    
  }

  def print_failure_message(error_message:String): Unit = {
    SpecialPrint.cprintln(error_message, Console.RED)
  }

  def load_books_to_db(): Unit = {
    var lineReader:BufferedReader = null
    try {
      connection.setAutoCommit(false); // To run multiple statements
      lineReader = new BufferedReader(new FileReader("books_updated.csv"))
      lineReader.readLine() // Skip the first line w/ the list of column names

      var lineText:String = lineReader.readLine() // If the variable is null i.e. empty file then the queries wont run
      val sql = "INSERT INTO books (title, author, genre, pages, publisher) VALUES (?, ?, ?, ?, ?)"
      var statement:PreparedStatement = connection.prepareStatement(sql)

      println("Attempting queuries...")

      val batchSize = 20
      var count = 0

      while(lineText != null) {
        var data = lineText.split(",")
        lineText = lineReader.readLine()
        var title = data(0)
        var author = data(1)
        var genre = data(2)
        var pages = data(3).toInt
        var publisher = data(4)

        //Build statement (query)
        statement.setString(1, title)
        statement.setString(2, author)
        statement.setString(3, genre)
        statement.setInt(4, pages)
        statement.setString(5, publisher)

        statement.addBatch()
        if(count % batchSize == 0) {
          statement.executeBatch()
        }
        
      }

      lineReader.close()

      // execute the remaining queries
      statement.executeBatch();
 
      connection.commit();

    }
    catch {
      case a: FileNotFoundException => a.printStackTrace(); print_failure_message("Requested file not found.");
      case b: SQLException => try {connection.rollback()} catch {case e: SQLException => e.printStackTrace()}
    }

  }

}// EOF