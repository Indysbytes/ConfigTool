package uk.co.triad.configtool.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: gfountas
 * Date: 08/09/14
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
@Service
public class HostnameDetectionServiceImpl implements HostnameDetectionService{

    private static final String HOSTNAME_ENVIRONMENT_VARIABLE = "HOSTNAME";
    private static final Logger LOG = LogManager.getLogger(HostnameDetectionServiceImpl.class.getName());

    @Override
    public String getHostName() {
        String hostname = null;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostName();
        } catch (UnknownHostException e) {
            LOG.error(e);

            hostname = System.getenv(HOSTNAME_ENVIRONMENT_VARIABLE);
            if (hostname!=null) {
                return hostname;
            }
        }

        return null;
    }
}
