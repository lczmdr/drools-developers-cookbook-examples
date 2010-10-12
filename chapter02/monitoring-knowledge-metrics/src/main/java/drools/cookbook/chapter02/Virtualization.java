package drools.cookbook.chapter02;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Lucas Amador
 *
 */
public class Virtualization {

    private String name;
    private String serverName;
    private int memory;
    private int diskSpace;
    private List<Service> services = new ArrayList<Service>();

    public Virtualization(String name, String serverName, int memory, int diskSpace) {
        this.name = name;
        this.serverName = serverName;
        this.memory = memory;
        this.diskSpace = diskSpace;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getServerName() {
        return serverName;
    }
    public void setMemory(int memory) {
        this.memory = memory;
    }
    public int getMemory() {
        return memory;
    }
    public void setDiskSpace(int diskSpace) {
        this.diskSpace = diskSpace;
    }
    public int getDiskSpace() {
        return diskSpace;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Service> getServices() {
        return services;
    }

    public void addService(Service service) {
        this.services.add(service);
    }

}
