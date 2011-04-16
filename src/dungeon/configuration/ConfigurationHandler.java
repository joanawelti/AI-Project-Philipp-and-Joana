package dungeon.configuration;


import org.w3c.dom.Node;

import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

public class ConfigurationHandler implements Persistent {
	
	private Configurations configurations;
	
	/**
	 * 
	 * @return configuration
	 */
	public Configurations getConfigurations() {
		return this.configurations;
	}
	
	/**
	 * Loads the configuration class as specified in the XML file
	 */
	public void load(Node node) {		
		if (XMLHelper.attributeExists(node, "Type")) {
			String class_name = XMLHelper.getStrValue(node, "Type");
			
			try {
				Class<?> c = Class.forName("dungeon.configuration." + class_name);
				
				if (Class.forName("dungeon.configuration.Configurations").isAssignableFrom(c)) {
					Configurations params = (Configurations)c.newInstance();
					if (params != null)
						this.configurations = params;
				} else {
					System.err.println("Specified parameter class " + class_name + " is not a subclass of dungeon.configuration.Configurations");
				}
			} catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}

	public void save(Node node) {
		String name = getConfigurations().getClass().getName();
		XMLHelper.setStrValue(node,"Configurations", name.split("Configurations")[0]);
	}
}


	
	


