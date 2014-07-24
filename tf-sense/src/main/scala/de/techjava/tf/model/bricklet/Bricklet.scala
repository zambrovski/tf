package de.techjava.tf.model.bricklet

import de.techjava.tf.model.brick.MasterBrick
import com.tinkerforge.IPConnection
import com.typesafe.scalalogging.slf4j.LazyLogging
import de.techjava.tf.{EventSink, EventService, Event}
import scala.collection.mutable

object Bricklet {

  /** List of all known Bricklets. */
  val brickletTypes = List(DistanceIRBricklet, DualRelayBricklet, HumidityBricklet, TemperatureBricklet, AmbientLightBricklet, BarometerBricklet)

  /** Factory method to create Bricklet from Device-ID. */
  def create(deviceIdentifier: Int, brickletUid: String, position: Char): Bricklet = {
    brickletTypes
      .find(_.deviceIdentifier == deviceIdentifier)
      .get
      .create(brickletUid, position)
  }
}

/**
 * @author Simon Sprünker
 */
trait Bricklet extends LazyLogging {

  /** UID of the Bricklet. */
  val uid: String

  /** MasterBrick, to which the Bricklet is connected. */
  var masterBrick: MasterBrick

  /** IP-Connection zum Bricklet. */
  def ipcon: IPConnection = masterBrick.connection

  /** The Device-Identifier. */
  def deviceIdentifier: Int

  /** Shut down the bricklet */
  def shutdown(): Unit = {
    logger.info("Shutting down bricklet {}", uid)
  }
}

/**
 * Event source (sensor)
 */
trait EventSource {
  /**
   * Register native listener.
   */
  def registerNative(): Unit = ???
}

/**
 * Event target (actuator)
 */
trait EventTarget {
  /**
   * Receive event.
   * @param event event to transfer to native implementation.
   */
  def event(event: Event): Unit = ???
}
