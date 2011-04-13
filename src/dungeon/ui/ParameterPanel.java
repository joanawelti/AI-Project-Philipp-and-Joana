package dungeon.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class ParameterPanel extends JPanel {
	
	private static final long serialVersionUID = 5458256289234189978L;
	
	private int ogreValue = 1;
	private double initValue = 0.01;
	private double alphaValue = 0.5;
	private double discountValue = 0.9;
	private double greedinessValue = 0.9;
	
	private JLabel ogresLabel = null;
	private JLabel initValueLabel = null;
	private JLabel alphaLabel = null;
	private JLabel discountLabel = null;
	private JLabel greedinessLabel = null;
	
	private JButton resetButton = null;
	
	private JSpinner ogresSpinner = null;
	private JSpinner initValueSpinner = null;
	private JSpinner alphaValueSpinner = null;
	private JSpinner discountValueSpinner = null;
	private JSpinner greedinessValueSpinner = null;
	private SpinnerModel model = null;
	
	private JPanel content = null;
	
	
	private String ogres = "Number of Ogres";
	private String init = "Initial Q-Values";
	private String alpha = "Alpha value";
	private String discountfactor = "Discount factor";
	private String greediness = "Greediness";
	
	private String title = "Settings";
	private String reset = "reset";
	
	
	
	public ParameterPanel() {
		setBorder(BorderFactory.createTitledBorder(title));
		setContent();
		setContentToVisible(false);
	}
	
	public void setContentToVisible(boolean visible) {
		content.setVisible(visible);
	}
	
	private void setContent() {			
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		
		ogresLabel = new JLabel(ogres);
		model = new SpinnerNumberModel(ogreValue, 0, 10, 1);
		ogresSpinner = new JSpinner(model);
		
		initValueLabel = new JLabel(init);
		model = new SpinnerNumberModel(initValue, 0.00, 1.00, 0.01);
		initValueSpinner = new JSpinner(model);
		
		alphaLabel = new JLabel(alpha);
		model = new SpinnerNumberModel(alphaValue, 0.00, 1.00, 0.01);
		alphaValueSpinner = new JSpinner(model);
		
		discountLabel = new JLabel(discountfactor);
		model = new SpinnerNumberModel(discountValue, 0.00, 1.00, 0.01);
		discountValueSpinner = new JSpinner(model);		
		
		greedinessLabel = new JLabel(greediness);
		model = new SpinnerNumberModel(greedinessValue, 0.00, 1.00, 0.01);
		greedinessValueSpinner = new JSpinner(model);
		
		
		ogresLabel.setLabelFor(ogresSpinner);
		initValueLabel.setLabelFor(initValueSpinner);
		alphaLabel.setLabelFor(alphaValueSpinner);
		discountLabel.setLabelFor(discountValueSpinner);
		greedinessLabel.setLabelFor(greedinessValueSpinner);
		
		resetButton = new JButton(reset);
		resetButton.addActionListener(fReset);
		
		//Lay out the labels and Spinners in a panel.
        JPanel labelPane = new JPanel(new GridLayout(5,2));
        labelPane.add(ogresLabel);
        labelPane.add(ogresSpinner);
        labelPane.add(initValueLabel);
        labelPane.add(initValueSpinner);
        labelPane.add(alphaLabel);
        labelPane.add(alphaValueSpinner);
        labelPane.add(discountLabel);
        labelPane.add(discountValueSpinner);
        labelPane.add(greedinessLabel);
        labelPane.add(greedinessValueSpinner);
          
        content.add(labelPane);
        content.add(resetButton);
        
        add(content);
	}
	
	
	
	public int getOgreNumber() {
		return (Integer) ogresSpinner.getValue();
	}
	
	public double getInitValue() {
		return (Double) initValueSpinner.getValue(); 
	}
	
	public double getAlphaValue() {
		return (Double) alphaValueSpinner.getValue(); 
	}
	
	public double getDiscountValue() {
		return (Double) discountValueSpinner.getValue(); 
	}
	
	public double getGreedinessValue() {
		return (Double) greedinessValueSpinner.getValue(); 
	}
	
	
	
	ActionListener fReset = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			resetValues();
		}
	};
	
	private void resetValues() {
		ogresSpinner.setValue((Integer) ogreValue);
		initValueSpinner.setValue((Double) initValue);
		alphaValueSpinner.setValue((Double) alphaValue);
		discountValueSpinner.setValue((Double) discountValue);
		greedinessValueSpinner.setValue((Double) greedinessValue);
	}
	
	
}
