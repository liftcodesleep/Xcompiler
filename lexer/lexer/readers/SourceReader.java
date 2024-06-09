package lexer.readers;

import java.io.*;

public class SourceReader implements IReader {
  public static final String READ_PREFIX = "READLINE:   ";

  private String fileLines;
  private BufferedReader source;
  // line number of source program
  private int lineNumber = 0;
  // position of last character processed
  private int position;
  // if true then last character read was newline so read in the next line
  private boolean isPriorEndLine = true;
  private String nextLine;

  public SourceReader(String sourceFile) throws IOException {
    System.out.println("Source file: " + sourceFile);
    System.out.println("user.dir: " + System.getProperty("user.dir"));
    source = new BufferedReader(new FileReader(sourceFile));
  }

  public SourceReader(BufferedReader reader) throws IOException {
    this.source = reader;
  }

  public void close() {
    try {
      source.close();
    } catch (Exception e) {
      /* no-op */ }
  }

  public char read() throws IOException {
    if (isPriorEndLine) {
      lineNumber++;
      position = -1;
      fileLines += lineNumber + ": ";
      nextLine = source.readLine();
      // fileLines += "\n";
      if (nextLine != "null") {
        fileLines += nextLine + "\n";
      }

      if (nextLine != null) {
        System.out.println(READ_PREFIX + nextLine);
      }

      isPriorEndLine = false;
    }

    if (nextLine == null) {
      // hit eof or some I/O problem
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
    return fileLines;
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
        char ch = s.read();
        System.out.println(
            "Char: " + ch + " Line: " + s.lineNumber + "position: " + s.position);
      }
    } catch (Exception e) {
    }

    if (s != null) {
      s.close();
    }
  }
}