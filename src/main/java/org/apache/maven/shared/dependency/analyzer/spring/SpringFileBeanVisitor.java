package org.apache.maven.shared.dependency.analyzer.spring;

/**
 * Visitor that gets applied to a Spring bean definition.
 *
 * @author tobias.gierke@code-sourcery.de
 * @see SpringXmlParser#parse(java.io.InputStream, SpringXmlFileBeanVisitor) 
 */
public interface SpringFileBeanVisitor {

	/**
	 * Visits bean definition.
	 * 
	 * @param beanClasz The bean's type
	 * @throws Exception
	 * @see SpringXmlParser#parse(java.io.InputStream, SpringXmlFileBeanVisitor)
	 */
	public void visitBeanDefinition(String beanClasz) throws Exception;
	
}
