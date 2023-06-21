package cn.share.event.bus.client.connect;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:44:28
 * @describe class responsibility
 */
public class ClientServer {
    private final int port;
    private final String eventBusServerIp;

    public ClientServer(int port, String eventBusServerIp) {
        this.port = port;
        this.eventBusServerIp = eventBusServerIp;
    }

    public void initClient() {

    }
}
