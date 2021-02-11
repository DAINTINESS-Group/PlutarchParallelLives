package daintiness.io.output;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import daintiness.clustering.*;
import daintiness.data.IDataHandler;
import daintiness.io.FileHandler;
import daintiness.io.TestUtilities;
import daintiness.maincontroller.MainController;
import daintiness.utilities.Constants;

import java.io.File;

public class ProjectExporterTest {
    File projectWithoutClustering = new File(
            "src" + Constants.FS + "test" + Constants.FS + "resources" + Constants.FS + "projects" + Constants.FS + "torrentpier_without_clustering");

    File projectWithClustering = new File(
            "src" + Constants.FS + "test" + Constants.FS + "resources" + Constants.FS + "projects" + Constants.FS + "torrentpier_with_clustering");

    File originalFile = new File(
            "src" + Constants.FS + "test" + Constants.FS + "resources" + Constants.FS + "torrentpier__torrentpier");

    TestUtilities utilities = new TestUtilities();

    @Test
    @DisplayName("Export project without clustering test")
    public void exportProjectWithoutClusteringTest() {
        File projectFile = projectWithoutClustering;
        MainController mainController = new MainController();
        mainController.load(originalFile);
        mainController.fitDataToGroupPhaseMeasurements(
                new ClusteringProfile(
                        new BeatClusteringProfile(mainController.getNumberOfBeats()),
                        new EntityClusteringProfile(mainController.getNumberOfEntities())
                )
        );
        mainController.generateChartDataOfType(Constants.MeasurementType.RAW_VALUE, Constants.AggregationType.SUM_OF_ALL);
        mainController.exportProject(projectFile);

        FileHandler actualFileHandler = new FileHandler();
        actualFileHandler.setGivenFile(projectWithoutClustering, Constants.FileType.TEM_GPM);
        IDataHandler actualDataHandler = actualFileHandler.loadTEM();
        IClusteringHandler actualClusteringHandler = new ClusteringHandler();
        actualClusteringHandler.setDataHandler(actualDataHandler);

        actualClusteringHandler.loadPhases(actualFileHandler.getPhasesData());
        actualClusteringHandler.loadEntityGroup(actualFileHandler.getEntityGroupData());
        actualClusteringHandler.loadClusteringData();


        Assertions.assertAll(
                () -> utilities.testPhases(mainController.getPhases(), actualClusteringHandler.getPhases()),
                () -> utilities.testEntityGroups(mainController.getEntityGroups(), actualClusteringHandler.getEntityGroups())
        );
    }

    @Test
    @DisplayName("Export project with clustering test")
    public void exportProjectWithClusteringTest() {
        File projectFile = projectWithClustering;
        MainController mainController = new MainController();
        mainController.load(originalFile);
        mainController.fitDataToGroupPhaseMeasurements(
                new ClusteringProfile(
                        new BeatClusteringProfile(100),
                        new EntityClusteringProfile(30)
                )
        );
        mainController.generateChartDataOfType(Constants.MeasurementType.RAW_VALUE, Constants.AggregationType.SUM_OF_ALL);
        mainController.exportProject(projectFile);


        FileHandler actualFileHandler = new FileHandler();
        actualFileHandler.setGivenFile(projectWithClustering, Constants.FileType.TEM_GPM);
        IDataHandler actualDataHandler = actualFileHandler.loadTEM();
        IClusteringHandler actualClusteringHandler = new ClusteringHandler();
        actualClusteringHandler.setDataHandler(actualDataHandler);

        actualClusteringHandler.loadPhases(actualFileHandler.getPhasesData());
        actualClusteringHandler.loadEntityGroup(actualFileHandler.getEntityGroupData());
        actualClusteringHandler.loadClusteringData();


        Assertions.assertAll(
                () -> utilities.testPhases(mainController.getPhases(), actualClusteringHandler.getPhases()),
                () -> utilities.testEntityGroups(mainController.getEntityGroups(), actualClusteringHandler.getEntityGroups())
        );
    }
}