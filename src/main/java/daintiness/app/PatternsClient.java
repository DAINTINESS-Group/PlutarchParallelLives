package daintiness.app;

import java.io.File;

import daintiness.clustering.BeatClusteringProfile;
import daintiness.clustering.ClusteringProfile;
import daintiness.clustering.EntityClusteringProfile;
import daintiness.maincontroller.IMainController;
import daintiness.maincontroller.MainControllerFactory;
import daintiness.utilities.Constants.PatternType;
import daintiness.utilities.Constants.SortingType;

public class PatternsClient {
	public static void main(String[] args) {
		
		
		if (args.length != 2) {
			System.out.println("You must enter only the input path as argument. Exiting...");
			System.exit(-1);
		}

		File directory = new File(args[0]);
		File outputDirectory = new File(args[1]);
		if (!directory.isDirectory() || !outputDirectory.isDirectory()) {
			System.out.println("One of paths is not a folder.");
			System.exit(-1);
		}

        MainControllerFactory factory = new MainControllerFactory();
		IMainController mainController = factory.getMainController("SIMPLE_MAIN_CONTROLLER");
        
		
		
        //Iterate through the files in directory
		for (File projectFolder : directory.listFiles()) {
			if (projectFolder.isDirectory()) {
				mainController.load(projectFolder);
				projectFolder.getName();
				BeatClusteringProfile bcp = new BeatClusteringProfile(mainController.getNumberOfBeats());
				EntityClusteringProfile ecp = new EntityClusteringProfile(mainController.getNumberOfEntities());
				ClusteringProfile clusteringProfile = new ClusteringProfile(bcp,ecp);
				mainController.fitDataToGroupPhaseMeasurements(clusteringProfile);
				mainController.sortChartData(SortingType.BIRTH_ASCENDING);
				mainController.getPatterns(PatternType.NO_TYPE);
				File outputs = new File(args[1] + "/" + projectFolder.getName() + "_Patterns.txt");
				mainController.printPatterns(outputs);
			}

		}
	}
	
}
