package th.book.texts.health.healthtextbooks.model;

/**
 * Created by KaowNeaw on 2/18/2016.
 */
public class ReciveDetail extends Recipe {

    int reciveMatId;
    int matId;
    float amount;
    String matName;
    String img;
    String matDesc;

    public ReciveDetail(int recipeId, String recipeName, String recipeDesc, String recipeDate, int reciveMatId, int matId, float amount, String matName, String img, String matDesc) {
        super(recipeId, recipeName, recipeDesc, recipeDate);
        this.reciveMatId = reciveMatId;
        this.matId = matId;
        this.amount = amount;
        this.matName = matName;
        this.img = img;
        this.matDesc = matDesc;
    }


    public int getReciveMatId() {
        return reciveMatId;
    }

    public void setReciveMatId(int reciveMatId) {
        this.reciveMatId = reciveMatId;
    }

    public int getMatId() {
        return matId;
    }

    public void setMatId(int matId) {
        this.matId = matId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMatDesc() {
        return matDesc;
    }

    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }
}
