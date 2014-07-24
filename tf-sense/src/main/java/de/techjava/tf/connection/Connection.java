package de.techjava.tf.connection;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Connection configuration.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, TYPE, METHOD, PARAMETER })
@Documented
public @interface Connection {
	/**
	 * Default connection string for connecting to a local TF Master brick
	 * connected via USB and running BrickD on default port.
	 */
	public static final String DEFAULT_CONNECTION_STRING = "localhost:4223";

	/**
	 * Connection string.
	 * 
	 * @return string formatted <b>host:port</b>. Defaults to localhost:4223.
	 */
	@Nonbinding String value() default DEFAULT_CONNECTION_STRING;
}