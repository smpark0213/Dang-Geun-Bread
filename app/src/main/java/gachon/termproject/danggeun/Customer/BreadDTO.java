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
        BreadName = breadName;
        BreadPrice = breadPrice;
        Count = count;
        TotalPrice = totalPrice;
    }

    public String getBreadName() {
        return BreadName;
    }

    public void setBreadName(String breadName) {
        BreadName = breadName;
    }

    public String getBreadPrice() {
        return BreadPrice;
    }

    public void setBreadPrice(String breadPrice) {
        BreadPrice = breadPrice;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }
}
