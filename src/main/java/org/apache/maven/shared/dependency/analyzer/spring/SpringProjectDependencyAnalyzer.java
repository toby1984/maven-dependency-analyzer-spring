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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.analyzer.spring.SpringXmlParser.NoSpringXmlException;
import org.codehaus.plexus.logging.Logger;

/**
 * Retrieves dependency information using Spring XML configuration 
 * files in a Maven project. 
 * 
 * This class parses bean definitions from Spring XML files and uses a 
 * {@link DefaultSpringXmlBeanVisitor} to gather dependency information for
 * each bean.
 * 
 * @author tobias.gierke@code-sourcery.de
 * @see DefaultSpringXmlBeanVisitor
 */
public class SpringProjectDependencyAnalyzer
{
    private SpringXmlParser fileParser;

    private SpringXmlFileLocator fileLocator;

    private ArtifactForClassResolver resolver;

    private Logger log;

    public SpringProjectDependencyAnalyzer()
    {
    }

    public void setLog( Logger log )
    {
        this.log = log;
    }

    /**
     * Retrieves dependency information from Spring XML configuration files in a Maven project.
     * 
     * @param project the project to analyze
     * @param dependentClasses A set of classes that already had their dependencies analyzed. This method will
     *            <b>ADD</b> all Spring-induced dependencies to this set and also use it to determine whether a given
     *            class needs to have it's dependencies analyzed.
     * @throws Exception
     */
    public void addSpringDependencyClasses( MavenProject project, final Set<String> dependentClasses )
        throws Exception
    {
        final SpringFileBeanVisitor beanVisitor = new DefaultSpringXmlBeanVisitor( this.resolver, dependentClasses );

        for ( File springXml : fileLocator.locateSpringXmls( project ) )
        {
            final BufferedInputStream in = new BufferedInputStream( new FileInputStream( springXml ) );
            try
            {
                fileParser.parse( in, beanVisitor );
                if ( log != null && log.isInfoEnabled() )
                {
                    log.info( "Scanned Spring XML " + springXml.getPath() );
                }
            }
            catch ( NoSpringXmlException ex )
            {
                if ( log != null && log.isDebugEnabled() )
                {
                    log.debug( "Not a Spring XML file : " + springXml.getPath() );
                }
                // ok
            }
            catch ( Exception e )
            {
                if ( log != null )
                {
                    log.error( "Failed to parse Spring XML " + springXml.getPath() + " ...", e );
                }
                throw e;
            }
            finally
            {
                in.close();
            }
        }
    }

    public void setFileParser( SpringXmlParser fileParser )
    {
        this.fileParser = fileParser;
    }

    public void setFileLocator( SpringXmlFileLocator fileLocator )
    {
        this.fileLocator = fileLocator;
    }

    public void setResolver( ArtifactForClassResolver resolver )
    {
        this.resolver = resolver;
    }
}
