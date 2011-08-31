package drools.cookbook.chapter08.planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.planner.core.score.SimpleScore;
import org.drools.planner.core.solution.Solution;

import drools.cookbook.chapter08.domain.ServiceRequest;
import drools.cookbook.chapter08.domain.Technician;

public class TechniciansSolution implements Solution<SimpleScore> {

    private List<Technician> technicians;
    private List<ServiceRequest> serviceRequests;
    private SimpleScore score;

    public TechniciansSolution(List<Technician> technicians, List<ServiceRequest> serviceRequests) {
        this.technicians = technicians;
        this.serviceRequests = serviceRequests;
    }

    private TechniciansSolution() {
    }

    @Override
    public SimpleScore getScore() {
        return score;
    }

    @Override
    public void setScore(SimpleScore score) {
        this.score = (SimpleScore) score;
    }

    @Override
    public Collection<ServiceRequest> getFacts() {
        return serviceRequests;
    }

    @Override
    public Solution<SimpleScore> cloneSolution() {
        TechniciansSolution solution = new TechniciansSolution();
        solution.score = score;
        solution.technicians = technicians;
        List<ServiceRequest> clonedServices = new ArrayList<ServiceRequest>(serviceRequests.size());
        for (ServiceRequest sr : serviceRequests) {
            clonedServices.add(new ServiceRequest(sr));
        }
        solution.serviceRequests = clonedServices;
        return solution;
    }

    public List<Technician> getTechnicians() {
        return technicians;
    }

    public List<ServiceRequest> getServiceRequests() {
        return serviceRequests;
    }

}
