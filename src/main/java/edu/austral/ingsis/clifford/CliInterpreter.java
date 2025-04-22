package edu.austral.ingsis.clifford;

import java.util.*;

public class CliInterpreter {
  private final InMemoryFileSystem fs;

  public CliInterpreter(InMemoryFileSystem fs) {
    this.fs = fs;
  }

  public String executeCommand(String input) {
    String[] parts = input.trim().split(" ");
    if (parts.length == 0 || parts[0].isEmpty()) return "";

    String command = parts[0];
    String[] args = Arrays.copyOfRange(parts, 1, parts.length);

    return switch (command) {
      case "ls" -> fs.ls(args);
      case "cd" -> args.length == 1 ? fs.cd(args[0]) : "Error: cd takes one argument";
      case "mkdir" -> args.length == 1 ? fs.mkdir(args[0]) : "Error: mkdir takes one argument";
      case "touch" -> args.length == 1 ? fs.touch(args[0]) : "Error: touch takes one argument";
      case "rm" -> parseRm(args);
      case "pwd" -> fs.pwd();
      default -> "Unknown command: " + command;
    };
  }

  private String parseRm(String[] args) {
    if (args.length == 1) return fs.rm(args[0], false);
    if (args.length == 2 && args[0].equals("--recursive")) return fs.rm(args[1], true);
    return "Error: invalid rm syntax";
  }
}
