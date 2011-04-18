package drools.cookbook.chapter07.model;

import java.io.Serializable;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class Virtualization implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String serverName;
    private int memory;
    private int diskSpace;

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

}
