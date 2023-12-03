package code.client.View;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;
import code.client.Model.Account;
import code.server.Recipe;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class View {
    private AppFrameHome home;
    private AppFrameMic audioCapture;
    private DetailsAppFrame detailedRecipe;
    private LoginUI login;
    private AccountCreationUI createAcc;
    private Scene mainScene;
    private OfflineUI offlineScreen;

    public View() throws IOException, URISyntaxException {
        offlineScreen = new OfflineUI();
        login = new LoginUI();
        home = new AppFrameHome();
        audioCapture = new AppFrameMic();
        detailedRecipe = new DetailsAppFrame();
        createAcc = new AccountCreationUI();
    }

    public void setScene(Scene scene) {
        mainScene = scene;
    }

    public void goToRecipeList() {
        mainScene.setRoot(home.getRoot());
    }

    public void goToAudioCapture() throws URISyntaxException, IOException {
        audioCapture = new AppFrameMic();
        mainScene.setRoot(audioCapture.getRoot());
    }

    public void goToDetailedView(Recipe recipe, boolean savedRecipe) {
        mainScene.setRoot(detailedRecipe.getRoot(recipe, savedRecipe));
    }

    public void goToCreateAcc() {
        mainScene.setRoot(createAcc.getRoot());
    }

    public void goToLoginUI() {
        mainScene.setRoot(login.getRoot());
    }

    public void goToOfflineUI() {
        mainScene.setRoot(offlineScreen);
    }

    public RecipeListUI getRecipeButtons() {
        return home.getRecipeList();
    }

    public DetailsAppFrame getDetailedView() {
        return detailedRecipe;
    }

    public AppFrameMic getAppFrameMic() {
        return audioCapture;
    }

    public AppFrameHome getAppFrameHome() {
        return home;
    }

    public LoginUI getLoginUI() {
        return login;
    }

    public AccountCreationUI getAccountCreationUI() {
        return createAcc;
    }
}
