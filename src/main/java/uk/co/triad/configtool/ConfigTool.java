package uk.co.triad.configtool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.co.triad.configtool.service.ConfigGenerationService;
import uk.co.triad.configtool.service.HostnameDetectionService;

/**
 * Created with IntelliJ IDEA.
 * User: gfountas
 * Date: 08/09/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
public class ConfigTool {

    private static final Logger LOG = LogManager.getLogger(ConfigTool.class.getName());

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        HostnameDetectionService hostnameDetectionService = (HostnameDetectionService)context.getBean(HostnameDetectionService.class);

        ConfigGenerationService configGenerationService = (ConfigGenerationService)context.getBean(ConfigGenerationService.class);
        String configurationDirectory=null;
        String keyValuePairDirectory=null;

        String hostname = null;
        if (areArgumentsValid(args)) {
            configurationDirectory = args[0];
            keyValuePairDirectory = args[1];

            if (args.length >2) {
                hostname = args[2];
            }
            else {
                hostname = hostnameDetectionService.getHostName();
            }
            configGenerationService.generateConfig(configurationDirectory,keyValuePairDirectory,hostname);
        }





    }
    public static boolean areArgumentsValid(String[] args){
        if (args.length <2) {
            LOG.error("Syntax: java -jar ConfigTool <Configuration File Dir> <Key Value Pair File Dir> [hostname]");
            return false;
        }
        return true;
    }
}
