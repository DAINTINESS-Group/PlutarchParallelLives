package daintiness.gui.tableview;


import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
//import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import daintiness.clustering.EntityGroup;
import daintiness.clustering.Phase;
import daintiness.clustering.measurements.ChartGroupPhaseMeasurement;
import daintiness.models.CellInfo;
import daintiness.models.PatternData;
import daintiness.models.measurement.EmptyIMeasurement;
import daintiness.models.measurement.IMeasurement;
import daintiness.utilities.Constants;
import daintiness.utilities.Constants.PatternType;
import javafx.collections.ObservableList;

public class PLDiagramSwing{


    private final int cellWidth = 25;
    private final int firstColumnWidth = 110;

    private EntityGroup selectedGroup;
    private IMeasurement selectedMeasurement;
    private Phase selectedPhase;
    
    private List<Phase> phasesJTable;
    private JTable table;
    private JScrollPane jScrollPane;
    private DefaultTableModel tableModel;
    private List<PatternData> patternList;
    

	
	  public PLDiagramSwing(ObservableList<ChartGroupPhaseMeasurement>
	  	observableList, List<Phase> phases, List<PatternData> patternList) {
	  
		  this.patternList = patternList; 
		  createJTable(observableList, phases);
		  createJTableMouseListener();
	  }
	 
    
    private void createJTable(ObservableList<ChartGroupPhaseMeasurement> observableList, List<Phase> phases) {
    	
    	List<String> columnsJTable = new ArrayList<String>();
    	
    	phasesJTable = new ArrayList<Phase>(phases);

    	//Initialize Columns
    	for(Phase i: phases) {
    		int phaseId = i.getPhaseId();
    		if(phaseId == 0) {
    			columnsJTable.add("Groups");
    		}
    		columnsJTable.add(Integer.toString(phaseId));	
    	}
    
    	String[] col = columnsJTable.toArray(new String[0]);
     	tableModel = new DefaultTableModel(col, 0);

     	//Initialize JTable
     	table = new JTable(tableModel) {
     		private static final long serialVersionUID = 1L;
     		public boolean isCellEditable(int row, int column){  
     	          return false;  
     	      };
     	};
     	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
     	table.getColumnModel().getColumn(0).setPreferredWidth(firstColumnWidth);

     	for(int i = 1; i < table.getColumnCount(); i++) {
     		table.getColumnModel().getColumn(i).setPreferredWidth(cellWidth);
     	}
     	
     	
     	//Initialize data in JTable based on a ArrayList with ChartGroupPhaseMeasurement Objects
		for(ChartGroupPhaseMeasurement tab : observableList) {
			List<ChartGroupPhaseMeasurement> entityGroupsJTable = new ArrayList<ChartGroupPhaseMeasurement>();

			entityGroupsJTable.add(tab);
			for (Phase phase : phases) {
				entityGroupsJTable.add(tab);
			}
			Object[] objs = entityGroupsJTable.toArray(new Object[phases.size()]);
			tableModel.addRow(objs);
			
		}

		table.setDefaultRenderer(Object.class, new PaintTableCellRenderer());
		table.setPreferredScrollableViewportSize(
			    new Dimension(
			        table.getPreferredSize().width + 1,
			        table.getRowHeight() *  (observableList.size() + 2)));
		

		jScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    }
    
    

    public class PaintTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	
        	super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        	
        	
        	ChartGroupPhaseMeasurement val = (ChartGroupPhaseMeasurement)value;
        	EntityGroup entityGroup = val.getEntityGroup();
    		String entityGroupName = null;
        	if(column == 0) {
        		if (entityGroup != null) {
    				if (entityGroup.getGroupComponents().size() == 1) {
    					entityGroupName = entityGroup.getGroupComponents().get(0).getEntityName();
    				} else {
    					entityGroupName = String.valueOf(entityGroup.getEntityGroupId());
    				}
    			}
        		

        		if(entityGroup.getLifeDetails().isAlive()) {
        			setForeground(java.awt.Color.white);
        			setBackground(java.awt.Color.decode("#47c4c4"));
        			setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, java.awt.Color.BLACK));
        			
