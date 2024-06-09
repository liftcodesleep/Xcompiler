package interpreter;

import interpreter.bytecode.ByteCode;
import interpreter.errors.StackUnderflowException;
import java.util.Stack;

public class VirtualMachine {
  private int pc;
  private RunTimeStack runTimeStack;
  private Stack<Integer> returnAddresses;
  private boolean isRunning;
  private boolean isOutputting;
  private Program program;

  public VirtualMachine(Program program) {
    this.program = program;
  }

  public void executeProgram() {
    pc = 0;
    runTimeStack = new RunTimeStack();
    returnAddresses = new Stack<Integer>();
    isRunning = true;

    while (isRunning) {
      ByteCode code = program.getCode(pc);
      code.execute(this);
      if (isOutputting) {
        System.out.print(code);
      }
      pc++;
    }
  }

  public int getStackSize() {
    return runTimeStack.getStackSize();
  }

  public int getPC() {
    return pc;
  }

  public void setPC(int pc) {
    this.pc = pc;
  }

  public void newFrameAt(int offset) {
    runTimeStack.newFrameAt(offset);
  }

  public int peekFramePointers() {
    return runTimeStack.peekFramePointers();
  }

  public String getParameters() throws StackUnderflowException {
    return runTimeStack.getParameters();
  }

  public int peekRunTimeStack() throws StackUnderflowException {
    return runTimeStack.peek();
  }

  public void pushRunTimeStack(int value) {
    runTimeStack.push(value);
  }

  public int popRunTimeStack() throws StackUnderflowException {
    return runTimeStack.pop();
  }

  public void loadRunTimeStack(int offset) {
    runTimeStack.load(offset);
  }

  public void storeRunTimeStack(int offset) throws StackUnderflowException {
    runTimeStack.store(offset);
  }

  public int peekReturnAddress() {
    return returnAddresses.peek();
  }

  public int pushReturnAddress(int address) {
    return returnAddresses.push(address);
  }

  public int popReturnAddress() {
    return returnAddresses.pop();
  }

  public int getReturnAddressSize() {
    return returnAddresses.size();
  }

  public void setNotRunning() {
    isRunning = false;
  }

  public void setIsOutputting(boolean state) {
    this.isOutputting = state;
  }
}
