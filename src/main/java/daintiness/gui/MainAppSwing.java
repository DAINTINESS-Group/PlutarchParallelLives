package daintiness.gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import daintiness.utilities.Constants.AggregationType;
import daintiness.utilities.Constants.MeasurementType;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JRadioButtonMenuItem;

public class MainAppSwing extends JFrame {

	private JPanel contentPane;
	
	public ControllerSwing controller = new ControllerSwing();
	public ClusteringProfileJDialog clusterDialog;
	
	private JMenuItem showPLDMI;
	private JMenuItem closePLDMI;
	private JMenuItem TakeScreenshotMI;
	private JMenu showPatternsMenu;
	private JMenuItem savePatternsReportMI;
	private JMenuItem ClusterDataMI;
	private JMenuItem SaveAsMI;
	private JMenuItem SaveMI;
	
	private JMenu activitySortMenu;
	private JMenu lifeDurationSortMenu;	
	private JMenu birthDateSortMenu;
	
	JMenuItem exportProjectMI;
	JMenuItem importProjectMI;
	
	private JRadioButtonMenuItem activityDescMI;
	private JRadioButtonMenuItem activityAscMI;
	private JRadioButtonMenuItem lifeDurationDescMI;
	private JRadioButtonMenuItem lifeDurationAscMI;
	private JRadioButtonMenuItem birthDateDescMI;
	private JRadioButtonMenuItem birthDateAscMI;
	
	private JRadioButtonMenuItem sumOfInsertionsMI;
	private JRadioButtonMenuItem sumOfUpdatesMI;
	private JRadioButtonMenuItem sumOfInsertionsAndUpdatesMI;
	private JRadioButtonMenuItem sumOfDeletionsAndUpdates;
	private JRadioButtonMenuItem sumOfAllMI;
	private JRadioButtonMenuItem noAggregationMI;
	private JRadioButtonMenuItem sumOfDeletionsMI;
	private JRadioButtonMenuItem sumOfInsertionsAndDeletionsMI;
	private JRadioButtonMenuItem rawValueMI;
	private JRadioButtonMenuItem deltaValueMI;
	private JMenuItem generateClusteredDataMI;
	
