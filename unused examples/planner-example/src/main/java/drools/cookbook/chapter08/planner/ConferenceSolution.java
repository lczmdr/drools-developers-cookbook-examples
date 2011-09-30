package drools.cookbook.chapter08.planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.planner.core.score.SimpleScore;
import org.drools.planner.core.solution.Solution;

import drools.cookbook.chapter08.domain.Room;
import drools.cookbook.chapter08.domain.Talk;

public class ConferenceSolution implements Solution<SimpleScore> {

    private List<Room> rooms;
    private List<Talk> talks;
    private SimpleScore score;

    public ConferenceSolution(List<Room> rooms, List<Talk> talks) {
        this.rooms = rooms;
        this.talks = talks;
    }

    public ConferenceSolution() {
    }

    @Override
    public SimpleScore getScore() {
        return score;
    }

    @Override
    public void setScore(SimpleScore score) {
        this.score = score;
    }

    @Override
    public Collection<? extends Object> getFacts() {
        return rooms;
    }

    @Override
    public Solution<SimpleScore> cloneSolution() {
        ConferenceSolution solution = new ConferenceSolution();
        solution.score = score;
        List<Room> clonedRooms = new ArrayList<Room>(rooms.size());
        for (Room room : rooms) {
            clonedRooms.add(new Room(room));
        }
        solution.rooms = rooms;
        return solution;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Talk> getTalks() {
        return talks;
    }

}
