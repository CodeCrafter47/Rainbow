package PluginReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** 
 * Collection of utility functions available to Rainbow plugins
 */ 			
public class RainbowUtils
{
	private static MC_Server server;

	/**
	 * Set the server instance. For internal use only. Should not be used by
	 * plugins.
	 * @param server the server instance
     */
	public static void setServer(MC_Server server) {
		RainbowUtils.server = server;
	}

	/**
	 * Get an instance of the MC_Server object.
	 * @return the server
     */
	public static MC_Server getServer() {
		return server;
	}
	 /** 
     * Get a short description of a number of milliseconds.
     * 
     * @param ms Milliseconds
     * @return String representation like 01m 23s if under 60 minutes, otherwise like 01h 23m
     */ 			
	public static String TimeDeltaString_JustMinutesSecs(long ms) 
	{
		long totalSecs = ms / 1000;
		long totalMins = ms / 1000 / 60;
		if(totalMins >= 60)
		{
			// If 60m+, go with hour+minute display
			long totalHours = totalMins / 60;

			return String.format("%02dh %02dm", totalHours, totalMins%60);
		}
		
		return String.format("%02dm %02ds", totalMins, totalSecs%60);
	}
	
	 /** 
     * Get a comma separated list (sorted) from a collection of strings.
     * 
     * @param arr Array of Strings
     * @return Comma separated list
     */ 			
	public static String GetCommaList(Collection<String> arr)
	{
		if((arr == null) || (arr.size() <= 0)) return "";
		return GetCommaList(arr, true);
	}

	 /** 
     * Get a comma separated list from a collection of strings.
     * 
     * @param arr Array of Strings
     * @param doSort option to sort
     * @return Comma separated list
     */ 			
	public static String GetCommaList(Collection<String> arr, boolean doSort)
	{
		ArrayList<String> list = new ArrayList<String>(arr);
		
		StringBuffer buf = new StringBuffer();
		Collections.sort(list);
		for(String str : list)
		{
			if(buf.length() > 0) buf.append(", ");
			buf.append(str);
		}
		return buf.toString();
	}
	 /** 
     * Get a comma separated list from an array of strings.
     * 
     * @param args Array of Strings
     * @return Comma separated list
     */ 			
	public static String GetCommaList(String[] args)
	{
		if(args == null) return "";
		return GetCommaList(Arrays.asList(args));
	}
	
	// -------------------------------------------------------------------


