package daintiness.patterns;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import daintiness.clustering.Phase;
import daintiness.clustering.measurements.ChartGroupPhaseMeasurement;
import daintiness.models.*;
import daintiness.patterns.patternAlgos.IPatternComputationHandler;
import daintiness.patterns.patternAlgos.PatternComputationHandlerFactory;
import daintiness.utilities.Constants.PatternType;
import javafx.collections.ObservableList;

public class PatternManager implements IPatternManager {
	private IPatternComputationHandler patternComputationHandler;
	private Long patternsComputationTime;

	public List<PatternData> getPatterns(ObservableList<ChartGroupPhaseMeasurement> totalValues, List<Phase> totalPhases, PatternType patternType) {
		
		PatternComputationHandlerFactory patternComputationHandlerFactory = new PatternComputationHandlerFactory();
		patternComputationHandler = patternComputationHandlerFactory.getPatternComputationHandler("SIMPLE_PATTERN_COMPUTATION_HANDLER");
		
		List<PatternData> patternList = new ArrayList<PatternData>();
		
		Long start = System.nanoTime();
		patternList = patternComputationHandler.computePatterns(totalValues, totalPhases, patternType);
		Long end = System.nanoTime();
		
		patternsComputationTime = end - start;

		//printPatterns(patternList);
        return patternList;
	}

	public void printPatterns(List<PatternData> patternList, File file, String projectName) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("Project Name: " + projectName + "\n");  
			fileWriter.write("Number of total patterns: " + patternList.size() + "\n"); 
			
			int birthsPatterns = 0;
			int deathsPatterns = 0;
			int updatesPatterns = 0;
			int ladderPatterns = 0;
			
			for (var pattern : patternList) {
				if(pattern.getPatternType() == PatternType.MULTIPLE_BIRTHS) {
					birthsPatterns+=1;
				
				}
				else if(pattern.getPatternType() == PatternType.MULTIPLE_DEATHS) {
					deathsPatterns+=1;
				
				}
				else if(pattern.getPatternType() == PatternType.MULTIPLE_UPDATES) {
					updatesPatterns+=1;
				
				}
				else if(pattern.getPatternType() == PatternType.LADDER) {
					ladderPatterns+=1;
				
				}
				
			}
			
			if(birthsPatterns > 0) {
				fileWriter.write("Number of births patterns: " + birthsPatterns + "\n");
			}
			if(deathsPatterns > 0) {
				fileWriter.write("Number of deaths patterns: " + deathsPatterns + "\n");			
						}
			if(updatesPatterns > 0) {
				fileWriter.write("Number of updates patterns: " + updatesPatterns + "\n");
			}
			if(ladderPatterns > 0) {
				fileWriter.write("Number of ladder patterns: " + ladderPatterns + "\n");
			}
			
			if(patternList.size() > 0) {
				double patternsComputationTimeSeconds = (double) patternsComputationTime / 1_000_000_000;
				
				fileWriter.write("Patterns computation took " + patternsComputationTimeSeconds + " seconds\n");
			}
			
			
			fileWriter.write("\n");
			for (var pattern : patternList) {
				fileWriter.write(pattern.getPatternType().toString() + "\n");
				
				if (pattern.getPatternCellsList().size() > 0) {
					fileWriter.write("The pattern consists of " + pattern.getPatternCellsList().size() +" cells\n");
					for (var item : pattern.getPatternCellsList()) {
						fileWriter.write("Entity Name : " + item.getEntityName() + " PhaseId: " + item.getPhaseId() + "\n");
					}
				}
				fileWriter.write("\n");
				
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
