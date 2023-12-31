package code.server;

import com.mongodb.MongoSocketReadException;
import com.mongodb.MongoWriteException;
import com.sun.net.httpserver.*;

import code.client.Model.RecipeCSVWriter;
import java.io.*;
import java.net.*;
import java.util.*;

public class RecipeRequestHandler implements HttpHandler {
    private IRecipeDb recipeDb;

    public RecipeRequestHandler(IRecipeDb recipeDb) {
        this.recipeDb = recipeDb;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not valid request method.");
            }
        } catch (MongoWriteException ex) {
            ex.printStackTrace();
            response = "Duplicate Key Error";
        } catch (MongoSocketReadException ex) {
            ex.printStackTrace();
            response = "Server Offline";
        } catch (Exception e) {
            response = "Error";
            System.out.println("An erroneous request");
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery(); // ?=
        if (query != null) {
            String userID = query.substring(query.indexOf("=") + 1);
            List<Recipe> recipeList = recipeDb.getList(userID);
            if (recipeList.isEmpty()) {
                response = "No recipes found";
            } else {
                Writer writer = new StringWriter();
                RecipeCSVWriter recipeWriter = new RecipeCSVWriter(writer);
                recipeWriter.writeRecipeList(recipeList);
                response = writer.toString();
            }
        } else {

        }

        return response;
    }

    private Recipe buildRecipeFromPostData(String postData) throws IOException {
        Reader reader = new StringReader(postData);
        RecipeCSVReader recipeCSVReader = new RecipeCSVReader(reader);
        Recipe recipe = recipeCSVReader.readRecipe();
        return recipe;
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        Recipe recipe = buildRecipeFromPostData(postData);
        recipeDb.add(recipe);
        String response = "Posted entry " + recipe.getTitle();
        System.out.println(response);
        scanner.close();
        return response;
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        Recipe recipe = buildRecipeFromPostData(postData);
        recipeDb.remove(recipe.getId());
        recipeDb.add(recipe);
        String response = "Updated entry " + recipe.getTitle();
        System.out.println(response);
        scanner.close();
        return response;
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        Recipe recipe = buildRecipeFromPostData(postData);
        recipeDb.remove(recipe.getId());
        response = "Removed entry " + recipe.getTitle();
        System.out.println(response);
        scanner.close();
        return response;
    }
}