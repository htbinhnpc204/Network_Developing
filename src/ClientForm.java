import Controller.Services;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ClientForm extends JFrame{
    private JTextField txtAddress;
    private JTextField txtPort;
    private JTextField txtName;
    private JButton joinButton;
    private JTextArea chatZone;
    private JTextField txtMessage;
    private JButton sendButton;
    private JPanel mainContent;
    private JList listRoomate;
    private JComboBox comboBox1;

    String name;
    InetAddress inetAddress;
    Socket client;
    int port = -1;
    Services services;
    String myName;

    public ClientForm() {
        setContentPane(mainContent);
        setSize(800, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        //join button event
        joinButton.addActionListener(e -> {
            if(joinButton.getText().equalsIgnoreCase("QUIT")){
                Close();
                return;
            }
            Connect();
        });
        sendButton.addActionListener(e -> {
            String selection = comboBox1.getSelectedItem().toString();
            chatZone.append("Selection: " + selection + "\n");

            List<String> list = listRoomate.getSelectedValuesList();
            for (String a:
                 list) {
                chatZone.append(a + "\n");
            }
            String message = txtMessage.getText();
            switch(selection){
                case "Private":
                    services.sendPrivate(list.get(0), message);break;
                case "Direct":
                    services.sendDirect(list.get(0), message);break;
                case "Group":
                    services.sendGroup(list, message);break;
                default: services.sendMessage(message);
            }
            chatZone.append("Me: " + txtMessage.getText() + "\n");
            txtMessage.setText("");
        });
    }

    private void Close() {
        try {
            services.getSocket().close();
            joinButton.setText("Join");
            chatZone.setText("You have been quit from chat room.\n");
            setState(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void Connect() {
        try {
            if (txtAddress.getText().contains("localhost")) {
                inetAddress = InetAddress.getLocalHost();
            }
            if(txtPort.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Port cannot be null");
            }
            else{
                try{
                    port = Integer.parseInt(txtPort.getText());
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Port must be a number");
                }
            }
            if(port <= 0){
                JOptionPane.showMessageDialog(null, "Port must be bigger than 0");
            }
            if(inetAddress != null && port > 0){
                services = new Services(inetAddress, port);
                if(services.doConnect((name = txtName.getText()))){
                    client = services.getSocket();
                    new Thread(() -> {
                        try {
                            DataInputStream in = new DataInputStream(client.getInputStream());
                            while (true){
                                String str;
                                if(!(str = in.readUTF()).equals("")){
                                    if(!str.contains("204UserList")){
                                        chatZone.append(str + "\n");
                                    }
                                    else {
                                        String[] arrUser = (str.replace("204UserList", "")).split("\\|");
                                        DefaultListModel model = new DefaultListModel();
                                        for(String s : arrUser){
                                            if(!s.equals("")){
                                                model.addElement(s);
                                            }
                                        }
                                        listRoomate.setModel(model);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            Close();
                        }
                    }).start();
                    setState(false);
                    joinButton.setText("QUIT");
                }else{
                    JOptionPane.showMessageDialog(null, "Kết nối thất bại");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setState(boolean isEnabled){
        txtAddress.setEnabled(isEnabled);
        txtName.setEnabled(isEnabled);
        txtPort.setEnabled(isEnabled);
        sendButton.setEnabled(!isEnabled);
        txtMessage.setEnabled(!isEnabled);
    }

    public static void main(String[] args) {
        new ClientForm();
    }
}

