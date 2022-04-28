import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChattingServer extends JFrame {

    //region declare panel component
    private JPanel mainContent;
    private JButton openServerButton;
    private JTextArea statusPane;
    private JTextField chatField;
    private JButton sendButton;
    private JTextField txtPort;
    private JList<MyClient> userList;
    private JTextField txtRoomName;
    //endregion

    //region function
    static ServerSocket serverSocket;
    static ArrayList<MyClient> sockets = new ArrayList<>();
    static int Port = -1;
    static String roomName;

    //endregion

    private DefaultListModel getListUser(){
        return (DefaultListModel)userList.getModel();
    }

    public ChattingServer() {
        setContentPane(mainContent);
        setSize(650, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        openServerButton.addActionListener(e -> {
            try {
                setPort();
                setName();
                if (!openServerButton.getText().toLowerCase(Locale.ROOT).contains("close") && Port != -1) {
                    roomName = txtRoomName.getText();
                    serverSocket = new ServerSocket(Port);
                    statusPane.append(String.format("Server started on port %s for %s.\n", Port, roomName));
                    openServerButton.setText("Close server");
                    txtPort.setEnabled(false);
                    txtRoomName.setEnabled(false);
                    workingThread();
                } else {
                    serverSocket.close();
                    statusPane.append("Server closed\n");
                    openServerButton.setText("Open server");
                    txtPort.setEnabled(true);
                    txtRoomName.setEnabled(true);
                    dissconnectAllUser();
                    //Refresh socket list
                    sockets = new ArrayList<>();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, String.format("Cannot start server on port %s.\n", Port));
            }
        });
        sendButton.addActionListener(e -> {
            String str = chatField.getText();
            if(str.equals("")){
                JOptionPane.showMessageDialog(null, "Message cannot be null or empty");
                return;
            }
            String serverSaid = "Server: " + str + "\n";
            sendMessage("Server", str);
            updateChat(serverSaid);
        });
    }

    private void updateChat(String serverSaid) {
        statusPane.append(serverSaid);
        chatField.setText("");
    }

    private void dissconnectAllUser() throws IOException {
        for(MyClient s : sockets){
            s.getS().close();
        }
    }

    private void setName() {
        if(txtRoomName.getText().equals("")){
            txtRoomName.setText("Room " + this.hashCode());
        }
    }

    void sendMessage(String name, String message){
        List<MyClient> list = userList.getSelectedValuesList();
        for (MyClient client:
             sockets) {
            try {
                DataOutputStream out = new DataOutputStream(client.getS().getOutputStream());
                if(list.contains(client.getName()) || list.size() == 0){
                    out.writeUTF(name + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendDirect(String nameFrom, String nameTo, String message){
        for (MyClient client:
                sockets) {
            try {
                DataOutputStream out = new DataOutputStream(client.getS().getOutputStream());
                if(Objects.equals(client.getName(), nameTo) && !client.getName().equals(nameFrom)){
                    out.writeUTF(nameFrom + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void workingThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    if (client.isConnected()) {
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        DataInputStream in = new DataInputStream(client.getInputStream());
                        String name;
                        MyClient mClient = new MyClient();
                        var model = getListUser();
                        if (!(name = in.readUTF()).equals("")) {
                            if(model.contains(name)){
                                name = name.concat(" " + client.hashCode());
                            }
                            mClient.setName(name);
                        } else {
                            mClient.setName("Unknown " + client.hashCode());
                        }
                        mClient.setS(client);
                        sockets.add(mClient);
                        out.writeUTF("Welcome " + mClient.getName() + " to " + txtRoomName.getText() +".");
                        statusPane.append("Accepted client " + mClient.getName() + "\n");
                        model.addElement(mClient.getName());
                        userList.setModel(model);
                        openListenThread(client, name, mClient);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    sendUserList(getListUser());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(40);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void sendUserList(DefaultListModel model) throws IOException {
        for (MyClient mClient:
             sockets) {
            String uList = "204UserList";
            for(int i = 0; i < model.size(); i++){
                uList = uList.concat(("|" + model.getElementAt(i)).equals("|" + mClient.getName()) ? "" : "|" + model.getElementAt(i));
            }
            var out = new DataOutputStream(mClient.getS().getOutputStream());
            out.writeUTF(uList);
        }
    }

    private void openListenThread(Socket client, String name, MyClient mClient) {
        Thread receive = new Thread(() -> {
            try {
                while (true) {
                    DataInputStream in1 = new DataInputStream(client.getInputStream());
                    String message = in1.readUTF();
                    if(message.contains("all")){
                        sendMessage(name, message.split("\\|")[1]);
                        statusPane.append(mClient.getName() + ": " + message.split("|")[1] + "\n");
                    }
                    else if(message.contains("direct")){
                        String[] directMessage = message.split("\\|");
                        sendDirect(name, directMessage[1], directMessage[2]);
                        statusPane.append(mClient.getName() + " send direct to " + directMessage[1] + ": " + directMessage[2] + "\n");
                    }
                    else if(message.contains("private")){
                        String[] privateMessage = message.split("\\|");
                        sendDirect("Hidden user", privateMessage[1], privateMessage[2]);
                        statusPane.append(mClient.getName() + " send private to " + privateMessage[1] + ": " + privateMessage[2] + "\n");
                    }
                    else if(message.contains("group")){
                        String[] groupMessage = message.split("\\|");
                        String[] toUser = groupMessage[1].split(",");
                        for (String user:
                             toUser) {
                            if(!Objects.equals(user, "")){
                                sendDirect(name, user, groupMessage[2]);
                            }
                        }
                        statusPane.append(mClient.getName() + " send to group: " + message + "\n");
                    }
                }
            } catch (Exception ex) {
                sockets.remove(mClient);
                var model2 = getListUser();
                model2.removeElement(mClient.getName());
                userList.setModel(model2);
                statusPane.append(mClient.getName() + " was out.\n");
            }
        });
        receive.start();
    }

    public static void main(String[] args) {
        new ChattingServer();
    }

    void setPort(){
        String portTxt;
        if ((portTxt = txtPort.getText()).equals("")) {
            JOptionPane.showMessageDialog(null, "Port cannot be null");
        } else {
            try {
                Port = Integer.parseInt(portTxt);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Port must be a number");
            }
        }
    }
}

class MyClient {
    private String name;
    private Socket s;

    public MyClient() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClient myClient = (MyClient) o;
        return name.equals(myClient.name) && s.equals(myClient.s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }
}