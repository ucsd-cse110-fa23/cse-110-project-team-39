package code.server.mocking;

import com.sun.net.httpserver.*;

import code.server.IHttpConnection;
import code.server.VoiceToText;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class MockWhisperRequestHandler extends VoiceToText implements HttpHandler {
    public MockWhisperRequestHandler() {
        super(new MockHttpConnection(200));
    }

    public MockWhisperRequestHandler(IHttpConnection connection) {
        super(connection);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String type = query.substring(query.indexOf("=") + 1);

        String response = "Error";
        if (type.equals("mealType")) {
            response = "Breakfast";
        } else if (type.equals("ingredients")) {
            response = "Chicken, eggs.";
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }
}
