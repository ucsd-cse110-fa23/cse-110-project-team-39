package code.client.Controllers;

import code.server.Recipe;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Format {
    public String buildPrompt(String mealType, String ingredients) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("I am a student on a budget with a busy schedule and I need to quickly cook a ")
                .append(mealType)
                .append(". ")
                .append(ingredients)
                .append(" Make a recipe using only these ingredients plus condiments. ")
                .append("Please give me a recipe in the following format with no comments after the instructions. Title: Ingredients: Instructions:");
        return prompt.toString();
    }

    public Recipe mapResponseToRecipe(String mealType, String responseText) {
        // Split the tokens into lines
        String[] tokenArr = responseText.split("\n");
        List<String> tokenList = new ArrayList<>(Arrays.asList(tokenArr));
        int i;

        // Remove empty tokens
        for (i = 0; i < tokenList.size();) {
            if (tokenList.get(i).isBlank()) {
                tokenList.remove(i);
            } else {
                ++i;
            }
        }

        // Create a new recipe with a title
        String title = tokenList.get(0);
        if (title.contains("Title:")) {
            title = title.replaceAll("Title:", ""); 
        } 
        Recipe recipe = new Recipe(title.trim(), mealType);
        Date now = new Date();
        recipe.setDate(now.getTime());
        // Parse recipe's ingredients
        String ingredient;
        boolean parse = false;
        for (i = 0; !tokenList.get(i).contains("Instructions"); ++i) {
            ingredient = tokenList.get(i).trim();
            if (ingredient.contains("Ingredients")) {
                parse = true;
            } else if (parse) {
                ingredient = removeDashFromIngredient(tokenList.get(i).trim());
                recipe.addIngredient(ingredient);
            }
        }

        // Parse recipe's instructions
        String instruction;
        for (i += 1; i < tokenList.size(); ++i) {
            instruction = removeNumberFromInstruction(tokenList.get(i).trim());
            recipe.addInstruction(instruction);
        }

        return recipe;
    }

    private String removeDashFromIngredient(String ingredient) {
        if (ingredient.charAt(0) == ('-')) {
            return ingredient.substring(1).trim();
        }
        return ingredient.substring(2);
    }

    private String removeNumberFromInstruction(String instruction) {
        StringBuilder strBuilder = new StringBuilder();
        int i;

        // Ignore characters until '.'
        for (i = 0; i < instruction.length() && (instruction.charAt(i) != '.'); ++i)
            ;

        // Ignore '.' and ' ' after
        // Get all characters until end of string
        for (i += 2; i < instruction.length(); ++i) {
            strBuilder.append(instruction.charAt(i));
        }

        return strBuilder.toString();
    }
}
