package org.jenkinsci.plugins.jat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.json.simple.JSONObject;

/**
 * The role of the JSONDataDispatcher is to wrap JSONData
 * and dispatch it to the JAT web application back end. The object
 * dispatches the jenkins build and test data to the JAT web
 * application back end, pointed to by the backendURLMapping.
 * If the data canno't be sent to the JAT web application back end
 * the persistance service is invoked and data is persisted locally.
 */
public class JSONDataDispatcher implements IDataDispatcher {

    private JSONObject jenkinsBuildData;
    private String backendURLMapping;

    /* File path for persisted data /work/persistedBuildData/ */
    private final String path = System.getProperty("user.dir")
                                + File.separator + "work"
                                + File.separator + "persistedBuildData";

    /**
     * @param obj The JSONObject containing the Jenkins data.
     * @param backendURLMapping Absolute URL pointing to the JAT web application back end.
     * @param shouldPersist boolean flag indicating if the obj should be persisted
     * locally should the HTTP forward to the JAT back end fail.
     * @param listener BuildListener used for logging purposes.
     */
    public JSONDataDispatcher(JSONObject jenkinsBuildData,
                              String backendURLMapping) {

        this.jenkinsBuildData = jenkinsBuildData;
        this.backendURLMapping = backendURLMapping;

    }

    @Override
    public void dispatch() throws DispatchException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.backendURLMapping);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(this.jenkinsBuildData.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {

                throw new DispatchException("JSONDataDispatcher chouldn't "
                        + " dispatch data to JAT web application due to "
                        + "invalid HTTP URL Connaction: " + conn.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            throw new DispatchException("JSONDataDispatcher chouldn't "
                    + " dispatch data to JAT web application due to "
                    + "invalid HTTP URL Connaction");

        } catch (IOException e) {
            throw new DispatchException("JSONDataDispatcher chouldn't "
                    + " dispatch data to JAT web application due to "
                    + "IOException");
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    public void persist() throws PersistException {

        JSONObject obj = this.jenkinsBuildData;
        if(obj != null) {
            if(!(new File(path).exists())) {

                File persistedBuildDataDirectory  = new File(path);
                try {
                    persistedBuildDataDirectory.mkdir();
                } catch (SecurityException e) {
                    throw new PersistException("Directory for persisted data couldn't "
                                + " be created due to exception: " + e.getMessage());
                }
            }

            String fileName = UUID.randomUUID().toString().replace("-", "") + ".txt";
            File filePersistedBuildData = new File(path, fileName);
            try {
                filePersistedBuildData.createNewFile();
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new FileWriter(filePersistedBuildData, true)));
                out.println(obj.toString());
                out.close();

            } catch (IOException e) {
                throw new PersistException("Build data couldn't be persisted! "
                                            + e.getMessage());
            }
        } else {
            throw new PersistException("Jenkins build was not persisted due to "
                    + "data object beeing null");
        }
    }
}
