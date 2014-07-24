package de.techjava.tf.connection;

/**
 * Host & Port pair.
 */
public class HostAndPort {

	String host;
	short port;

	/**
	 * Parses the connection string
	 * 
	 * @param connectionString
	 * @return
	 */
	public final static HostAndPort valueOf(final String connectionString) {
		String[] split = connectionString.split(":");
		HostAndPort hostAndPort = null;
		if (split != null && split.length == 2) {
			hostAndPort = new HostAndPort();
			hostAndPort.host = split[0];
			try {
				hostAndPort.port = Short.parseShort(split[1]);
			} catch (NumberFormatException nfe) {
				hostAndPort = null;
			}
		}
		return hostAndPort;
	}

	@Override
	public String toString() {
		return "HostAndPort [host=" + host + ", port=" + port + "]";
	}
	
	

}