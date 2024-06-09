package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class DbgCode extends ByteCode {
  private boolean debugging;

  public DbgCode(List<String> args) {
    debugging = (Integer.parseInt(args.get(0)) == 0) ? false : true;
  }

  @Override
  public void execute(VirtualMachine vm) {
    vm.setIsOutputting(debugging);
  }

  @Override
  public String toString() {
    return (!debugging) ? "DBG OFF" : "DBG ON";
  }

}
