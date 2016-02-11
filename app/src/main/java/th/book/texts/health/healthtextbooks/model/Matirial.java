package th.book.texts.health.healthtextbooks.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KaowNeaw on 1/27/2016.
 */
public class Matirial implements Parcelable {

    private int matId;
    private String matName;
    private int matTypeId;
    private float calorie;
    private int unitId;
    private int expireDay;
    private String matDesc;
    private String img;
    private double price;
    private double amount;

    public Matirial(int matId, String matName, int matTypeId, float calorie, int unitId, int expireDay, String matDesc, String img, double price, double amount) {
        this.matId = matId;
        this.matName = matName;
        this.matTypeId = matTypeId;
        this.calorie = calorie;
        this.unitId = unitId;
        this.expireDay = expireDay;
        this.matDesc = matDesc;
        this.img = img;
        this.price = price;
        this.amount = amount;
    }

    public int getMatId() {
        return matId;
    }

    public void setMatId(int matId) {
        this.matId = matId;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public int getMatTypeId() {
        return matTypeId;
    }

    public void setMatTypeId(int matTypeId) {
        this.matTypeId = matTypeId;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(int expireDay) {
        this.expireDay = expireDay;
    }

    public String getMatDesc() {
        return matDesc;
    }

    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.matId);
        dest.writeString(this.matName);
        dest.writeInt(this.matTypeId);
        dest.writeFloat(this.calorie);
        dest.writeInt(this.unitId);
        dest.writeInt(this.expireDay);
        dest.writeString(this.matDesc);
        dest.writeString(this.img);
        dest.writeDouble(this.price);
        dest.writeDouble(this.amount);
    }

    protected Matirial(Parcel in) {
        this.matId = in.readInt();
        this.matName = in.readString();
        this.matTypeId = in.readInt();
        this.calorie = in.readFloat();
        this.unitId = in.readInt();
        this.expireDay = in.readInt();
        this.matDesc = in.readString();
        this.img = in.readString();
        this.price = in.readDouble();
        this.amount = in.readDouble();
    }

    public static final Creator<Matirial> CREATOR = new Creator<Matirial>() {
        public Matirial createFromParcel(Parcel source) {
            return new Matirial(source);
        }

        public Matirial[] newArray(int size) {
            return new Matirial[size];
        }
    };
}

