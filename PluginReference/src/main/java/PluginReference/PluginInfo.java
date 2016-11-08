package PluginReference;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** 
 * Information about a plugin
 */ 			
public class PluginInfo
{
	 /** 
          * Plugin Description
          */ 			
	public String description = null;
	 /** 
          * Plugin Version (optional)
          */ 			
	public String version = null;
	 /** 
          * Event order/priority. Use negative value to get called earlier than default, otherwise higher. Use extreme values if you want to force yourself to begin or end of plugin order
          */ 			
	public double eventSortOrder = 0.0f; 

	/** 
         * If require events *before* another plugin, add those plugin names here
         */ 			
	public List<String> pluginNamesINeedToGetEventsBefore = null; 
	/** 
         * // If require events *after* another plugin, add those plugin names here
         */ 			
	public List<String> pluginNamesINeedToGetEventsAfter = null; 
	/** 
         * Optional data (can be used for inter-plugin communication)
         */ 			
	public ConcurrentHashMap<String, String> optionalData = new ConcurrentHashMap<String, String>(); 

	// The below will be set by Rainbow, you can use if needed after retrieving with server.getPlugins()
	/** 
         * Plugin name (set by Rainbow after getPluginInfo call)
         */ 			
	public String name = null;
	/** 
         * Plugin path (set by Rainbow after getPluginInfo call)
         */ 			
	public String path = null;
	/** 
         * Reference to 'MyPlugin' object (set by Rainbow after getPluginInfo call)
        */ 			
	public PluginBase ref = null;	
}
