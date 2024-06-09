package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import interpreter.bytecode.ByteCode;
import java.util.Arrays;

public class ByteCodeLoader {
  private BufferedReader source;

  public ByteCodeLoader(String byteCodeFile) throws IOException {
    this.source = new BufferedReader(new FileReader(byteCodeFile));
  }

  public Program loadCodes() {
    Program program = new Program();
    try {
      String currentLine = source.readLine();
      while (currentLine != null) {
        String[] bytecodeCall = currentLine.split("\\s", 2);
        String classCall = getByteCodeName(bytecodeCall[0]);
        String args = "";
        if (bytecodeCall.length > 1) {
          args = bytecodeCall[1];
        }

        ByteCode bytecode = (ByteCode) (Class.forName(classCall)
            .getConstructor(Class.forName("java.util.List"))
            .newInstance(Arrays.asList(args.split(" "))));

        program.addCode(bytecode);
        currentLine = source.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    program.resolveSymbolicAddresses();
    return program;
  }

  private String getByteCodeName(String code) {
    return CodeTable.get(code);
  }
}