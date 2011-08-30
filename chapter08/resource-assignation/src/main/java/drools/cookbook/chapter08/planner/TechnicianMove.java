package drools.cookbook.chapter08.planner;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;

import drools.cookbook.chapter08.domain.ServiceRequest;
import drools.cookbook.chapter08.domain.Technician;

public class TechnicianMove implements Move {

    private ServiceRequest serviceRequest;
    private Technician technician;

    public TechnicianMove(ServiceRequest serviceRequest, Technician technician) {
        this.serviceRequest = serviceRequest;
        this.technician = technician;
    }

    @Override
    public boolean isMoveDoable(WorkingMemory workingMemory) {
        return !serviceRequest.getTechnician().equals(technician);
    }

    @Override
    public Move createUndoMove(WorkingMemory workingMemory) {
        return new TechnicianMove(serviceRequest, serviceRequest.getTechnician());
    }

    @Override
    public void doMove(WorkingMemory workingMemory) {
        FactHandle serviceRequestHandle = workingMemory.getFactHandle(serviceRequest);
        serviceRequest.setTechnician(technician);
        workingMemory.update(serviceRequestHandle, serviceRequest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof TechnicianMove) {
            TechnicianMove other = (TechnicianMove) o;
            return new EqualsBuilder().append(serviceRequest, other.serviceRequest)
                    .append(technician, other.technician).isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(serviceRequest).append(technician).toHashCode();
    }

    @Override
    public String toString() {
        return serviceRequest.toString() + " to " + technician.toString();
    }
}
