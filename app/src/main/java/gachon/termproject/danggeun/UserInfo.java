package gachon.termproject.danggeun;

import java.util.ArrayList;
import java.util.HashMap;

public class UserInfo {

    public UserInfo(){}

    public static String BakeryName;
    public static String userId;
    public static String closeTime;
    private String openTime;
    private String location;
    public static String nickname;
    public static String profileImg;
    public static boolean isConsumer;
    private ArrayList<HashMap<String,Long>> reservation;

    public UserInfo(String userId,String closeTime, boolean isConsumer,String location,String nickname,String openTime,String profileImg,ArrayList<HashMap<String,Long>> reservation,String BakeryName){
        this.userId=userId;
        this.closeTime=closeTime;
        this.openTime=openTime;
        this.location=location;
        this.nickname=nickname;
        this.profileImg=profileImg;
        this.isConsumer=isConsumer;
        this.reservation=this.reservation;
        this.BakeryName=BakeryName;

    }

    public String getUserId() { return this.userId; }
    public String getNickname() { return this.nickname; }
    public String getProfileImg() { return this.profileImg; }
    public boolean getIsConsumer() { return  this.getIsConsumer(); }
    public String getCloseTime() { return this.closeTime; }
    public String getOpenTime() { return this.openTime; }
    public String getLocation() { return this.location; }
    public String getBakeryName(){return this.BakeryName; }


    public void setUserId(String userId) { this.userId = userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setIsConsumer(boolean isConsumer) { this.isConsumer = isConsumer; }
}
