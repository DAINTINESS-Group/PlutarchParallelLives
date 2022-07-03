package daintiness.patterns;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import daintiness.clustering.BeatClusteringProfile;
import daintiness.clustering.ClusteringHandlerFactory;
import daintiness.clustering.ClusteringProfile;
import daintiness.clustering.EntityClusteringProfile;
import daintiness.clustering.EntityGroup;
import daintiness.clustering.IClusteringHandler;
import daintiness.clustering.Phase;
import daintiness.clustering.measurements.ChartGroupPhaseMeasurement;
import daintiness.data.IDataHandler;
import daintiness.io.input.ILoader;
import daintiness.io.input.SimpleLoader;
import daintiness.maincontroller.MainController;
import daintiness.models.Beat;
import daintiness.models.CellInfo;
import daintiness.models.Entity;
import daintiness.models.PatternData;
import daintiness.models.measurement.EmptyIMeasurement;
import daintiness.models.measurement.IMeasurement;
import daintiness.utilities.Constants;
import javafx.collections.ObservableList;

public class PatternsTest {
	MainController mainController = new MainController();
	File originalFile = new File(
			"src" + Constants.FS + "test" + Constants.FS + "resources" + Constants.FS + "biosql");
	String Path = "src" + Constants.FS + "test" + Constants.FS + "resources" + Constants.FS + "biosql"
			+ Constants.FS + "figures" + Constants.FS + "PPL";
	
	ILoader loader;
	IDataHandler dataHandler;
	IPatternManager patternManager;
	IClusteringHandler clusteringHandler;
	
	
	
	List<PatternData> actualPatternList = new ArrayList<PatternData>();

	List<CellInfo> lastBirthCellsEachPhase = new ArrayList<CellInfo>();
	
	List<String> TotalEntitiesNamesAscOrder = new ArrayList<String>();

	Constants.MeasurementType measurementType = Constants.MeasurementType.RAW_VALUE;
    Constants.AggregationType aggregationType = Constants.AggregationType.SUM_OF_ALL;
	
	@Test
	@DisplayName("Check if patterns are correctly test")
	public void patternTest() {
		
		PatternManagerFactory patternManagerFactory = new PatternManagerFactory();
		patternManager = patternManagerFactory.getPatternManager("SIMPLE_PATTERN_MANAGER");
		
		// Manage TSV
		String filePath = Path + "/biosql_detailedPLD.tsv";

		File tsvFile = new File(filePath);
		
		loader = new SimpleLoader(tsvFile, "\t");
		dataHandler = loader.load();
		

		
		
        
		
		// Load TSV file
		mainController.load(originalFile);
		mainController.fitDataToGroupPhaseMeasurements(
				new ClusteringProfile(new BeatClusteringProfile(mainController.getNumberOfBeats()),
						new EntityClusteringProfile(mainController.getNumberOfEntities())));
		

        mainController.generateChartDataOfType(measurementType, aggregationType);


        mainController.sortChartData(Constants.SortingType.BIRTH_ASCENDING);
		
		ObservableList<ChartGroupPhaseMeasurement> observableList = mainController.getChartData();
		
		List<Phase> phases = mainController.getPhases();
		
		List<PatternData> expectedPatternList = patternManager.getPatterns(observableList, phases);
		
				

		List<Entity> actualEntities = dataHandler.getPopulation();
		List<Beat> actualBeats = dataHandler.getTimeline();
		
		List<Entity> ascedingOrderEntityList = new ArrayList<Entity>();
		for(ChartGroupPhaseMeasurement item : observableList) {
			
			ascedingOrderEntityList.add(item.getEntityGroup().getGroupComponents().get(0));
		}
		
		
		sortList(actualEntities,ascedingOrderEntityList);


		
		testBUDPatterns(actualEntities,actualBeats);
		
		testLadderPatterns();

		 
		
		for(int i = 0; i < actualPatternList.size(); i++){
			PatternData expectedPatternData = expectedPatternList.get(i);
			PatternData actualPatternData = actualPatternList.get(i);
			
			
			Assertions.assertAll(
                    () -> Assertions.assertEquals(expectedPatternData.getPatternType(), actualPatternData.getPatternType()),
                    () -> testCellsList(expectedPatternData.getPatternCellsList(), actualPatternData.getPatternCellsList())
            );
        }

	}
	
