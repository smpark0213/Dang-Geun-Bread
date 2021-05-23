package gachon.termproject.danggeun.Util.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

import gachon.termproject.danggeun.Customer.BreadDTO;

public class ReservatoinRequest {
    private String userId;
    private String storeId;
    // false = 예약 확인, true = 예약 취소
    private boolean status;
    // 시간
    private Timestamp timestamp;
    private ArrayList<BreadDTO> breadArrayList;

    public static class ReservationBread{
        private String breadId;
        private int count;
    }

    public ReservatoinRequest(String userId, String storeId, ArrayList<BreadDTO> breadArrayList, Timestamp timestamp){
        this.userId = userId;
        this.storeId = storeId;
        this.breadArrayList = breadArrayList;
        this.timestamp = timestamp;
        this.status = false;
    }

    public String getUserId() {
        return userId;
    }
    public String getStoreId() {
        return storeId;
    }

}
