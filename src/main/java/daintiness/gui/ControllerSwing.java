package daintiness.gui;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import daintiness.clustering.BeatClusteringProfile;
import daintiness.clustering.ClusteringProfile;
import daintiness.clustering.EntityClusteringProfile;

import daintiness.gui.tableview.PLDiagramSwing;
import daintiness.maincontroller.IMainController;
import daintiness.maincontroller.MainControllerFactory;
import daintiness.models.PatternData;
import daintiness.utilities.Constants;
import daintiness.utilities.Constants.AggregationType;
import daintiness.utilities.Constants.MeasurementType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ControllerSwing {

	private IMainController mainController;

	private final File initialDirectory = new File(
			"src" + Constants.FS + "main" + Constants.FS + "resources" + Constants.FS + "data");

	private final File initialPatternsDirectory = new File(
			"src" + Constants.FS + "main" + Constants.FS + "resources" + Constants.FS + "SavedPatterns");

	private PLDiagramSwing pld;

	private PLDiagramSwing patternsHighlightPLD;
	
	private JScrollPane pldJScrollPane;

	private int numberOfBeats;
	
	private int numberOfEntities;
	

	public ControllerSwing() {
		MainControllerFactory factory = new MainControllerFactory();
		mainController = factory.getMainController("SIMPLE_MAIN_CONTROLLER");
		//enableButtons(GuiCondition.NO_DATA);

	}

	public Boolean loadFromFolder() {
		JFileChooser fileChooser = new JFileChooser(); 
		
		fileChooser.setCurrentDirectory(initialDirectory);
		fileChooser.setDialogTitle("Select a folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showSaveDialog(null);
		
		File selectedDirectory = fileChooser.getSelectedFile();
		if ((selectedDirectory != null) && selectedDirectory.exists()) {
            mainController.load(selectedDirectory);
            return true;
        }
		return false;
	}
	
	public Boolean loadFromFile() {
		JFileChooser fileChooser = new JFileChooser(); 
		
		fileChooser.setCurrentDirectory(initialDirectory);
		fileChooser.setDialogTitle("Select a file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.showSaveDialog(null);
		
		File selectedFile = fileChooser.getSelectedFile();
		if ((selectedFile != null) && selectedFile.exists()) {
            mainController.load(selectedFile);
            return true;
        }
		return false;
	}
	
	public List<AggregationType> initializeAggregationTypes() {
		
		return mainController.getAvailableAggregationTypesList();
	}
	
	public List<MeasurementType> initializeMeasurementTypes() {
		return mainController.getAvailableMeasurementTypesList();
	}

    public void save() {
        if (mainController.hasOutputPath()) {
            mainController.save();
        } else {
            saveAs();
        }
    }


    public void saveAs() {
    	JFileChooser chooser = new JFileChooser(); 
		
    	chooser.setCurrentDirectory(new File(
    			"src" + Constants.FS + "main" + Constants.FS + "resources"));
    	chooser.setDialogTitle("Save as");
    	chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
    	chooser.showSaveDialog(null);
		
		File selectedFile = chooser.getSelectedFile();
		if (selectedFile != null) {
			String selectedFilePath = selectedFile.getAbsolutePath();
			if (!selectedFilePath.endsWith(".tsv")) {
				selectedFilePath = selectedFilePath + ".tsv";
			}
			mainController.save(new File(selectedFilePath));
        }
    }
	
    public void importProject() {
    	JFileChooser chooser = new JFileChooser(); 
		
    	chooser.setCurrentDirectory(initialDirectory);
    	chooser.setDialogTitle("Select a project");
    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	chooser.showSaveDialog(null);
		
		File selectedFile = chooser.getSelectedFile();
		if ((selectedFile != null) && selectedFile.exists()) {
			mainController.importProject(selectedFile);
			
			pld = new PLDiagramSwing(
	                mainController.getChartData(),
	                mainController.getPhases(),null);
	        
	        pldJScrollPane =  pld.getJScrollPane();
        }
    }
	 

    public void exportProject() { 
        JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose folder to save the PLD data");
		chooser.setCurrentDirectory(initialDirectory);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showSaveDialog(null);
		
		File selectedFile = chooser.getSelectedFile();
		if (selectedFile != null) {
			mainController.exportProject(selectedFile);
        }
    }
    
	public int getNumberOfBeats() {
		numberOfBeats = mainController.getNumberOfBeats();
		return numberOfBeats;
	}
	
	public int getNumberOfEntities() {
		numberOfEntities = mainController.getNumberOfEntities();
		return numberOfEntities;
	}
	
	public JScrollPane getPldScrollPane() {
		return pldJScrollPane;
	}
	
	private BeatClusteringProfile constructBeatClusteringProfile(int desiredNumberOfPhases, double weightSliderValue, Boolean timeClusteringCheckBox) {
        if (timeClusteringCheckBox == true) {
            return new BeatClusteringProfile(desiredNumberOfPhases, weightSliderValue, false);
        } else {
            return new BeatClusteringProfile(desiredNumberOfPhases);
        }
    }

    private EntityClusteringProfile constructEntityClusteringProfile(int getDesiredNumberOfEntityGroups, Boolean entityClusteringCheckBox) {
        if (entityClusteringCheckBox == true) {
            return new EntityClusteringProfile(getDesiredNumberOfEntityGroups);
        }
        return null;
    }

    public ClusteringProfile getClusteringProfile(int desiredNumberOfPhases, double weightSliderValue, Boolean timeClusteringCheckBox,int getDesiredNumberOfEntityGroups, Boolean entityClusteringCheckBox) {
    	BeatClusteringProfile beatClusteringProfile = constructBeatClusteringProfile(desiredNumberOfPhases, weightSliderValue, timeClusteringCheckBox);
		EntityClusteringProfile entityClusteringProfile = constructEntityClusteringProfile(getDesiredNumberOfEntityGroups, entityClusteringCheckBox);
        return new ClusteringProfile(beatClusteringProfile, entityClusteringProfile);
    }
    
    public void generateChartData(int desiredNumberOfPhases, double weightSliderValue, Boolean timeClusteringCheckBox,int getDesiredNumberOfEntityGroups, Boolean entityClusteringCheckBox) {
    	ClusteringProfile profile = getClusteringProfile(desiredNumberOfPhases, weightSliderValue, timeClusteringCheckBox, getDesiredNumberOfEntityGroups, entityClusteringCheckBox);

        if (profile != null) {
            mainController.fitDataToGroupPhaseMeasurements(profile);
        }

        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }
    
    public void generateDataWithClusterTypes(String measurementType, String aggregationType) {
    	
    	Constants.MeasurementType measurementTypeConst = Constants.MeasurementType.valueOf(measurementType);
    	Constants.AggregationType aggregationTypeConst = Constants.AggregationType.valueOf(aggregationType);
    	
    	mainController.generateChartDataOfType(measurementTypeConst, aggregationTypeConst);
    	
    	pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }
    
    public String getMeasurementType() {
    	MeasurementType measurementType = mainController.getMeasurementType();
    	return measurementType.toString();
    }
    public String getAggregationType() {
    	AggregationType aggregationType = mainController.getAggregationType();
    	return aggregationType.toString(); 
    }
    
    public void takeScreenshot() {
    	JFileChooser fileChooser = new JFileChooser(); 
    	fileChooser.setCurrentDirectory(new File("src" + Constants.FS + "main" + Constants.FS + "resources"));
    	fileChooser.setDialogTitle("Save image of PLD");
    	fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.png", "png"));
    	fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
    	fileChooser.showSaveDialog(null);

		File selectedFile = fileChooser.getSelectedFile();
		if (selectedFile != null && pld.getJTable() != null) {
			
			String selectedFilePath = selectedFile.getAbsolutePath();
			if (!selectedFilePath.endsWith(".png")) {
				selectedFilePath = selectedFilePath + ".png";
			}
			BufferedImage img = new BufferedImage(pld.getJTable().getWidth(), pld.getJTable().getHeight(), BufferedImage.TYPE_INT_RGB);
			pld.getJTable().paintAll(img.getGraphics());

	    	try {
				ImageIO.write(img, "png", new File(selectedFilePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    
    
    public void sortByActivityDesc() {
        mainController.sortChartData(Constants.SortingType.ACTIVITY_DESCENDING);
        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }
    
    public void sortByActivityAsc() {
        mainController.sortChartData(Constants.SortingType.ACTIVITY_ASCENDING);
        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }
    
    public void sortByLifeDurationDesc() {
        mainController.sortChartData(Constants.SortingType.LIFE_DURATION_DESCENDING);
        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }
    
    public void sortByLifeDurationAsc() {
        mainController.sortChartData(Constants.SortingType.LIFE_DURATION_ASCENDING);
        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }
    
    public void sortByBirthDateDesc() {
        mainController.sortChartData(Constants.SortingType.BIRTH_DESCENDING);
        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
        
    }
    
    public void sortByBirthDateAsc() {
        mainController.sortChartData(Constants.SortingType.BIRTH_ASCENDING);
        pld = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),null);
        
        pldJScrollPane =  pld.getJScrollPane();
    }

    
    public void showAllPatterns() {
    	
    	mainController.sortChartData(Constants.SortingType.BIRTH_ASCENDING);
		List<PatternData> patternList = mainController.getPatterns(Constants.PatternType.NO_TYPE);
    	createJFrameHighlightedPatterns(patternList);
    }
    
    public void showBirthsPatterns() {
		List<PatternData> patternList = mainController.getPatterns(Constants.PatternType.MULTIPLE_BIRTHS);
    	createJFrameHighlightedPatterns(patternList);
    }
    
    public void showUpdatesPatterns() {
    	//mainController.sortChartData(Constants.SortingType.BIRTH_ASCENDING);
		List<PatternData> patternList = mainController.getPatterns(Constants.PatternType.MULTIPLE_UPDATES);
    	createJFrameHighlightedPatterns(patternList);
    	
    }
    
    public void showDeathsPatterns() {
    	//mainController.sortChartData(Constants.SortingType.BIRTH_ASCENDING);
		List<PatternData> patternList = mainController.getPatterns(Constants.PatternType.MULTIPLE_DEATHS);
    	createJFrameHighlightedPatterns(patternList);
    	
    }
    
    public void showLadderPatterns() {
    	mainController.sortChartData(Constants.SortingType.BIRTH_ASCENDING);

		List<PatternData> patternList = mainController.getPatterns(Constants.PatternType.LADDER);
    	createJFrameHighlightedPatterns(patternList);
    }
    
    private void createJFrameHighlightedPatterns(List<PatternData> patternList) {
    	patternsHighlightPLD = new PLDiagramSwing(
                mainController.getChartData(),
                mainController.getPhases(),patternList);
    	
    	javax.swing.JFrame frame = new javax.swing.JFrame(); 
        
		frame.setTitle("PLD with patterns highlighted");
		frame.add(new javax.swing.JPanel(), BorderLayout.NORTH);

		frame.add(patternsHighlightPLD.getJScrollPane(), BorderLayout.CENTER);
        frame.setSize(new Dimension(1097, 837));
		frame.setVisible(true);
		
    }
	
	public void savePatternsReport() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save patterns as .txt file");
		if (!initialPatternsDirectory.exists()) {
			initialPatternsDirectory.mkdirs();
		}
		chooser.setCurrentDirectory(initialPatternsDirectory);
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		chooser.showSaveDialog(null);
		
		File selectedFile = chooser.getSelectedFile();
		if (selectedFile != null) {
			String selectedFilePath = selectedFile.getAbsolutePath();
			if (!selectedFilePath.endsWith(".txt")) {
				selectedFilePath = selectedFilePath + ".txt";
			}
			
			mainController.printPatterns(new File(selectedFilePath));
        }
	}
	 
}
