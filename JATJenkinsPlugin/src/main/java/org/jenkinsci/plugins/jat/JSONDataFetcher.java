package org.jenkinsci.plugins.jat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * The role of the JSONDataFetcher is to fetch Jenkins build data
 * and wrap it in a JSONObject representation. The object is configured
 * to fetch data from the build and the test report associated with the
 * build.
 */
public class JSONDataFetcher implements IDataFetcher {

    private String buildReportAPI;
    private String testReportAPI;
    private final String path = System.getProperty("user.dir") + File.separator + "work"
            + File.separator + "persistedBuildData"; /* Path used for fetching JSONbject data */
    private final String TEST_REPORT = "testReport"; /* Used to find key 'testReport' in JSONObject */
    private final int BYTES_TO_READ = 1024; /* Defaults bytes to read from URL to 1024 */

    /**
     * @param jenkinsBuildURLMapping Absolute URL pointing to a Jenkins build.
     * @param listener BuildListener used for logging purposes.
     */
    public JSONDataFetcher(String jenkinsBuildURLMapping) {

        this.buildReportAPI = jenkinsBuildURLMapping + "api/json?pretty=true";
        this.testReportAPI = jenkinsBuildURLMapping + "testReport/api/json?pretty=true";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object fetch() throws Exception {

        JSONObject json = null;
        json = fetchDataFromURL(this.buildReportAPI);

        if(json.containsKey(TEST_REPORT)) {

            /* Only fetch test report if it exists */
            JSONObject jsonTestData = null;
            jsonTestData = fetchDataFromURL(this.testReportAPI);
            json.put("TestExecutionData", jsonTestData);

        }
        return json;
    }


    @Override
    public  ArrayList<IPersistedDataObject> recover() throws Exception {

        ArrayList<IPersistedDataObject> retrievedJSONBuildData = null;
        if(new File(path).exists()) {

            File directory = new File(path);
            File[] directoryListing = directory.listFiles();
            JSONParser parser = new JSONParser();
            retrievedJSONBuildData = new ArrayList<IPersistedDataObject>();

            if (directoryListing != null) {
                for (File persistedBuildDataInstance : directoryListing) {
                    FileReader fileReader = null;
                    Object object = null;
                    fileReader = new FileReader(persistedBuildDataInstance.toString());
                    object = parser.parse(fileReader);
                    JSONObject obj = (JSONObject) object;
                    PersistedJSONObject  persistedJSONObject =
                            new PersistedJSONObject(obj, persistedBuildDataInstance);
                    retrievedJSONBuildData.add(persistedJSONObject);
                    fileReader.close();
                }
            }
        }
        return retrievedJSONBuildData;
    }

    /**
     * Fetches some arbitrary data and constructs a JSONObject of the data.
     * @param URL Absolute URL pointing to some data set.
     * @return The JSONObject representation of the data pointed to by URL.
     * @throws IOException
     */
    private JSONObject fetchDataFromURL(String URL) throws Exception {

        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        URL parsedURL = new URL(URL);
        InputStreamReader inputStream = new InputStreamReader(parsedURL.openStream());
        reader = new BufferedReader(inputStream);
        int read;
        char[] chars = new char[BYTES_TO_READ];

        try {
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if(reader != null) {
                reader.close();
            }
        }

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        json = (JSONObject) parser.parse(buffer.toString());

        return json;
    }
}
