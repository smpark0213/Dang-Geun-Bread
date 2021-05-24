package gachon.termproject.danggeun.Customer;

import java.io.Serializable;

public class BreadDTO implements Serializable {

    private String BreadName;
    private String BreadPrice;
    private String Count;
    private String TotalPrice;

    public BreadDTO() {
    }

    public BreadDTO(String breadName, String breadPrice, String count, String totalPrice) {
        this.BreadName = breadName;
        this.BreadPrice = breadPrice;
        this.Count = count;
        this.TotalPrice = totalPrice;
    }

    public String getBreadName() {
        return this.BreadName;
    }
    public String getCount() {
        return this.Count;
    }
    public String getTotalPrice() {
        return this.TotalPrice;
    }

    public void setBreadName(String breadName) {
        BreadName = breadName;
    }

    public String getBreadPrice() {
        return this.BreadPrice;
    }

    public void setBreadPrice(String breadPrice) {
        BreadPrice = breadPrice;
    }

    public void setCount(String count) {
        Count = count;
    }


    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }
}
