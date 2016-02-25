package org.projectrainbow;

public class _ColorHelper {
    public static char COLOR_CHAR;
    public static String ColorPrefix;
    public static String BLACK;
    public static String DARK_BLUE;
    public static String DARK_GREEN;
    public static String DARK_AQUA;
    public static String DARK_RED;
    public static String DARK_PURPLE;
    public static String GOLD;
    public static String GRAY;
    public static String DARK_GRAY;
    public static String BLUE;
    public static String GREEN;
    public static String AQUA;
    public static String RED;
    public static String LIGHT_PURPLE;
    public static String YELLOW;
    public static String WHITE;
    public static String MAGIC;
    public static String BOLD;
    public static String STRIKETHROUGH;
    public static String UNDERLINE;
    public static String ITALIC;
    public static String RESET;

    static {
        _ColorHelper.COLOR_CHAR = '§';
        _ColorHelper.ColorPrefix = String.valueOf(_ColorHelper.COLOR_CHAR);
        _ColorHelper.BLACK = String.valueOf(_ColorHelper.ColorPrefix) + "0";
        _ColorHelper.DARK_BLUE = String.valueOf(_ColorHelper.ColorPrefix) + "1";
        _ColorHelper.DARK_GREEN = String.valueOf(_ColorHelper.ColorPrefix) + "2";
        _ColorHelper.DARK_AQUA = String.valueOf(_ColorHelper.ColorPrefix) + "3";
        _ColorHelper.DARK_RED = String.valueOf(_ColorHelper.ColorPrefix) + "4";
        _ColorHelper.DARK_PURPLE = String.valueOf(_ColorHelper.ColorPrefix) + "5";
        _ColorHelper.GOLD = String.valueOf(_ColorHelper.ColorPrefix) + "6";
        _ColorHelper.GRAY = String.valueOf(_ColorHelper.ColorPrefix) + "7";
        _ColorHelper.DARK_GRAY = String.valueOf(_ColorHelper.ColorPrefix) + "8";
        _ColorHelper.BLUE = String.valueOf(_ColorHelper.ColorPrefix) + "9";
        _ColorHelper.GREEN = String.valueOf(_ColorHelper.ColorPrefix) + "a";
        _ColorHelper.AQUA = String.valueOf(_ColorHelper.ColorPrefix) + "b";
        _ColorHelper.RED = String.valueOf(_ColorHelper.ColorPrefix) + "c";
        _ColorHelper.LIGHT_PURPLE = String.valueOf(_ColorHelper.ColorPrefix) + "d";
        _ColorHelper.YELLOW = String.valueOf(_ColorHelper.ColorPrefix) + "e";
        _ColorHelper.WHITE = String.valueOf(_ColorHelper.ColorPrefix) + "f";
        _ColorHelper.MAGIC = String.valueOf(_ColorHelper.ColorPrefix) + "k";
        _ColorHelper.BOLD = String.valueOf(_ColorHelper.ColorPrefix) + "l";
        _ColorHelper.STRIKETHROUGH = String.valueOf(_ColorHelper.ColorPrefix) + "m";
        _ColorHelper.UNDERLINE = String.valueOf(_ColorHelper.ColorPrefix) + "n";
        _ColorHelper.ITALIC = String.valueOf(_ColorHelper.ColorPrefix) + "o";
        _ColorHelper.RESET = String.valueOf(_ColorHelper.ColorPrefix) + "r";
    }

    public static String stripColor(final String str) {
        if (str == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final char colorChar = _ColorHelper.ColorPrefix.charAt(0);
        boolean colorMode = false;
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            if (ch == colorChar) {
                colorMode = true;
            }
            else if (colorMode) {
                colorMode = false;
            }
            else if (ch != '\u205a') {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
