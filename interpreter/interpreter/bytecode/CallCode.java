package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class CallCode extends ByteCode {
  private String functionName;
  private String functionLabel;
  private String parameters;
  private int branchTarget;

  public CallCode(List<String> args) {
    functionLabel = args.get(0);
    functionName = args.get(0).split("<<", 2)[0];
    parameters = "";
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
      parameters = vm.getParameters();
    } catch (Exception e) {
      e.printStackTrace();
    }
    vm.pushReturnAddress(vm.getPC());
    vm.setPC(branchTarget);
  }

  @Override
  public String toString() {
    return String.format("%-25s%s(", String.format("CALL %s", functionLabel), functionName) + parameters + ")";
  }
}