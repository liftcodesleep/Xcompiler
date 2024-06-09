package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class HaltCode extends ByteCode {
  public HaltCode() {
  }

  public HaltCode(List<String> args) {

  }

  @Override
  public void execute(VirtualMachine vm) {
    vm.setNotRunning();
  }

  @Override
  public String toString() {
    return "HALT";
  }

}