        			setValue(entityGroupName);
        		}
        		else {
        			setForeground(java.awt.Color.white);
            		setBackground(java.awt.Color.decode("#c26847"));
            		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, java.awt.Color.BLACK));
            		setValue(entityGroupName);
            		
            	}
        	}
        	else {
        		if(val != null) {
    				Phase phase = phasesJTable.get(column-1);
    				int phaseId = phase.getPhaseId();
    				if(phaseId == column - 1) {
    					Constants.GPMType type = val.getEntityGroup().getGPMType(phase.getFirstPhaseBeat().getBeatId(),
        						phase.getLastPhaseBeat().getBeatId());
        				IMeasurement measurement = new EmptyIMeasurement(type);
        				
        				
        				if (type.equals(Constants.GPMType.ACTIVE) && val.containsMeasurementInPhase(phaseId)) {
        					String color = val.getMeasurement(phaseId).getColor();
        					if(color != null) {
        						setBackground(java.awt.Color.decode(val.getMeasurement(phaseId).getColor()));
            					setValue("");
        					}
        				} else {
        					String color = measurement.getColor();
        					if(color != null) {
        						setBackground(java.awt.Color.decode(measurement.getColor()));
            					setValue("");
        					}
        				}
    				}
    				
    				if(patternList != null) {
    					for(PatternData pattern: patternList) {
							for(CellInfo cell: pattern.getPatternCellsList()) {
								
								if (pattern.getPatternType() == PatternType.MULTIPLE_BIRTHS && cell
										.getEntityName() == entityGroup.getGroupComponents().get(0).getEntityName()
										&& cell.getPhaseId() == phaseId) {
									setBackground(java.awt.Color.BLUE);
								}
								else if (pattern.getPatternType() == PatternType.MULTIPLE_UPDATES && cell
										.getEntityName() == entityGroup.getGroupComponents().get(0).getEntityName()
										&& cell.getPhaseId() == phaseId) {
									setBackground(java.awt.Color.ORANGE);
								}
								else if (pattern.getPatternType() == PatternType.MULTIPLE_DEATHS && cell
										.getEntityName() == entityGroup.getGroupComponents().get(0).getEntityName()
										&& cell.getPhaseId() == phaseId) {
									setBackground(java.awt.Color.RED);
								}
								 
								else if (pattern.getPatternType() == PatternType.LADDER && cell
										.getEntityName() == entityGroup.getGroupComponents().get(0).getEntityName()
										&& cell.getPhaseId() == phaseId) {
									setBackground(java.awt.Color.MAGENTA);
								}
							}    						
    					}
    				}
        		}
        	}
        	
        	return this;
        }
    }
    
    public void createJTableMouseListener() {
    	getJTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 1) {
                	final JTable target = (JTable)e.getSource();
                    final int row = target.getSelectedRow();
                    final int column = target.getSelectedColumn();
                    // Cast to ur Object type
                	final ChartGroupPhaseMeasurement cell = (ChartGroupPhaseMeasurement)target.getValueAt(row, column);
                	
                	if(column == 0) {
            			String entityGroupName = "";
                		if (cell.getEntityGroup().getGroupComponents().size() == 1) {
        					entityGroupName = cell.getEntityGroup().getGroupComponents().get(0).getEntityName();
        				} else {
        					entityGroupName = String.valueOf(cell.getEntityGroup().getEntityGroupId());
        				}
                		
            			 String entityGroupInfo = String.format("Group id: %d\n" +
            					 	"Entity name: %s\n" +
            		                "GroupLifeDetails:\n" +
            		                "->Birth: %d\n" +
            		                "->Death: %d\n" +
            		                "->Duration: %d\n" +
            		                "->Alive: %b\n",
            		                cell.getEntityGroup().getEntityGroupId(),
            		                entityGroupName,
            		                cell.getEntityGroup().getLifeDetails().getBirthBeatId(),
            		                cell.getEntityGroup().getLifeDetails().getDeathBeatId(),
            		                cell.getEntityGroup().getLifeDetails().getDuration(),
            		                cell.getEntityGroup().getLifeDetails().isAlive());
            			 
            			 
            			 JOptionPane.showMessageDialog(null,entityGroupInfo,"Entity info", JOptionPane.INFORMATION_MESSAGE);
            		}    	
                }
            }
        });

    }
 
    public EntityGroup getSelectedGroup() {
        return selectedGroup;
    }

    public IMeasurement getSelectedMeasurement() {
        return selectedMeasurement;
    }

    public Phase getSelectedPhase() {
        return selectedPhase;
    }
    
    public TableModel getTableModel() {
        return tableModel;
    }
    
    public JTable getJTable() {
        return table;
    }
    
    public JScrollPane getJScrollPane() {
        return jScrollPane;
    }
    
}
