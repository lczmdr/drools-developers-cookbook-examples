package drools.cookbook.chapter01.helper;

import java.io.InputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import drools.cookbook.chapter01.Server;

/**
 * 
 * @author Lucas Amador
 *
 */
public class ServerConfigurationReader {

    private static List<Server> servers;

    @SuppressWarnings("unchecked")
    public static Server loadServerConfiguration(String serverName) {
        if (servers==null) {
            InputStream resourceAsStream = ServerConfigurationReader.class.getResourceAsStream("/drools/cookbook/chapter01/servers.xml");
            XStream xstream = new XStream();
            xstream.alias("server", Server.class);
            xstream.alias("servers", List.class);
            servers = (List<Server>) xstream.fromXML(resourceAsStream);
        }
        for (Server server : servers) {
            if (server.getName().equalsIgnoreCase(serverName)) {
                return server;
            }
        }
        return null;
    }

}
