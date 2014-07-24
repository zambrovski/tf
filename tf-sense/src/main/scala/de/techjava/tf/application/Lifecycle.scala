package de.techjava.tf.application

import javax.servlet.annotation.WebServlet
import com.typesafe.scalalogging.slf4j.LazyLogging
import javax.servlet.http.HttpServlet
import javax.servlet.ServletConfig
import javax.servlet.ServletException
import de.techjava.tf.Configuration
import de.techjava.tf.model.brick.MasterBrick
import de.techjava.tf.service.MasterBrickService
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.servlet.ServletContextEvent

/**
 * Starter for callback registration.
 *
 * @author Simon Zambrovski
 *
 */
@WebServlet(urlPatterns = Array("/starter"), loadOnStartup = 1)
class Init extends HttpServlet with LazyLogging {

  val serialVersionUID = 1L

  @throws[ServletException]
  override def init(config: ServletConfig) = {
    super.init(config);

    logger.info("Initializing starter");
    // Create all known MasterBricks
    val masterBricks = Configuration.masterBricks.keys.map(brickUid => new MasterBrick(brickUid))
    // Add them to the Service, so the service has a list of all Bricks
    MasterBrickService.add(masterBricks)

    // Connect all MasterBricks
    logger.info("Connecting...");
    MasterBrickService.connect()
  }

}

@WebListener
class Stop extends ServletContextListener with LazyLogging {

  override def contextInitialized(sce: ServletContextEvent) {
    
  }

  override def contextDestroyed(sce: ServletContextEvent) = {
	  MasterBrickService.disconnect();
	  logger.info("Disconnected.");
  }  
}




