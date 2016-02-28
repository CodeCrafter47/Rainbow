package PluginReference;

/** 
 * Event Info w/option to cancel
 */ 			
public class MC_EventInfo
{
	/** 
	 * Set to TRUE if event is cancelled.
	 */ 			
	public boolean isCancelled = false;
	/** 
	 * Internal value, only set where explicitly documented.
	 */ 			
	public String tag = null;
	/** 
	 * Indicates event parameters (such as lists) were modified.
	 */ 			
	public boolean isModified = false;
}
