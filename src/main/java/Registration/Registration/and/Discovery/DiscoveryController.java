package Registration.Registration.and.Discovery;


import org.apache.zookeeper.KeeperException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiscoveryController {

    private final ServiceRegistry serviceRegistry;
    private final LeaderElection leaderElection;

    public DiscoveryController(ServiceRegistry serviceRegistry, LeaderElection leaderElection) {
        this.serviceRegistry = serviceRegistry;
        this.leaderElection = leaderElection;
    }
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        String status = leaderElection.isLeader() ? " Server is currently:Leader" : "Server is currently:Worker";
        return ResponseEntity.ok(status);
    }

    @GetMapping("/services")
    public ResponseEntity<List<String>> getServices() {
        List<String> services = serviceRegistry.getAllServiceAddresses();
        if (services == null || services.isEmpty()) {
            return ResponseEntity.ok(List.of("No services registered."));
        }
        return ResponseEntity.ok(services);
    }
}

