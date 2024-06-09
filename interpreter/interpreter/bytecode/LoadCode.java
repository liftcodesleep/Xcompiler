package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.List;

public class LoadCode extends ByteCode {
  private int offset;
  private String id;

  public LoadCode(List<String> args) {
    if (args.size() > 1) {
      offset = Integer.parseInt(args.get(0));
      id = args.get(1);
    }
  }

  @Override
  public void execute(VirtualMachine vm) {
    vm.loadRunTimeStack(offset);
  }

  @Override
  public String toString() {
    return String.format(
        "%-25s<load %s>",
        String.format("LOAD %s %s", offset, id),
        id);
  }

}
