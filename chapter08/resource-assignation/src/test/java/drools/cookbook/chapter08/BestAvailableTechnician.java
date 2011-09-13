package drools.cookbook.chapter08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.drools.planner.config.XmlSolverConfigurer;
import org.drools.planner.core.Solver;

import drools.cookbook.chapter08.domain.Location;
import drools.cookbook.chapter08.domain.ServiceRequest;
import drools.cookbook.chapter08.domain.Skill;
import drools.cookbook.chapter08.domain.Technician;
import drools.cookbook.chapter08.domain.TrainingLevel;
import drools.cookbook.chapter08.planner.TechniciansSolution;

public class BestAvailableTechnician {

    public static void main(String[] args) {
        List<Technician> technicians = new ArrayList<Technician>();
        technicians.add(new Technician(Location.WASHINGTON_DC, TrainingLevel.JUNIOR, false, Collections
                .<Skill> emptySet()));
        technicians.add(new Technician(Location.MONTANA, TrainingLevel.SEMISENIOR, false, EnumSet.of(Skill.HADOOP)));
        technicians.add(new Technician(Location.NY, TrainingLevel.SENIOR, true, EnumSet.of(Skill.JAVA, Skill.DROOLS)));
        technicians.add(new Technician(Location.NORTH_CAROLINA, TrainingLevel.SENIOR, true, EnumSet.of(Skill.JAVA)));
        technicians
                .add(new Technician(Location.NY, TrainingLevel.SEMISENIOR, false, EnumSet.of(Skill.JAVA, Skill.REST)));
        technicians.add(new Technician(Location.SAN_DIEGO, TrainingLevel.SENIOR, false, EnumSet.of(Skill.SCALA)));
        List<ServiceRequest> requests = new ArrayList<ServiceRequest>();
        requests.add(new ServiceRequest(Location.SAN_DIEGO, EnumSet.of(Skill.JAVA)));

        XmlSolverConfigurer configurer = new XmlSolverConfigurer();
        configurer.configure("/ServiceRequestSolverConfig.xml");
        Solver solver = configurer.buildSolver();

        for (ServiceRequest serviceRequest : requests) {
            serviceRequest.setTechnician(technicians.get(0));
        }
        TechniciansSolution initialSolution = new TechniciansSolution(technicians, requests);
        solver.setPlanningProblem(initialSolution);
        solver.solve();

        TechniciansSolution finalSolution = (TechniciansSolution) solver.getBestSolution();

        Technician selectedTechnician = finalSolution.getServiceRequests().get(0).getTechnician();
        System.out.println("Selected technician: " + selectedTechnician);
    }

}
