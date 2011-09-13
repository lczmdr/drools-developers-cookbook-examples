package drools.cookbook.chapter08;

import org.drools.planner.benchmark.XmlSolverBenchmarker;

public class BestAvailableTechnicianBenchmark {

    public static void main(String[] args) {
        XmlSolverBenchmarker benchmarker = new XmlSolverBenchmarker();
        benchmarker.configure("/ServiceRequestSolverBenchmarkConfig.xml");
        benchmarker.benchmark();
    }

}
