package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class LabelCode extends ByteCode {
  private String label;

  public LabelCode(List<String> args) {
    label = args.get(0);
  }

  @Override
  public void execute(VirtualMachine vm) {

  }

  @Override
  public String toString() {
    return String.format("LABEL %s", label);
  }
}
