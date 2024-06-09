package lexer.readers;

import java.io.*;

public class SourceReader implements IReader {

  private StringBuffer fileLines = new StringBuffer("");
  private BufferedReader source;
  private int lineNumber = 0;
  private int position;
  private boolean isPriorEndLine = true;
  private String nextLine;

  public SourceReader(String sourceFile) throws IOException {
    source = new BufferedReader(new FileReader(sourceFile));
  }

  public SourceReader(BufferedReader reader) throws IOException {
    this.source = reader;
  }

  public void close() {
    try {
      source.close();
    } catch (Exception e) {
    }
  }

  public char read() throws IOException {
    if (isPriorEndLine) {
      lineNumber++;
      position = -1;
      nextLine = source.readLine();
      // System.out.println(fileLines);
      if (nextLine.length() == 0) {
        fileLines.append(String.format("%3d:\n", lineNumber));
      } else {
        fileLines.append(String.format("%3d: %s\n", lineNumber, nextLine));
      }

      // System.out.println(fileLines);
      isPriorEndLine = false;
    }

    if (nextLine == null) {
      throw new IOException();
    }

    if (nextLine.length() == 0) {
      isPriorEndLine = true;
      return ' ';
    }

    position++;
    if (position >= nextLine.length()) {
      isPriorEndLine = true;
      return ' ';
    }

    return nextLine.charAt(position);
  }

  public String toString() {
    return fileLines.toString();
  }

  public int getPosition() {
    return position;
  }

  public int getLineno() {
    return lineNumber;
  }

  public static void main(String args[]) {
    SourceReader s = null;

    try {
      s = new SourceReader(args[0]);

      while (true) {
        // char ch = s.read();
        // System.out.println("Char: " + ch + " Line: " + s.lineNumber + "position: " +
        // s.position);
      }
    } catch (Exception e) {
    }

    if (s != null) {
      s.close();
    }
  }
}