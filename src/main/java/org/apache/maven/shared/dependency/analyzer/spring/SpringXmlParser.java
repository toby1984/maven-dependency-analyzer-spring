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
