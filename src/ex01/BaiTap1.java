package ex01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

public class BaiTap1 {
    private static void menu() {
        System.out.println("*** Menu bài tập phần 1 ***");
        System.out.println("1. Bài tập 1");
        System.out.println("2. Bài tập 2");
        System.out.println("3. Bài tập 3");
        System.out.println("4. Bài tập 4");
        System.out.println("9. Thoát");
    }

    private static void exercise1() throws UnknownHostException {
        System.out.println("*** Bài tập 1 ***");
        System.out.printf("Localhost: %s%n", InetAddress.getLocalHost());
        System.out.printf("Null host: %s%n", InetAddress.getByName(null));
        System.out.printf("Host address: %s%n", InetAddress.getByName("facebook.com"));
    }

    private static void exercise2() throws UnknownHostException {
        System.out.println("*** Bài tập 2 ***");
        System.out.printf("Host address: %s%n", (Object) InetAddress.getAllByName("facebook.com"));
    }

    private static void exercise3(Scanner scanner) throws IOException {
        System.out.println("*** Bài tập 3 ***");
        System.out.print("Nhập URL: ");
        scanner.nextLine();
        URL url = new URL(scanner.nextLine());
        var connection = url.openConnection();
        for(int i = 0; i < connection.getHeaderFields().size(); i++) {
            System.out.printf("Header %d: %s%n", i, connection.getHeaderField(i));
        }
    }

    private static void exercise4(Scanner scanner) throws IOException {
        System.out.println("*** Bài tập 4 ***");
        System.out.print("Nhập URL: ");
        scanner.nextLine();
        URL url = new URL(scanner.nextLine());
        var connection = url.openConnection();
        var content = (InputStream) connection.getContent();
        readContent(content);
    }

    private static void readContent(InputStream content) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
    }

    public static void main(String[] args) {
        int option;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            menu();
            System.out.print("Chọn bài tập: ");
            try {
                option = scanner.nextInt();
                if (option == 9) {
                    break;
                }

                switch (option) {
                    case 1 -> exercise1();
                    case 2 -> exercise2();
                    case 3 -> exercise3(scanner);
                    case 4 -> exercise4(scanner);
                }
                System.out.println("Ấn phím enter để tiếp tục...");
                System.in.read();
            } catch (Exception ex) {
                System.out.println("Vui lòng chọn lại.");
            }
        }
    }
}
