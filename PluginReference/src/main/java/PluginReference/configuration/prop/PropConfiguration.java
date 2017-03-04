package PluginReference.configuration.prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * .properties file editer,
 * Configuration for plugins.
 * 
 * @author Isaiah Patton
 * @version 1.0.0
 * @see java.util.Properties
 */
public class PropConfiguration {
    public File file;

    public PropConfiguration(File f) {
        this.file = f;
    }
    
    /**
     * Sets field to new text
     */
    public void set(String field, String text){
        try {
			Properties properties = new Properties();
            properties.load(new FileInputStream(file));
			properties.setProperty(field, text);

			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, " ");
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Returns the field text.
     * 
     * @return String
     */
 	public String get(String field) throws FileNotFoundException {
        FileInputStream in = new FileInputStream(file);
 		Properties prop = new Properties();
        try {
 			prop.load(in);
 		} catch (IOException e1) {
 			e1.printStackTrace();
 		}
        try {
 			in.close();
 		} catch (IOException e1) {
 			e1.printStackTrace();
 		}
 	    return prop.getProperty(field);
 	}

 	/**
 	 * 
 	 * @return true <i>if field equals true</i> false <i>if field does not equals true</i>
 	 */
    public boolean getBoolean(String field) {
        try {
            return get(field).toLowerCase().contains("true");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR!");
            e.printStackTrace();
            return false;
        }
    }
}