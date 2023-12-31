package code.client.View;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import code.server.Recipe;

public class RecipeUI extends HBox {
    private Button mealType;
    private Button deleteButton, detailsButton;
    private Recipe recipe;

    RecipeUI(Recipe recipe) {
        // Index of the recipe in the recipe list
        HBox style = new HBox();
        mealType = new Button();
        mealType.setDisable(true);
        mealType.setPrefSize(250, 50);
        mealType.setAlignment(Pos.CENTER);
        // Button to enter detailed recipe display
        detailsButton = new Button();
        detailsButton.setPrefSize(400, 100);
        detailsButton.setAlignment(Pos.CENTER);
        detailsButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        // Button to delete unwanted recipes from the recipe list
        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(100, 100);
        deleteButton.setAlignment(Pos.CENTER);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        this.recipe = recipe;
        this.detailsButton.setText(recipe.getTitle());

        // Add all the elements to the recipe UI
        Button marginMaker = new Button();
        marginMaker.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        marginMaker.setDisable(true);
        style.getChildren().addAll(marginMaker, mealType, detailsButton, deleteButton);
        style.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        MealTagStyler.styleTags(recipe, mealType);
        this.getChildren().add(style);
        this.setPrefSize(50, 50);
        this.setMinSize(50, 50);
        this.setAlignment(Pos.CENTER);
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public Button getDetailsButton() {
        return this.detailsButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    public String getRecipeName() {
        return this.recipe.getTitle();
    }

}
