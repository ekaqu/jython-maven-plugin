package com.ekaqu.jython;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.Properties;

/**
 * Creates a jython shell
 *
 * @goal shell
 */
public class ShellMojo extends AbstractDependencyMojo {

  public void execute() throws MojoExecutionException, MojoFailureException {
    Properties properties = new Properties();
    properties.putAll(System.getProperties());
    properties.putAll(getPluginContext());
    getLog().debug(properties.toString());

    new JythonShell(properties, getDependenciesPath()).interact();
  }
}
