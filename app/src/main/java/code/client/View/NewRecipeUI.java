package code.client.View;

import code.client.Controllers.*;
import code.client.Model.*;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
// import javax.sound.sampled.*;

class MealTypeSelection extends GridPane {
    private Label prompt;
    private Button recordButton;
    private ImageView microphone;
    private TextField mealTypeField;

    // =================FIRST PROMPT=================//
    MealTypeSelection() {
        // Set the preferred vertical and horizontal gaps
        this.setVgap(20);
        this.setHgap(20);
        // Get a picture of a microphone for the voice recording button
        File file = new File("app/src/main/java/code/client/View/microphone.png");
        microphone = new ImageView(new Image(file.toURI().toString()));
        // Set the size of the microphone image
        microphone.setFitWidth(30);
        microphone.setFitHeight(30);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Select Meal Type (Breakfast, Lunch, or Dinner)");
        prompt.setStyle("-fx-font-size: 16;");
        // Set a textField for the meal type that was selected
        mealTypeField = new TextField();
        mealTypeField.setPromptText("Meal Type");
        // Add all of the elements to the MealTypeSelection
        this.add(recordButton, 0, 0);
        this.add(prompt, 1, 0);
        this.add(mealTypeField, 0, 1);
    }

    public TextField getMealType() {
        return mealTypeField;
    }

    public Button getRecordButton() {
        return recordButton;
    }
}

class IngredientsList extends GridPane {

    private Label prompt;
    private Button recordButton;
    private ImageView microphone;
    private TextField ingredientsField;

    // ==============SECOND PROMPT=================//
    IngredientsList() {
        // Set the preferred vertical and horizontal gaps
        this.setVgap(20);
        this.setHgap(20);
        // Get a picture of a microphone for the voice recording button
        File file = new File("app/src/main/java/code/client/View/microphone.png");
        microphone = new ImageView(new Image(file.toURI().toString()));
        // Set the size of the microphone image
        microphone.setFitWidth(30);
        microphone.setFitHeight(30);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Please List Your Ingredients");
        prompt.setStyle("-fx-font-size: 16;");
        // Set a textField for the meal type that was selected
        ingredientsField = new TextField();
        ingredientsField.setPromptText("Ingredients");
        // Add all of the elements to the MealTypeSelection
        this.add(recordButton, 0, 0);
        this.add(prompt, 1, 0);
        this.add(ingredientsField, 0, 1);
    }

    public TextField getIngredients() {
        return ingredientsField;
    }

    public Button getRecordButton() {
        return recordButton;
    }
}

class GPTRecipe extends GridPane {
    private Label recipeLabel;
    private TextField recipeField;

    GPTRecipe() {
        this.setVgap(20);
        recipeLabel = new Label("Here Is Your Recipe");
        recipeField = new TextField();
        this.add(recipeLabel, 0, 0);
        this.add(recipeField, 0, 1);
    }
}

class HeaderMic extends HBox {
    HeaderMic() {
        this.setPrefSize(700, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe Creation");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER);
    }
}

class AppFrameMic extends BorderPane {
    // Inputs for WhisperHandler
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    // Helper variables for button functionality
    private boolean recording; // keeps track if the app is currently recording
    private String mealType; // stores the meal type specified by the user
    private String ingredients; // stores the ingredients listed out by the user
    private String recipeSteps; // stores the recipe steps generated by ChatGPT
    // AppFrameMic elements
    private GridPane recipeCreationGrid;
    private Header header;
    private MealTypeSelection mealTypeSelection;
    private IngredientsList ingredientsList;
    private GPTRecipe gptrecipe;
    private Button recordButton1, recordButton2, createButton, saveButton;

    // Scene Transitions
    private ArrayList<IWindowUI> scenes;
    private Stage primaryStage;
    private Scene mainScene;
    private RecipeUI newRecipe;