	 /** 
     * Concatenate a string array with spaces.
     * For example if 2 strings "hello" and "world" passed in, result is "hello world".
     * 
     * @param args Array of Strings
     * @param startIdx Starting Index
     * @return Concatenated list with spaces
     */ 			
	public static String ConcatArgs(String[] args, int startIdx) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIdx; i < args.length; i++) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(args[i]);
		}
		return sb.toString();
	}
	
	
	
	 /** 
     * Extracts an Integer 'argument' from a string with a space in between.
     * For example, use on "/mycommand 123" to get "123".
     * 
     * @param msg String to parse
     * @param defaultValue Default value if error obtaining
     * @return Resulting Integer
     */ 			
	public static Integer GetIntegerArgument(String msg, Integer defaultValue)
	{
		int idxSpace = msg.indexOf(" ");
		if(idxSpace >= 0)
		{
			try
			{
				return Integer.parseInt(msg.substring(idxSpace+1));
			}
			catch(Exception exc)
			{
			}
		}
		return defaultValue;
	}
	// -------------------------------------------------------------------

	
	// Create a colorful rainbow string.  I.e.  RainbowString("Hello World")
	// -------------------------------------------------------------------
	 /** 
     * Creates a rainbow colored string.
     * 
     * @param str String to convert to rainbow format
     * @return Rainbow colored string
     */ 			
	public static String RainbowString(String str) {
		return RainbowString(str, "");
	}

	 /** 
     * Creates a rainbow colored string (with options).
     * 
     * @param str String to convert to rainbow format
     * @param ctl "b" for bold, "i" for italic, "u" for underline, or combination.
     * @return Rainbow colored string
     */ 			
	public static String RainbowString(String str, String ctl) {
		if (ctl.equalsIgnoreCase("x")) return str;

		StringBuilder sb = new StringBuilder();
		int idx = 0;
		boolean useBold = ctl.indexOf('b') >= 0;
		boolean useItalics = ctl.indexOf('i') >= 0;
		boolean useUnderline = ctl.indexOf('u') >= 0;

		for (int i = 0; i < str.length(); i++) {
			if (idx % 6 == 0) sb.append(ChatColor.RED);
			else if (idx % 6 == 1) sb.append(ChatColor.GOLD);
			else if (idx % 6 == 2) sb.append(ChatColor.YELLOW);
			else if (idx % 6 == 3) sb.append(ChatColor.GREEN);
			else if (idx % 6 == 4) sb.append(ChatColor.AQUA);
			else if (idx % 6 == 5) sb.append(ChatColor.LIGHT_PURPLE);

			if (useBold) sb.append(ChatColor.BOLD);
			if (useItalics) sb.append(ChatColor.ITALIC);
			if (useUnderline) sb.append(ChatColor.UNDERLINE);

			sb.append(str.charAt(i));

			if (str.charAt(i) != ' ') idx++;

		}
		return sb.toString();
	}
	// -------------------------------------------------------------------

	// Break input string into 'tokens' (individual strings)
	 /** 
     * Convert a space-separated string into an array of strings.
     * 
     * @param msg String separated by spaces
     * @return String array of tokens in string.
     */ 			
	public static String[] GetTokens(String msg)
	{
    	return msg.split("\\s+");
	}

	
	 /** 
     * Get a padded string such that when displayed by Minecraft's default font will align to a specified length.
     * Useful for sending column output.
     * 
     * @param str String to align.
     * @param padLen Length to pad
     * @return Padded string
     */ 			
	public static String TextLabel(String str, int padLen) {
		return str + ChatColor.DARK_GRAY + TextAlignTrailerPerfect(str, padLen);
	}

	 /** 
     * Constructs padded trailer used by TextLabel() for aligning text.
     * 
     * @param str String to align.
     * @param padLen Length to pad
     * @return Padded string
     */ 			
	public static String TextAlignTrailerPerfect(String str, int padLen) {
		StringBuffer tgt = new StringBuffer();

		int pixelsTaken = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (ch == 'f') pixelsTaken += 5;
			else if (ch == 'i') pixelsTaken += 2;
			else if (ch == ',') pixelsTaken += 2;
			else if (ch == 'k') pixelsTaken += 5;
			else if (ch == 'l') pixelsTaken += 3;
			else if (ch == '\'') pixelsTaken += 3;
			else if (ch == 't') pixelsTaken += 4;
			else if (ch == 'I') pixelsTaken += 4;
			else if (ch == '[') pixelsTaken += 4;
			else if (ch == ']') pixelsTaken += 4;
			else if (ch == ' ') pixelsTaken += 4;
			else if (ch == '\u262E') pixelsTaken += 4;
			else if (ch == '\u2694') pixelsTaken += 7;

			else
				pixelsTaken += 6; // default
			
		}

		// space is 4 pixels
		int spacesPixels = (padLen * 6 - pixelsTaken);
		int left = spacesPixels % 4;

		for (int i = 0; i < left; i++)
			tgt.append("\u205A");
		for (int i = 0; i < spacesPixels / 4; i++)
			tgt.append(" ");

		return tgt.toString();
	}
	

	 /** 
     * Calculate yaw angle needed for source location to face a target location.
     * 
     * @param src Source Location
     * @param dest Destination Location
     * @return Yaw angle for Source to Face Destination
     */ 			
	public static float YawToFaceLocation(MC_Location src, MC_Location dest)
	{
		double dz = -(dest.x - src.x);
		double dx = (dest.z - src.z);
		// Zero handler
		if(Math.abs(dx) < 0.0001) dx = 0.0001;
		
		double yawSrcToTarget = 360*Math.atan(dz / dx)/(2*3.1415926535);
		if(dx < 0) yawSrcToTarget += 180.0;
		if(yawSrcToTarget < 0) yawSrcToTarget += 360.0;
		return (float)yawSrcToTarget;
	}

	 /** 
     * Calculate pitch angle needed for source location to face a target location.
     * 
     * @param src Source Location
     * @param dest Destination Location
     * @return Pitch angle for Source to Face Destination
     */ 			
	public static float PitchToFaceLocation(MC_Location src, MC_Location dest)
	{
		double dz = -(dest.x - src.x);
		double dx = (dest.z - src.z);
		double dy = -(dest.y - src.y);
		double d = Math.sqrt(dz*dz + dx*dx);
		// Zero handler
		if(Math.abs(d) < 0.0001) d = 0.0001;
		
		double pitchToTarget = 360*Math.atan(dy / d)/(2*3.1415926535);
		return (float)pitchToTarget;
	}

	 /** 
     * Create a rainbow colored, comma separated list from an array of strings.
     * 
     * @param strings String array
     * @return Rainbow colored, comma separated list.
     */ 			
	  public static String RainbowStringList(String[] strings)
	  {
		  return RainbowStringList(new ArrayList(Arrays.asList(strings)));
	  }

    /** 
     * Create a rainbow colored, comma separated list from an list of strings.
     * 
     * @param strings String array
     * @return Rainbow colored, comma separated list.
     */ 			
	  public static String RainbowStringList(List<String> strings)
	  {
		  String res = ChatColor.RED + "None";
		  if(strings.size() > 0) 
		  {
			  String nextColor = ChatColor.YELLOW;
			  StringBuilder sb = new StringBuilder();
			  for (String iterator : strings) {
				  if(sb.length() > 0) sb.append(ChatColor.WHITE + ", ");
				  sb.append(nextColor + iterator);
				  if(nextColor == ChatColor.GOLD) nextColor = ChatColor.YELLOW;
				  else if(nextColor == ChatColor.YELLOW) nextColor = ChatColor.GREEN;
				  else if(nextColor == ChatColor.GREEN) nextColor = ChatColor.AQUA;
				  else if(nextColor == ChatColor.AQUA) nextColor = ChatColor.LIGHT_PURPLE;
				  else if(nextColor == ChatColor.LIGHT_PURPLE) nextColor = ChatColor.RED;
				  else if(nextColor == ChatColor.RED) nextColor = ChatColor.GOLD;
			  }
			  res = sb.toString(); 
		  }
		  return res;
	  }
	
	
}
