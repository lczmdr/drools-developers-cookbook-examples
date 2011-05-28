package org.drools.guvnor.api;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;

public class GuvnorRestApi {

    private String guvnorURI;

    public GuvnorRestApi(String guvnorURI) {
        this.guvnorURI = guvnorURI;
    }

    public InputStream getBinaryPackage(String packageName) throws Exception {
        URL url = new URL(guvnorURI + "/rest/packages/" + packageName + "/binary");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", MediaType.APPLICATION_OCTET_STREAM);
        connection.connect();
        if (connection.getResponseCode() != 200) {
            throw new Exception("Bad response code: " + connection.getResponseCode());
        }
        if (!connection.getContentType().equalsIgnoreCase(MediaType.APPLICATION_OCTET_STREAM)) {
            throw new Exception("Bad response content type: " + connection.getContentType());
        }
        return connection.getInputStream();
    }

    public String getGuvnorURI() {
        return guvnorURI;
    }

    public void setGuvnorURI(String guvnorURI) {
        this.guvnorURI = guvnorURI;
    }

}
