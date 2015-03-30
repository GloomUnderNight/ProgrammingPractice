import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

public class Server implements HttpHandler {
    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();

    public static void main(String[] args) {
        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else {
            try {
                System.out.println("Server is starting...");
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                System.out.println("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                System.out.println("Get list of messages: GET http://" + serverHost + ":" + port + "/chat?token={token}");

                System.out.println("Send message: POST http://" + serverHost + ":" + port +
                        "/chat provide body json in format " + " " +
                        "{\"id\":\"{id}\", \"user\":\"{user}\", \"message\":\"{message}\", \"date\":\"{date}\"}");

                System.out.println("Delete message: DELETE " + serverHost + ":" + port + "/chat " +
                        "provide json body in format " + " " + "{\"id\":\"{id}\"}");

                System.out.println("Edit message: PUT " + serverHost + ":" + port + "/chat " +
                        "provide json body in format " + " " + "{\"id\":\"{id}\", \"message\":\"{message}\"}");

                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                System.out.println("Error creating http server: " + e);
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";

        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        } else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            doDelete(httpExchange);
        } else if ("PUT".equals(httpExchange.getRequestMethod())){
                doEdit(httpExchange);
        } else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
        }
        sendResponse(httpExchange, response);
    }

    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                List<String> tmpStringList = new ArrayList<String>();
                List<Message> tmpMessageList = history.subList(index, history.size());
                for (int i = 0; i < tmpMessageList.size(); i++){
                    tmpStringList.add(i,tmpMessageList.get(i).toString());
                }
                return messageExchange.getServerResponse(tmpStringList);
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            Message tmp  = messageExchange.getClientMessage(httpExchange.getRequestBody());
            System.out.println("Get Message from " + tmp.getAuthor() + " at " + tmp.getDate() +
                    " : " + tmp.getMessageText());
            history.add(tmp);
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doDelete(HttpExchange httpExchange){
        try {
            Message messageToDelete = messageExchange.getClientMessageToDelete(httpExchange.getRequestBody());
            for (Message aHistory : history) {
                if (messageToDelete.getId() == aHistory.getId()) {
                    System.out.println("Message with Id = " + aHistory.getId() + " was deleted by "
                            + aHistory.getAuthor() + " at "
                            + aHistory.getDate());
                    aHistory.delete();
                    aHistory.setAuthor("System");
                    aHistory.setDeleted(true);
                    break;
                }
            }
        }
        catch (ParseException e) {
            System.err.println("Invalid message for deletion: " + httpExchange.getRequestBody());
        }
    }

    private void doEdit(HttpExchange httpExchange){
        try {
            Message messageToEdit = messageExchange.getClientMessageToEdit(httpExchange.getRequestBody());
            for (Message aHistory : history) {
                if (messageToEdit.getId() == aHistory.getId() && !aHistory.isDeleted()) {
                    System.out.println("Message with Id = " + aHistory.getId() + " was edited by "
                            + aHistory.getAuthor() + " at "
                            + new Date().toString());
                    aHistory.edit(messageToEdit.getMessageText());
                    break;
                }
            }
        }
        catch (ParseException e) {
            System.err.println("Invalid message for deletion: " + httpExchange.getRequestBody());
        }
    }

    private void sendResponse(HttpExchange httpExchange, String response) {
        try {
            byte[] bytes = response.getBytes();
            Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin","*");
            httpExchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write( bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
