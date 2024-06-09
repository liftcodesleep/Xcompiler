package interpreter;

import java.util.HashMap;

public class CodeTable {

  private static java.util.HashMap<String, String> bytecodes = new HashMap<>();

  public static void init() {
    bytecodes.put("ARGS", "ArgsCode");
    bytecodes.put("BOP", "BopCode");
    bytecodes.put("DBG", "DbgCode");
    bytecodes.put("CALL", "CallCode");
    bytecodes.put("FALSEBRANCH", "FalsebranchCode");
    bytecodes.put("GOTO", "GotoCode");
    bytecodes.put("HALT", "HaltCode");
    bytecodes.put("LABEL", "LabelCode");
    bytecodes.put("LIT", "LitCode");
    bytecodes.put("LOAD", "LoadCode");
    bytecodes.put("POP", "PopCode");
    bytecodes.put("READ", "ReadCode");
    bytecodes.put("RETURN", "ReturnCode");
    bytecodes.put("STORE", "StoreCode");
    bytecodes.put("WRITE", "WriteCode");
  }

  public static String get(String code) {
    if (bytecodes.get(code) == null) {
      return null;
    }
    return "interpreter.bytecode." + bytecodes.get(code);
  }
}