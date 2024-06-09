package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.List;

public class BopCode extends ByteCode {
  private String operator;
  private int result;

  public BopCode(List<String> args) {
    operator = args.get(0);
  }

  @Override
  public void execute(VirtualMachine vm) {
    int lhs = 0;
    int rhs = 0;
    try {
      rhs = vm.popRunTimeStack();
      lhs = vm.popRunTimeStack();
    } catch (Exception e) {
      e.printStackTrace();
    }
    result = calculate(lhs, rhs, operator);
    vm.pushRunTimeStack(result);
  }

  private int calculate(int lhs, int rhs, String operation) {
    int solution = 0;
    switch (operation) {
      case "+":
        solution = lhs + rhs;
        break;
      case "-":
        solution = lhs - rhs;
        break;
      case "*":
        solution = lhs * rhs;
        break;
      case "/":
        solution = lhs / rhs;
        break;
      case "==":
        if (lhs == rhs) {
          solution = 1;
          break;
        }
        break;
      case "!=":
        if (lhs != rhs) {
          solution = 1;
          break;
        }
        break;
      case "<":
        if (lhs < rhs) {
          solution = 1;
          break;
        }
        break;
      case ">":
        if (lhs > rhs) {
          solution = 1;
          break;
        }
        break;
      case "<=":
        if (lhs <= rhs) {
          solution = 1;
          break;
        }
        break;
      case ">=":
        if (lhs >= rhs) {
          solution = 1;
          break;
        }
        break;
      case "&":
        if (lhs > 0 && rhs > 0) {
          solution = 1;
          break;
        }
        break;
      case "|":
        if (lhs > 0 || rhs > 0) {
          solution = 1;
          break;
        }
        break;
    }
    return solution;
  }

  public String toString() {
    return "BOP " + operator;
  }
}
