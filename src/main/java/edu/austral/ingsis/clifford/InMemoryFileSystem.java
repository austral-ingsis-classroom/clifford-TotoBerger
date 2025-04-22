package edu.austral.ingsis.clifford;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryFileSystem implements FileSystem {

  private final Directory root;
  private Directory current;

  public InMemoryFileSystem() {
    this.root = new Directory("", null);
    this.current = root;
  }

  public String ls(String[] args) {
    List<FileSystemElement> children = new ArrayList<>(current.getChildren());

    Optional<String> order =
        Arrays.stream(args)
            .filter(arg -> arg.startsWith("--ord="))
            .map(arg -> arg.substring(6))
            .findFirst();

    if (order.isPresent()) {
      String ord = order.get();
      Comparator<FileSystemElement> comparator = Comparator.comparing(FileSystemElement::getName);

      if (ord.equals("asc")) {
        children.sort(comparator);
      } else if (ord.equals("desc")) {
        children.sort(comparator.reversed());
      } else {
        return "invalid argument for --ord";
      }
    }

    if (children.isEmpty()) return "";

    return children.stream().map(FileSystemElement::getName).collect(Collectors.joining(" "));
  }

  public String cd(String path) {
    try {
      Directory target = resolvePath(path);
      this.current = target;
      String dirName = (target.getParent() == null) ? "/" : target.getName();
      return "moved to directory '" + dirName + "'";
    } catch (IllegalArgumentException e) {
      return e.getMessage();
    }
  }

  public String mkdir(String name) {
    if (!isValidName(name)) {
      return "Invalid directory name";
    }
    if (current.findChild(name).isPresent()) {
      return "Element already exists";
    }

    current.add(new Directory(name, current));
    return "'" + name + "' directory created";
  }

  public String touch(String name) {
    if (!isValidName(name)) {
      return "Invalid file name";
    }
    if (current.findChild(name).isPresent()) {
      return "Element already exists";
    }

    current.add(new File(name, current));
    return "'" + name + "' file created";
  }

  public String rm(String name, boolean recursive) {
    Optional<FileSystemElement> childOpt = current.findChild(name);
    if (childOpt.isEmpty()) {
      return "No such file or directory";
    }

    FileSystemElement child = childOpt.get();
    if (child.isDirectory() && !recursive) {
      return "cannot remove '" + name + "', is a directory";
    }

    current.remove(child);
    return "'" + name + "' removed";
  }

  public String pwd() {
    return getDirectoryPath(current);
  }

  private Directory resolvePath(String path) {
    if (path.equals(".")) {
      return current;
    }
    if (path.equals("..")) {
      return current.getParent() != null ? current.getParent() : current;
    }

    Directory dir = path.startsWith("/") ? root : current;
    String[] parts = path.split("/");

    for (String part : parts) {
      if (part.isEmpty() || part.equals(".")) {
        continue;
      }
      if (part.equals("..")) {
        if (dir.getParent() != null) {
          dir = dir.getParent();
        }
      } else {
        Optional<FileSystemElement> next = dir.findChild(part);
        if (next.isEmpty() || !next.get().isDirectory()) {
          throw new IllegalArgumentException("'" + path + "' directory does not exist");
        }
        dir = (Directory) next.get();
      }
    }

    return dir;
  }

  private boolean isValidName(String name) {
    return !name.contains("/") && !name.contains(" ");
  }

  private String getDirectoryPath(Directory dir) {
    if (dir == root || dir == null) {
      return "/";
    }
    List<String> path = new ArrayList<>();
    while (dir != null && dir != root) {
      path.add(dir.getName());
      dir = dir.getParent();
    }
    Collections.reverse(path);
    return "/" + String.join("/", path);
  }
}
