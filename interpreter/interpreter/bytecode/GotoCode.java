package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class GotoCode extends ByteCode {
  private String label;
  private int branchTarget;

  public GotoCode(List<String> args) {
    label = args.get(0);
  }

  public void setBranchTarget(int index) {
    branchTarget = index;
  }

  public int getBranchTarget() {
    return branchTarget;
  }

  @Override
  public void execute(VirtualMachine vm) {
    vm.setPC(branchTarget);
  }

  @Override
  public String toString() {
    return String.format("GOTO %s", label);

  }

}
