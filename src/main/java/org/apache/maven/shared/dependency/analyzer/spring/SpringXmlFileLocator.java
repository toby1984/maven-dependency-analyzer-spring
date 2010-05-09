/**
 * Copyright 2010 Tobias Gierke <tobias.gierke@code-sourcery.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
