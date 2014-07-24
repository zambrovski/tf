package de.techjava.tf.model.bricklet

import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * @author Simon Sprünker
 */
trait DeviceFactory extends LazyLogging {

  /** The Device-identifier (what type of device) */
  val deviceIdentifier: Int

  /** Name of the Bricklet Type (Distance, Temperature, Humidity, ...). */
  val typeName: String

  /** Creates an instance of the Bricklet. */
  def create(id: String, position: Char): Bricklet
}
