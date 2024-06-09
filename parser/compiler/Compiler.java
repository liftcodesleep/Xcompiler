package compiler;

import ast.*;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import parser.Parser;
import visitor.*;

public class Compiler {

  String sourceFile;

  public Compiler(String sourceFile) {
    this.sourceFile = sourceFile;
  }

  public void compileProgram() {
    try {
      Parser parser = new Parser(sourceFile);
      AST t = parser.execute();
      System.out.println(parser.getLex());

      System.out.println("---------------AST-------------");
      PrintVisitor pv = new PrintVisitor();
      t.accept(pv);
    } catch (Exception e) {
      System.out.println("********exception*******" + e.toString());
      e.printStackTrace();
    }
  }

  public void generateAstImage() {
    try {
      System.out.println("---------------TOKENS-------------");
      Parser parser = new Parser(sourceFile);
      AST ast = parser.execute();

      System.out.println(parser.getLex());

      System.out.println("---------------AST-------------");
      PrintVisitor printVisitor = new PrintVisitor();
      ast.accept(printVisitor);

      CountVisitor countVisitor = new CountVisitor();
      ast.accept(countVisitor);

      OffsetVisitor offsetVisitor = new OffsetVisitor();
      ast.accept(offsetVisitor);

      DrawOffsetVisitor drawOffsetVisitor = new DrawOffsetVisitor(offsetVisitor.getOffsets(),
          offsetVisitor.getNodeCount(),
          offsetVisitor.getMaxOffset());
      ast.accept(drawOffsetVisitor);

      try {
        File imagefile = new File(sourceFile + ".png");
        ImageIO.write(drawOffsetVisitor.getImage(), "png", imagefile);
      } catch (Exception e) {
        System.out.println("Error in saving image: " + e.getMessage());
      }

      final JFrame f = new JFrame();
      f.addWindowListener(
          new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              f.dispose();
              System.exit(0);
            }
          });

      JLabel imagelabel = new JLabel(new ImageIcon(drawOffsetVisitor.getImage()));
      f.add("Center", imagelabel);
      f.pack();
      f.setSize(
          new Dimension(
              drawOffsetVisitor.getImage().getWidth() + 30,
              drawOffsetVisitor.getImage().getHeight() + 40));
      f.setVisible(true);
      f.setResizable(false);
      f.repaint();
    } catch (Exception e) {
      System.out.println("********exception*******" + e.toString());
      e.printStackTrace();
    }

  }

  public static void main(String args[]) {
    if (args.length == 0) {
      System.out.println(
          "***Incorrect usage, try: java compiler.Compiler <file> [-image]");
      System.exit(1);
    }
    Compiler compiler = new Compiler(args[0]);

    if (args.length == 2 && args[1].equalsIgnoreCase("-image")) {
      compiler.generateAstImage();
    } else {
      compiler.compileProgram();
    }
  }
}
