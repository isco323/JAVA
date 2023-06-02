import java.io.IOException;

public class Main {

    public Main() {
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        System.out.println("Сервер запущен");

        while (true) {
            System.out.println("-----------Ч--М--С--Мс--------Ожидаю...-------------------------------");
            server.runi();
        }
    }
}