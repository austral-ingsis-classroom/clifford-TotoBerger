package edu.austral.ingsis.clifford;

public abstract class FileSystemElement {
  protected final String name;
  protected final Directory parent;

  public FileSystemElement(String name, Directory parent) {
    this.name = name;
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public Directory getParent() {
    return parent;
  }

  public abstract boolean isDirectory();
}
