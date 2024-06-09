package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class LitCode extends ByteCode {
  private int value;
  private String id;

  public LitCode(List<String> args) {
    id = "";
    value = Integer.parseInt(args.get(0));
    if (args.size() > 1) {
      id = args.get(1);
    }
  }

  @Override
  public void execute(VirtualMachine vm) {
    vm.pushRunTimeStack(value);
  }

  @Override
  public String toString() {
    if (id == "") {
      return String.format(
          "%-25sint %s",
          String.format("LIT %s", value),
          value);
    }
    return String.format(
        "%-25sint %s = %s",
        String.format("LIT %s %s", value, id),
        id,
        value);
  }
}