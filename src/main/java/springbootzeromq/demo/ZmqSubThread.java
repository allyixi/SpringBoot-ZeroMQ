package springbootzeromq.demo;

import org.zeromq.ZMQ;

/**
 * ZMQ接收线程
 */
public abstract class ZmqSubThread implements Runnable {

    /**
     * ZMQ启动线程数
     */
    private int ZMQThreadCount = Integer.parseInt("1");

    /**
     * ZMQ接收端口
     */
    private int ZMQRecvPort;

    /**
     * ZMQ监听接收端口
     */
    private String ZMQRecvIP;

    private ZMQ.Context context = null;
    private ZMQ.Socket subSock = null;

    public ZmqSubThread() {
        initZMQ();
    }

    public ZmqSubThread(String ZMQRecvIP, int ZMQRecvPort) {
        this.ZMQRecvIP = ZMQRecvIP;
        this.ZMQRecvPort = ZMQRecvPort;
        initZMQ();
    }

    /**
     * 初始化ZMQ对象
     */
    public void initZMQ() {
        if (ZMQRecvIP == null || "".equals(ZMQRecvIP)) {
            throw new RuntimeException("IP Error!");
        }
        if (ZMQRecvPort == 0) {
            throw new RuntimeException("Port Error!");
        }

        context = ZMQ.context(ZMQThreadCount);
        subSock = context.socket(ZMQ.SUB);
        String ConUri = "tcp://" + ZMQRecvIP + ":" + ZMQRecvPort;
        subSock.connect(ConUri);
        subSock.subscribe("".getBytes());
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] recvBuf = subSock.recv(ZMQ.SUB);
                if (recvBuf == null) {
                    continue;
                }
                dealWith(recvBuf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理接收到数据的抽象方法
     */
    public abstract void dealWith(byte[] data);
}
