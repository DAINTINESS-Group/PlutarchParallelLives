package daintiness.gui.tableview;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import daintiness.clustering.BeatClusteringProfile;
import daintiness.clustering.ClusteringProfile;
import daintiness.clustering.EntityClusteringProfile;
import daintiness.clustering.Phase;
import daintiness.clustering.measurements.ChartGroupPhaseMeasurement;
import daintiness.maincontroller.MainController;
import daintiness.models.Beat;
import daintiness.models.Entity;
import daintiness.models.LifeDetails;
import daintiness.utilities.Constants;
import javafx.collections.ObservableList;

public class JTableTest {
	MainController mainController = new MainController();
	File originalFile = new File(
			"src" + Constants.FS + "test" + Constants.FS + "resources" + Constants.FS + "schema_evo_mock_data");

	List<Entity> expectedEntities;
	
	List<Beat> expectedBeatList;
	
	List<Double> expectedMeasurements = new ArrayList<Double>();

	@Test
	@DisplayName("Check if new JTable is filled correctly test")
	public void newJTableTest() {
		List<Entity> actualEntities = new ArrayList<Entity>();
		
		// Manage data in JTable Data
		mainController.load(originalFile);
		mainController.fitDataToGroupPhaseMeasurements(
				new ClusteringProfile(new BeatClusteringProfile(mainController.getNumberOfBeats()),
						new EntityClusteringProfile(mainController.getNumberOfEntities())));


		ObservableList<ChartGroupPhaseMeasurement> observableList = mainController.getChartData();
		List<Phase> phases = mainController.getPhases();
				
		List<Double> actualMeasurements = new ArrayList<Double>();

		for (ChartGroupPhaseMeasurement tab : observableList) {
			
			actualEntities.add(tab.getEntityGroup().getGroupComponents().get(0));
			
			for(Phase phase: phases) {
				if(tab.getMeasurement(phase.getPhaseId()) != null) {
					actualMeasurements.add(tab.getMeasurement(phase.getPhaseId()).getValue());
				}
				else {
					actualMeasurements.add(null);
				}
				
			}
		}
		
		List<Beat> actualBeatList = new  ArrayList<Beat>(); 
		for(Phase ps: phases) {
			actualBeatList.add(ps.getPhaseComponents().get(0));			
		}
		
		
		initializeExpectedPLD();
		
		//TimeLineTest
		Assumptions.assumeTrue(expectedBeatList.size() == actualBeatList.size());

		for (int i = 0; i < actualBeatList.size(); i++) {
			Assertions.assertEquals(expectedBeatList.get(i).getDate().toString(), actualBeatList.get(i).getDate().toString());
		}
		  
		  
		// EntitiesTest
		Assumptions.assumeTrue(expectedEntities.size() == actualEntities.size());

		for (int i = 0; i < actualEntities.size(); i++) {
			Assertions.assertEquals(expectedEntities.get(i).getEntityName(), actualEntities.get(i).getEntityName());
		}
		 
        //MeasurementsTest
		Assumptions.assumeTrue(expectedMeasurements.size() == actualMeasurements.size());
		for (int i = 0; i < actualMeasurements.size(); i++) {
			Assertions.assertEquals(expectedMeasurements.get(i), actualMeasurements.get(i));
		}

	}
	
	private void initializeExpectedPLD() {
		expectedEntities = new ArrayList<Entity>() {private static final long serialVersionUID = 1L;

		{
			add(new Entity(1,"t1",new LifeDetails(1,5,false)));
			add(new Entity(2,"t2",new LifeDetails(0,7,false)));
			add(new Entity(3,"t3",new LifeDetails(6,10,true)));
			add(new Entity(4,"t4",new LifeDetails(0,10,false)));
		}};
		
		expectedBeatList = new  ArrayList<Beat>(){private static final long serialVersionUID = 1L;

		{
			add(new Beat(1,"2009-02-11 17:25:48",dateTimeConverter("2009-02-11 17:25:48")));
			add(new Beat(1,"2009-03-16 00:43:59",dateTimeConverter("2009-03-16 00:43:59")));
			add(new Beat(1,"2009-03-16 13:01:18",dateTimeConverter("2009-03-16 13:01:18")));
			add(new Beat(1,"2009-03-16 15:44:37",dateTimeConverter("2009-03-16 15:44:37")));
			add(new Beat(1,"2009-03-17 01:34:08",dateTimeConverter("2009-03-17 01:34:08")));
			add(new Beat(1,"2009-03-18 15:56:09",dateTimeConverter("2009-03-18 15:56:09")));
			add(new Beat(1,"2009-03-18 16:08:40",dateTimeConverter("2009-03-18 16:08:40")));
			add(new Beat(1,"2009-03-19 14:11:22",dateTimeConverter("2009-03-19 14:11:22")));
			add(new Beat(1,"2009-03-19 16:58:12",dateTimeConverter("2009-03-19 16:58:12")));
			add(new Beat(1,"2009-03-20 18:11:00",dateTimeConverter("2009-03-20 18:11:00")));
			add(new Beat(1,"2009-03-20 18:13:28",dateTimeConverter("2009-03-20 18:13:28")));
		}};
		
		expectedMeasurements = new ArrayList<Double>() {private static final long serialVersionUID = 1L;

		{
			add(null);add(null);add(null);add(1.0);add(null);add(null);add(null);add(null);add(null);add(null);add(null);
			add(null);add(null);add(1.0);add(null);add(1.0);add(null);add(null);add(null);add(null);add(null);add(null);
			add(null);add(null);add(null);add(null);add(null);add(null);add(null);add(null);add(null);add(1.0);add(null);
			add(null);add(null);add(null);add(null);add(null);add(null);add(1.0);add(null);add(1.0);add(null);add(null);
		}};

	}
	
	//Convert string date to LocalDateTime object
	private LocalDateTime dateTimeConverter(String rawDateTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(rawDateTime, formatter);
		return dateTime;
	}				 	 
}
