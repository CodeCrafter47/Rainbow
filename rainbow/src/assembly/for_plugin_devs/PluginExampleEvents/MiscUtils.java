package PluginExampleEvents;

import PluginReference.*;


public class MiscUtils
{
	public static String FullTranslate(String parm)
	{
		if(parm == null) return "";
		parm = SpecialTranslate(parm);
		parm = TranslateColorString(parm,  true);
		return parm;
	}
	
	public static String TranslateColorString(String parm, boolean IsOp) {
		if(parm == null) return "";
		return TranslateColorString(parm, IsOp, false);

	}

	public static String TranslateColorString(String parm, boolean IsOp, boolean fAllowSpaces) {
		if(parm == null) return "";
		StringBuilder res = new StringBuilder();
		boolean pending = false;
		for (int i = 0; i < parm.length(); i++) {
			char ch = parm.charAt(i);
			if (ch == '&') pending = true;
			else {
				if (pending == true) {
					pending = false;
					if ((ch == '0') && IsOp) res.append(ChatColor.BLACK + "");
					else if (ch == '1') res.append(ChatColor.DARK_BLUE + "");
					else if (ch == '2') res.append(ChatColor.DARK_GREEN + "");
					else if (ch == '3') res.append(ChatColor.DARK_AQUA + "");
					else if ((ch == '4') && IsOp) res.append(ChatColor.DARK_RED + "");
					else if (ch == '5') res.append(ChatColor.DARK_PURPLE + "");
					else if (ch == '6') res.append(ChatColor.GOLD + "");
					else if (ch == '7') res.append(ChatColor.GRAY + "");
					else if (ch == '8') res.append(ChatColor.DARK_GRAY + "");
					else if (ch == '9') res.append(ChatColor.BLUE + "");
					else if (ch == 'a') res.append(ChatColor.GREEN + "");
					else if (ch == 'b') res.append(ChatColor.AQUA + "");
					else if ((ch == 'c') && IsOp) res.append(ChatColor.RED + "");
					else if (ch == 'd') res.append(ChatColor.LIGHT_PURPLE + "");
					else if (ch == 'e') res.append(ChatColor.YELLOW + "");
					else if (ch == 'f') res.append(ChatColor.WHITE + "");
					else if (ch == 'l') res.append(ChatColor.BOLD + "");
					else if (ch == 'm') res.append(ChatColor.STRIKETHROUGH + "");
					else if ((ch == 'k') && IsOp) res.append(ChatColor.MAGIC + "");
					else if ((ch == 'n') && IsOp) res.append(ChatColor.UNDERLINE + "");
					else if (ch == 'o') res.append(ChatColor.ITALIC + "");
				}
				else {
					if (IsOp) {
						res.append(ch);
					}
					else {
						if (IsCharLetterOrDigit(ch)) res.append(ch);
						else if (fAllowSpaces && (ch == ' ')) res.append(ch);
					}
				}
			}
		}
		return res.toString();

	}

	public static boolean IsCharLetterOrDigit(char ch) {
		// check if ch is a letter
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) return true;
		// check if ch is a digit
		if (ch >= '0' && ch <= '9') return true;
		return false;

	}

	public static String StringReplace(String src, String key, String val) {
		int idx = src.indexOf(key);
		if (idx < 0) return src;
		return src.substring(0, idx) + val + src.substring(idx + key.length());
	}

	public static String SpecialTranslate(String txt) {
		String res = txt;
		while (res.indexOf("{star1}") >= 0) res = StringReplace(res, "{star1}", "\u269D");
		while (res.indexOf("{star2}") >= 0) res = StringReplace(res, "{star2}", "\u2605");
		while (res.indexOf("{star3}") >= 0) res = StringReplace(res, "{star3}", "\u2606");
		while (res.indexOf("{space}") >= 0) res = StringReplace(res, "{space}", " ");
		while (res.indexOf("{_}") >= 0) res = StringReplace(res, "{_}", " ");

		while (res.indexOf("{heart1}") >= 0) res = StringReplace(res, "{heart1}", "\u2764");
		while (res.indexOf("{heart2}") >= 0) res = StringReplace(res, "{heart2}", "\u2661");
		while (res.indexOf("{heart3}") >= 0) res = StringReplace(res, "{heart3}", "\u2665");
		while (res.indexOf("{cross1}") >= 0) res = StringReplace(res, "{cross1}", "\u271E");
		while (res.indexOf("{cross2}") >= 0) res = StringReplace(res, "{cross2}", "\u2671");
		while (res.indexOf("{cross3}") >= 0) res = StringReplace(res, "{cross3}", "\u2670");
		while (res.indexOf("{diamond1}") >= 0) res = StringReplace(res, "{diamond1}", "\u2666");
		while (res.indexOf("{diamond2}") >= 0) res = StringReplace(res, "{diamond2}", "\u2662");
		while (res.indexOf("{radio}") >= 0) res = StringReplace(res, "{radio}", "\u2622");
		while (res.indexOf("{bio}") >= 0) res = StringReplace(res, "{bio}", "\u2623");
		while (res.indexOf("{ankh}") >= 0) res = StringReplace(res, "{ankh}", "\u2625");
		while (res.indexOf("{peace}") >= 0) res = StringReplace(res, "{peace}", "\u262E");
		while (res.indexOf("{yinyang}") >= 0) res = StringReplace(res, "{yinyang}", "\u262F");
		while (res.indexOf("{male}") >= 0) res = StringReplace(res, "{male}", "\u2642");
		while (res.indexOf("{female}") >= 0) res = StringReplace(res, "{female}", "\u2640");
		while (res.indexOf("{aquarius}") >= 0) res = StringReplace(res, "{aquarius}", "\u2652");
		while (res.indexOf("{music1}") >= 0) res = StringReplace(res, "{music1}", "\u2669");
		while (res.indexOf("{music2}") >= 0) res = StringReplace(res, "{music2}", "\u266A");
		while (res.indexOf("{music3}") >= 0) res = StringReplace(res, "{music3}", "\u266B");
		while (res.indexOf("{music4}") >= 0) res = StringReplace(res, "{music4}", "\u266C");
		while (res.indexOf("{music5}") >= 0) res = StringReplace(res, "{music5}", "\u266D");
		while (res.indexOf("{anchor}") >= 0) res = StringReplace(res, "{anchor}", "\u2693");
		while (res.indexOf("{atom}") >= 0) res = StringReplace(res, "{atom}", "\u269B");
		while (res.indexOf("{bolt}") >= 0) res = StringReplace(res, "{bolt}", "\u26A1");
		while (res.indexOf("{plane}") >= 0) res = StringReplace(res, "{plane}", "\u2708");
		while (res.indexOf("{flower1}") >= 0) res = StringReplace(res, "{flower1}", "\u2740");
		while (res.indexOf("{flower2}") >= 0) res = StringReplace(res, "{flower2}", "\u2743");
		while (res.indexOf("{flower3}") >= 0) res = StringReplace(res, "{flower3}", "\u273C");
		while (res.indexOf("{newline}") >= 0) res = StringReplace(res, "{newline}", "\n");

		return res;

	}

}
