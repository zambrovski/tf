package de.techjava.tf.model.bricklet

import com.tinkerforge.BrickletAmbientLight
import com.tinkerforge.BrickletAmbientLight.IlluminanceListener
import de.techjava.tf.model.brick.MasterBrick
import de.techjava.tf.{Event, EventService}

/**
 * Ambient Light bricklet factory.
 * @author Simon Zambrovski
 */
object AmbientLightBricklet extends DeviceFactory {

  override val deviceIdentifier: Int = BrickletAmbientLight.DEVICE_IDENTIFIER
  override val typeName: String = "AmbientLight"

  override def create(id: String, position: Char) = new AmbientLightBricklet(id, position)

}

class AmbientLightBricklet(id: String, position: Char) extends Bricklet with EventSource {

  override var masterBrick: MasterBrick = _
  override val uid: String = id
  private lazy val nativeBricklet = new BrickletAmbientLight(uid, ipcon)

  /** The Device-Identifier. */
  override def deviceIdentifier: Int = AmbientLightBricklet.deviceIdentifier


  override def registerNative(): Unit = {
    nativeBricklet.addIlluminanceListener(new IlluminanceListener {
      override def illuminance(p1: Int): Unit = {
      EventService.event(new IlluminanceEvent(uid, null, p1, System.currentTimeMillis() ))
    }})
    nativeBricklet.setIlluminanceCallbackPeriod(1000)
  }
}

case class IlluminanceEvent(override val eventSource: String,
                         override val eventTarget: String,
                         override val value: Long,
                         override val timestamp: Long) extends Event {
}