package drools.cookbook.chapter08.domain;

public class Talk {

    private String name;
    private long requiredRoomSize;
    private boolean mainRoom;

    public Talk(String name, long requiredRoomSize, boolean mainRoom) {
        this.name = name;
        this.requiredRoomSize = requiredRoomSize;
        this.mainRoom = mainRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRequiredRoomSize() {
        return requiredRoomSize;
    }

    public void setRequiredRoomSize(long requiredRoomSize) {
        this.requiredRoomSize = requiredRoomSize;
    }

    public boolean isMainRoom() {
        return mainRoom;
    }

    public void setMainRoom(boolean mainRoom) {
        this.mainRoom = mainRoom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = (int) (prime * result + requiredRoomSize);
        result = prime * result + new Boolean(mainRoom).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Talk other = (Talk) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (requiredRoomSize != other.requiredRoomSize) {
            return false;
        }
        if (mainRoom != other.mainRoom) {
            return false;
        }
        return true;
    }

}
