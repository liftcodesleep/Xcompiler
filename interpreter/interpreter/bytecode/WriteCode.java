package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class WriteCode extends ByteCode {
  public WriteCode() {
  }

  public WriteCode(List<String> args) {

  }

  @Override
  public void execute(VirtualMachine vm) {
    try {
      System.out.println(vm.peekRunTimeStack());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return String.format("%sWRITE%s", System.lineSeparator(), System.lineSeparator());
  }

}
