package de.techjava.tf.model.connection

import com.tinkerforge._
import com.tinkerforge.IPConnectionBase._
import com.typesafe.scalalogging.slf4j.LazyLogging
import de.techjava.tf.model.bricklet._
import de.techjava.tf.service.MasterBrickService


/**
 * 
 * @author Simon Sprünker
 */
class DeviceListener extends IPConnection.EnumerateListener with LazyLogging {

  override def enumerate(brickletUid: String, masterBrickUid: String, position: Char, hardwareVersion: Array[Short],
                         firmwareVersion: Array[Short], deviceIdentifier: Int, enumerationType: Short): Unit = {

    def disconnectBricklet = {
      logger.info("Disconnecting Bricklet '{}' from MasterBrick '{}", brickletUid, masterBrickUid)
      MasterBrickService.findByUid(masterBrickUid).get.remove(brickletUid)
    }

    def connectBricklet = {
      logger.info("Connecting Bricklet '{}:{}' on MasterBrick '{}' at position '{}'", brickletUid, deviceIdentifier.toString, masterBrickUid, position.toString)

      // Create new Bricklet
      val bricklet = Bricklet.create(deviceIdentifier, brickletUid, position)

      // Set Bricklet in the MasterBrick
      MasterBrickService.findByUid(masterBrickUid).get.add(bricklet)

      // Add Listeners to Bricklets
      if (bricklet.isInstanceOf[EventSource]) {
        bricklet.asInstanceOf[EventSource].registerNative()
      }
    }

    // Only connect Bricklets
    if (masterBrickUid != "0") {
      enumerationType match {
        case ENUMERATION_TYPE_DISCONNECTED =>
          disconnectBricklet
        case ENUMERATION_TYPE_CONNECTED =>
          connectBricklet
        case ENUMERATION_TYPE_AVAILABLE =>
          logger.info(".")
        case _ =>
          logger.warn("Unknown connection state '{}'", enumerationType.toString)
      }
    }
  }

}
