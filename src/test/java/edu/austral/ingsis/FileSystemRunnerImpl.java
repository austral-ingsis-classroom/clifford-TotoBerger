package edu.austral.ingsis;

import edu.austral.ingsis.clifford.CliInterpreter;
import edu.austral.ingsis.clifford.InMemoryFileSystem;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemRunnerImpl implements FileSystemRunner {

  @Override
  public List<String> executeCommands(List<String> commands) {
    InMemoryFileSystem fs = new InMemoryFileSystem();
    CliInterpreter cli = new CliInterpreter(fs);
    return commands.stream().map(cli::executeCommand).collect(Collectors.toList());
  }
}
