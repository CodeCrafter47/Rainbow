package org.projectrainbow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Check if Rainbow is outdated. 
 * (Sorry for messy code, had to do fix for old Java versions)
 * 
 * @author Isaiah Patton
 */
public class Updater {
    public static String getLatestVersion() {
        try {
            return latestVersion();
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return "ERROR";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "ERROR";
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    
    public static String latestVersion() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        /**
         * Fix error with HTTPS on Java 7 and lower. (You no like? then Update Java)
         */
        TrustManager[] trustAllCerts = new TrustManager[] {
           new X509TrustManager() {
              public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
              }

              public void checkClientTrusted(X509Certificate[] certs, String authType) {/**/}
              public void checkServerTrusted(X509Certificate[] certs, String authType) {/**/}

           }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
              return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /*
         * end of the fix
         */

        URL url = new URL("https://ci.codecrafter47.de/job/Rainbow/lastStableBuild/buildNumber"); // Do latest stable build.
        URLConnection urlc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        String latestStableVer = in.readLine();
        in.close();
        return latestStableVer;
    }
}