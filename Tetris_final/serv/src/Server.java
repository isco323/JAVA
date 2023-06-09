import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    int score;
    double randata;
    private String[] stlArray;
    public void runi() throws IOException {
        ReceiveRequest();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS");
        System.out.println(formatter.format(date)+": Данные приняты");
        Date date2 = new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS");
        System.out.println(formatter2.format(date2)+": Счётчик сработал");
        score = Integer.valueOf(stlArray[0]);
        //randata =Double.valueOf(stlArray[1]);
        Calculate();
        System.out.println(score +": Счётчик сработал");
        SendResponce(score);
        Date date1 = new Date();
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS");
        System.out.println(formatter1.format(date1)+": Данные отправлены");
    }

    private void Calculate() {
        Thread t = new Thread();
        int n  = score;
        final int[] res = {0};
        Runnable task = new Runnable() {
            public void run() {
                int localResult = 0;
                switch (n)
                {
                    case 1:
                        localResult +=100;
                        break;
                    case 2:
                        localResult +=300;
                        break;
                    case 3:
                        localResult +=700;
                        break;
                    case 4:
                        localResult +=1500;
                        break;
                }
                synchronized(this) {
                    res[0] = localResult;
                }
            }
        };
        t = new Thread(task);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        score = res[0];
    }

    private void SendResponce(int responseData) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] data = String.valueOf(responseData).getBytes();
        InetAddress clientAddress = InetAddress.getByName("127.0.0.1");
        int clientPort = 12346;
        DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
        socket.send(packet);
        socket.close();
    }

    public void ReceiveRequest() throws IOException, IOException {
        DatagramSocket socket = new DatagramSocket(12345);
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        String requestData = new String(message);
        socket.close();
        stlArray = requestData.split(",");
    }

}
