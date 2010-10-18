package drools.cookbook.chapter02;

import java.util.Random;

public class ServiceRequestFactory {

    private static final String[] serverNames = {"debianServer", "ubuntuServer"};
    private static final String[] ftpServicesNames = new String[]{"ftp.server01.com","ftp.server02.com"};
    private static final String[] httpServicesNames = new String[]{"http.server01.com"};
    private static Random rnd = new Random();

    public static ServiceRequest create() {
        ServiceRequest serviceRequest = new ServiceRequest();
        int serverNameIndex = rnd.nextInt(serverNames.length);
        serviceRequest.setServerName(serverNames[serverNameIndex]);
        int serviceType = rnd.nextInt(2);
        if (serviceType==0) {
            serviceRequest.setServiceType(ServiceType.FTP);
            int ftpServicesIndex = rnd.nextInt(ftpServicesNames.length);
            serviceRequest.setServiceName(ftpServicesNames[ftpServicesIndex]);
        }
        else if (serviceType==1) {
            serviceRequest.setServiceType(ServiceType.HTTP);
            int httpServicesIndex = rnd.nextInt(httpServicesNames.length);
            serviceRequest.setServiceName(httpServicesNames[httpServicesIndex]);
        }
        return serviceRequest;
    }

}
