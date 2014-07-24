package de.techjava.tf.model.bricklet

import com.tinkerforge.BrickletBarometer
import com.tinkerforge.BrickletBarometer.AirPressureListener
import de.techjava.tf.model.brick.MasterBrick
import de.techjava.tf.{EventService, Event}
import scala.collection.mutable
import java.sql.Date

/**
 * Barometer bricklet factory.
 */
object BarometerBricklet extends DeviceFactory {

  override val deviceIdentifier: Int = BrickletBarometer.DEVICE_IDENTIFIER
  override val typeName: String = "Barometer"

  override def create(id: String, position: Char) = new BarometerBricklet(id, position)

}

class BarometerBricklet(id: String, position: Char) extends Bricklet with EventSource {

  override var masterBrick: MasterBrick = _
  override val uid: String = id
  private lazy val nativeBricklet = new BrickletBarometer(uid, ipcon)

  /** The Device-Identifier. */
  override def deviceIdentifier: Int = BarometerBricklet.deviceIdentifier


  override def registerNative(): Unit = {
    nativeBricklet.addAirPressureListener(new AirPressureListener {
      override def airPressure(p1: Int): Unit = {
        EventService.event(new PressureEvent(uid, null, p1, System.currentTimeMillis() ))
      }
    })
    nativeBricklet.setAirPressureCallbackPeriod(1000)
  }
}

  case class PressureEvent(override val eventSource: String,
                         override val eventTarget: String,
                         override val value: Long,
                         override val timestamp: Long) extends Event {
}