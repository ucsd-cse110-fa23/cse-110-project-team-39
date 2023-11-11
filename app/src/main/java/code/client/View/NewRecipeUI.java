package code.client.View;

import code.client.Controllers.*;
import code.client.Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
        microphone.setFitWidth(50);
        microphone.setFitHeight(50);
        microphone.setScaleX(1.5);
        microphone.setScaleY(1.5);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Select Meal Type (Breakfast, Lunch, or Dinner)");
        prompt.setStyle("-fx-font-size: 16;");
        prompt.setTextFill(Color.web("#ADD8E6"));
        // Set a textField for the meal type that was selected
        mealTypeField = new TextField();
        mealTypeField.setPromptText("Meal Type");
        mealTypeField.setStyle("-fx-font-size: 16"); //CHANGE 1 (FONT)
        mealTypeField.setPrefWidth(300);
        mealTypeField.setPrefHeight(50);
        
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
        microphone.setFitWidth(50);
        microphone.setFitHeight(50);
        microphone.setScaleX(1.5);
        microphone.setScaleY(1.5);
        // Create a recording button
        recordButton = new Button();
        recordButton.setGraphic(microphone);
        // Set the user prompt for meal type selection
        prompt = new Label("Please List Your Ingredients");
        prompt.setStyle("-fx-font-size: 16;");
        prompt.setTextFill(Color.web("#ADD8E6")); //CHANGE 2 (COLOR)
        // Set a textField for the meal type that was selected
        ingredientsField = new TextField();
        ingredientsField.setPromptText("Ingredients");
        ingredientsField.setStyle("-fx-font-size: 16"); //change
        ingredientsField.setPrefWidth(300); // CHANGE 3 (WIDTH OF PROMPT)
        ingredientsField.setPrefHeight(50); // CHANGE
        
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
        recipeField.setPrefWidth(500); // change
        recipeField.setPrefHeight(200); // change
        recipeLabel.setStyle("-fx-font-size: 16"); // change
        recipeLabel.setTextFill(Color.web("#ADD8E6")); // change
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

    AppFrameMic() {
        Button backButton = new Button("Back");
        // backButton.setOnAction(e -> goBack());
        HBox backButtonContainer = new HBox(backButton);
        backButtonContainer.setPadding(new Insets(10)); // padding

        // Set the top of the BorderPane to contain the back button
        this.setTop(backButtonContainer);
        
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

    public void addListeners() {

        AudioRecorder recorder = new AudioRecorder(new Label("Recording..."));
        WhisperHandler audioProcessor = new WhisperHandler(API_ENDPOINT, TOKEN, MODEL);

        recordButton1.setOnAction(e -> {

            if (!recording) {
                recorder.startRecording();
                recording = true;

            } else {
                recorder.stopRecording();
                recording = false;
                try {
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

                primaryStage.setScene(details.getSceneWindow());
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

    NewRecipeUI() {
        root = new AppFrameMic();
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
}
