package drools.cookbook.chapter08;

import java.util.Arrays;
import java.util.List;

import org.drools.planner.config.XmlSolverConfigurer;
import org.drools.planner.core.Solver;
import org.junit.Assert;
import org.junit.Test;

import drools.cookbook.chapter08.domain.Room;
import drools.cookbook.chapter08.domain.Talk;
import drools.cookbook.chapter08.planner.ConferenceSolution;

public class ConferenceSchedulingTest {

    private static final String SOLVER_CONFIG = "/conferenceSchedulingSolverConfig.xml";

    @Test
    public void test() {
        List<Room> rooms = getConferenceRooms();
        List<Talk> talks = getConferenceTalks();
        Solver solver = createSolver();
        solver.setStartingSolution(getInitialSolution(rooms, talks));

        solver.solve();
        ConferenceSolution bestSolution = (ConferenceSolution) solver.getBestSolution();
        Assert.assertNotNull(bestSolution.getRooms());
    }

    private ConferenceSolution getInitialSolution(List<Room> rooms, List<Talk> talks) {
        int talksAmount = talks.size() / rooms.size();
        int roomNumber = 0;
        for (Room room : rooms) {
            int from = roomNumber * talksAmount;
            int to = talksAmount * (roomNumber + 1);
            if (to > talks.size()) {
                to = talks.size();
            }
            List<Talk> roomTalks = talks.subList(from, to);
            room.setTalks(roomTalks);
            roomNumber++;
        }
        ConferenceSolution initialSolution = new ConferenceSolution(rooms, talks);
        return initialSolution;
    }

    public static void main(String[] args) {
        int talksAmount = 5;
        int roomNumber = 0;
        for (int i = 0; i < 4; i++) {
            System.out.println(roomNumber * talksAmount + " to " + talksAmount * (roomNumber + 1));
            roomNumber++;
        }
    }

    private Solver createSolver() {
        XmlSolverConfigurer configurer = new XmlSolverConfigurer();
        configurer.configure(SOLVER_CONFIG);
        return configurer.buildSolver();
    }

    private static List<Room> getConferenceRooms() {
        return Arrays.asList(new Room[] { new Room("Lincoln", 100, true), 
                                          new Room("Quintin", 70, false),
                                          new Room("Loo", 85, false)
        });
    }
    
    private static List<Talk> getConferenceTalks() {
        return Arrays.asList(new Talk[] { new Talk("EJB3 persistence", 140, false), 
                                          new Talk("Guvnor blah", 90, false),
                                          new Talk("ESB integration with Drools", 85, true)
        });
    }
}
