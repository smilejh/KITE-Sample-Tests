package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.instrumentation.Instance;
import io.cosmosoftware.kite.instrumentation.Instrumentation;
import io.cosmosoftware.kite.manager.SSHManager;
import io.cosmosoftware.kite.util.ReportUtils;
import org.apache.log4j.Logger;

import javax.json.JsonObject;

public class Scenario {

  private  Integer clientId = 0;
  private  String name;
  private  String gateway;
  private  String command;
  private  Integer duration = 10000;
  private final Logger logger;

  public Scenario(JsonObject jsonObject, Logger logger, Integer i) throws Exception {

    Integer jsonClientId = jsonObject.getInt("clientId", 0);
    if (jsonClientId >= 0) {
      clientId = jsonClientId;
    }
    if (jsonObject.containsKey("gateway")) {
      gateway = jsonObject.getString("gateway");
    } else {
      throw new Exception("Error in json config scenario, gateway is invalid.");
    }
    if (jsonObject.containsKey("command")) {
      command = jsonObject.getString("command");
    } else {
      throw new Exception("Error in json config scenario, command is invalid.");
    }
    name = jsonObject.getString("name", "Scenario number : " + i);
    Integer jsonDuration = jsonObject.getInt("duration", 10000);
    if (jsonDuration > 0) {
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
    Instance instance = instrumentation.get(this.gateway);
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
    Instance instance = instrumentation.get(this.gateway);
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
