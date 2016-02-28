package PluginReference;

import java.util.List;

/** 
 * Interface representing a Command object
 */ 			
public interface MC_Command
{
	 /** 
     * Get the command name.
     * 
     * @return Command name. 
     */ 		
	public String getCommandName();
	 /** 
     * Get aliases for this command (if any)
     * 
     * @return List of aliases 
     */ 		
	public List<String> getAliases();
	
	 /** 
     * Get string that appears when user does /help
     * 
     * @param plr Player Object
     * @return Help text 
     */ 		
	public String getHelpLine(MC_Player plr);
	
	 /** 
     * Called when user runs command registered for this instance
     * 
     * @param plr Player object, null if non-player (like Console)
     * @param args List of parameters
     */ 		
	public void handleCommand(MC_Player plr, String[] args);

	 /** 
     * Called to check if user has permission to this command.
     * 
     * @param plr Player Object
     * @return True if has permission, False otherwise. 
     */ 			
	public boolean hasPermissionToUse(MC_Player plr);

	 /** 
     * Called when user uses [tab] key with your command.
     * 
     * @param plr Player Object
     * @param args Current arguments being passed
     * @return Help text 
     */ 		
	public List<String> getTabCompletionList(MC_Player plr, String[] args);
	
	
}

