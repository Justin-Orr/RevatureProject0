object SpecialPrint {
  def cprintln(s: String, color:String, alter:String = Console.RESET): Unit = {
    print(alter)
    print(color)
    print(s)
    print(Console.RESET + "\n")
  }

  def cprint(s: String, color:String = Console.WHITE, alter:String = Console.RESET): Unit = {
    print(alter)
    print(color)
    print(s)
    print(Console.RESET)
  }
}