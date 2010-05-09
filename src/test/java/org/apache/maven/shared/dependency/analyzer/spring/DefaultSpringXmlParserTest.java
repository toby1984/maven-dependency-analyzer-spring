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
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.maven.shared.dependency.analyzer.spring.DefaultSpringXmlParser;
import org.apache.maven.shared.dependency.analyzer.spring.SpringFileBeanVisitor;
import org.apache.maven.shared.dependency.analyzer.spring.SpringXmlParser.NoSpringXmlException;

public class DefaultSpringXmlParserTest extends TestCase {

	private DefaultSpringXmlParser parser;

	private static final String SIMPLE_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
			"<beans>\n" + 
			"<bean id=\"someBean\" class=\"some.package.SomeClass\" />\n" + 
			"<bean id=\"someOtherBean\" class=\"some.other.package.SomeOtherClass\" />\n" + 
			"</beans>";
	
	private static final String SIMPLE_XML_WITH_NS = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
	"<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:aop=\"http://www.springframework.org/schema/aop\" xmlns:tx=\"http://www.springframework.org/schema/tx\"\n" + 
	"xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\">\n" + 
	"<bean id=\"someBean\" class=\"some.package.SomeClass\" />\n" + 
	"<bean id=\"someOtherBean\" class=\"some.other.package.SomeOtherClass\" />\n" + 
	"</beans>";
	
	private static final String INVALID_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
	"<unexpectedRootElement/>"; 
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		parser = new DefaultSpringXmlParser();
	}
	
	private static InputStream toStream(String data) {
		return new ByteArrayInputStream( data.getBytes() );
	}
	
	public void testParseInvalidFile() throws Exception {
		
		final SpringFileBeanVisitor visitor =
			createMock( SpringFileBeanVisitor.class );
		
		replay( visitor );

		try {
			parser.parse( toStream( INVALID_XML ), visitor );
			fail("Should've failed");
		} catch(NoSpringXmlException e) {
			// ok
		}
		
		verify( visitor );
	}
	
	public void testParseValidFile() throws Exception {
	
		final SpringFileBeanVisitor visitor =
			createMock( SpringFileBeanVisitor.class );
		
		visitor.visitBeanDefinition("some.package.SomeClass" );
		visitor.visitBeanDefinition("some.other.package.SomeOtherClass" );
		
		replay( visitor );
		
		parser.parse( toStream( SIMPLE_XML ), visitor );
		
		verify( visitor );
	}
	
	public void testParseValidFileWithNamespaces() throws Exception {
		
		final SpringFileBeanVisitor visitor =
			createMock( SpringFileBeanVisitor.class );
		
		visitor.visitBeanDefinition("some.package.SomeClass" );
		visitor.visitBeanDefinition("some.other.package.SomeOtherClass" );
		
		replay( visitor );
		
		parser.parse( toStream( SIMPLE_XML_WITH_NS ), visitor );
		
		verify( visitor );
	}
}
