package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class StoreCode extends ByteCode {
  private int literal;
  private String id;

  public StoreCode(List<String> args) {
    if (args.size() > 1) {
      literal = Integer.parseInt(args.get(0));
      id = args.get(1);
    }
  }

  @Override
  public void execute(VirtualMachine vm) {
    try {
      vm.storeRunTimeStack(literal);
      literal = vm.peekRunTimeStack();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return String.format(
        "%-25s%s = %s",
        String.format("STORE %d %s", 0, id),
        id,
        literal);
  }

}
