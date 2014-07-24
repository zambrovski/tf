package de.techjava.tf.connection;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

public class IpConnectionFactory {
	private Logger logger = LoggerFactory.getLogger(IpConnectionFactory.class);

	/**
	 * Produces TinkerForge IP Connection.
	 * 
	 * @param injectionPoint
	 *            injection point of connection.
	 * @return IP Connection.
	 */
	@Produces
	@Connection
	public IPConnection createIPConnection(final InjectionPoint injectionPoint) {
		final HostAndPort hostAndPort = extractHostAndPort(injectionPoint);
		return createIPConnection(hostAndPort);
	}

	public IPConnection createIPConnection(final String connectionString) {
		final HostAndPort hostAndPort = HostAndPort.valueOf(connectionString);
		return createIPConnection(hostAndPort);
	}
	
	public IPConnection createIPConnection(final HostAndPort hostAndPort) {
		logger.info("Producing IP Connection to: " + hostAndPort);
		try {
			final IPConnection connection = new IPConnection();
			connection.setAutoReconnect(true);
			connection.connect(hostAndPort.host, hostAndPort.port);
			return connection;
		} catch (UnknownHostException e) {
			logger.error("Unknown host initializing connection", e);
		} catch (AlreadyConnectedException e) {
			logger.error("Already connected initializing connection", e);
		} catch (IOException e) {
			logger.error("Error creating connection", e);
		}
		return null;		
	}

	/**
	 * Extracts host and port from annotation on injection point.
	 * 
	 * @param injectionPoint
	 *            injection point containing configuration, or empty for
	 *            default.
	 * @return host and port.
	 */
	private HostAndPort extractHostAndPort(final InjectionPoint injectionPoint) {
		final Annotated annotated = injectionPoint.getAnnotated();
		final Connection annotation = annotated.getAnnotation(Connection.class);
		HostAndPort hostAndPort = null;
		if (annotation != null) {
			hostAndPort = HostAndPort.valueOf(annotation.value());
		}
		if (hostAndPort == null) {
			hostAndPort = HostAndPort
					.valueOf(Connection.DEFAULT_CONNECTION_STRING);
		}
		return hostAndPort;
	}

	/**
	 * Disposal.
	 * 
	 * @param connection
	 *            connection to dispose.
	 */
	public void closeConnection(
			@Disposes @Connection final IPConnection connection) {
		try {
			connection.disconnect();
		} catch (NotConnectedException e) {
			logger.error("Error disconnecting", e);
		}
	}

}
