package org.apache.maven.shared.dependency.analyzer;

import org.apache.maven.plugin.logging.Log;

public interface LoggingAware {

	public void setLog(Log log);
}
