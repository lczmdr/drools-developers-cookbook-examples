package drools.cookbook.chapter02;

/**
 * 
 * @author Lucas Amador
 *
 */
public class ServiceRequest {

    private String serverName;
    private String serviceName;
    private ServiceType serviceType;

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    public ServiceType getServiceType() {
        return serviceType;
    }
    
    @Override
    public String toString() {
        return "ServiceRequest[serverName=" + serverName + " serviceName=" + serviceName + " serviceType=" + serviceType + "]";
    }

}
