package org.projectrainbow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Check if Rainbow is outdated.
 * 
 * @author Isaiah Patton
 */
public class Updater {

    private String branch;
    private String repo;
    private boolean ignoreDirty;

    public Updater(String branch, String repo, boolean ignoreDirty) {
        this.branch = branch;
        this.repo = repo;
        this.ignoreDirty = ignoreDirty;
    }

    public String checkForUpdate() {
        int behind = getBehind();

        if (behind == 0)
            return "You are running the latest version.";

        if (behind == -2)
            return "HTTP 404 Error. Check your internet connection.";

        if (behind < 0)
            return "Unknown version, custom build?";

        return "You are running " + behind + " versions behind.";
    }

    public int getBehind() {
        Attributes a = getManifest(Updater.class).getMainAttributes();
        String hash = a.getValue("GitCommitHash");
        if (hash.endsWith("-dirty")) {
            if (ignoreDirty) hash = hash.replace("-dirty", "");
            else return -4;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    "https://api.github.com/repos/" + repo + "/compare/" + branch + "..." + hash).openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) 
                return -2;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            JsonObject obj = (JsonObject) new JsonParser().parse(reader);
            String status = obj.get("status").getAsString();

            if (status.equalsIgnoreCase("identical"))
                return 0;

            if (status.equalsIgnoreCase("behind"))
                return obj.get("behind_by").getAsInt();

            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }
    }

    private Manifest getManifest(Class<?> clz) {
        String resource = "/" + clz.getName().replace(".", "/") + ".class";
        String fullPath = clz.getResource(resource).toString();
        String archivePath = fullPath.substring(0, fullPath.length() - resource.length());

        try {
            return new Manifest( new URL(archivePath + "/META-INF/MANIFEST.MF").openStream() );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
