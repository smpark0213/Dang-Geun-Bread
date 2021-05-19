package gachon.termproject.danggeun;

public class BreadInfo {
    private int breadId;
    private String breadName;
    // int로 하니까 오류남
    private String price;

    public BreadInfo (){ }

    public BreadInfo (String breadName, String price){
        this.breadName = breadName;
        this.price = price;
    }

    public int getBreadId() { return breadId; }
    public String getPrice() { return price; }
    public String getBreadName() { return breadName; }

    public void setBreadId(int breadId) { this.breadId = breadId; }
    public void setBreadName(String breadName) {
        this.breadName = breadName;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}
