package th.book.texts.health.healthtextbooks.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KaowNeaw on 1/30/2016.
 */
public class Order implements Parcelable {

    private int orderId;
    private String personId;
    private String orderDate;
    private int orderStatus;
    private double totalPrice;

    public Order(int orderId, String personId, String orderDate, int orderStatus, double totalPrice) {
        this.orderId = orderId;
        this.personId = personId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderId);
        dest.writeString(this.personId);
        dest.writeString(this.orderDate);
        dest.writeInt(this.orderStatus);
        dest.writeDouble(this.totalPrice);
    }

    protected Order(Parcel in) {
        this.orderId = in.readInt();
        this.personId = in.readString();
        this.orderDate = in.readString();
        this.orderStatus = in.readInt();
        this.totalPrice = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