	private String selectedAggregationType;
	private String selectedMeasurementType;
	private JMenuItem openZoomablePLDMI;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainAppSwing frame = new MainAppSwing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainAppSwing() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 1097, 837);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		
		
		
		JMenu FileMenu = new JMenu("File");
		menuBar.add(FileMenu);
		
		JMenuItem LoadFolderMI = new JMenuItem("Load from folder");
		FileMenu.add(LoadFolderMI);
		
		LoadFolderMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean loadFolder = controller.loadFromFolder();
				if(loadFolder == true) {
					clusterDialog = new ClusteringProfileJDialog(controller,showPLDMI);
					clusterDialog.setVisible(true);
					
					showPLDMI.setEnabled(true);
					showPatternsMenu.setEnabled(true);
					exportProjectMI.setEnabled(true);
					ClusterDataMI.setEnabled(true);
					SaveMI.setEnabled(true);
					SaveAsMI.setEnabled(true);
				}
			}
		});
		
		JMenuItem LoadFIleMI = new JMenuItem("Load from file");
		FileMenu.add(LoadFIleMI);
		
		LoadFIleMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean loadFile = controller.loadFromFile();
				if(loadFile == true) {
					clusterDialog = new ClusteringProfileJDialog(controller, showPLDMI);
					clusterDialog.setVisible(true);
					
					showPLDMI.setEnabled(true);
					showPatternsMenu.setEnabled(true);
					exportProjectMI.setEnabled(true);
					ClusterDataMI.setEnabled(true);
					SaveMI.setEnabled(true);
					SaveAsMI.setEnabled(true);

				}
			}
		});
		
		FileMenu.addSeparator();
		
		SaveMI = new JMenuItem("Save");
		SaveMI.setEnabled(false);
		FileMenu.add(SaveMI);
		SaveMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.save();
			}
		});
		
		SaveAsMI = new JMenuItem("Save As");
		SaveAsMI.setEnabled(false);
		FileMenu.add(SaveAsMI);
		
		SaveAsMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveAs();
			}
		});
		
		FileMenu.addSeparator();
		
		ClusterDataMI = new JMenuItem("Cluster data");
		ClusterDataMI.setEnabled(false);
		FileMenu.add(ClusterDataMI);
		
		ClusterDataMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clusterDialog = new ClusteringProfileJDialog(controller, showPLDMI);
				clusterDialog.setVisible(true);
			}
		});
		
		TakeScreenshotMI = new JMenuItem("Take Screenshot");
		TakeScreenshotMI.setEnabled(false);
		FileMenu.add(TakeScreenshotMI);
		
		TakeScreenshotMI.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.takeScreenshot();
			}
		});
		
		FileMenu.addSeparator();
		
		JMenuItem ExitMI = new JMenuItem("Exit");
		FileMenu.add(ExitMI);
		
		ExitMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JMenu PldMenu = new JMenu("PLD Actions");
		menuBar.add(PldMenu);
		
		showPLDMI = new JMenuItem("Show PLD");
		showPLDMI.setEnabled(false);
		PldMenu.add(showPLDMI);
		
		showPLDMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				initializeClusterTypes();
				revalidate();
				repaint();
				
				closePLDMI.setEnabled(true);
				TakeScreenshotMI.setEnabled(true);
				activitySortMenu.setEnabled(true);
				lifeDurationSortMenu.setEnabled(true);	
				birthDateSortMenu.setEnabled(true);
				generateClusteredDataMI.setEnabled(true);
				openZoomablePLDMI.setEnabled(true);
			}
		});
		
		closePLDMI = new JMenuItem("Close PLD");
		closePLDMI.setEnabled(false);
		PldMenu.add(closePLDMI);
		
		openZoomablePLDMI = new JMenuItem("Open Zoomable PLD");
		openZoomablePLDMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openZoomablePLD();
				getContentPane().removeAll();
				revalidate();
				repaint();
			}
		});
		openZoomablePLDMI.setEnabled(false);
		PldMenu.add(openZoomablePLDMI);
		
		closePLDMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().removeAll();
				revalidate();
				repaint();
			}
		});
		
		JMenu ProjectMenu = new JMenu("Project");
		menuBar.add(ProjectMenu);
		
		importProjectMI = new JMenuItem("Import Project");
		ProjectMenu.add(importProjectMI);
		importProjectMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.importProject();

				
				exportProjectMI.setEnabled(true);
				ClusterDataMI.setEnabled(true);
				SaveMI.setEnabled(true);
				SaveAsMI.setEnabled(true);
				showPLDMI.setEnabled(true);
				showPatternsMenu.setEnabled(true);
				
				showPLDMI.doClick();

			}
		});
		
		exportProjectMI = new JMenuItem("Export Project");
		exportProjectMI.setEnabled(false);
		ProjectMenu.add(exportProjectMI);
		

		exportProjectMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.exportProject();
			}
		});
		
		JMenu SortPldMenu = new JMenu("Sort PLD");
		menuBar.add(SortPldMenu);
		
		activitySortMenu = new JMenu("By activity");
		activitySortMenu.setEnabled(false);
		SortPldMenu.add(activitySortMenu);
		
		activityDescMI = new JRadioButtonMenuItem("Descending");
		activitySortMenu.add(activityDescMI);
		
		activityDescMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sortByActivityDesc();
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
				activityAscMI.setSelected(false);
				lifeDurationDescMI.setSelected(false);
				lifeDurationAscMI.setSelected(false);
				birthDateDescMI.setSelected(false);
				birthDateAscMI.setSelected(false);
			}
		});
		
		activityAscMI = new JRadioButtonMenuItem("Ascending");
		activitySortMenu.add(activityAscMI);
		
		activityAscMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sortByActivityAsc();
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
				activityDescMI.setSelected(false);
				lifeDurationDescMI.setSelected(false);
				lifeDurationAscMI.setSelected(false);
				birthDateDescMI.setSelected(false);
				birthDateAscMI.setSelected(false);
			}
		});
		
		lifeDurationSortMenu = new JMenu("By life duration");
		lifeDurationSortMenu.setEnabled(false);
		SortPldMenu.add(lifeDurationSortMenu);
		
		lifeDurationDescMI = new JRadioButtonMenuItem("Descending");
		lifeDurationSortMenu.add(lifeDurationDescMI);
		
		lifeDurationDescMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sortByLifeDurationDesc();
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
				activityDescMI.setSelected(false);
				activityAscMI.setSelected(false);
				lifeDurationAscMI.setSelected(false);
				birthDateDescMI.setSelected(false);
				birthDateAscMI.setSelected(false);
			}
		});
		
		lifeDurationAscMI = new JRadioButtonMenuItem("Ascending");
		lifeDurationSortMenu.add(lifeDurationAscMI);
		
		lifeDurationAscMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sortByLifeDurationAsc();
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
				activityDescMI.setSelected(false);
				activityAscMI.setSelected(false);
				lifeDurationDescMI.setSelected(false);
				birthDateDescMI.setSelected(false);
				birthDateAscMI.setSelected(false);
			}
		});
		
		birthDateSortMenu = new JMenu("By birth date");
		birthDateSortMenu.setEnabled(false);
		SortPldMenu.add(birthDateSortMenu);
		
		birthDateDescMI = new JRadioButtonMenuItem("Descending");
		birthDateSortMenu.add(birthDateDescMI);
		
		birthDateDescMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sortByBirthDateDesc();
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
				activityDescMI.setSelected(false);
				activityAscMI.setSelected(false);
				lifeDurationDescMI.setSelected(false);
				lifeDurationAscMI.setSelected(false);
				birthDateAscMI.setSelected(false);
			}
		});
		
		birthDateAscMI = new JRadioButtonMenuItem("Ascending");
		birthDateSortMenu.add(birthDateAscMI);
		
		birthDateAscMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.sortByBirthDateAsc();
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
				activityDescMI.setSelected(false);
				activityAscMI.setSelected(false);
				lifeDurationDescMI.setSelected(false);
				lifeDurationAscMI.setSelected(false);
				birthDateDescMI.setSelected(false);
			}
		});
		
		JMenu AvailableClusteringTypesMenu = new JMenu("Available clustering types");
		menuBar.add(AvailableClusteringTypesMenu);
		
		JMenu aggregationTypesMenu = new JMenu("Aggregation Types");
		AvailableClusteringTypesMenu.add(aggregationTypesMenu);
		
		sumOfInsertionsMI = new JRadioButtonMenuItem("SUM_OF_INSERTIONS");
		sumOfInsertionsMI.setEnabled(false);
		aggregationTypesMenu.add(sumOfInsertionsMI);
		
		sumOfInsertionsMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				setAggregationType(sumOfInsertionsMI.getText());
				sumOfDeletionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				sumOfAllMI.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		sumOfDeletionsMI = new JRadioButtonMenuItem("SUM_OF_DELETIONS");
		sumOfDeletionsMI.setEnabled(false);
		aggregationTypesMenu.add(sumOfDeletionsMI);
		
		sumOfDeletionsMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(sumOfDeletionsMI.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				sumOfAllMI.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		sumOfUpdatesMI = new JRadioButtonMenuItem("SUM_OF_UPDATES");
		sumOfUpdatesMI.setEnabled(false);
		aggregationTypesMenu.add(sumOfUpdatesMI);
		
		sumOfUpdatesMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(sumOfUpdatesMI.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfDeletionsMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				sumOfAllMI.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		sumOfInsertionsAndDeletionsMI = new JRadioButtonMenuItem("SUM_OF_INSERTIONS_AND_DELETIONS");
		sumOfInsertionsAndDeletionsMI.setEnabled(false);
		aggregationTypesMenu.add(sumOfInsertionsAndDeletionsMI);
		
		sumOfInsertionsAndDeletionsMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(sumOfInsertionsAndDeletionsMI.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfDeletionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				sumOfAllMI.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		sumOfInsertionsAndUpdatesMI = new JRadioButtonMenuItem("SUM_OF_INSERTIONS_AND_UPDATES");
		sumOfInsertionsAndUpdatesMI.setEnabled(false);
		aggregationTypesMenu.add(sumOfInsertionsAndUpdatesMI);
		
		sumOfInsertionsAndUpdatesMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(sumOfInsertionsAndUpdatesMI.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfDeletionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				sumOfAllMI.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		sumOfDeletionsAndUpdates = new JRadioButtonMenuItem("SUM_OF_DELETIONS_AND_UPDATES");
		sumOfDeletionsAndUpdates.setEnabled(false);
		aggregationTypesMenu.add(sumOfDeletionsAndUpdates);
		
		sumOfDeletionsAndUpdates.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(sumOfDeletionsAndUpdates.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfDeletionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfAllMI.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		sumOfAllMI = new JRadioButtonMenuItem("SUM_OF_ALL");
		sumOfAllMI.setEnabled(false);
		aggregationTypesMenu.add(sumOfAllMI);
		
		sumOfAllMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(sumOfAllMI.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfDeletionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				noAggregationMI.setSelected(false);
			}
		});
		
		noAggregationMI = new JRadioButtonMenuItem("NO_AGGREGATION");
		noAggregationMI.setEnabled(false);
		aggregationTypesMenu.add(noAggregationMI);
		
		noAggregationMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAggregationType(noAggregationMI.getText());
				sumOfInsertionsMI.setSelected(false);
				sumOfDeletionsMI.setSelected(false);
				sumOfUpdatesMI.setSelected(false);
				sumOfInsertionsAndDeletionsMI.setSelected(false);
				sumOfInsertionsAndUpdatesMI.setSelected(false);
				sumOfDeletionsAndUpdates.setSelected(false);
				sumOfAllMI.setSelected(false);
			}
		});
		
		JMenu measurementTypesMenu = new JMenu("Measurement Types");
		AvailableClusteringTypesMenu.add(measurementTypesMenu);
		
		rawValueMI = new JRadioButtonMenuItem("RAW_VALUE");
		rawValueMI.setEnabled(false);
		measurementTypesMenu.add(rawValueMI);
		
		rawValueMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setMeasurementType(rawValueMI.getText());
				deltaValueMI.setSelected(false);
			}
		});
		
		deltaValueMI = new JRadioButtonMenuItem("DELTA_VALUE");
		deltaValueMI.setEnabled(false);
		measurementTypesMenu.add(deltaValueMI);
		
		deltaValueMI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setMeasurementType(deltaValueMI.getText());
				rawValueMI.setSelected(false);
			}
		});
		
		generateClusteredDataMI = new JMenuItem("Generate clustered data");
		generateClusteredDataMI.setEnabled(false);
		
		generateClusteredDataMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.generateDataWithClusterTypes(selectedMeasurementType,selectedAggregationType);
				getContentPane().removeAll();
				setJScrollPaneToJPanel(controller.getPldScrollPane());
				revalidate();
				repaint();
				
			}
		});
		AvailableClusteringTypesMenu.add(generateClusteredDataMI);

		
		JMenu PatternsMenu = new JMenu("Patterns");
		menuBar.add(PatternsMenu);
		
		showPatternsMenu = new JMenu("Show patterns");
		showPatternsMenu.setEnabled(false);
		PatternsMenu.add(showPatternsMenu);
		
		JMenuItem birthPatternsMI = new JMenuItem("Birth patterns");
		showPatternsMenu.add(birthPatternsMI);
		
		birthPatternsMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showBirthsPatterns();
				savePatternsReportMI.setEnabled(true);
			}
		});
		
		JMenuItem updatePatternsMI = new JMenuItem("Update patterns");
		showPatternsMenu.add(updatePatternsMI);
		
		updatePatternsMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showUpdatesPatterns();
				savePatternsReportMI.setEnabled(true);
			}
		});
		
		JMenuItem deathPatternsMI = new JMenuItem("Death patterns");
		showPatternsMenu.add(deathPatternsMI);
		
		deathPatternsMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showDeathsPatterns();
				savePatternsReportMI.setEnabled(true);
			}
		});
		
		JMenuItem ladderPaternsMI = new JMenuItem("Ladders Patterns");
		showPatternsMenu.add(ladderPaternsMI);
		
		ladderPaternsMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showLadderPatterns();
				savePatternsReportMI.setEnabled(true);
			}
		});
		
		JMenuItem allPatternsMI = new JMenuItem("All patterns");
		showPatternsMenu.add(allPatternsMI);
		
		allPatternsMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showAllPatterns();
				savePatternsReportMI.setEnabled(true);
			}
		});
		
		savePatternsReportMI = new JMenuItem("Save report of loaded patterns");
		savePatternsReportMI.setEnabled(false);
		PatternsMenu.add(savePatternsReportMI);
		
		savePatternsReportMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.savePatternsReport();
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
	}
	
	private void setJScrollPaneToJPanel(JScrollPane jScrollPane) {
		contentPane.add(jScrollPane);
	}
	
	private void initializeClusterTypes() {		
		List<AggregationType> aggregationTypes = controller.initializeAggregationTypes();
		
		sumOfInsertionsMI.setEnabled(false);
		sumOfDeletionsMI.setEnabled(false);
		sumOfUpdatesMI.setEnabled(false);
		sumOfInsertionsAndDeletionsMI.setEnabled(false);
		sumOfInsertionsAndUpdatesMI.setEnabled(false);
		sumOfDeletionsAndUpdates.setEnabled(false);
		sumOfAllMI.setEnabled(false);
		noAggregationMI.setEnabled(false);
		rawValueMI.setEnabled(false);		
		deltaValueMI.setEnabled(false);
		
		String currentAggregrationType = controller.getAggregationType();
	
		for(AggregationType type: aggregationTypes) {
			if(type.toString() == "SUM_OF_INSERTIONS") {
				sumOfInsertionsMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfInsertionsMI.setSelected(true);
				}
			}
			else if(type.toString() == "SUM_OF_DELETIONS") {
				sumOfDeletionsMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfDeletionsMI.setSelected(true);
				}
			}
			else if(type.toString() == "SUM_OF_UPDATES") {
				sumOfUpdatesMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfUpdatesMI.setSelected(true);
				}
			}
			else if(type.toString() == "SUM_OF_INSERTIONS_AND_DELETIONS") {
				sumOfInsertionsAndDeletionsMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfInsertionsAndDeletionsMI.setSelected(true);
				}
			}
			else if(type.toString() == "SUM_OF_INSERTIONS_AND_UPDATES") {
				sumOfInsertionsAndUpdatesMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfInsertionsAndUpdatesMI.setSelected(true);
				}
			}
			else if(type.toString() == "SUM_OF_DELETIONS_AND_UPDATES") {
				sumOfDeletionsAndUpdates.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfDeletionsAndUpdates.setSelected(true);
				}
			}
			else if(type.toString() == "SUM_OF_ALL") {
				sumOfAllMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					sumOfAllMI.setSelected(true);
				}
			}
			else if(type.toString() == "NO_AGGREGATION") {
				noAggregationMI.setEnabled(true);
				if(currentAggregrationType == type.toString()) {
					setAggregationType(type.toString());
					noAggregationMI.setSelected(true);
				}
			}
			
		}
		
		String currentMeasurementType = controller.getMeasurementType();

		List<MeasurementType> measurementTypes = controller.initializeMeasurementTypes();
		for(MeasurementType type: measurementTypes) {
			if(type.toString() == "RAW_VALUE") {
				rawValueMI.setEnabled(true);
				if(currentMeasurementType == type.toString()) {
					setMeasurementType(type.toString());
					rawValueMI.setSelected(true);
				}
			}
			else if(type.toString() == "DELTA_VALUE") {
				deltaValueMI.setEnabled(true);
				if(currentMeasurementType == type.toString()) {
					setMeasurementType(type.toString());
					deltaValueMI.setSelected(true);
				}
			}
		}
	}

	private void setMeasurementType(String measurementType) {
		selectedMeasurementType = measurementType;
	}
	private void setAggregationType(String aggregationType) {
		selectedAggregationType = aggregationType;		
	}
}
