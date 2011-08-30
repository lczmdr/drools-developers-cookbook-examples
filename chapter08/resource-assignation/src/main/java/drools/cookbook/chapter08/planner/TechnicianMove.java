package drools.cookbook.chapter08.planner;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.planner.core.move.Move;

import drools.cookbook.chapter08.domain.ServiceRequest;
import drools.cookbook.chapter08.domain.Technician;

public class TechnicianMove implements Move {

    private ServiceRequest serviceDelivery;
    private Technician technician;

    public TechnicianMove(ServiceRequest serviceDelivery, Technician technician) {
        this.serviceDelivery = serviceDelivery;
        this.technician = technician;
    }

    @Override
    public boolean isMoveDoable(WorkingMemory workingMemory) {
        return !serviceDelivery.getTechnician().equals(technician);
    }

    @Override
    public Move createUndoMove(WorkingMemory workingMemory) {
        return new TechnicianMove(serviceDelivery, serviceDelivery.getTechnician());
    }

    @Override
    public void doMove(WorkingMemory workingMemory) {
        FactHandle serviceDeliveryHandle = workingMemory.getFactHandle(serviceDelivery);
        serviceDelivery.setTechnician(technician);
        workingMemory.update(serviceDeliveryHandle, serviceDelivery);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof TechnicianMove) {
            TechnicianMove other = (TechnicianMove) o;
            return new EqualsBuilder().append(serviceDelivery, other.serviceDelivery)
                    .append(technician, other.technician).isEquals();
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(serviceDelivery).append(technician).toHashCode();
    }

    @Override
    public String toString() {
        return serviceDelivery.toString() + " to " + technician.toString();
    }
}
