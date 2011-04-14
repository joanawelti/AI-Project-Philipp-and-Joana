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

import dungeon.ai.ReinforcementLearnerParameters;

public class ParameterPanel extends JPanel {
	
	private static final long serialVersionUID = 5458256289234189978L;
	
	private JLabel initValueLabel = null;
	private JLabel alphaLabel = null;
	private JLabel discountLabel = null;
	private JLabel greedinessLabel = null;
	
	private JButton resetButton = null;
	
	private JSpinner initValueSpinner = null;
	private JSpinner alphaValueSpinner = null;
	private JSpinner discountValueSpinner = null;
	private JSpinner greedinessValueSpinner = null;
	private SpinnerModel model = null;
	
	private JPanel content = null;
	
	
	private String init = "Initial Q-Values";
	private String alpha = "Alpha value";
	private String discountfactor = "Discount factor";
	private String greediness = "Greediness";
	
	private String title = "Settings";
	private String reset = "reset";
	private String set = "set";
	
	private ReinforcementLearnerParameters params;
	
	
	public ParameterPanel() {
		params = new ReinforcementLearnerParameters();
		
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
		
		initValueLabel = new JLabel(init);
		model = new SpinnerNumberModel(params.getInitDefaultValue(), 0.00, 1.00, 0.01);
		initValueSpinner = new JSpinner(model);
		
		alphaLabel = new JLabel(alpha);
		model = new SpinnerNumberModel(params.getAlphaDefaultValue(), 0.00, 1.00, 0.01);
		alphaValueSpinner = new JSpinner(model);
		
		discountLabel = new JLabel(discountfactor);
		model = new SpinnerNumberModel(params.getDiscountDefaultValue(), 0.00, 1.00, 0.01);
		discountValueSpinner = new JSpinner(model);		
		
		greedinessLabel = new JLabel(greediness);
		model = new SpinnerNumberModel(params.getGreedinessDefaultValue(), 0.00, 1.00, 0.01);
		greedinessValueSpinner = new JSpinner(model);
		
		initValueLabel.setLabelFor(initValueSpinner);
		alphaLabel.setLabelFor(alphaValueSpinner);
		discountLabel.setLabelFor(discountValueSpinner);
		greedinessLabel.setLabelFor(greedinessValueSpinner);
		
		resetButton = new JButton(reset);
		resetButton.addActionListener(fReset);
		
		
		//Lay out the labels and Spinners in a panel.
        JPanel labelPane = new JPanel(new GridLayout(4,2));
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
	
	
	
	public double getInitValue() {
		return (Double) initValueSpinner.getValue();
	}
	
	public void setInitValue(double value) {
		initValueSpinner.setValue((Double) value);
	}
	
	public double getAlphaValue() {
		return (Double) alphaValueSpinner.getValue(); 
	}
	
	public void setAlphaValue(double value) {
		alphaValueSpinner.setValue((Double) value);
	}
	
	public double getDiscountValue() {
		return (Double) discountValueSpinner.getValue(); 
		
	}
	
	public void setDiscountValue(double value) {
		discountValueSpinner.setValue((Double) value);
	}
	
	public double getGreedinessValue() {
		return (Double) greedinessValueSpinner.getValue(); 
		
	}
	
	public void setGreedinessValue(double value) {
		greedinessValueSpinner.setValue((Double) value);
	}
	
	public void getAndSetParameterValues(ReinforcementLearnerParameters params) {
		params.setInitValue(getInitValue());
		setInitValue(params.getInitValue());
		params.setAlphaValue(getAlphaValue());
		setAlphaValue(params.getAlphaValue());
		params.setDiscountValue(getDiscountValue());
		setDiscountValue(params.getDiscountFactorValue());
		params.setGreedinessValue(getGreedinessValue());
		setGreedinessValue(params.getGreedinessValue());
	}
	
	ActionListener fReset = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			resetValues();
		}
	};
	
	
	
	private void resetValues() {
		initValueSpinner.setValue((Double) params.getInitDefaultValue());
		alphaValueSpinner.setValue((Double) params.getAlphaDefaultValue());
		discountValueSpinner.setValue((Double) params.getDiscountDefaultValue());
		greedinessValueSpinner.setValue((Double) params.getGreedinessDefaultValue());
	}
	
	
}
