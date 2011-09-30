package drools.cookbook.chapter08.planner;

import java.util.ArrayList;
import java.util.List;

import org.drools.planner.core.move.Move;
import org.drools.planner.core.move.factory.CachedMoveFactory;
import org.drools.planner.core.solution.Solution;

import drools.cookbook.chapter08.domain.ServiceRequest;
import drools.cookbook.chapter08.domain.Technician;

public class SelectNextTechnicians extends CachedMoveFactory {

    @Override
    public List<Move> createCachedMoveList(Solution solution) {
        TechniciansSolution techSolution = (TechniciansSolution) solution;
        List<Move> moves = new ArrayList<Move>();
        for (ServiceRequest serviceDelivery : techSolution.getServiceDeliveries()) {
            for (Technician technician : techSolution.getTechnicians()) {
                moves.add(new TechnicianMove(serviceDelivery, technician));
            }
        }
        return moves;
    }

}
