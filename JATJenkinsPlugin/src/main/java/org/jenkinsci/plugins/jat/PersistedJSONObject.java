package org.jenkinsci.plugins.jat;

import java.io.File;

import org.json.simple.JSONObject;

public class PersistedJSONObject implements IPersistedDataObject {

    private JSONObject json;
    private File fileName;

    public PersistedJSONObject(JSONObject json, File fileName) {
        this.json = json;
        this.fileName = fileName;
    }

    @Override
    public void remove() throws Exception {
        if(this.fileName != null) {
            if(this.fileName.delete()) {
                /* Deletion successful */
                return;
            } else {
                throw new Exception("Failed to remove persisted build located in file " +
                        this.fileName.toString());
            }
        }
    }

    @Override
    public Object getObject() {
        return this.json;
    }

}
