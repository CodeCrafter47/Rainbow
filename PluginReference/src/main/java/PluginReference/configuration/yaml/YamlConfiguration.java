package PluginReference.configuration.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Java YAML for Project Rainbow.
 * @author Isaiah Patton
 */
public class YamlConfiguration {
    private File file;
    private InputStream is;
    private Yaml yaml = new Yaml();
    private Map<String, Object> yamlParsers;

    @SuppressWarnings("unchecked") 
    public YamlConfiguration(File f) throws FileNotFoundException {
        this.file = f;
        this.is = new FileInputStream(f);
        this.yamlParsers = (Map<String, Object>) yaml.load(is);
    }

    public ArrayList<?> get(String field) {
        return (ArrayList<?>) yamlParsers.get(field);
    }

    /**
     * @return String
     */
    public Object getString(String field){
        return yamlParsers.get(field);
    }

    /**
     * Gets an sub field Ex:
     *
     * getSub("yaml:good")
     *   =
     * yaml:
     *   good: true
     *   bad: false
     *
     */
    public Object getSub(String field) {
        String[] f = field.split(":");
        HashMap<?, ?> messages = (HashMap<?, ?>) (Object) yamlParsers.get(f[0]);
        System.out.println(messages);
        return messages.get(f[1]);
    }

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    public void setSub(String field, ArrayList value) {
        String[] f = field.split(":");
        HashMap<?, ?> messages = (HashMap<?, ?>) (Object) yamlParsers.get(f[0]);
        set(messages.get(f[1]).toString(), value);
    }
    
    /**
     * @return ArrayList
     */
    public ArrayList<?> getList(String field){
        return (ArrayList<?>) yamlParsers.get(field);
    }
    
    /**
     * @return ArrayList
     */
    public boolean getBoolean(String field){
        return Boolean.valueOf(yamlParsers.get(field).toString());
    }
    
    /**
     * set the yaml field
     */ 
    @SuppressWarnings("rawtypes") 
    public void set(String field, Object value) {
        yamlParsers.put(field, value);
        
        try { 
            yaml.dump(yamlParsers, new PrintWriter(file)); 
        } catch (FileNotFoundException e) { 
            System.out.println(e); 
        } 
    } 
}
