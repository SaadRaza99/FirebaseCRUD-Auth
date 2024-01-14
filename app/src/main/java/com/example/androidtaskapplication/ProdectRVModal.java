package com.example.androidtaskapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class ProdectRVModal implements Parcelable {
    // creating variables for our different fields.
    private String productName;



    private String productPrice;
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }




    // creating an empty constructor.
    public ProdectRVModal() {

    }

    protected ProdectRVModal(Parcel in) {
        productName = in.readString();
        productId = in.readString();
        productPrice = in.readString();
    }

    public static final Creator<ProdectRVModal> CREATOR = new Creator<ProdectRVModal>() {
        @Override
        public ProdectRVModal createFromParcel(Parcel in) {
            return new ProdectRVModal(in);
        }

        @Override
        public ProdectRVModal[] newArray(int size) {
            return new ProdectRVModal[size];
        }
    };

    // creating getter and setter methods.
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }





    public ProdectRVModal(String productId, String productName, String productPrice) {
        this.productName = productName;
        this.productId = productId;
        this.productPrice = productPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(productId);
        dest.writeString(productPrice);
    }
}
