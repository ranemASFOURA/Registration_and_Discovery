package Registration.Registration.and.Discovery;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OnElectionAction implements OnElectionCallback {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
    private final ServiceRegistry serviceRegistry;
    private final int port;

    public OnElectionAction(ServiceRegistry serviceRegistry, int port) {
        this.serviceRegistry = serviceRegistry;
        this.port = port;
    }

    @Override
    public void onElectedToBeLeader() {
        serviceRegistry.unregisterFromCluster();
        serviceRegistry.registerForUpdates();
    }

    @Override
    public void onWorker() {
        try {
            String currentServerAddress = String.format("http://%s:%d", InetAddress.getLocalHost().getCanonicalHostName(), port);
            serviceRegistry.registerToCluster(currentServerAddress);
        } catch (InterruptedException | UnknownHostException | KeeperException e) {
            logger.error("Error during worker registration", e);
        }
    }
}

