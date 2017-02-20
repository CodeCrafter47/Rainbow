package org.projectrainbow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;
import java.lang.NumberFormatException;

import org.projectrainbow.launch.Bootstrap;

/**
 * Check if Rainbow is outdated.
 * 
 * @author Isaiah Patton
 */
public class Updater {
    public static String checkForUpdate() {
        String latest = getLatestVersion();
        if (!latest.startsWith("ERROR")) {
            try {
                int behind = (Integer.valueOf(latest) - Integer.valueOf(Bootstrap.buildNumber));
                if (behind == 0) {
                    return "You are running the latest version.";
                }
                return "You are running " + behind + " versions behind.";
            } catch (NumberFormatException e) {
                return "Unknown version, custom build?";
            }
        } else {
            return latest;
        }
    }

    public static String getLatestVersion() {
        try {
            return RawlatestVersion();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    public static String RawlatestVersion() throws IOException {
        URL url = new URL("https://ci.codecrafter47.de/job/Rainbow/lastStableBuild/buildNumber"); // Do latest stable build.
        URLConnection urlc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        String latestStableVer = in.readLine();
        in.close();
        return latestStableVer;
    }
}
