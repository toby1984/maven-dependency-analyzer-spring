package org.apache.maven.shared.dependency.analyzer.spring;

import org.apache.maven.artifact.Artifact;

/**
 * Locates the {@link Artifact} that contains a given Java class.
 *
 * @author tobias.gierke@code-sourcery.de
 */
public interface ArtifactForClassResolver {

	/**
	 * Locates the artifact that contains a given class.
	 *  
	 * @param className Fully-qualified class name
	 * @return the artifact or <code>null</code> if the
	 * artifact could not be found
	 */
	public Artifact findArtifactForClass(String className);
}
