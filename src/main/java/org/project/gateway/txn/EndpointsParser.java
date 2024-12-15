package org.project.gateway.txn;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EndpointsParser {

    private static Map apiEndpoints = new HashMap<>();

    private EndpointsParser() {

    }


    public static synchronized void populateApiEndpoints(String filePath) throws IOException {
        // Create an ObjectMapper for JSON parsing
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file into the map if it's empty
        if (apiEndpoints.isEmpty()) {
            apiEndpoints = objectMapper.readValue(new File(filePath), HashMap.class);
        }
    }

    public static String getApiEndpoints(String reqName)  {
        if (apiEndpoints.isEmpty()) {
            try {
                populateApiEndpoints("api.json");
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return (String) apiEndpoints.get(reqName);
    }

    /**
     * Clears the cached API endpoints map. Useful for reloading data from disk.
     */
    public static synchronized void clearCache() {
        apiEndpoints.clear();
    }

}
