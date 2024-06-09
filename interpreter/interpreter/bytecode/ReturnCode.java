package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class ReturnCode extends ByteCode {
  private String functionName;
  private String functionLabel;
  private int literal;

  public ReturnCode(List<String> args) {
    functionLabel = args.get(0);
    functionName = args.get(0).split("<<", 2)[0];
  }

  @Override
  public void execute(VirtualMachine vm) {
    try {
      literal = vm.peekRunTimeStack();
    } catch (Exception e) {
      e.printStackTrace();
    }
    vm.setPC(vm.popReturnAddress());
  }

  @Override
  public String toString() {
    return String.format(
        "%-25send %s: %s",
        String.format("RETURN %s", functionLabel),
        functionName,
        literal);
  }

}
