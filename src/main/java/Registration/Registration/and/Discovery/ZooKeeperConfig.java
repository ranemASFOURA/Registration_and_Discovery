package Registration.Registration.and.Discovery;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ZooKeeperConfig {

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    @Value("${zookeeper.sessionTimeout}")
    private int sessionTimeout;

    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        return new ZooKeeper(zookeeperAddress, sessionTimeout, watchedEvent -> {});

    }

    @Bean
    public LeaderElection leaderElection(ZooKeeper zooKeeper, ServiceRegistry serviceRegistry) {
        OnElectionCallback callback = new OnElectionAction(serviceRegistry, 8080);
        return new LeaderElection(zooKeeper, callback);
    }
}
