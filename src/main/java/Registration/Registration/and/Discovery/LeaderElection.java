package Registration.Registration.and.Discovery;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class LeaderElection implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(LeaderElection.class);
    private static final String ELECTION_NAMESPACE = "/election";
    private final ZooKeeper zooKeeper;
    private String currentZnodeName;
    private OnElectionCallback onElectionCallback;

    public LeaderElection(ZooKeeper zooKeeper, OnElectionCallback onElectionCallback) {
        this.zooKeeper = zooKeeper;
        this.onElectionCallback = onElectionCallback;
    }

    public void volunteerForLeadership() throws KeeperException, InterruptedException {
        String znodePrefix = ELECTION_NAMESPACE + "/c_";
        String znodeFullPath = zooKeeper.create(znodePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        this.currentZnodeName = znodeFullPath.replace(ELECTION_NAMESPACE + "/", "");
        logger.info("Volunteered for leadership with znode: {}", currentZnodeName);
    }

    public boolean isLeader() {
        try {
            List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);
            Collections.sort(children);
            return currentZnodeName != null && currentZnodeName.equals(children.get(0));
        } catch (Exception e) {
            logger.error("Error checking leadership status", e);
            return false;
        }
    }
    public void reelectLeader() throws InterruptedException, KeeperException {
        String predecessorName = "";
        Stat predecessorStat = null;

        //this while to guarantee get predecessor even if it deleted just before zookeeper.exist
        while (predecessorStat == null) {
            List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);
            Collections.sort(children);

            String smallestChild = children.get(0); //the first element
            if (smallestChild.equals(currentZnodeName)) {
                logger.info("I'm a leader");
                onElectionCallback.onElectedToBeLeader();

                return;
            } else {
                System.out.println("I'm not a leader");
                logger.info("I'm not a leader");
                int predecessorIndex = children.indexOf(currentZnodeName) - 1;
                predecessorName = children.get(predecessorIndex);
                predecessorStat = zooKeeper.exists(ELECTION_NAMESPACE + "/" + predecessorName, this);
            }
        }
        onElectionCallback.onWorker();
//        System.out.println("Watching znode " + predecessorName);
        logger.info("Watching znode {}", predecessorName);
        System.out.println();

    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            reelectLeader();
        } catch (KeeperException | InterruptedException e) {
            logger.error("Error processing watched event", e);
        }
    }
}

