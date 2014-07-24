package de.techjava.tf.model.brick

import com.tinkerforge.IPConnection
import com.typesafe.scalalogging.slf4j.LazyLogging
import java.lang.Exception
import de.techjava.tf.Configuration
import de.techjava.tf.model.connection.DeviceListener
import de.techjava.tf.model.bricklet.Bricklet
import scala.annotation.meta.field

/**
 * The MasterBrick.
 *
 * @author Simon Sprünker
 */
class MasterBrick(id: String) extends LazyLogging {

  /**
   * UID of the Brick as read from the firmware.
   */
  val uid = id

  /**
   * Connected Bricklets.
   */
  private var bricklets = Set[Bricklet]()

  /**
   * Collects all information about the devices connected to this
   * MasterBrick and holds the callback Methods.
   */
  val deviceListener = new DeviceListener

  val connection = readConnection
  
  /**
   * read IP Connection
   */
  def readConnection: IPConnection = {
    val config: Array[String] = Configuration.masterBricks.getOrElse(id, Array("localhost:4223"))
    val connection = new IPConnection
    val host = config(1).split(":")(0)
    val port = 4223
    connection.connect(host, port)
    return connection
  }

  /**
   * Retrieves the name of the MasterBrick.
   * @return Name of the MasterBrick
   */
  def name = Configuration.masterBricks.getOrElse(id, "unknown")

  /**
   * Adds Bricklet to this MasterBrick. Called by the enumerate-Callback.
   * @param bricklet Bricklet to add to this MasterBrick.
   */
  def add(bricklet: Bricklet) = {
    bricklet.masterBrick = this
    bricklets = bricklets + bricklet
  }

  /**
   * Adds a list of Bricklets to this MasterBrick.
   * @param newBricklets List of Bricklets.
   */
  def add(newBricklets: Iterable[Bricklet]): Unit = newBricklets.foreach(bricklet => add(bricklet))

  /**
   * Remove the given Bricklet from this MasterBrick.
   * @param bricklet Bricklet to remove.
   */
  def remove(bricklet: Bricklet) = {
    bricklet.masterBrick = null
    bricklet.shutdown
    bricklets = bricklets - bricklet
  }

  /**
   * Remove the Bricklet with the given UID from this MasterBrick.
   * @param brickletUid UID of the Bricklet to remove.
   */
  def remove(brickletUid: String): Unit = remove(bricklets.find(bricklet => bricklet.uid.equals(brickletUid)).get)

  def findBrickletByUid(brickletUid: String) = bricklets.find(bricklet => bricklet.uid.equals(brickletUid))
  def findBrickletByDevice(deviceIdentifier: Int): Set[Bricklet] = bricklets.filter(bricklet => bricklet.deviceIdentifier == deviceIdentifier)

  /**
   * Connects the master brick.
   */
  def connect() = {
    logger.info("Connecting to MasterBrick[{}] '{}'", id, name)

    try {
      connection.addEnumerateListener(deviceListener)
      // Call the enumerate-Callback on all connected devices
      connection.enumerate()
    } catch {
      case ex: Exception => logger.warn("Could not connect to MasterBrick {} :", name, ex)
    }
  }
  
  /**
   * Disconnects master brick.
   */
  def disconnect() = {
    logger.info("Disconnecting from MasterBrick[{}] '{}'", id, name)
    connection.disconnect();
  }
}
