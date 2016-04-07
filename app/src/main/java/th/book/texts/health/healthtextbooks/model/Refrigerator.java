package th.book.texts.health.healthtextbooks.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KaowNeaw on 1/24/2016.
 */
public class Refrigerator implements Parcelable {

    private int reId;
    private String personId;
    private int matId;
    private float amount;
    private String dateRecive;
    private String matName;
    private int matTypeId;
    private float calorie;
    private int unitId;
    private int expireDay;
    private String matDesc;
    private  int expireIn;
    private String img;

    public Refrigerator(int reId, String personId, int matId, float amount, String dateRecive, String matName, int matTypeId, float calorie, int unitId, int expireDay, String matDesc, int expireIn, String img) {
        this.reId = reId;
        this.personId = personId;
        this.matId = matId;
        this.amount = amount;
        this.dateRecive = dateRecive;
        this.matName = matName;
        this.matTypeId = matTypeId;
        this.calorie = calorie;
        this.unitId = unitId;
        this.expireDay = expireDay;
        this.matDesc = matDesc;
        this.expireIn = expireIn;
        this.img = img;
    }

    public int getReId() {
        return reId;
    }

    public void setReId(int reId) {
        this.reId = reId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    public String getDateRecive() {
        return dateRecive;
    }

    public void setDateRecive(String dateRecive) {
        this.dateRecive = dateRecive;
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

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.reId);
        dest.writeString(this.personId);
        dest.writeInt(this.matId);
        dest.writeFloat(this.amount);
        dest.writeString(this.dateRecive);
        dest.writeString(this.matName);
        dest.writeInt(this.matTypeId);
        dest.writeFloat(this.calorie);
        dest.writeInt(this.unitId);
        dest.writeInt(this.expireDay);
        dest.writeString(this.matDesc);
        dest.writeInt(this.expireIn);
        dest.writeString(this.img);
    }

    protected Refrigerator(Parcel in) {
        this.reId = in.readInt();
        this.personId = in.readString();
        this.matId = in.readInt();
        this.amount = in.readFloat();
        this.dateRecive = in.readString();
        this.matName = in.readString();
        this.matTypeId = in.readInt();
        this.calorie = in.readFloat();
        this.unitId = in.readInt();
        this.expireDay = in.readInt();
        this.matDesc = in.readString();
        this.expireIn = in.readInt();
        this.img = in.readString();
    }

    public static final Creator<Refrigerator> CREATOR = new Creator<Refrigerator>() {
        public Refrigerator createFromParcel(Parcel source) {
            return new Refrigerator(source);
        }

        public Refrigerator[] newArray(int size) {
            return new Refrigerator[size];
        }
    };
}