    AppFrameMic() throws URISyntaxException, IOException {
        header = new Header();
        mealTypeSelection = new MealTypeSelection();
        ingredientsList = new IngredientsList();
        gptrecipe = new GPTRecipe();

        recipeCreationGrid = new GridPane();
        recipeCreationGrid.setAlignment(Pos.CENTER);
        recipeCreationGrid.setVgap(20);
        recipeCreationGrid.setHgap(20);
        recipeCreationGrid.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        recipeCreationGrid.add(mealTypeSelection, 0, 0);
        recipeCreationGrid.add(ingredientsList, 0, 1);
        recipeCreationGrid.add(gptrecipe, 0, 2);

        this.setTop(header);
        this.setCenter(recipeCreationGrid);

        recordButton1 = mealTypeSelection.getRecordButton();
        recordButton2 = ingredientsList.getRecordButton();

        createButton = new Button("Create Recipe");
        recipeCreationGrid.add(createButton, 0, 3);

        saveButton = new Button("Save Setup");
        recipeCreationGrid.add(saveButton, 0, 4);

        addListeners();
    }

    public void storeNewRecipeUI(RecipeUI recipeUI) {
        newRecipe = recipeUI;
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param primaryStage - Main stage that has the window
     * @param scenes       - list of different scenes to switch between.
     */
    public void setScenes(Stage primaryStage, ArrayList<IWindowUI> scenes) {
        this.scenes = scenes;
        this.primaryStage = primaryStage;
    }

    public void setMain(Scene main) {
        mainScene = main;
    }

    public void addListeners() throws URISyntaxException, IOException {

        AudioRecorder recorder = new AudioRecorder(new Label("Recording..."));

        File file = new File("recording.wav");
        WhisperHandler audioProcessor = new WhisperHandler(API_ENDPOINT, TOKEN, MODEL);

        recordButton1.setOnAction(e -> {

            if (!recording) {
                recorder.startRecording();
                recording = true;

            } else {
                recorder.stopRecording();
                recording = false;
                try {
                    audioProcessor.sendHttpRequest(file);
                    mealType = audioProcessor.processAudio();
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                }
                mealTypeSelection.getMealType().setText(mealType);
            }
        });

        recordButton2.setOnAction(e -> {

            if (!recording) {
                recorder.startRecording();
                recording = true;

            } else {
                recorder.stopRecording();
                recording = false;
                try {
                    audioProcessor.sendHttpRequest(file);
                    ingredients = audioProcessor.processAudio();
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                }
                ingredientsList.getIngredients().setText(ingredients);
            }
        });

        // createButton.setOnAction(e -> {

        // });

        // CHANGE SCENE TO DETAILED RECIPE DISPLAY
        saveButton.setOnAction(e -> {

            ITextToRecipe caller = new TextToRecipe();
            /*
             * WhisperHandler audio = new WhisperHandler(
             * "https://api.openai.com/v1/audio/transcriptions",
             * "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",
             * "whisper-1");
             */
            try {

                String audioOutput = ingredients;// audio.processAudio();
                String responseText = caller.getChatGPTResponse(audioOutput);
                Recipe recipe = caller.mapResponseToRecipe(responseText);
                RecipeDetailsUI detailsUI = new RecipeDetailsUI(recipe);

                // gets the DetailsAppFrame
                DetailsAppFrame details = (DetailsAppFrame) scenes.get(2);
                details.setRecipeHolder(detailsUI); // should have RecipeDetailsUI
                details.setRecipeUI(newRecipe);

                details.setMain(mainScene);
                details.setRoot(mainScene); // Changes UI to Detailed Recipe Screen
                // primaryStage.setScene(details.getSceneWindow());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });
    }
}

public class NewRecipeUI implements IWindowUI {
    private ArrayList<IWindowUI> scenes;
    private Stage primaryStage;
    private AppFrameMic root;
    private Scene mainScene;

    NewRecipeUI() throws URISyntaxException, IOException {
        root = new AppFrameMic();
    }

    public void storeNewRecipeUI(RecipeUI recipeUI) {
        root.storeNewRecipeUI(recipeUI);
    }

    public Scene getSceneWindow() {

        Scene scene = new Scene(root, 700, 700);
        return scene;
    }

    /**
     * This method provides the UI holder with the different scenes that can be
     * switched between.
     * 
     * @param primaryStage - Main stage that has the window
     * @param scenes       - list of different scenes to switch between.
     */
    public void setScenes(Stage primaryStage, ArrayList<IWindowUI> scenes) {
        root.setScenes(primaryStage, scenes);
    }

    @Override
    public void setRoot(Scene scene) {
        // TODO Auto-generated method stub
        scene.setRoot(root);
    }

    public void setMain(Scene main) {
        root.setMain(main);
    }
}
