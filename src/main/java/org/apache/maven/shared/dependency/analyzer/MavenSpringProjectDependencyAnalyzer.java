package org.apache.maven.shared.dependency.analyzer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.analyzer.spring.ArtifactForClassResolver;
import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlFileLocator;
import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlParser;
import org.apache.maven.shared.dependency.analyzer.spring.SpringProjectDependencyAnalyzer;

/**
 * 
 *
 * @author tobias.gierke@code-sourcery.de
 * @plexus.component role="org.apache.maven.shared.dependency.analyzer.ProjectDependencyAnalyzer" role-hint="spring"
 */
public class MavenSpringProjectDependencyAnalyzer extends DefaultProjectDependencyAnalyzer implements LoggingAware {

	private Log log;
	
	@SuppressWarnings("unchecked")
	@Override
	protected Set<String> buildDependencyClasses(MavenProject project,final Map artifactClassMap) 
	throws java.io.IOException 
	{
		final Map<Artifact,Set<String>> typedMap = artifactClassMap;
		final Set<String> result = super.buildDependencyClasses( project , typedMap );
		
		final ArtifactForClassResolver resolver = new ArtifactForClassResolver() {
			
			@Override
			public Artifact findArtifactForClass(String className) {
				return findArtifactForClassName( artifactClassMap , className );
			}
		};
		
		final SpringProjectDependencyAnalyzer analyzer =
			new SpringProjectDependencyAnalyzer();
		
		analyzer.setLog( log );
		
		final DefaultSpringXmlFileLocator fileLocator = 
			new DefaultSpringXmlFileLocator();
	
		fileLocator.setLog( log );
		
		analyzer.setFileLocator( fileLocator );
		analyzer.setFileParser( new DefaultSpringXmlParser() );
		analyzer.setResolver( resolver );
		
		try {
			analyzer.addSpringDependencyClasses( project , result );
		} catch(RuntimeException e) {
			throw e;
		} catch(IOException e) {
			throw e;
		} catch (Exception e) {
			// TODO: Most likely not the right type of exception to throw
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public void setLog(Log log) {
		this.log = log;
	}

}
