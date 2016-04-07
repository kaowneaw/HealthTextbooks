package th.book.texts.health.healthtextbooks.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KaowNeaw on 1/17/2016.
 */
public class Person implements Parcelable {
    private String personId;
    private String personName;
    private String personNameSend;
    private String personAddress;
    private String personTel;
    private float personWeight;
    private float personHeight;
    private String personBirth;
    private String personEmail;
    private String username;
    private String password;
    private String personImg;

    public String getPersonNameSend() {
        return personNameSend;
    }

    public void setPersonNameSend(String personNameSend) {
        this.personNameSend = personNameSend;
    }

    public Person(String personId, String personName, String personAddress, String personTel, float personWeight, float personHeight, String personBirth, String personEmail, String username, String password, String personImg) {
        this.personId = personId;
        this.personName = personName;
        this.personAddress = personAddress;
        this.personTel = personTel;
        this.personWeight = personWeight;
        this.personHeight = personHeight;
        this.personBirth = personBirth;
        this.personEmail = personEmail;
        this.username = username;
        this.password = password;
        this.personImg = personImg;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getPersonTel() {
        return personTel;
    }

    public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }

    public float getPersonWeight() {
        return personWeight;
    }

    public void setPersonWeight(float personWeight) {
        this.personWeight = personWeight;
    }

    public float getPersonHeight() {
        return personHeight;
    }

    public void setPersonHeight(float personHeight) {
        this.personHeight = personHeight;
    }

    public String getPersonBirth() {
        return personBirth;
    }

    public void setPersonBirth(String personBirth) {
        this.personBirth = personBirth;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonImg() {
        return personImg;
    }

    public void setPersonImg(String personImg) {
        this.personImg = personImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.personId);
        dest.writeString(this.personName);
        dest.writeString(this.personAddress);
        dest.writeString(this.personTel);
        dest.writeFloat(this.personWeight);
        dest.writeFloat(this.personHeight);
        dest.writeString(this.personBirth);
        dest.writeString(this.personEmail);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.personImg);
    }

    protected Person(Parcel in) {
        this.personId = in.readString();
        this.personName = in.readString();
        this.personAddress = in.readString();
        this.personTel = in.readString();
        this.personWeight = in.readFloat();
        this.personHeight = in.readFloat();
        this.personBirth = in.readString();
        this.personEmail = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.personImg = in.readString();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
