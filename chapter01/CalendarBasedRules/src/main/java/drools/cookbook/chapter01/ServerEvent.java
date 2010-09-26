package drools.cookbook.chapter01;

import java.util.Date;

public class ServerEvent {

    private Server server;
    private Date time;
    private ServerStatus status;

    public ServerEvent(Server server, Date time, ServerStatus status) {
        this.server = server;
        this.time = time;
        this.setStatus(status);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public ServerStatus getStatus() {
        return status;
    }

}
