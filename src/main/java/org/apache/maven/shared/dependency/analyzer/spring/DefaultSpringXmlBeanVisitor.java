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

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.analyzer.ClassFileVisitor;
import org.apache.maven.shared.dependency.analyzer.asm.DependencyClassFileVisitor;

/**
 * A <code>SpringXmlFileBeanVisitor</code> that applies a {@link DependencyClassFileVisitor} the bean's class.
 * 
 * @author tobias.gierke@code-sourcery.de
 */
public class DefaultSpringXmlBeanVisitor
    implements SpringFileBeanVisitor
{
    private final Set<String> result;

    private final ArtifactForClassResolver resolver;

    /**
     * Create instance.
     * 
     * @param resolver used for locating the JAR that contains a given class
     * @param result A set holding the names of all classes that already had their dependencies analyzed. Newly
     *            discovered dependencies will be added to this set.
     */
    public DefaultSpringXmlBeanVisitor( ArtifactForClassResolver resolver, Set<String> result )
    {
        if ( resolver == null )
        {
            throw new IllegalArgumentException( "resolver cannot be NULL" );
        }
        if ( result == null )
        {
            throw new IllegalArgumentException( "result cannot be NULL" );
        }
        this.resolver = resolver;
        this.result = result;
    }

    protected void visitClassInJar( URL jarUrl, String className, ClassFileVisitor visitor )
        throws IOException
    {
        final JarInputStream in = new JarInputStream( jarUrl.openStream() );
        try
        {
            JarEntry entry = null;
            final String expectedName = className.replace( '.', '/' ) + ".class";
            while ( ( entry = in.getNextJarEntry() ) != null )
            {
                if ( expectedName.equals( entry.getName() ) )
                {
                    visitor.visitClass( className, in );
                    return;
                }
            }
        }
        finally
        {
            in.close();
        }
        // TODO: Most likely not the correct type of exception to throw here...
        throw new RuntimeException( "Unable to find class " + className + " in " + jarUrl );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void visitBeanDefinition( String beanClass )
        throws Exception
    {
        // only analyze classes we've not seen yet
        if ( result.contains( beanClass ) )
        {
            return;
        }

        result.add( beanClass );

        final Artifact artifact = resolver.findArtifactForClass( beanClass );
        if ( artifact == null )
        {
            // TODO: RuntimeException is most likely not the right type to throw here...
            throw new RuntimeException( "Failed to locate artifact for class " + beanClass );
        }

        final DependencyClassFileVisitor visitor = new DependencyClassFileVisitor();
        visitClassInJar( artifact.getFile().toURI().toURL(), beanClass, visitor );
        result.addAll( visitor.getDependencies() );
    }
}
