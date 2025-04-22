package edu.austral.ingsis.clifford;

public class File extends FileSystemElement {

  public File(String name, Directory parent) {
    super(name, parent);
  }

  @Override
  public boolean isDirectory() {
    return false;
  }
}
