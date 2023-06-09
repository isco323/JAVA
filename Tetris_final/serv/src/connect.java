import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class connect {
    private int lines = 0;
    private double score = 0;
    private static int Result;
    public int getLines(){
        return lines;
    }
    public connect()
    {
        this.Result = connect.Result;
    }
    public double getscore()
    {
        return score;
    }

    public void setResult(int result) {
        Result = result;
    }
    public int getResult(){
        return Result;
    }
    public void SendData(int score) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] data = (score +", "+ getscore() + " = Score").getBytes();
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
        int serverPort = 12345;
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
        socket.send(packet);
        socket.close();
    }
    public int ReadData() throws IOException {
        DatagramSocket socket = new DatagramSocket(12346);
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        String response = new String(message);
        socket.close();
        return Integer.valueOf(response);
    }
    public void runServer(int score) throws IOException, InterruptedException {
            SendData(score);
            setResult(ReadData());

    }
}
