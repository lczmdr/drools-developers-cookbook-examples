package drools.cookbook.chapter01;

public class Virtualization {

    private String name;
    private int memory;
    private int diskSpace;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
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
