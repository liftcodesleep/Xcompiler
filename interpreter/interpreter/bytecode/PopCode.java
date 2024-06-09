package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.List;

public class PopCode extends ByteCode {
  private int count;

  public PopCode(List<String> args) {
    count = Integer.parseInt(args.get(0));
  }

  @Override
  public void execute(VirtualMachine vm) {
    try {
      for (int i = 0; i < count; i++) {
        vm.popRunTimeStack();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return String.format("POP %s", count);
  }

}
