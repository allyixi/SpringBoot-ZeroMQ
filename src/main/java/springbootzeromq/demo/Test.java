package springbootzeromq.demo;

public class Test {
    public static void main(String[] args) {
        ZmqSubThread zmqSubThread = new ZmqSubThread("127.0.0.1", 7111) {
            @Override
            public void dealWith(byte[] data) {
                System.out.println(new String(data));
            }
        };
        Thread thread = new Thread(zmqSubThread);
        thread.start();
    }
}
