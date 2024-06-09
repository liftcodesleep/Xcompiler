package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class FalsebranchCode extends ByteCode {
  private String label;
  private int branchTarget;

  public FalsebranchCode(List<String> args) {
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
    try {
      if (vm.popRunTimeStack() == 0) {
        vm.setPC(branchTarget);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return String.format("FALSEBRANCH %s", label);
  }

}
