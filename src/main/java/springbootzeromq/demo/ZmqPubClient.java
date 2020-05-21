package springbootzeromq.demo;

import org.zeromq.ZMQ;

/**
 * ZMQ发布数据
 */
public class ZmqPubClient {

    /**
     * ZMQ启动线程数
     */
    private static int ZMQThreadCount = Integer.parseInt("1");

    /**
     * ZMQ数据广播端口
     */
    private static int ZMQSendPort = Integer.parseInt("7111");

    private static ZMQ.Context context = null;
    private static ZMQ.Socket pubSock = null;

    /**
     * 初始化ZMQ对象
     */
    private static void initZMQ() {
        if (context == null) {
            context = ZMQ.context(ZMQThreadCount);
        }
        if (ZMQSendPort != 0) {
            pubSock = context.socket(ZMQ.PUB);
            String bindUri = "tcp://*:" + ZMQSendPort;
            pubSock.bind(bindUri);
        } else {
            throw new RuntimeException("Error!");
        }
    }

    private static ZMQ.Socket getPubSock() {
        if (pubSock == null) {
            initZMQ();
        }
        return pubSock;
    }

    private static ZMQ.Socket getPubSock(int port) {
        if (context == null) {
            context = ZMQ.context(ZMQThreadCount);
        }
        ZMQ.Socket socket = context.socket(ZMQ.PUB);
        String binUri = "tcp://*" + port;
        socket.bind(binUri);
        return socket;
    }

    public static void sendData(String msg) {
        getPubSock().send(msg, ZMQ.NOBLOCK);
    }

    public static void sendData(byte[] msg) {
        getPubSock().send(msg);
    }

    public static void sendData(int port, String msg) {
        ZMQ.Socket socket = getPubSock(port);
        socket.send(msg);
        socket.close();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            sendData("测试测试".getBytes());
            System.out.println("发送测试");
            Thread.sleep(1000);
        }
    }
}
