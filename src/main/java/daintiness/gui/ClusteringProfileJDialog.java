package daintiness.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.beans.binding.Bindings;

import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JSlider;



public class ClusteringProfileJDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField numberPhasesInput;
	private JTextField numberEntitiesInput;
	private JSlider DistanceWeightsSlider;
	private JCheckBox EntityClusteringParameterCheckBox;
	private JCheckBox TimeClusterParametersCheckBox;
	private JLabel changesValueLabel;
	private JLabel timeValueLabel;


	/**
	 * Create the dialog.
	 */
	public ClusteringProfileJDialog(ControllerSwing controller, JMenuItem showPLD) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel TimeClusterParametersLabel = new JLabel("Time-clustering parameters");
			TimeClusterParametersLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
			TimeClusterParametersLabel.setBounds(10, 7, 191, 14);
			contentPanel.add(TimeClusterParametersLabel);
		}
		{
			TimeClusterParametersCheckBox = new JCheckBox("");
			TimeClusterParametersCheckBox.setSelected(true);
			TimeClusterParametersCheckBox.setBounds(207, 7, 21, 23);
			contentPanel.add(TimeClusterParametersCheckBox);
		}
		{
			JLabel DesiredNumberPhasesLabel = new JLabel("Desired number of phases");
			DesiredNumberPhasesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			DesiredNumberPhasesLabel.setBounds(10, 52, 171, 14);
			contentPanel.add(DesiredNumberPhasesLabel);
		}
		{
			numberPhasesInput = new JTextField();
			numberPhasesInput.setColumns(10);
			numberPhasesInput.setBounds(208, 50, 61, 20);
			numberPhasesInput.setText(Integer.toString(controller.getNumberOfBeats()));
			contentPanel.add(numberPhasesInput);
		}
		{
			JLabel DistanceWeightsLabel = new JLabel("Distance weights");
			DistanceWeightsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			DistanceWeightsLabel.setBounds(10, 92, 155, 14);
			contentPanel.add(DistanceWeightsLabel);
		}
		{
			DistanceWeightsSlider = new JSlider();
			DistanceWeightsSlider.setPaintTicks(true);
			DistanceWeightsSlider.setBounds(207, 93, 130, 26);
			DistanceWeightsSlider.setMinimum(0);
			DistanceWeightsSlider.setMaximum(100);
			DistanceWeightsSlider.setValue(50);
			contentPanel.add(DistanceWeightsSlider);
			
			DistanceWeightsSlider.addChangeListener(new ChangeListener() {
			      public void stateChanged(ChangeEvent e) {
			    	  if(DistanceWeightsSlider.getValue() > 50) {
			    		  changesValueLabel.setText(Double.toString((double)DistanceWeightsSlider.getValue()/100));
				    	  timeValueLabel.setText(Double.toString((double)(100-DistanceWeightsSlider.getValue())/100));
			    	  }
			    	  else if(DistanceWeightsSlider.getValue() < 50) {
			    		  changesValueLabel.setText(Double.toString((double)(100-DistanceWeightsSlider.getValue())/100));
				    	  timeValueLabel.setText(Double.toString((double)DistanceWeightsSlider.getValue()/100));
			    	  }
			    	  
			      }
			    });
		}
		{
			JLabel EntityClusteringParametersLabel = new JLabel("Entity-clustering parameters");
			EntityClusteringParametersLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
			EntityClusteringParametersLabel.setBounds(10, 155, 191, 14);
			contentPanel.add(EntityClusteringParametersLabel);
		}
		{
			EntityClusteringParameterCheckBox = new JCheckBox("");
			EntityClusteringParameterCheckBox.setSelected(true);
			EntityClusteringParameterCheckBox.setBounds(207, 152, 21, 23);
			contentPanel.add(EntityClusteringParameterCheckBox);
		}
		{
			JLabel DesiredNumberEntityGroups = new JLabel("Desired number of entityGroups");
			DesiredNumberEntityGroups.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			DesiredNumberEntityGroups.setBounds(10, 199, 188, 14);
			contentPanel.add(DesiredNumberEntityGroups);
		}
		{
			numberEntitiesInput = new JTextField();
			numberEntitiesInput.setColumns(10);
			numberEntitiesInput.setBounds(208, 197, 61, 20);
			numberEntitiesInput.setText(Integer.toString(controller.getNumberOfEntities()));
			contentPanel.add(numberEntitiesInput);
		}
		{
			JLabel changesLabel = new JLabel("Changes");
			changesLabel.setBounds(155, 93, 46, 14);
			contentPanel.add(changesLabel);
		}
		{
			JLabel timeLabel = new JLabel("Time");
			timeLabel.setBounds(347, 93, 46, 14);
			contentPanel.add(timeLabel);
		}
		{
			timeValueLabel = new JLabel("0,50");
			timeValueLabel.setBounds(347, 118, 46, 14);
			contentPanel.add(timeValueLabel);
		}
		{
			changesValueLabel = new JLabel("0,50");
			changesValueLabel.setBounds(155, 117, 46, 14);
			contentPanel.add(changesValueLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						controller.generateChartData(Integer.parseInt(numberPhasesInput.getText()),DistanceWeightsSlider.getValue()/100,TimeClusterParametersCheckBox.isSelected(),Integer.parseInt(numberEntitiesInput.getText()),EntityClusteringParameterCheckBox.isSelected());
						showPLD.doClick();
						dispose(); 
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose(); 
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

}
