package code;

import javafx.application.Application;
import javafx.stage.Stage;

import code.client.Model.*;
import code.client.View.*;
import code.client.Controllers.*;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model);
        ServerCheck checker = new ServerCheck();
        Scene login = new Scene(view.getLoginUI().getRoot());
        view.setScene(login);
        if (checker.isOnline()) {
            System.out.println("Server is online");
            controller.addListenersToList();
        } else {
            System.out.println("Server is offline");
            view.goToOfflineUI();
        }
        primaryStage.setMinWidth(600);
        primaryStage.setScene(login);

        primaryStage.setTitle("Pantry Pal");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(620);
        primaryStage.setMinHeight(620);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}