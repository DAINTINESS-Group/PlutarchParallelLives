package daintiness.models;

import daintiness.models.measurement.IMeasurement;
import daintiness.utilities.Constants;

public class CellInfo {
	private final String EntityName;
	private final int PhaseId;
	private final IMeasurement Measurement;
	
	public CellInfo(String entityName, int phaseId, IMeasurement measurement) {
		this.EntityName = entityName;
		this.PhaseId = phaseId;
		this.Measurement = measurement;
	}
	
	public String getEntityName() {
        return EntityName;
    }
	public int getPhaseId() {
        return PhaseId;
    }
	public IMeasurement getMeasurement() {
        return Measurement;
    }
	
	
	/*
	 * public void setMeasurement(IMeasurement newMeasurement) { this.Measurement =
	 * newMeasurement;
	 * 
	 * }
	 */
	
	@Override
    public String toString() {
		Double measurementValue = 0.0;
		measurementValue = (Measurement != null) ? measurementValue : 0;
        return  "Entity Name : " + EntityName +
                " at phaseId: " + PhaseId +
                ", and measurement: " + measurementValue;
    }

}
