package org.apache.maven.shared.dependency.analyzer;

import org.apache.maven.plugin.logging.Log;

/**
 * Helper interface to inject the Maven logger at runtime.
 *
 * TODO: The Maven core probably has a better way of doing this...find out how.
 * 
 * @author tobias.gierke@code-sourcery.de
 */
public interface LoggingAware {

	public void setLog(Log log);
}
