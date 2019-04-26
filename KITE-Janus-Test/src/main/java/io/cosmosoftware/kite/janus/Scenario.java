package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.axel.Instance;
import io.cosmosoftware.kite.axel.Instrumentation;
import io.cosmosoftware.kite.manager.SSHManager;
import io.cosmosoftware.kite.util.ReportUtils;
import org.apache.log4j.Logger;

import javax.json.JsonObject;

public class Scenario {

  protected  Integer clientId;
  protected  String name;
  protected  String gateway;
  protected  String command;
  protected  Integer duration;
  private final Logger logger;

  public Scenario(JsonObject jsonObject, Logger logger) throws Exception {

    Integer jsonClientId = jsonObject.getInt("clientId", -1);
    if (jsonClientId < 0) {
      clientId =0;
    } else {
      clientId = jsonClientId;
    }
    String jsonGateway = jsonObject.getString("gateway");
    if (jsonGateway == null) {
      throw new Exception("Error in json config scenario, gateway is invalid.");
    } else {
      gateway = jsonGateway;
    }
    String jsonCommand = jsonObject.getString("command");
    if (jsonCommand == null) {
      throw new Exception("Error in json config scenario, command is invalid.");
    } else {
      command = jsonCommand;
    }
    String jsonName  = jsonObject.getString("name");
    if (jsonName == null) {
      name = command;
    } else {
      name = jsonName;
    }
    Integer jsonDuration = jsonObject.getInt("duration");
    if (jsonDuration < 0 || jsonDuration == 0) {
      duration = 10000;
    } else {
      duration = jsonDuration;
    }
    this.logger = logger;
  }

  public String getName() {
    return name;
  }

  public int getClientId() {
    return clientId;
  }

  public String getGateway() {
    return gateway;
  }

  public String getCommand() {
    return command;
  }

  public Integer getDuration() {
    return duration;
  }

  public String runCommands(Instrumentation instrumentation) {
    logger.info("Trying to run " + this.command + " on " + this.gateway);
    Instance instance = instrumentation.getInstanceById(this.gateway);
    logger.info("TEST : " + instance.getIpAddress());
    String result = this.command;
    logger.info("Executing command : " + command + " on " + instance.getIpAddress());
    try {
      SSHManager sshManager = new SSHManager(instance.getKeyFilePath(), instance.getUsername(),
          instance.getIpAddress(), command);
      if (sshManager.call().commandSuccessful()) {
        Thread.sleep(this.duration);
        logger.info("runCommands() : \r\n" + command);
        result += "  SUCCESS (Client : " + instance.getIpAddress() + ")";
      } else {
        logger.error("Failed runCommands() : \r\n" + this.command);
        result += "  FAILURE (Client : " + instance.getIpAddress() + ")";
      }
    } catch (Exception e) {
      logger.error(
          "runCommand(). Command:\r\n"
              + command
              + "\r\n"
              + ReportUtils.getStackTrace(e));
      result += "  Error " + e.getMessage();
    }
    return result;
  }

  public String cleanUp(Instrumentation instrumentation) {
    String result = "";
    Instance instance = instrumentation.getInstanceById(this.gateway);
    String[]  interfacesList = {instance.getNit0(), instance.getNit1(), instance.getNit2()};
    for (String inter : interfacesList) {
      if (this.command.contains(inter)) {
        String cleanUpCommand = "sudo tc qdisc del dev " + inter + " root";
        try {
          SSHManager sshManager = new SSHManager(instance.getKeyFilePath(), instance.getUsername(),
              instance.getIpAddress(), cleanUpCommand);
          if (sshManager.call().commandSuccessful()) {
            Thread.sleep(1000);
            logger.info("cleanUp() : " + inter);
            result += cleanUpCommand;
          } else {
            logger.error("Failed cleanUp() : " + inter);
            result += "  FAILURE (Client : " + instance.getIpAddress() + ")";
          }
        } catch (Exception e) {
          logger.error(
              "cleanUp(). \r\n"
                  + inter
                  + "\r\n"
                  + ReportUtils.getStackTrace(e));
          result += "  Error " + e.getMessage();
        }
      }
      else {
        logger.info("No CleanUp to do on interface : " + inter);
      }
    }
    return result;

  }


}
