object LibraryPrompter {

  def loginPrompt(): String = {
    
    var s = "_________________"
    s = s + "\nRev Library" 
    s = s + "\n-----------------"
    return s
  }

  def welcomeMessage(): String = {
    var s = "***********************************************"
    s = s + "\nWelcome back to Rev Library. You can search for books within our library. Listed below are your available commands:" 
    s = s + "\n***********************************************"
    return s
  }

  def help(): String = {
    val s = "Available commands:" + "\nhelp" + "\nsearch" + "\nquit"
    return s
  }

  def searchPrompt(): String = {
    val s = "Search Function Call"
    return s
  }
}