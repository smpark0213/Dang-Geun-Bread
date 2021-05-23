package gachon.termproject.danggeun.Util.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

import gachon.termproject.danggeun.Customer.BreadDTO;

public class ReservatoinRequest {
    private String userId;
    private String storeId;
    // false = 예약 취소, true = 예약 됨
    private boolean status;
    // 시간
    private com.google.firebase.Timestamp timestamp;
    private ArrayList<BreadDTO> breadArrayList;
    private String storeName;

    public ReservatoinRequest() { }
    public ReservatoinRequest(String userId, String storeId, ArrayList<BreadDTO> breadArrayList, Timestamp timestamp, String storeName){
        this.userId = userId;
        this.storeId = storeId;
        this.status = true;
        this.timestamp = timestamp;
        this.breadArrayList = breadArrayList;
        this.storeName = storeName;
    }

    public ReservatoinRequest(String userId, String storeId, ArrayList<BreadDTO> breadArrayList, Timestamp timestamp, Boolean status){
        this.userId = userId;
        this.storeId = storeId;
        this.status = status;
        this.timestamp = timestamp;
        this.breadArrayList = breadArrayList;
    }


    public String getUserId() {
        return userId;
    }
    public String getStoreId() {
        return storeId;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public ArrayList<BreadDTO> getBreadArrayList() {
        return breadArrayList;
    }
    public String getStoreName() { return storeName; }

    public boolean isStatus() {
        return status;
    }
}
