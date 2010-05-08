package org.apache.maven.shared.dependency.analyzer.spring;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.maven.project.MavenProject;

/**
 * Locates all Spring XMLs in a Maven project.
 * 
 * @author tobias.gierke@code-sourcery.de
 */
public interface SpringXmlFileLocator {

	/**
	 * Finds all Spring XML files in a Maven project.
	 * 
	 * Scans a project's resources and test resources
	 * for valid Spring XML files. Resource excludes
	 * and includes apply.
	 * 
	 * @param project
	 * @return Set of spring XML files, never <code>null</code>
	 * @throws IOException
	 */
	public Set<File> locateSpringXmls(MavenProject project) throws IOException;
}
