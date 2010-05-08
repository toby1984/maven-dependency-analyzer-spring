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
