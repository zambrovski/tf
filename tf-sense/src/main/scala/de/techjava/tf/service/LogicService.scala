package de.techjava.tf.service

import de.techjava.tf.model.bricklet.{Bricklet, DualRelayBricklet, DistanceIRBricklet}
import com.tinkerforge.BrickletDistanceIR.DistanceListener
import com.typesafe.scalalogging.slf4j.LazyLogging
import com.tinkerforge.DeviceListener
import de.techjava.tf.EventService

/**
 * This service defines the listeners containing the business logic and provides methods of assigning these listeners
 * to different Bricklets.
 *
 * @author Simon Sprünker
 */
object LogicService extends LazyLogging {


  val livingRoomCabinetRelayUid = "kCg"
  val livingRoomCabinetDistanceUid = "hK1"

  /**
   * Method that controls the lighting in the living room cabinet.
   *
   * @return
   */
  def livingRoomCabinetDistanceListener = {
    new DistanceListener {
      override def distance(distance: Int): Unit = {
        // Find Bricklet
        val dualRelayBricklet: DualRelayBricklet = MasterBrickService.findDualRelayBricklet(livingRoomCabinetRelayUid)
        // Switch on light, if cabinet is opened
        if (distance < 99) dualRelayBricklet.setSelectedState(1, false)
        else dualRelayBricklet.setSelectedState(1, true)
      }
    }
  }

  /**
   * This method takes a Bricklet as Parameter. It decides wich listeners get assigned to that Bricklet.
   *
   * @param bricklet Bricklet to assign listeners to.
   */
  def addListeners(bricklet: Bricklet) = {
    bricklet match {
      case distanceIRBricklet : DistanceIRBricklet if (bricklet.uid == livingRoomCabinetDistanceUid) =>
//        distanceIRBricklet.addListener(livingRoomCabinetDistanceListener)
      case _ => Unit
    }
  }


//  EventService.addProcess(event => logger.info("{}", event.toString))
//  EventService.addProcess(event => )

}
