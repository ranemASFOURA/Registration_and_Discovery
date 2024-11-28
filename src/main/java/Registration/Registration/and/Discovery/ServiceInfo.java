package Registration.Registration.and.Discovery;

public class ServiceInfo {
    private String name;
    private String address;

    public ServiceInfo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

