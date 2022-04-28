package UDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class clientUDP {
        public static void main(String args[]) throws Exception {
                DatagramSocket clientSocket = new DatagramSocket(1234);
                InetAddress IPAddress = InetAddress.getByName("localhost");

                while (true) {
                        byte[] sendData;
                        byte[] receiveData = new byte[2048];
                        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                        String sentence = inFromUser.readLine();
                        sendData = sentence.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                        clientSocket.send(sendPacket);
                        
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);
                        String modified_Sentence = new String(receivePacket.getData());
                        System.out.println("FROM SERVER:\n" + modified_Sentence);

                        if (sentence.compareTo("quit") == 0)
                                break;
                }
                clientSocket.close();
        }
}