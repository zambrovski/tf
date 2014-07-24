package de.techjava.tf

import de.techjava.tf.model.bricklet.Bricklet
import com.typesafe.scalalogging.slf4j.LazyLogging
import scala.collection.mutable

/**
 * An event service is responsible for unifying events from different sources/sensors.
 * @author Simon Zambrovski
 */
object EventService extends EventSink {

}

/**
 * Responsible for writing events...
 */
trait EventSink extends LazyLogging {

  /**
   * Filters are registered per logical event source
   */
  val filters = collection.mutable.Map[String, Seq[Event => Boolean]]()
  val NONE: Event => Boolean = event => true
  filters.put(Configuration.NONE, new mutable.MutableList[Event => Boolean] :+ NONE)

  /**
   * Event reception.
   * @param event event to be delivered to the sink.
   */
  def event(event: Event) = {

    // retrieve the logical name of the pipe
    val pipeName = Configuration.sensors.getOrElse(event.eventSource, Configuration.NONE)

    // get the pipe
    val pipe = filters.get(pipeName)

    // TODO

    logger.info("{} {}", pipeName, event.toString)
  }

  def addProcess(process: Event => Event): Unit = {

  }

}

/**
 * Represents a generic event.
 * eventSource source id of the bricklet
 * eventTarget target id of the bricklet
 * value contained value.
 * timestamp time of event creation
 */
trait Event {
  def eventSource: String
  def eventTarget: String
  def value: Long
  def timestamp: Long
}

trait Filter[T] {
  def filter(element: Option[T]): Option[T] = ???
}

class FilterChain[T](head: Filter[T], tail: Filter[T]) extends Filter[T] {
  override def filter(elem: Option[T]): Option[T] = ???

}

class EventFilter(criteria: Event => Boolean) extends Filter[Event] {
  override def filter(event: Option[Event]): Option[Event] = {
    event match {
      case Some(e) => criteria.apply(e) match {
        case true => Option(e)
        case _ => None
      }
      case None => None
    }
  }
}

