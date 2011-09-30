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
    private List<ServiceRequest> serviceDeliveries;
    private SimpleScore score;

    public TechniciansSolution(List<Technician> technicians, List<ServiceRequest> serviceDelivery) {
        this.technicians = technicians;
        this.serviceDeliveries = serviceDelivery;
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
        return serviceDeliveries;
    }

    @Override
    public Solution<SimpleScore> cloneSolution() {
        TechniciansSolution solution = new TechniciansSolution();
        solution.score = score;
        solution.technicians = technicians;
        List<ServiceRequest> clonedServices = new ArrayList<ServiceRequest>(serviceDeliveries.size());
        for (ServiceRequest serviceDelivery : serviceDeliveries) {
            clonedServices.add(new ServiceRequest(serviceDelivery));
        }
        solution.serviceDeliveries = clonedServices;
        return solution;
    }

    public List<Technician> getTechnicians() {
        return technicians;
    }

    public List<ServiceRequest> getServiceDeliveries() {
        return serviceDeliveries;
    }

}
