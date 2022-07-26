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
import daintiness.utilities.Constants;
import daintiness.utilities.Constants.PatternType;
import javafx.collections.ObservableList;

public class PatternManager implements IPatternManager {
	private IPatternComputationHandler patternComputationHandler;

	public List<PatternData> getPatterns(ObservableList<ChartGroupPhaseMeasurement> totalValues, List<Phase> totalPhases, PatternType patternType) {
		
		PatternComputationHandlerFactory patternComputationHandlerFactory = new PatternComputationHandlerFactory();
		patternComputationHandler = patternComputationHandlerFactory.getPatternComputationHandler("SIMPLE_PATTERN_COMPUTATION_HANDLER");
		
		List<PatternData> patternList = new ArrayList<PatternData>();

		patternList = patternComputationHandler.computePatterns(totalValues, totalPhases, patternType);

		//printPatterns(patternList);
        return patternList;
	}

	public void printPatterns(List<PatternData> patternList, String patternTypeString, String fileNameString, File file) {
		try {
			
			if(file == null) {
				File directory = new File("src" + Constants.FS + "main" + Constants.FS + "resources" + Constants.FS + "SavedPatterns");
				if (!directory.exists()) {
					directory.mkdirs();
				}
						
				
				String fileName = patternTypeString + "_" + fileNameString + ".txt";
				file = new File("src" + Constants.FS + "main" + Constants.FS + "resources" + Constants.FS + "SavedPatterns" +  Constants.FS + fileName);
				
			}
			FileWriter fileWriter = new FileWriter(file);
			for (var pattern : patternList) {
				//System.out.println(pattern.getPatternType().toString());
				fileWriter.write(pattern.getPatternType().toString() + "\n");

				if (pattern.getPatternCellsList().size() > 0) {
					for (var item : pattern.getPatternCellsList()) {
						
						//System.out.println("Entity Name : " + item.getEntityName() + " PhaseId: " + item.getPhaseId());
						fileWriter.write("Entity Name : " + item.getEntityName() + " PhaseId: " + item.getPhaseId() + "\n");
						
					}
				}
				//System.out.println("\n");
				fileWriter.write("\n");
				
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
