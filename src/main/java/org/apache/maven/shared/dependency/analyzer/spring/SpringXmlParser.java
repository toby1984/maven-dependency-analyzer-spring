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

import java.io.InputStream;

/**
 * Scans a Spring XML configuration for
 * bean definitions and invokes a {@link SpringFileBeanVisitor}
 * for each of them.
 *
 * @author tobias.gierke@code-sourcery.de
 * @see SpringFileBeanVisitor
 */
public interface SpringXmlParser {
	
	/**
	 * Thrown when a input XML could not be
	 * recoganized as Spring XML configuration data.
	 * 
	 * @author tobias.gierke@code-sourcery.de
	 */
	public static final class NoSpringXmlException extends java.text.ParseException {
		
		private static final long serialVersionUID = 1L;

		public NoSpringXmlException(String msg,int offset) {
			super( msg , offset );
		}
	}

	/**
	 * Parses a Spring XML configuration while
	 * invoking a {@link SpringFileBeanVisitor}
	 * for each bean definition.
	 * 
	 * @param springXml the Spring XML configuration to process
	 * @param beanVisitor The visitor to invoke
	 * @throws Exception
	 * @throws NoSpringXmlException
	 */
	public void parse(InputStream springXml, SpringFileBeanVisitor beanVisitor) throws Exception,NoSpringXmlException;
}
