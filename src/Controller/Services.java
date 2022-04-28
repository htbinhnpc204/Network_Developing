package Controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Services {

    private Socket socket;

    private InetAddress address;
    int port;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket s) {
        this.socket = s;
    }

    public Services(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public boolean doConnect(String yourName){
        try {
            socket = new Socket(address, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(yourName);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sendMessage(String message){
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("all|" + message);
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sendDirect(String receiverName, String message){
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("direct|" + receiverName+ "|" + message);
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sendPrivate(String receiverName, String message){
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("private|" + receiverName+ "|" + message);
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sendGroup(List<String> group, String message){
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String forwardMessage = "group|";
            for (String str:
                 group) {
                forwardMessage += str + ",";
            }
            forwardMessage += "|" + message;
            out.writeUTF(forwardMessage);
        }catch (IOException e) {
            return false;
        }
        return false;
    }
}
