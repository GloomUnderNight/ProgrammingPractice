import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {

    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();
    private String host;
    private Integer port;
    private String Username;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.Username = loggingIn();
    }


    public static void main(String[] args) {
        if (args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else {

            System.out.println("Connection to server...");
            String serverHost = args[0];
            Integer serverPort = Integer.parseInt(args[1]);
            Client client = new Client(serverHost, serverPort);
            new Thread(client).start();
            System.out.println("Connected to server: " + serverHost + ":" + serverPort);
            client.listen();
        }
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/chat?token=" + messageExchange.getToken(history.size()));
        return (HttpURLConnection) url.openConnection();
    }

    public List<Message> getMessages() {
        List<String> list = new ArrayList<String>();
        HttpURLConnection connection = null;
        List<Message> tmp = new ArrayList<Message>();
        try {
            connection = getHttpURLConnection();
            connection.connect();
            String response = messageExchange.inputStreamToString(connection.getInputStream());
            JSONObject jsonObject = messageExchange.getJSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("messages");
            for (Object o : jsonArray) {
                list.add(o.toString());
            }

            for (int i = 0; i < list.size(); i++){
                tmp.add(i, Message.fromString(list.get(i)));
                System.out.println(tmp.get(i).getAuthor() + ": " + tmp.get(i).getMessageText() + " (" +
                tmp.get(i).getDate() + " )");
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return tmp;
    }

    public void sendMessage(Message message) {
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection();
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            byte[] bytes = messageExchange.getClientSendMessageRequest(message).getBytes();
            wr.write(bytes, 0, bytes.length);
            wr.flush();
            wr.close();

            connection.getInputStream();

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void listen() {
        while (true) {
            List<Message> list = getMessages();

            if (list.size() > 0) {
                history.addAll(list);
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String message = scanner.nextLine();
            Message tmp = new Message(message, Username);
            sendMessage(tmp);
        }
    }

    private String loggingIn(){
        System.out.print("Input your name: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

}
