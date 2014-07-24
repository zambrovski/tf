package de.techjava.tf

import com.tinkerforge.IPConnection
import org.jboss.arquillian.core.api.annotation.Inject
import scala.annotation.meta.field
import de.techjava.tf.connection.IpConnectionFactory

/**
 * Configuration of the ids and logical names.
 * @author Simon Sprünker
 * @author Simon Zambrovski
 */
object Configuration {

  val NONE = "none"


  /**
   * Maps MasterBrick-IDs to their names.
   */
  val masterBricks = Map(
    // Simon Zambrovski ClimateStation
    "6r12b9" -> Array("ClimateStation", "192.168.0.96:4223")  
    // Simon Sprünker Schrank
    // "68z4Qu" -> "living room"
    )

  /**
   * Maps sensor ids to their names.
   */
  val sensors = Map(
    // Simon Zambrovski Sensorik
    "nnA" -> "Temperatur",
    "k61" -> "Druck",
    "mcy" -> "Helligkeit",
    "kfp" -> "Feuchtigkeit",

    // Simon Sprünker Sensorik
    "kCg" -> "Relais",
    "hK1" -> "Abstand")

}
