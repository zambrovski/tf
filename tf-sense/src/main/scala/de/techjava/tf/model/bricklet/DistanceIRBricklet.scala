package de.techjava.tf.model.bricklet

import com.tinkerforge.BrickletDistanceIR
import com.tinkerforge.BrickletDistanceIR.DistanceListener
import de.techjava.tf.model.brick.MasterBrick
import de.techjava.tf.{Event, EventService}

object DistanceIRBricklet extends DeviceFactory {

  override val deviceIdentifier: Int = BrickletDistanceIR.DEVICE_IDENTIFIER
  override val typeName: String = "Distance"

  override def create(id: String, position: Char) = new DistanceIRBricklet(id, position)

}

/**
 * @author Simon Sprünker
 */
class DistanceIRBricklet(id: String, position: Char) extends Bricklet with EventSource {

  override var masterBrick: MasterBrick = _
  override val uid: String = id

  private lazy val nativeBricklet = new BrickletDistanceIR(uid, ipcon)
  override def deviceIdentifier: Int = DistanceIRBricklet.deviceIdentifier

  override def registerNative(): Unit = {
    nativeBricklet.addDistanceListener(new DistanceListener {
      override def distance (p1: Int): Unit = {
        EventService.event(new DistanceEvent(uid, null, p1, System.currentTimeMillis() ))
      }
    })
    nativeBricklet.setDistanceCallbackPeriod(100)
  }
}

case class DistanceEvent(override val eventSource: String,
                         override val eventTarget: String,
                         override val value: Long,
                         override val timestamp: Long) extends Event {
}
