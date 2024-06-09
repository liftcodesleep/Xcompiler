package interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import interpreter.bytecode.ByteCode;
import interpreter.bytecode.CallCode;
import interpreter.bytecode.FalsebranchCode;
import interpreter.bytecode.GotoCode;
import interpreter.bytecode.LabelCode;

public class Program {
  private List<ByteCode> program = new ArrayList<>();

  public void addCode(ByteCode bc) {
    program.add(bc);
  }

  public ByteCode getCode(int index) {
    return program.get(index);
  }

  public boolean hasSymbolicAddress(ByteCode bytecode) {
    return bytecode.getClass() == CallCode.class || bytecode.getClass() == FalsebranchCode.class
        || bytecode.getClass() == GotoCode.class;
  }

  public void resolveSymbolicAddresses() {
    HashMap<String, Integer> labels = new HashMap<>();
    for (int i = 0; i < program.size(); i++) {
      if (program.get(i) instanceof LabelCode) {
        String[] label = program.get(i).toString().split(" ");
        labels.put(label[1], i);
      }
    }

    for (int i = 0; i < program.size(); i++) {
      String[] components = program.get(i).toString().split(" ");
      if (hasSymbolicAddress(program.get(i)) && labels.containsKey(components[1])) {
        if (program.get(i).getClass() == CallCode.class) {
          ((CallCode) program.get(i)).setBranchTarget(labels.get(components[1]));
        }
        if (program.get(i).getClass() == FalsebranchCode.class) {
          ((FalsebranchCode) program.get(i)).setBranchTarget(labels.get(components[1]));
        }
        if (program.get(i).getClass() == GotoCode.class) {
          ((GotoCode) program.get(i)).setBranchTarget(labels.get(components[1]));
        }
      }
    }

  }
}