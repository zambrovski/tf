package de.techjava.tf.service

import de.techjava.tf.model.brick.MasterBrick
import de.techjava.tf.model.bricklet.{DualRelayBricklet, DeviceFactory, Bricklet}

/**
 * Provides convenience methods to deal with the MasterBricks.
 *
 * @author Simon Sprünker
 */
object MasterBrickService {

  private var masterBricks = Set[MasterBrick]()

  // unused
  def add(masterBrick: MasterBrick) = masterBricks = masterBricks + masterBrick

  def add(newMasterBricks: Iterable[MasterBrick]) = masterBricks = masterBricks ++ newMasterBricks

  // unused
  def remove(masterBrick: MasterBrick) = masterBricks = masterBricks - masterBrick

  def findByUid(masterBrickUid: String): Option[MasterBrick] = masterBricks.find(brick => brick.uid.equals(masterBrickUid))

  def findBrickletsOfType(example: Bricklet): Set[Bricklet] = {
    masterBricks.map(brick => brick.findBrickletByDevice(example.deviceIdentifier)).flatten
  }

  // unused
  def findDualRelayBricklet(uid: String): DualRelayBricklet = {
    // Find a Set of matching Bricklets (hopefully exactly one)
    val bricklets = masterBricks.map(brick => brick.findBrickletByUid(uid)).flatten
    if (bricklets.size == 1) {
      bricklets.head match {
        case dualRelayBricklet: DualRelayBricklet => dualRelayBricklet
        case _ => null
      }
    } else null
    //masterBricks.map(brick => brick.findBrickletByUid(uid)).flatten.filter(bricklet => bricklet.isInstanceOf[DualRelayBricklet]).head
  }

  /** 
   *  Connect all MasterBricks. 
   **/
  def connect() = {
    masterBricks.map(brick => brick.connect())
  }
  
   /** 
   *  Disconnect all MasterBricks. 
   **/
  def disconnect() = {
    masterBricks.map(brick => brick.disconnect())
  }
}
