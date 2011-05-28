package drools.cookbook.chapter04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.rule.Rule;
import org.drools.guvnor.api.GuvnorRestApi;
import org.drools.guvnor.jaxb.Asset;
import org.drools.io.impl.InputStreamResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class GuvnorRestApiTest {

    @Test
    public void getAssetFromPackage() throws Exception {
        URL url = new URL(
                "http://localhost:8080/guvnor-webapp-5.2.0-SNAPSHOT/rest/packages/drools.cookbook/assets/testing");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", MediaType.APPLICATION_XML);
        connection.connect();
        Assert.assertEquals(200, connection.getResponseCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, connection.getContentType());
        Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JAXBContext context = JAXBContext.newInstance(Asset.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Asset asset = (Asset) unmarshaller.unmarshal(reader);
        Assert.assertNotNull(asset);
    }

    @Test
    public void getPackageSource() throws Exception {
        URL url = new URL("http://localhost:8080/guvnor-webapp-5.2.0-SNAPSHOT/rest/packages/drools.cookbook/source");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", MediaType.TEXT_PLAIN);
        connection.connect();
        Assert.assertEquals(200, connection.getResponseCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, connection.getContentType());
        String source = readAsString(connection.getInputStream());
        Assert.assertNotNull(source);
        Assert.assertTrue(source.length() > 0);
    }

    @Test
    public void getPackageBinary() throws Exception {

        GuvnorRestApi guvnorRestApi = new GuvnorRestApi("http://localhost:8080/guvnor-webapp-5.2.0-SNAPSHOT");
        InputStream binaryPackage = guvnorRestApi.getBinaryPackage("defaultPackage");

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new InputStreamResource(binaryPackage), ResourceType.PKG);
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        for (KnowledgePackage kpackage : kbase.getKnowledgePackages()) {
            System.out.println(kpackage.getName());
            for (Rule rule : kpackage.getRules()) {
                System.out.println("\t" + rule.getName());
            }
        }
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        Assert.assertNotNull(ksession);
    }

    @Test
    // @Ignore
    public void updateAssetFromPackage() throws Exception {
        URL url = new URL(
                "http://localhost:8080/guvnor-webapp-5.2.0-SNAPSHOT/rest/packages/drools.cookbook/assets/testing");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", MediaType.APPLICATION_XML);
        connection.connect();
        Assert.assertEquals(200, connection.getResponseCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, connection.getContentType());
        Reader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JAXBContext context = JAXBContext.newInstance(Asset.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Asset asset = (Asset) unmarshaller.unmarshal(reader);
        Assert.assertNotNull(asset);
        String ruleContent = asset.getMetadata().getNote();
        ruleContent = ruleContent.replaceAll("100", "200");
        System.out.println(ruleContent);
        connection.disconnect();

        connection = (HttpURLConnection) url.openConnection();
        Marshaller ma = context.createMarshaller();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", MediaType.APPLICATION_XML);
        connection.setRequestProperty("Content-Length", Integer.toString(asset.toString().getBytes().length));
        String userpassword = "user" + ":" + "password";
        byte[] authEncBytes = Base64.encodeBase64(userpassword.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + new String(authEncBytes));

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        ma.marshal(asset, connection.getOutputStream());
        Assert.assertEquals(200, connection.getResponseCode());
        connection.disconnect();
    }

    @Test
    public void topologyDefinition() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpHost targetHost = new HttpHost("127.0.0.1", 8080, "http");
        HttpGet httpget = new HttpGet("/guvnor-webapp-5.2.0-SNAPSHOT/rest/packages/drools.cookbook/assets/testing");

        String userPassword = "guest" + ":" + "guest";
        byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
        httpget.addHeader("Authorization", "BASIC " + new String(encodeBase64));
        httpget.addHeader("Accept", MediaType.APPLICATION_XML);

        HttpResponse response = httpclient.execute(targetHost, httpget);
        HttpEntity entity = response.getEntity();

        System.out.println(entity.getContentType().getValue());

        Reader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        JAXBContext context = JAXBContext.newInstance(Asset.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Asset asset = (Asset) unmarshaller.unmarshal(reader);
        Assert.assertNotNull(asset);
        String ruleContent = asset.getMetadata().getNote();
        ruleContent = ruleContent.replaceAll("100", "300");
        System.out.println(ruleContent);

        HttpPut httpput = new HttpPut("/guvnor-webapp-5.2.0-SNAPSHOT/rest/packages/drools.cookbook/assets/testing");
        httpput.setEntity(new ByteArrayEntity(ruleContent.getBytes()));
        System.out.println(httpput.getEntity());
        response = httpclient.execute(targetHost, httpput);

        // Marshaller ma = context.createMarshaller();
        // ma.marshal(asset, httpPut.getEntity().ge)

        System.out.println("--> " + response.getStatusLine().getStatusCode());

        httpclient.getConnectionManager().shutdown();

    }

    private String readAsString(InputStream is) throws IOException {
        StringBuffer ret = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            ret.append(line + "\n");
        }
        return ret.toString();
    }
}
