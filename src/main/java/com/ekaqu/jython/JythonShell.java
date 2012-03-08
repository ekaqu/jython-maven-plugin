package com.ekaqu.jython;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.python.core.PySystemState;
import org.python.util.InteractiveConsole;
import org.python.util.JLineConsole;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Date: 3/4/12
 * Time: 8:23 PM
 */
public class JythonShell {
  private final InteractiveConsole console;

  JythonShell(Properties properties, final List<String> dependenciesPath) {
    if(dependenciesPath != null && dependenciesPath.size() > 0) {
      properties.setProperty("python.path", Joiner.on(":").join(dependenciesPath));
    }
    PySystemState.initialize(
      PySystemState.getBaseProperties(),
      properties);
    console = getConsole();
  }

  public void interact() {
    console.interact();
  }
  
  public void exec(String line) {
    console.exec(line);
  }
  
  public void execFile(String fileName) {
    console.execfile(fileName);
  }
  
  public void execFile(InputStream stream) {
    console.execfile(stream);
  }

  public static void main(String[] args) {
    JythonShell shell = new JythonShell(System.getProperties(), null);
    // process args
    shell.interact();
  }

  private static InteractiveConsole getConsole() {
    String interpreter = PySystemState.registry.getProperty("python.console", "");
    if (Strings.isNullOrEmpty(interpreter)) {
      return new JLineConsole();
    } else {
      try {
        return (InteractiveConsole) Class.forName(interpreter).newInstance();
      } catch (Throwable e) {
        throw Throwables.propagate(e);
      }
    }
  }
}
