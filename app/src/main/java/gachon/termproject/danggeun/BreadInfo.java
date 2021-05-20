package gachon.termproject.danggeun;

public class BreadInfo {
    private String breadId;
    private String breadName;
    // int로 하니까 오류남
    private String price;

    public BreadInfo (){ }

    public BreadInfo (String breadName, String price, String breadId){
        this.breadName = breadName;
        this.price = price;
        this.breadId = breadId;
    }

    public String getBreadId() { return breadId; }
    public String getPrice() { return price; }
    public String getBreadName() { return breadName; }

    public void setBreadId(String breadId) { this.breadId = breadId; }
    public void setBreadName(String breadName) {
        this.breadName = breadName;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}
