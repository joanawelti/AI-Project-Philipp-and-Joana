package dungeon.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import dungeon.App;
import dungeon.configuration.Configurations;

public class ParameterPanel extends JPanel {
	
	private static final long serialVersionUID = 5458256289234189978L;
	
	private LinkedList<JLabel> labels = new LinkedList<JLabel>();
	private HashMap<Integer, JSpinner> spinners = new HashMap<Integer, JSpinner>();
	private SpinnerModel model = null;
	
	private JButton resetButton = null;
	
	private JPanel content = null;
	
	
	private String title = "Settings";
	private String reset = "reset";
	
	private HashMap<Integer, Double> defaults;
	
	
	
	public ParameterPanel() {		
		setBorder(BorderFactory.createTitledBorder(title));
		
		initializeContent();
	}
	
	private void initializeContent() {
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		setContent();
		add(content);
	}
	
	private void setContentToVisible(boolean visible) {
		content.setVisible(visible);
	}
	
	
	public void setContent() {			
		if (App.getGame().hasConfigurations()) {
			Configurations configs = App.getGame().getConfigurations();
			HashMap<Integer, String> names = configs.getLabels();
			HashMap<Integer, Double> intervals = configs.getParameterIntervals();
			defaults = configs.getDefaultValues();
			HashMap<Integer, Double> mins = configs.getParameterMins();
			HashMap<Integer, Double> maxs = configs.getParameterMax();
			
			JPanel pPane = new JPanel(new GridLayout(configs.getKeys().length,2));
			
			for (int i : configs.getKeys()) {
				JLabel label = new JLabel(names.get(i));
				labels.add(label);
				model = new SpinnerNumberModel(defaults.get(i), mins.get(i), maxs.get(i), intervals.get(i));
				JSpinner spinner = new JSpinner(model);
				spinners.put(i,spinner);
				label.setLabelFor(spinner);
				
				pPane.add(label);
				pPane.add(spinner);
				
			}
			resetButton = new JButton(reset);
			resetButton.addActionListener(fReset);
			
			content.add(pPane);
	        content.add(resetButton);
	        
	        setContentToVisible(true);
		} else {
			setContentToVisible(false);
		}
		
	}
	
	
	
	
	public HashMap<Integer, Double> getParameterValues() {
		HashMap<Integer, Double> parameterValues = new HashMap<Integer, Double>();
		for (Map.Entry<Integer, JSpinner> entry : spinners.entrySet()) {
		    Integer key = entry.getKey();
		    JSpinner spinner = entry.getValue();
		    parameterValues.put(key, (Double) spinner.getValue());
		}
		return parameterValues;
	}
	
	ActionListener fReset = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			resetValues();
		}
	};
	
	
	
	private void resetValues() {
		for (Map.Entry<Integer, JSpinner> entry : spinners.entrySet()) {
		    entry.getValue().setValue(defaults.get(entry.getKey()));
		}
		
	}
	
	
}
