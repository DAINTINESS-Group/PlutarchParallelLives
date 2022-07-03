package daintiness.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import daintiness.clustering.Phase;
import daintiness.clustering.measurements.ChartGroupPhaseMeasurement;
import daintiness.models.*;
import daintiness.models.measurement.IMeasurement;
import daintiness.utilities.Constants;
import javafx.collections.ObservableList;

public class PatternManager implements IPatternManager {

	
	
	public List<PatternData> getPatterns(ObservableList<ChartGroupPhaseMeasurement> totalValues, List<Phase> totalPhases) {
		
		List<PatternData> patternList = new ArrayList<PatternData>();
		
		List<CellInfo> lastBirthCellsEachPhase = new ArrayList<CellInfo>();

		List<String> TotalEntitiesNamesAscOrder = new ArrayList<String>(); 
    	
       
		BirthsUpdatesDeathsPatternAlgo(totalValues, totalPhases, patternList, lastBirthCellsEachPhase, TotalEntitiesNamesAscOrder);	
		
		
		LadderPatternAlgo(patternList, lastBirthCellsEachPhase, TotalEntitiesNamesAscOrder);
    	
		
    	printPatterns(patternList);
		 
		     	
        return patternList;
	}
	

	
	public void BirthsUpdatesDeathsPatternAlgo(ObservableList<ChartGroupPhaseMeasurement> totalValues, List<Phase> totalPhases, List<PatternData> patternList, List<CellInfo> lastBirthCellsEachPhase, List<String> TotalEntitiesNamesAscOrder) {
		
		for(Phase phase: totalPhases) {  		
    		List<CellInfo> cellsMultipleBirths = new ArrayList<CellInfo>();
    		List<CellInfo> cellsMultipleUpdates = new ArrayList<CellInfo>();
    		List<CellInfo> cellsMultipleDeaths = new ArrayList<CellInfo>();
    		
    		for(ChartGroupPhaseMeasurement tab: totalValues) {
    			
    			var cellMeasurementType = tab.getEntityGroup().getGPMType(phase.getFirstPhaseBeat().getBeatId(), phase.getLastPhaseBeat().getBeatId());    			
    			
    			int phaseId = phase.getPhaseId();
    			String entityName = tab.getEntityGroup().getGroupComponentsNames().get(0);
    			
    			
    			if(phaseId == 0) {
    				TotalEntitiesNamesAscOrder.add(entityName);
    			}
    			
    			
    			IMeasurement measurement = tab.getMeasurement(phase.getPhaseId());
    			CellInfo cell = new CellInfo(entityName, phaseId, measurement);
    			
    			if(cellMeasurementType == Constants.GPMType.BIRTH){
    				//lastBirthCellsEachPhase.add(cell);
    				cellsMultipleBirths.add(cell);
    			}
    			else if(cellMeasurementType == Constants.GPMType.ACTIVE && tab.getMeasurement(phase.getPhaseId()) != null) {			
    				cellsMultipleUpdates.add(cell);
    			}
    			else if(cellMeasurementType == Constants.GPMType.DEATH) {
    				cellsMultipleDeaths.add(cell);
    			}

            }
    		
    		
    		PatternData patternMultipleBirths = new PatternData(Constants.PatternType.MULTIPLE_BIRTHS,cellsMultipleBirths);
    		PatternData patternMultipleUpdates = new PatternData(Constants.PatternType.MULTIPLE_UPDATES,cellsMultipleUpdates);
    		PatternData patternMultipleDeaths = new PatternData(Constants.PatternType.MULTIPLE_DEATHS,cellsMultipleDeaths);
    		
    		if(cellsMultipleBirths.size() > 3) {
    			patternList.add(patternMultipleBirths);
    			
    		}
    		if(cellsMultipleUpdates.size() > 3){
    			patternList.add(patternMultipleUpdates);
    		}
    		if(cellsMultipleDeaths.size() > 3){    			
    			patternList.add(patternMultipleDeaths);
    		}
    		
    		//LAST BIRTH CELL AT EACH PHASE
    		if(patternMultipleBirths.getPatternCellsList().size() > 0) {
    			CellInfo ladderCell = patternMultipleBirths.getLastCellOfPattern();    			
    			lastBirthCellsEachPhase.add(ladderCell);        		
    		}
    		
    	}
	}
	
	
	public void LadderPatternAlgo(List<PatternData> patternList, List<CellInfo> lastBirthCellsEachPhase, List<String> TotalEntitiesNamesAscOrder) {
		List<CellInfo> cellsLadderPattern = new ArrayList<CellInfo>();
    	
    	for(int i = 0; i < lastBirthCellsEachPhase.size() - 1;i++) {    		
    		CellInfo currentCell = lastBirthCellsEachPhase.get(i);
    		
    		
    		CellInfo nextCell = lastBirthCellsEachPhase.get(i+1);
    		    		
    		
    		int currentCellPhaseId = lastBirthCellsEachPhase.get(i).getPhaseId();
    		
    		
    		int difPhases1 = nextCell.getPhaseId() - currentCellPhaseId;
    		
    		
    		//To achieve the Ladder pattern we need to rearrange the table based on the ascending order of births, so we have to find the new position of the entities in the table.
    		int currentCellEntityNameIndex = TotalEntitiesNamesAscOrder.indexOf(lastBirthCellsEachPhase.get(i).getEntityName());
			int nextCellEntityNameIndex = TotalEntitiesNamesAscOrder.indexOf(lastBirthCellsEachPhase.get(i + 1).getEntityName());
			
			int difEntities1 = nextCellEntityNameIndex - currentCellEntityNameIndex;

    		
    		boolean ladderConnection = false;    		
    		
			if (difPhases1 <= 3 && difEntities1 <= 3 ) {
				
				cellsLadderPattern.add(currentCell);
				cellsLadderPattern.add(nextCell);
				ladderConnection = true;
			}
			if(ladderConnection == false) {
				List<CellInfo> distinctCellsLadderPattern = cellsLadderPattern.stream().distinct().collect(Collectors.toList());
				if(distinctCellsLadderPattern.size() >= 3) {
					PatternData patternLadder = new PatternData(Constants.PatternType.LADDER, distinctCellsLadderPattern);
					patternList.add(patternLadder);

				}
				
				cellsLadderPattern = new ArrayList<CellInfo>();
				
			}
    	}
    	List<CellInfo> distinctCellsLadderPattern = cellsLadderPattern.stream().distinct().collect(Collectors.toList());
    	if(distinctCellsLadderPattern.size() >= 3) {
			PatternData patternLadder = new PatternData(Constants.PatternType.LADDER, distinctCellsLadderPattern);
			patternList.add(patternLadder);

		}
	}
	
	public void printPatterns(List<PatternData> patternList) {
		for (var pattern : patternList) {
			System.out.println(pattern.getPatternType().toString());

			if (pattern.getPatternCellsList().size() > 0) {
				for (var item : pattern.getPatternCellsList()) {
					if (item.getMeasurement() != null) {
						System.out.println("Entity Name : " + item.getEntityName() + " PhaseId: " + item.getPhaseId()
								+ " Measurement: " + item.getMeasurement().getValue());
					} else {
						System.out.println("Entity Name : " + item.getEntityName() + " PhaseId: " + item.getPhaseId()
								+ " Measurement: " + item.getMeasurement());
					}
				}
			}
			System.out.println("\n");
		}
	}
}
