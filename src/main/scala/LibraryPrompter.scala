object LibraryPrompter {

  def printLoginPrompt(): Unit = {
    SpecialPrint.cprintln("___________", Console.CYAN)
    SpecialPrint.cprintln("Rev Library", Console.CYAN, Console.UNDERLINED)
  }

  def printWelcomeMessage(): Unit = {
    SpecialPrint.cprintln("***********************************************", Console.CYAN)
    SpecialPrint.cprintln("Welcome back to the Rev Library Lookup. You can search for books within our library. Listed below are your available commands:", Console.CYAN)
    SpecialPrint.cprintln("***********************************************", Console.CYAN)
  }

  def printHelp(): Unit = {
    SpecialPrint.cprintln("Available commands:" + "\nhelp" + "\nsearch" + "\nquit", Console.CYAN)
  }

  def printSearchMenuPrompt(): Unit = {
    SpecialPrint.cprintln("Search for books based on one of the four attributes: title, author, genre, or publisher", Console.MAGENTA)
    printSearchMenuHelp()
  }

  def printSearchMenuHelp(): Unit = {
    SpecialPrint.cprintln("Available commands:" + "\ntitle" + "\nauthor" + "\ngenre" + "\npublisher" + "\nhelp" + "\nback", Console.MAGENTA)
    print("\n")
    printSearchMenuExample()
  }

  def printSearchMenuExample(): Unit = {
    SpecialPrint.cprintln("Example Search:", Console.MAGENTA, Console.UNDERLINED)
    SpecialPrint.cprintln("search_attribute>: title", Console.MAGENTA)
    SpecialPrint.cprintln("title>: Data Smart", Console.MAGENTA)
    SpecialPrint.cprintln("\nResults:", Console.MAGENTA, Console.UNDERLINED)
    SpecialPrint.cprintln("Title: Data Smart | Author: John Foreman | Genre: data_science | Pages: 235 | Publisher: Wiley", Console.MAGENTA)
  }

}