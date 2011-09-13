package drools.cookbook.chapter08;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import junit.framework.Assert;

import org.drools.planner.config.XmlSolverConfigurer;
import org.drools.planner.core.Solver;
import org.junit.Test;

import drools.cookbook.chapter08.domain.Location;
import drools.cookbook.chapter08.domain.ServiceRequest;
import drools.cookbook.chapter08.domain.Skill;
import drools.cookbook.chapter08.domain.Technician;
import drools.cookbook.chapter08.domain.TrainingLevel;
import drools.cookbook.chapter08.planner.TechniciansSolution;

public class BestTechnicianAvailableTest {

    @Test
    public void findBestTechnician() {
        List<Technician> technicians = createTechnicians();
        List<ServiceRequest> requests = createServiceRequests();

        XmlSolverConfigurer configurer = new XmlSolverConfigurer();
        configurer.configure("/ServiceRequestSolverConfig.xml");
        Solver solver = configurer.buildSolver();

        solver.setPlanningProblem(getInitialSolution(technicians, requests));
        solver.solve();

        TechniciansSolution finalSolution = (TechniciansSolution) solver.getBestSolution();

        Assert.assertEquals(technicians.get(4), finalSolution.getServiceRequests().get(0).getTechnician());
    }

    private TechniciansSolution getInitialSolution(List<Technician> technicians, List<ServiceRequest> requests) {
        for (ServiceRequest serviceRequests : requests) {
            serviceRequests.setTechnician(technicians.get(0));
        }
        TechniciansSolution initialSolution = new TechniciansSolution(technicians, requests);
        return initialSolution;
    }

    private static List<ServiceRequest> createServiceRequests() {
        return Arrays.asList(new ServiceRequest[] { new ServiceRequest(Location.SAN_DIEGO, EnumSet.of(Skill.JAVA)) });
    }

    private static List<Technician> createTechnicians() {
        return Arrays.asList(new Technician[] {
                new Technician(Location.WASHINGTON_DC, TrainingLevel.JUNIOR, false, Collections.<Skill> emptySet()),
                new Technician(Location.MONTANA, TrainingLevel.SEMISENIOR, false, EnumSet.of(Skill.HADOOP)),
                new Technician(Location.NY, TrainingLevel.SENIOR, true, EnumSet.of(Skill.JAVA, Skill.DROOLS)),
                new Technician(Location.NORTH_CAROLINA, TrainingLevel.SENIOR, true, EnumSet.of(Skill.JAVA)),
                new Technician(Location.NY, TrainingLevel.SEMISENIOR, false, EnumSet.of(Skill.JAVA, Skill.REST)),
                new Technician(Location.SAN_DIEGO, TrainingLevel.SENIOR, false, EnumSet.of(Skill.SCALA)) });
    }

}
