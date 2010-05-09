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
import java.net.URL;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlFileLocator;

public class DefaultSpringXmlFileLocatorTest extends TestCase {

	public static final String PATH = "/it/simpleproject/pom.xml";
	
	private DefaultSpringXmlFileLocator locator;
	private File baseDir;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		locator = new DefaultSpringXmlFileLocator();
		
		// locate root of our test project
		final URL pom = getClass().getResource( PATH );
		assertNotNull( "Unable to locate test project ?", pom );
		baseDir = new File( pom.toURI().getPath() ).getParentFile();
	}
	
	private File toAbsoluteFile(String relativePath) {
		return new File( baseDir , relativePath );
	}

	private String toAbsolutePath(String relativePath) {
		return toAbsoluteFile( relativePath ).getAbsolutePath();
	}
	
	public void testSimpleProject() throws Exception {
		
		// setup mock project
		final MavenProject project = new MavenProject();
		final Build build = new Build();
		build.setDirectory( baseDir.getAbsolutePath() );
		project.setBuild( build );
		
		final Resource resource = new Resource();
		resource.setDirectory( toAbsolutePath( "src/main/resources" ) );
		
		final Resource testResource = new Resource();
		testResource.setDirectory( toAbsolutePath( "src/test/resources" ) );
		
		project.addResource( resource );
		project.addTestResource( testResource );
	
		// run test
		final Set<File> files =
			locator.locateSpringXmls( project );
		
		assertEquals( 3 , files.size() );
		assertTrue( files.contains( toAbsoluteFile( "src/test/resources/test-spring.xml" ) ) );
		assertTrue( files.contains( toAbsoluteFile( "src/main/resources/other.xml" ) ) );
		assertTrue( files.contains( toAbsoluteFile( "src/main/resources/main-spring.xml" ) ) );
	}
}
