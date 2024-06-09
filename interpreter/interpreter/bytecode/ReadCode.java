package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;
import java.util.Scanner;

public class ReadCode extends ByteCode {
  Scanner scanner = new Scanner(System.in);

  public ReadCode() {
  }

  public ReadCode(List<String> args) {

  }

  @Override
  public void execute(VirtualMachine vm) {
    System.out.println("Enter an integer: ");
    Integer userInput = Integer.parseInt(scanner.nextLine());
    vm.pushRunTimeStack(userInput);
    scanner.close();
  }

  @Override
  public String toString() {
    return "READ";
  }

}
