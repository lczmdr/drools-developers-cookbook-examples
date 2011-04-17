package drools.cookbook.chapter07.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import drools.cookbook.chapter07.model.Server;

public class ServerConverter implements Converter {

    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        Server s = (Server) object;
        writer.addAttribute("id", s.getName());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Server p = new Server();
        p.setName(reader.getAttribute("id"));
        return p;
    }

    public boolean canConvert(Class clazz) {
        return clazz.equals(Server.class);
    }
}