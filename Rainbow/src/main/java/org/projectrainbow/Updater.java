package org.projectrainbow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLHandshakeException;

/**
 * Check if Rainbow is outdated.
 * 
 * @author Isaiah Patton
 */
public class Updater {
    public static String getLatestVersion() {
        try {
            return RawlatestVersion();
        } catch (SSLHandshakeException e) {
            return "ERROR: Update Java";
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: java.io.IOException";
        }
    }

    public static String RawlatestVersion() throws SSLHandshakeException, IOException {
        URL url = new URL("https://ci.codecrafter47.de/job/Rainbow/lastStableBuild/buildNumber"); // Do latest stable build.
        URLConnection urlc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        String latestStableVer = in.readLine();
        in.close();
        return latestStableVer;
    }
}
