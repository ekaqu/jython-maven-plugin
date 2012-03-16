package com.ekaqu.jython;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Date: 3/7/12
 * Time: 9:12 PM
 */
public abstract class AbstractDependencyMojo extends AbstractMojo {

  /**
   * POM
   *
   * @parameter expression="${project}"
   * @readonly
   * @required
   */
  protected MavenProject project;

//  /**
//   * Used to look up Artifacts in the remote repository.
//   *
//   * @parameter expression=
//   * "${component.org.apache.maven.artifact.factory.ArtifactFactory}"
//   * @required
//   * @readonly
//   */
//  protected ArtifactFactory factory;

  /**
   * Used to look up Artifacts in the remote repository.
   *
   * @parameter expression=
   * "${component.org.apache.maven.artifact.resolver.ArtifactResolver}"
   * @required
   * @readonly
   */
  protected ArtifactResolver artifactResolver;

  /**
   * List of Remote Repositories used by the resolver
   *
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @readonly
   * @required
   */
  protected List remoteRepositories;

  /**
   * Location of the local repository.
   *
   * @parameter expression="${localRepository}"
   * @readonly
   * @required
   */
  protected ArtifactRepository localRepository;

//  /**
//   * Where jython cache dir lives
//   *
//   * @parameter expression="${jython.cache.directory}" default-value="${project.build.directory}/jython/cache"
//   * @optional
//   */
//  protected File cacheDirectory;

//  /**
//   * Where the compiled code is
//   *
//   * @parameter expression="${jython.project.compile.directory}" default-value="${project.build.directory}/classes"
//   * @optional
//   */
//  protected File compiledDirectory;

  /**
   * Where the test compiled code is
   *
   * @parameter expression="${jython.project.test.compile.directory}" default-value="${project.build.directory}/test-classes"
   * @optional
   */
  protected File testCompiledDirectory;

  protected Set<String> getDependenciesPaths() {
    Set<Artifact> artifacts = project.getDependencyArtifacts();
    Set<Artifact> transitiveArtifacts = Sets.newHashSet();

    Set<String> paths = Sets.newHashSet();
    for(Artifact artifact : artifacts) {
      try {
        artifactResolver.resolve(artifact, this.remoteRepositories, this.localRepository);
        paths.add(artifact.getFile().getAbsolutePath());
        ArtifactResolutionResult results = artifactResolver.resolveTransitively(artifacts, artifact, localRepository, remoteRepositories, null, null);
        transitiveArtifacts.addAll(results.getArtifacts());
      } catch (Exception e) {
        Throwables.propagate(e);
      }
    }
    for(Artifact artifact : transitiveArtifacts) {
      paths.add(artifact.getFile().getAbsolutePath());
    }
    if(getLog().isDebugEnabled()) {
      getLog().debug("Jars: " + paths);
    }
    return paths;
  }
}
