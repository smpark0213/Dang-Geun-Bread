package gachon.termproject.danggeun.Util.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

import gachon.termproject.danggeun.Customer.BreadDTO;

public class ReservationDto {
    //  가게 id, 가게 이름, 예약 상태, 예약 시간, reservationId, 빵 리스트
    private String shopId;
    private String shopName;
    private Timestamp timestamp;
    private boolean status;
    private String reId;
    private ArrayList<BreadDTO> breadList;

    public ReservationDto(){ }
    public ReservationDto(String shopId, String shopName, boolean status, Timestamp timestamp, String reId, ArrayList<BreadDTO> breadList){
        this.shopId = shopId;
        this.shopName = shopName;
        this.status = status;
        this.timestamp = timestamp;
        this.reId = reId;
        this.breadList = breadList;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public ArrayList<BreadDTO> getBreadList() {
        return breadList;
    }
    public String getReId() {
        return reId;
    }
    public String getShopId() {
        return shopId;
    }
    public String getShopName() {
        return shopName;
    }
    public boolean isStatus() {
        return status;
    }
}
