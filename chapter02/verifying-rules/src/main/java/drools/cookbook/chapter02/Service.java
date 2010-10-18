package drools.cookbook.chapter02;

/**
 * 
 * @author Lucas Amador
 *
 */
public class Service {

    private String serviceName;
    private ServiceType type;

    public Service(String serviceName, ServiceType type) {
        this.serviceName = serviceName;
        this.type = type;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    } 

    public void setType(ServiceType type) {
        this.type = type;
    }

    public ServiceType getType() {
        return type;
    }

}
