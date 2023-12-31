package code.client.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URI;
import java.nio.file.*;
import java.net.URLEncoder;

public class Model {
    public String performAccountRequest(String method, String user, String password) {
        String response = "Error";
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.ACCOUNT_PATH;
            urlString += "?=" + user + ":" + password;
            URL url = new URI(urlString).toURL();
            // POSSIBLY this later --> "url/username=123&&password=345"
            // Temporarily THIS --> "url/?=123:345"
            // send GET request to see if user exists
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            System.out.println("Method is " + method);

            // make a new user
            if (method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(user + "," + password);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = in.readLine();
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = "Error: " + ex.getMessage();
        }
        return response;
    }

    public String performRecipeRequest(String method, String recipe, String userId) {
        // Implement your HTTP request logic here and return the response
        String response = "Error";
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.RECIPE_PATH;
            if (userId != null) {
                urlString += "?=" + userId;
            }

            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(recipe);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String tempResponse = "";
            String line;
            while ((line = in.readLine()) != null) {
                tempResponse += line + "\n";
            }
            if (!(tempResponse.toLowerCase().contains("error"))) {
                response = tempResponse;
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = "Error: " + ex.getMessage();
        }
        return response;
    }

    public String performChatGPTRequest(String method, String mealType, String ingredients) {
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.CHATGPT_PATH;
            mealType = URLEncoder.encode(mealType, "UTF-8");
            ingredients = URLEncoder.encode(ingredients, "UTF-8");
            urlString += "?=" + mealType + "::" + ingredients;
            URL url = new URI(urlString).toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }
            in.close();

            return response.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public String performDallERequest(String method, String recipeTitle) {
        try {
            String urlString = AppConfig.SERVER_URL + AppConfig.DALLE_PATH;
            urlString += "?=" + URLEncoder.encode(recipeTitle, "UTF-8");
            URL url = new URI(urlString).toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public String performWhisperRequest(String method, String type) throws MalformedURLException, IOException {
        String response = "Error";
        String urlString = AppConfig.SERVER_URL + AppConfig.WHISPER_PATH;
        urlString += "?=" + type;
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";
        URLConnection connection = new URL(urlString).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // send Whisper Request
        File audioFile = new File(AppConfig.AUDIO_FILE);
        try (OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + audioFile.getName() + "\"")
                    .append(CRLF);
            writer.append("Content-Length: " + audioFile.length()).append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(audioFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(audioFile.toPath(), output);
            output.flush();
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println("Response code: [" + responseCode + "]");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            response = "";

            while ((line = in.readLine()) != null) {
                response += line + "\n";
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            response = "Error: " + e;
        }

        System.out.println("Whisper response: " + response);
        return response.trim();
    }
}