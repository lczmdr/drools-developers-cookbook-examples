package drools.cookbook.chapter08.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class Technician {

    private Location location;
    private TrainingLevel trainingLevel;
    private boolean busy;
    private Set<Skill> skills;

    public Technician(Location location, TrainingLevel training, boolean busy, Set<Skill> skills) {
        this.location = location;
        this.trainingLevel = training;
        this.busy = busy;
        this.skills = skills;
    }

    public Technician(Technician technician) {
        this(technician.location, technician.trainingLevel, technician.busy,
                (technician.skills.isEmpty()) ? Collections.<Skill> emptySet() : technician.skills);
    }

    public Location getLocation() {
        return location;
    }

    public TrainingLevel getTrainingLevel() {
        return trainingLevel;
    }

    @Override
    public String toString() {
        return "{" + location.name() + "(" + trainingLevel.getAbreviation() + ")" + " skills: "
                + Arrays.toString(skills.toArray()) + " | " + (busy ? "busy" : "available") + "}";
    }

    public boolean isBusy() {
        return busy;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (busy ? 1231 : 1237);
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((skills == null) ? 0 : skills.hashCode());
        result = prime * result + ((trainingLevel == null) ? 0 : trainingLevel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Technician other = (Technician) obj;
        if (busy != other.busy) {
            return false;
        }
        if (location != other.location) {
            return false;
        }
        if (skills == null) {
            if (other.skills != null) {
                return false;
            }
        } else if (!skills.equals(other.skills)) {
            return false;
        }
        if (trainingLevel != other.trainingLevel) {
            return false;
        }
        return true;
    }
}
