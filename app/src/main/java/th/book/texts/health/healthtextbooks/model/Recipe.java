package th.book.texts.health.healthtextbooks.model;

/**
 * Created by KaowNeaw on 2/18/2016.
 */
public class Recipe {

    int recipeId;
    String recipeName;
    String recipeDesc;
    String recipeDate;

    public Recipe(int recipeId, String recipeName, String recipeDesc, String recipeDate) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeDesc = recipeDesc;
        this.recipeDate = recipeDate;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

    public void setRecipeDesc(String recipeDesc) {
        this.recipeDesc = recipeDesc;
    }

    public String getRecipeDate() {
        return recipeDate;
    }

    public void setRecipeDate(String recipeDate) {
        this.recipeDate = recipeDate;
    }
}
