package code.client.Model;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;

import static com.mongodb.client.model.Filters.eq;
import java.util.List;
import java.util.ArrayList;

public class RecipeMongoDb implements IRecipeDb {
    private MongoCollection<Document> recipeDocumentCollection;

    public RecipeMongoDb(MongoCollection<Document> recipeDocumentCollection) {
        this.recipeDocumentCollection = recipeDocumentCollection;
    }

    private Recipe jsonToRecipe(Document recipeDocument) {
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeDocument.toJson(), Recipe.class);
        return recipe;
    }

    @Override
    public List<Recipe> getList() {
        Recipe recipe;
        Document recipeDocument;
        var recipeDocumentCursor = recipeDocumentCollection.find().cursor();
        List<Recipe> recipeList = new ArrayList<>();

        while (recipeDocumentCursor.hasNext()) {
            recipeDocument = recipeDocumentCursor.next();
            recipe = jsonToRecipe(recipeDocument);
            recipeList.add(recipe);
        }

        return recipeList;
    }

    @Override
    public boolean add(Recipe recipe) {
        Document recipeDocument = new Document("_id", new ObjectId(recipe.getId()));
        recipeDocument.append("title", recipe.getTitle())
                .append("ingredients", Lists.newArrayList(recipe.getIngredientIterator()))
                .append("instructions", Lists.newArrayList(recipe.getInstructionIterator()));
        recipeDocumentCollection.insertOne(recipeDocument);
        return true;
    }

    @Override
    public Recipe find(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        var recipeDocumentIter = recipeDocumentCollection.find(filter);
        Document recipeDocument = recipeDocumentIter.first();
        if (recipeDocument == null) {
            // Recipe does not exist
            return null;
        }
        return jsonToRecipe(recipeDocument);
    }

    @Override
    public boolean update(Recipe updatedRecipe) {
        Recipe oldRecipe = remove(updatedRecipe.getId());
        if (oldRecipe == null) {
            // Recipe does not exist
            return false;
        }
        add(updatedRecipe);
        return true;
    }

    @Override
    public Recipe remove(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        Document recipeDocument = recipeDocumentCollection.findOneAndDelete(filter);
        if (recipeDocument == null) {
            // Recipe does not exist
            return null;
        }
        return jsonToRecipe(recipeDocument);
    }

    @Override
    public void clear() {
        recipeDocumentCollection.drop();
    }

    @Override
    public int size() {
        return (int) recipeDocumentCollection.countDocuments();
    }
}
