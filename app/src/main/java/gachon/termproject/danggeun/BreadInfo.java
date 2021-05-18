package gachon.termproject.danggeun;

public class BreadInfo {
    private int breadId;
    private String breadName;
    private int price;

    public BreadInfo (){ }

    public BreadInfo (String breadName, int price){
        this.breadName = breadName;
        this.price = price;
    }

    public int getBreadId() { return breadId; }
    public int getPrice() { return price; }
    public String getBreadName() { return breadName; }

    public void setBreadId(int breadId) { this.breadId = breadId; }
    public void setBreadName(String breadName) {
        this.breadName = breadName;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
