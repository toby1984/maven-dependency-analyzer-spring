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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.analyzer.spring.SpringXmlParser.NoSpringXmlException;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;

public class SpringProjectDependencyAnalyzerTest
    extends TestCase
{

    private SpringProjectDependencyAnalyzer analyzer;

    private final Set<File> springFiles = new HashSet<File>();

    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        analyzer = new SpringProjectDependencyAnalyzer();

        for ( int i = 1; i < 4; i++ )
        {
            final File tmpFile = File.createTempFile( "tmp" + i, "tmp" );
            final FileWriter writer = new FileWriter( tmpFile );
            writer.write( Integer.toString( i ) + "\n" );
            writer.close();
            System.out.println( "Created " + tmpFile.getAbsolutePath() );
            tmpFile.deleteOnExit();
            springFiles.add( tmpFile );
        }
    }

    @Override
    protected void tearDown()
        throws Exception
    {
        super.tearDown();
        for ( File tmpFile : springFiles )
        {
            tmpFile.delete();
        }
        springFiles.clear();
    }

    private static final class InputStreamMatcher
        implements IArgumentMatcher
    {

        private final String contents;

        private InputStreamMatcher( String contents )
        {
            this.contents = contents;
        }

        @Override
        public void appendTo( StringBuffer buffer )
        {
            buffer.append( "stream_with_contents{ " + contents + " }" );
        }

        @Override
        public boolean matches( Object argument )
        {
            if ( !( argument instanceof InputStream ) )
            {
                return false;
            }

            final InputStream stream = (InputStream) argument;

            // this matcher requires a stream
            // with mark support since otherwise
            // if this matcher fails any subsequent
            // matcher will also fail because
            // the stream is already at EOF
            if ( !stream.markSupported() )
            {
                fail( "Internal error, stream has no MARK support ?" );
            }
            stream.mark( 1024 );

            final BufferedReader reader = new BufferedReader( new InputStreamReader( stream ) );
            try
            {
                final StringBuffer buffer = new StringBuffer();
                String line;
                while ( ( line = reader.readLine() ) != null )
                {
                    buffer.append( line );
                }
                return buffer.toString().equals( contents );
            }
            catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
            finally
            {
                try
                {
                    stream.reset();
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( e );
                }
            }
        }

    }

    private static InputStream eqStream( String contents )
    {
        EasyMock.reportMatcher( new InputStreamMatcher( contents ) );
        return null;
    }

    public void testAnalyze()
        throws Exception
    {

        final MavenProject project = new MavenProject();

        // setup file locator
        final SpringXmlFileLocator fileLocator = createMock( SpringXmlFileLocator.class );

        expect( fileLocator.locateSpringXmls( project ) ).andReturn( springFiles ).once();
        replay( fileLocator );

        analyzer.setFileLocator( fileLocator );

        // setup file parser
        final SpringXmlParser fileParser = createMock( SpringXmlParser.class );

        fileParser.parse( eqStream( "1" ), isA( SpringFileBeanVisitor.class ) );
        fileParser.parse( eqStream( "2" ), isA( SpringFileBeanVisitor.class ) );
        fileParser.parse( eqStream( "3" ), isA( SpringFileBeanVisitor.class ) );

        replay( fileParser );

        analyzer.setFileParser( fileParser );

        final ArtifactForClassResolver resolver = createMock( ArtifactForClassResolver.class );

        replay( resolver );

        analyzer.setResolver( resolver );

        // run test
        final Set<String> dependencies = new HashSet<String>();
        analyzer.addSpringDependencyClasses( project, dependencies );

        // verify

        verify( fileLocator );
        verify( fileParser );
        verify( resolver );
    }

    public void testAnalyzeWithInvalidSpringXML()
        throws Exception
    {

        final MavenProject project = new MavenProject();

        // setup file locator
        final SpringXmlFileLocator fileLocator = createMock( SpringXmlFileLocator.class );

        expect( fileLocator.locateSpringXmls( project ) ).andReturn( springFiles ).once();
        replay( fileLocator );

        analyzer.setFileLocator( fileLocator );

        // setup file parser
        final SpringXmlParser fileParser = createMock( SpringXmlParser.class );

        fileParser.parse( eqStream( "1" ), isA( SpringFileBeanVisitor.class ) );
        fileParser.parse( eqStream( "2" ), isA( SpringFileBeanVisitor.class ) );
        expectLastCall().andThrow( new NoSpringXmlException( "dummy", 42 ) );
        fileParser.parse( eqStream( "3" ), isA( SpringFileBeanVisitor.class ) );

        replay( fileParser );

        analyzer.setFileParser( fileParser );

        final ArtifactForClassResolver resolver = createMock( ArtifactForClassResolver.class );

        replay( resolver );

        analyzer.setResolver( resolver );

        // run test
        final Set<String> dependencies = new HashSet<String>();
        analyzer.addSpringDependencyClasses( project, dependencies );

        // verify

        verify( fileLocator );
        verify( fileParser );
        verify( resolver );
    }
}
