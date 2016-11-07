package PluginReference;

/** 
 * Class containing color codes and related functions.
 */ 			
public class ChatColor {
	public static String ColorPrefix   = "\u00A7";
        public static char   ColorChar     = '\u00A7';
	public static String BLACK	   = ColorPrefix + "0";
	public static String DARK_BLUE 	   = ColorPrefix + "1";
	public static String DARK_GREEN    = ColorPrefix + "2";
	public static String DARK_AQUA 	   = ColorPrefix + "3";
	public static String DARK_RED      = ColorPrefix + "4";
	public static String DARK_PURPLE   = ColorPrefix + "5";
	public static String GOLD          = ColorPrefix + "6";
	public static String GRAY          = ColorPrefix + "7";
	public static String DARK_GRAY     = ColorPrefix + "8";
	public static String BLUE          = ColorPrefix + "9";
	public static String GREEN         = ColorPrefix + "a";
	public static String AQUA          = ColorPrefix + "b";
	public static String RED           = ColorPrefix + "c";
	public static String LIGHT_PURPLE  = ColorPrefix + "d";
	public static String YELLOW        = ColorPrefix + "e";
	public static String WHITE         = ColorPrefix + "f";
	public static String MAGIC         = ColorPrefix + "k";
	public static String BOLD          = ColorPrefix + "l";
	public static String STRIKETHROUGH = ColorPrefix + "m";
	public static String UNDERLINE     = ColorPrefix + "n";
	public static String ITALIC        = ColorPrefix + "o";
	public static String RESET         = ColorPrefix + "r";
        
	
	
	public static String StripColor(String str) {
		if(str == null) return "";
		
		StringBuilder sb = new StringBuilder();
		
		char colorChar = ColorPrefix.charAt(0);
		boolean colorMode = false;
		for(int i=0; i<str.length(); i++) {
			char ch = str.charAt(i);
			if(ch == colorChar){
				colorMode = true;
				continue;
			} if(colorMode) {
				colorMode = false;
				continue;
			}
			if(ch == '\u205A') continue; // skip special character used by TextLabel()
			
			sb.append(ch);
		}
		return sb.toString();		
	}
    
    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] a = textToTranslate.toCharArray();
        for (int b = 0; b < a.length - 1; b++) {
            if (a[b] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(a[b+1]) > -1) {
                a[b] = ColorChar;
                a[b+1] = Character.toLowerCase(a[b+1]);
            }
        }
        return String.valueOf(a);
    }
}
