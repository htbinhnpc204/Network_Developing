package UDP;

import StringHelper.StringHelper;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDP_Server {

    public static void main(String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        while (true) {
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData()).trim();
            InetAddress IPAddress = receivePacket.getAddress();
            DatagramPacket sendPacket;
            int port = receivePacket.getPort();

            String sentence_to_client = "Chuoi nghich dao: " + StringHelper.Reverse(sentence.trim()) + "\n" + "Chuoi ket hop: " + StringHelper.Mix(sentence);
            sendData = sentence_to_client.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}