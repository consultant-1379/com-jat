package org.jenkinsci.plugins.jat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The URLValidator is used in the JAT
 * to determine if a URL is valid
 */
public class URLValidator {

    private String URL;
    private final int LOWER_HTTP_OK = 200;
    private final int UPPER_HTTP_OK = 400;

    public URLValidator(String URL) {
        this.URL = URL;
    }

    /**
     * Determines if the URL associated with the URLValidator
     * Object is a some reachable and valid URL.
     * @return true if the this.URL is a valid URL, else false.
     */
    public boolean isValidURL() {

        URL url;
        HttpURLConnection httpURLConnection = null;
        int responseCode = -1;
        try {
            url = new URL(this.URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            responseCode = httpURLConnection.getResponseCode();

            return (responseCode >= LOWER_HTTP_OK && responseCode <= UPPER_HTTP_OK);
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
