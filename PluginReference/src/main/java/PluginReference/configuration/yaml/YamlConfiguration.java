package PluginReference.configuration.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Java YML
 * @author Isaiah Patton
 */
public class YamlConfiguration {
    private File file;
    private InputStream is;
    private Yaml yaml = new Yaml();
    private Map<String, ArrayList<?>> yamlParsers;

    @SuppressWarnings("unchecked") 
    public YamlConfiguration(File f) throws FileNotFoundException {
        this.file = f;
        this.is = new FileInputStream(f);
        this.yamlParsers = (Map<String, ArrayList<?>>) yaml.load(is);
    }

    public ArrayList<?> get(String field) {
        return yamlParsers.get(field);
    }
    
    /**
     * @return String
     */
    public Object getString(String field){
        return yamlParsers.get(field);
    }
    
    /**
     * @return ArrayList
     */
    public ArrayList<?> getList(String field){
        return yamlParsers.get(field);
    }
    
    /**
     * @return Boolean
     */
    public boolean getBoolean(String field){
        return getString(field).toString() == "true" ? true : false;
    }
    
    /**
     * set the yaml field
     */ 
    @SuppressWarnings("rawtypes") 
    public void set(String field, ArrayList value) {
        yamlParsers.put(field, value);
        
        try { 
            yaml.dump(yamlParsers, new PrintWriter(file)); 
        } catch (FileNotFoundException e) { 
            System.out.println(e); 
        } 
    } 
}
