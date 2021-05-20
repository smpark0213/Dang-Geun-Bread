package gachon.termproject.danggeun;

public class UserInfo {
    public static String userId;
    public static String nickname;
    public static String profileImg;
    public static boolean isConsumer;

    public String getUserId() { return this.userId; }
    public String getNickname() { return this.nickname; }
    public String getProfileImg() { return this.profileImg; }
    public boolean getIsConsumer() { return  this.getIsConsumer(); }
    public void setUserId(String userId) { this.userId = userId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }
    public void setIsConsumer(boolean isConsumer) { this.isConsumer = isConsumer; }
}
