package de.techjava.tf.model.bricklet

import com.tinkerforge.BrickletTemperature
import com.tinkerforge.BrickletTemperature.TemperatureListener
import com.typesafe.scalalogging.slf4j.LazyLogging
import de.techjava.tf.model.brick.MasterBrick
import de.techjava.tf.{Event, EventService}

object TemperatureBricklet extends DeviceFactory {

  override val deviceIdentifier: Int = BrickletTemperature.DEVICE_IDENTIFIER
  override val typeName: String = "Temperature"

  override def create(id: String, position: Char) = new TemperatureBricklet(id, position)

}

/**
 * @author Simon Sprünker
 */
class TemperatureBricklet(id: String, position: Char) extends Bricklet with EventSource {

  override var masterBrick: MasterBrick = _
  override val uid: String = id


  private lazy val nativeBricklet = new BrickletTemperature(uid, ipcon)
  /** The Device-Identifier. */
  override def deviceIdentifier: Int = TemperatureBricklet.deviceIdentifier

  override def registerNative(): Unit = {
    nativeBricklet.addTemperatureListener(new TemperatureListener {
      override def temperature(p1: Short): Unit = {
        EventService.event(new TemperatureEvent(uid, null, p1, System.currentTimeMillis() ))
      }
    })
    nativeBricklet.setTemperatureCallbackPeriod(1000)
  }
}

case class TemperatureEvent(override val eventSource: String,
                         override val eventTarget: String,
                         override val value: Long,
                         override val timestamp: Long) extends Event {
}