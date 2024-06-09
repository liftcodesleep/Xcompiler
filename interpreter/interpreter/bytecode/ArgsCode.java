package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class ArgsCode extends ByteCode {
  private int argsNeeded;

  public ArgsCode(List<String> args) {
    argsNeeded = Integer.parseInt(args.get(0));
  }

  @Override
  public void execute(VirtualMachine vm) {
    vm.newFrameAt(argsNeeded);
  }

  @Override
  public String toString() {
    return "ARGS " + Integer.toString(argsNeeded);
  }
}
