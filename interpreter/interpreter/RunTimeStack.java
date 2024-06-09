package interpreter;

import java.util.Stack;
import java.util.Vector;
import interpreter.errors.StackUnderflowException;

public class RunTimeStack {

  private Stack<Integer> framePointers;
  private Vector<Integer> runStack;

  public RunTimeStack() {
    runStack = new Vector<Integer>();
    framePointers = new Stack<Integer>();
    framePointers.add(0);
  }

  public String toString() {
    String output = "[";

    for (int i = 0; i < runStack.size(); i++) {
      if (i != 0 && framePointers.contains(i)) {
        output += "] [";
      }
      if (!framePointers.contains(i)) {
        output += ",";
      }
      output += runStack.elementAt(i);
    }
    output += "]";
    return output;
  }

  public int peek() throws StackUnderflowException {
    if (!runStack.isEmpty()) {
      return runStack.get(runStack.size() - 1);
    } else {
      throw new StackUnderflowException();
    }
  }

  public int pop() throws StackUnderflowException {
    if (!runStack.isEmpty() && framePointers.peek() < runStack.size()) {
      return runStack.remove(runStack.size() - 1);
    } else {
      throw new StackUnderflowException();
    }
  }

  public int push(int item) {
    runStack.add(item);
    return item;
  }

  public Integer push(Integer item) {
    runStack.add(item);
    return item;
  }

  public void newFrameAt(int offset) {
    framePointers.push(runStack.size() - offset);
  }

  public int peekFramePointers() {
    return framePointers.peek();
  }

  public String getParameters() throws StackUnderflowException {
    if (framePointers.size() == 1) {
      throw new StackUnderflowException();
    }
    String parameters = "";
    int frameSize = runStack.size() - framePointers.peek();
    for (int i = 0; i < frameSize; i++) {
      parameters += runStack.get(i);
      if (frameSize - i != 1) {
        parameters += ",";
      }
    }
    return parameters;
  }

  public void popFrame() throws StackUnderflowException {
    if (framePointers.size() == 1) {
      throw new StackUnderflowException();
    }
    int returnValue = runStack.lastElement();
    int frameTop = framePointers.pop();
    int frameSize = runStack.size() - frameTop;
    for (int i = 0; i < frameSize; i++) {
      runStack.remove(frameTop);
    }
    push(returnValue);
  }

  public int store(int offset) throws StackUnderflowException {
    if (offset + framePointers.peek() < 0) {
      throw new StackUnderflowException();
    }
    int frameOffset = framePointers.peek() + offset;
    runStack.set(frameOffset, runStack.lastElement());
    return pop();
  }

  public int load(int offset) {
    int frameOffset = framePointers.peek() + offset;
    push(runStack.get(frameOffset));
    return runStack.lastElement();
  }

  public int getStackSize() {
    return runStack.size();
  }
}