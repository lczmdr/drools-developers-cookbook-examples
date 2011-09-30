package drools.cookbook.chapter08.planner;

import java.util.ArrayList;
import java.util.List;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;

import drools.cookbook.chapter08.domain.Room;
import drools.cookbook.chapter08.domain.Talk;

public class SelectNextRoom extends CachedMoveFactory {

    @Override
    public List<Move> createCachedMoveList(Solution solution) {
        System.out.println("AAAAAAAAA");
        ConferenceSolution conferenceSolution = (ConferenceSolution) solution;
        List<Move> moves = new ArrayList<Move>();
        int talksAmount = conferenceSolution.getTalks().size();
        int roomNumber = 0;
        for (Room room : conferenceSolution.getRooms()) {
            for (Talk talk : conferenceSolution.getTalks()) {
                int from = roomNumber * talksAmount;
                int to = talksAmount * (roomNumber + 1);
                if (to > conferenceSolution.getTalks().size()) {
                    to = conferenceSolution.getTalks().size();
                }
                List<Talk> roomTalks = conferenceSolution.getTalks().subList(from, to);
                moves.add(new RoomMove(room, roomTalks));
            }
        }
//        TechniciansSolution techSolution = (TechniciansSolution) solution;
//        List<Move> moves = new ArrayList<Move>();
//        for ( ServiceDelivery serviceDelivery : techSolution.getServiceDeliveries() ) {
//            for ( Technician technician : techSolution.getTechnicians() ) {
//                moves.add( new NewTechnicianMove( serviceDelivery,
//                                                 technician ) );
//            }
//        }
//        return moves;
        return moves;
    }

}
