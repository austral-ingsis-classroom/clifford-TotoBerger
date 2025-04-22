package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Directory extends FileSystemElement {

  private final List<FileSystemElement> children = new ArrayList<>();

  public Directory(String name, Directory parent) {
    super(name, parent);
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  public void add(FileSystemElement element) {
    children.add(element);
  }

  public void remove(FileSystemElement element) {
    children.remove(element);
  }

  public List<FileSystemElement> getChildren() {
    return children;
  }

  public Optional<FileSystemElement> findChild(String name) {
    return children.stream().filter(e -> e.getName().equals(name)).findFirst();
  }
}
