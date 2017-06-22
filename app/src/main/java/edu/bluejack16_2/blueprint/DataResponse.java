package edu.bluejack16_2.blueprint;

import org.json.JSONObject;

/**
 * Created by BAGAS on 22-Jun-17.
 */

public interface DataResponse {

    void processFinish(JSONObject obj);
    void processRunning();
}
