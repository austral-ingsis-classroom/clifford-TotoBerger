package edu.austral.ingsis.clifford;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    InMemoryFileSystem fs = new InMemoryFileSystem();
    CliInterpreter cli = new CliInterpreter(fs);
    Scanner scanner = new Scanner(System.in);

    System.out.println("CLI File System - Ingrese comandos (ctrl + C para salir)");
    while (true) {
      System.out.print("$ ");
      String input = scanner.nextLine();
      String output = cli.executeCommand(input);
      System.out.println(output);
    }
  }
}
