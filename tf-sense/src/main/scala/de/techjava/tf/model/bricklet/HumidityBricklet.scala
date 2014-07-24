package de.techjava.tf.model.bricklet

import com.tinkerforge.BrickletHumidity
import com.tinkerforge.BrickletHumidity.HumidityListener
import com.typesafe.scalalogging.slf4j.LazyLogging
import de.techjava.tf.{Event, EventService}
import de.techjava.tf.model.brick.MasterBrick

object HumidityBricklet extends DeviceFactory {

  override val deviceIdentifier: Int = BrickletHumidity.DEVICE_IDENTIFIER
  override val typeName: String = "Humidity"

  override def create(id: String, position: Char) = new HumidityBricklet(id, position)

}


/**
 * @author Simon Sprünker
 */
class HumidityBricklet(id: String, position: Char) extends Bricklet with EventSource {

  override var masterBrick: MasterBrick = _
  override val uid: String = id

  private lazy val nativeBricklet = new BrickletHumidity(uid, ipcon)
  /** The Device-Identifier. */
  override def deviceIdentifier: Int = HumidityBricklet.deviceIdentifier


  override def registerNative(): Unit = {
    nativeBricklet.addHumidityListener(new HumidityListener {
      override def humidity(p1: Int): Unit = {
        EventService.event(new HumidityEvent(uid, null, p1, System.currentTimeMillis() ))
      }
    })
    nativeBricklet.setHumidityCallbackPeriod(1000)
  }
}

case class HumidityEvent(override val eventSource: String,
                         override val eventTarget: String,
                         override val value: Long,
                         override val timestamp: Long) extends Event {
}
