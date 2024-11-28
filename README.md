Here is a detailed example of a `README.md` file for the provided project:

```markdown
# Registration and Discovery Service

This project implements a **Registration and Discovery Service** using **Apache ZooKeeper** for distributed systems. The service supports leader election and service discovery by leveraging ZooKeeper's features, enabling high availability and fault tolerance.

## Features

1. **Leader Election**: 
   - Ensures a single leader is elected among multiple nodes.
   - The leader node manages critical tasks, while other nodes act as workers.

2. **Service Registration**:
   - Nodes register themselves with the service registry in ZooKeeper.
   - Registered nodes' metadata (e.g., address) is stored for discovery purposes.

3. **Service Discovery**:
   - Provides a list of all active services in the cluster.
   - Updates dynamically as services are added or removed.

4. **Spring Boot Integration**:
   - The application uses Spring Boot for rapid development and configuration management.

## Prerequisites

- **Java 11 or later**.
- **Apache ZooKeeper** running on the configured host.
- **Maven** for project dependency management.

## Configuration

The application settings are managed through `application.properties`:
```properties
server.port=8080
election.port=8080
spring.application.name=Registration-and-Discovery
zookeeper.address=192.168.184.129:2181
zookeeper.sessionTimeout=3000
```

### Key Properties:
- `server.port`: The port for the HTTP server.
- `election.port`: The port for leader election and service metadata.
- `spring.application.name`: The name of the Spring Boot application.
- `zookeeper.address`: ZooKeeper server address.
- `zookeeper.sessionTimeout`: Session timeout for ZooKeeper connections.

## Code Structure

- **`ZooKeeperConfig`**: Configures ZooKeeper and creates beans for `ZooKeeper` and `LeaderElection`.
- **`ServiceRegistry`**: Handles service registration and updates in ZooKeeper.
- **`LeaderElection`**: Implements leader election logic using ZooKeeper ephemeral nodes.
- **`OnElectionAction`**: Defines actions performed when a node becomes a leader or worker.
- **`DiscoveryController`**: RESTful API to check cluster status and list active services.
- **`Application`**: Main application logic, including ZooKeeper initialization and leader re-election.

## REST API Endpoints

1. **Check Status**:
    - **URL**: `/status`
    - **Method**: GET
    - **Response**:
        - `"Server is currently: Leader"`
        - `"Server is currently: Worker"`

## Key Classes and Their Responsibilities

### 1. `ZooKeeperConfig`
- Configures the ZooKeeper connection using Spring's `@Value` annotation.
- Defines beans for `ZooKeeper` and `LeaderElection`.

### 2. `LeaderElection`
- Handles leader election using ZooKeeper ephemeral sequential nodes.
- Watches for changes in the election namespace to re-elect leaders.

### 3. `ServiceRegistry`
- Manages service registration under the `/service_registry` znode.
- Updates and unregisters services dynamically.

### 4. `OnElectionAction`
- Defines actions for leader (`onElectedToBeLeader`) and worker (`onWorker`) roles.

### 5. `DiscoveryController`
- Provides REST endpoints for status and service discovery.

## Example Workflow

1. Multiple instances of the application connect to ZooKeeper.
2. Each instance:
    - Registers itself in the service registry.
    - Volunteers for leadership.
3. ZooKeeper elects a leader.
4. The leader monitors other services and updates the registry as needed.
5. Clients interact with the system via the REST API for discovery and monitoring.


