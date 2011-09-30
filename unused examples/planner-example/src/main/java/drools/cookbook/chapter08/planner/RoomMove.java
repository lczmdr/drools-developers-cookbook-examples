package drools.cookbook.chapter08.planner;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;

import drools.cookbook.chapter08.domain.Room;
import drools.cookbook.chapter08.domain.Talk;

public class RoomMove implements Move {

    private Room room;
    private List<Talk> talks;

    public RoomMove(Room room, List<Talk> talks) {
        this.room = room;
        this.talks = talks;
    }

    @Override
    public boolean isMoveDoable(WorkingMemory workingMemory) {
        for (Talk talk : talks) {
            if (!room.getTalks().contains(talk)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Move createUndoMove(WorkingMemory workingMemory) {
        return new RoomMove(room, room.getTalks());
    }

    @Override
    public void doMove(WorkingMemory workingMemory) {
        FactHandle factHandle = workingMemory.getFactHandle(room);
        room.setTalks(talks);
        workingMemory.update(factHandle, room);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof RoomMove) {
            RoomMove other = (RoomMove) o;
            return new EqualsBuilder().append(room, other.room).append(talks, other.talks).isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder().append(room);
        for (Talk talk : talks) {
            hashCodeBuilder.append(talk);
        }
        return hashCodeBuilder.toHashCode();
    }

}
