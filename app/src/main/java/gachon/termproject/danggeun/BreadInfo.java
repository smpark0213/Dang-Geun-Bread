package gachon.termproject.danggeun;

import java.util.Date;

public class BreadInfo {

    private String breadId;
    private String breadName;
    // int로 하니까 오류남
    private String price;
    private String storeID;
    private long count;
    private String photoURL;



    public BreadInfo (){ }

    public BreadInfo (String storeID, String breadId,String breadName,String price,long count, String photoURL){
        this.storeID=storeID;
        this.breadId=breadId;
        this.breadName = breadName;
        this.price = price;
        this.count=count;
        this.photoURL=photoURL;


    }

    public String getBreadId(){return breadId;}
    public String getStoreID() {return storeID; }
    public String getPhotoURL() {return photoURL; }
    public String getPrice() { return price; }
    public String getBreadName() { return breadName; }

    public  long getCount(){return count;}


    public void setBreadName(String breadName) {
        this.breadName = breadName;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setStoreID(String storeId) {
        this.storeID = storeId;
    }
    public void setCount(long count){this.count=count;}
    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }


}
