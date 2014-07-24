package de.techjava.tf.model.bricklet

import de.techjava.tf.model.brick.MasterBrick
import com.tinkerforge.BrickletDualRelay

object DualRelayBricklet extends DeviceFactory {

  override val deviceIdentifier: Int = BrickletDualRelay.DEVICE_IDENTIFIER
  override val typeName: String = "DualRelay"

  override def create(id: String, position: Char) = new DualRelayBricklet(id, position)

}

/**
 * @author Simon Sprünker
 */
class DualRelayBricklet(id: String, position: Char) extends Bricklet {

  override var masterBrick: MasterBrick = _
  override val uid: String = id
  private lazy val nativeBricklet = new BrickletDualRelay(uid, ipcon)

  /** The Device-Identifier. */
  override def deviceIdentifier: Int = DualRelayBricklet.deviceIdentifier

  /**
   * Sets the given Relay (1 or 2) to the given state. True means 'on', false means 'off'.
   * @param relay Relay switch 1 or 2
   * @param state True: On, false: Off
   */
  def setSelectedState(relay: Short, state: Boolean): Unit = {

    def reallySet = {
      logger.debug("Switch relay '{}' to '{}'", relay.toString, if (state) "on" else "off")
      nativeBricklet.setSelectedState(relay, state)
    }

    val oldState = nativeBricklet.getState
    (relay, state, oldState) match {
      case (1, s, sOld) if s != sOld.relay1 => reallySet
      case (2, s, sOld) if s != sOld.relay2 => reallySet
      case _ => Unit // Nothing to do: old state = new state
    }
  }

}