	//Test births, updates, deaths patterns
	public void testBUDPatterns(List<Entity> actualEntities, List<Beat> actualBeats) {
		
		for(Beat beat: actualBeats) {
			List<CellInfo> cellsMultipleBirths = new ArrayList<CellInfo>();
			List<CellInfo> cellsMultipleDeaths = new ArrayList<CellInfo>();
			List<CellInfo> cellsMultipleUpdates = new ArrayList<CellInfo>();
			
			int beatId = beat.getBeatId();
			for(Entity entity: actualEntities) {
				String entityName = entity.getEntityName();
				List<IMeasurement> measurementsList = new ArrayList<IMeasurement>();
				
				if(beatId == 0) {
    				TotalEntitiesNamesAscOrder.add(entityName);
    			}

				
				
				measurementsList = dataHandler.getTem(entity, beat).getMeasurements();
				CellInfo cell;
				
				if(measurementsList != null) {
					int measurementIndex = dataHandler.getTem(entity, beat).containsMeasurementType(measurementType, Constants.AggregationType.NO_AGGREGATION);
					
					IMeasurement measurement = new EmptyIMeasurement(Constants.GPMType.ACTIVE);
					if (measurementIndex != -1) {							
						measurement = dataHandler.getTem(entity, beat).getMeasurements().get(measurementIndex);
                    }
					cell = new CellInfo(entityName, beatId, measurement);
					cellsMultipleUpdates.add(cell);
				}

				if(beatId == entity.getLifeDetails().getBirthBeatId()) {
					cell = new CellInfo(entityName, beatId, null);
					cellsMultipleBirths.add(cell);
				}
				else if(beatId == entity.getLifeDetails().getDeathBeatId() && entity.getLifeDetails().isAlive() == false){
					cell = new CellInfo(entityName, beatId, null);
					cellsMultipleDeaths.add(cell);
				}

			}
			
			PatternData patternMultipleBirths = new PatternData(Constants.PatternType.MULTIPLE_BIRTHS,cellsMultipleBirths);
			PatternData patternMultipleUpdates = new PatternData(Constants.PatternType.MULTIPLE_UPDATES,cellsMultipleUpdates);
    		PatternData patternMultipleDeaths = new PatternData(Constants.PatternType.MULTIPLE_DEATHS,cellsMultipleDeaths);
			
			
			if(cellsMultipleBirths.size() > 3) {
				actualPatternList.add(patternMultipleBirths);
    			
    		}
			if(cellsMultipleUpdates.size() > 3){
				actualPatternList.add(patternMultipleUpdates);
    		}
    		if(cellsMultipleDeaths.size() > 3){    			
    			actualPatternList.add(patternMultipleDeaths);
    		}
    		
    		
    	
    		//LAST BIRTH CELL AT EACH PHASE
    		if(patternMultipleBirths.getPatternCellsList().size() > 0) {
    			CellInfo ladderCell = patternMultipleBirths.getLastCellOfPattern();    			
    			lastBirthCellsEachPhase.add(ladderCell);    
    		}
		}
		
	}
	
	//Test ladder patterns
	public void testLadderPatterns() {
		List<CellInfo> cellsLadderPattern = new ArrayList<CellInfo>();
    	
    	for(int i = 0; i < lastBirthCellsEachPhase.size() - 1;i++) {    		
    		CellInfo currentCell = lastBirthCellsEachPhase.get(i);
    		
    		
    		CellInfo nextCell = lastBirthCellsEachPhase.get(i+1);
    		    		
    		
    		int currentCellPhaseId = lastBirthCellsEachPhase.get(i).getPhaseId();
    		
    		
    		int difPhases1 = nextCell.getPhaseId() - currentCellPhaseId;
    		
    		
    		//To achieve the Ladder pattern we need to arrange the table based on the ascending order of births, so we have to find the new position of the entities in the table.
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
					actualPatternList.add(patternLadder);

				}
				
				cellsLadderPattern = new ArrayList<CellInfo>();
				
			}
    	}
    	List<CellInfo> distinctCellsLadderPattern = cellsLadderPattern.stream().distinct().collect(Collectors.toList());
    	if(distinctCellsLadderPattern.size() >= 3) {
			PatternData patternLadder = new PatternData(Constants.PatternType.LADDER, distinctCellsLadderPattern);
			actualPatternList.add(patternLadder);

		}
	}
	
	public void testCellsList(List<CellInfo> expectedCells, List<CellInfo> actualCells) {
		Assumptions.assumeTrue(expectedCells.size() == actualCells.size());

        for (int i=0; i < expectedCells.size(); i++) {
        	CellInfo expectedCell = expectedCells.get(i);
        	CellInfo actualCell = actualCells.get(i);
        	
        	testCell(expectedCell, actualCell);
        }
	}
	
	public void testCell(CellInfo expectedCell, CellInfo actualCell) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedCell.getPhaseId(), actualCell.getPhaseId()),
                () -> Assertions.assertEquals(expectedCell.getEntityName(), actualCell.getEntityName()),
                () -> testCellMeasurement(expectedCell.getMeasurement(), actualCell.getMeasurement())
        );
    }
	
	public void testCellMeasurement(IMeasurement expectedMeasurement, IMeasurement actualMeasurement) {
		Double expectedVal = expectedMeasurement != null ? expectedMeasurement.getValue() : null;
		Double actualVal = actualMeasurement != null ? actualMeasurement.getValue() : null;

    	Assertions.assertEquals(expectedVal, actualVal);
    }
	
	
	
	
	//There isn't a way to sort .tsv file, so we order entities like the list we pass as parameter to getPatterns method 
	//This method orders a list of Entities the same way as the other Entities list by entity name
	public static void sortList(List<Entity> objectsToOrder, List<Entity> orderedObjects) {

		HashMap<String, Integer> indexMap = new HashMap<>();
	    int index = 0;
	    for (Entity object : orderedObjects) {
	        indexMap.put(object.getEntityName(), index);
	        index++;
	    }

	    Collections.sort(objectsToOrder, new Comparator<Entity>() {

	    	
	        public int compare(Entity left, Entity right) {

	            Integer leftIndex = indexMap.get(left.getEntityName());
	            Integer rightIndex = indexMap.get(right.getEntityName());
	            if (leftIndex == null) {
	                return -1;
	            }
	            if (rightIndex == null) {
	                return 1;
	            }

	            return Integer.compare(leftIndex, rightIndex);
	        }
	    });
	}
	
}
