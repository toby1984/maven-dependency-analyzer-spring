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
