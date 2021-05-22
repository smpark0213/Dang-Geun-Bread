package gachon.termproject.danggeun.Util.Model;

import java.io.Serializable;

public class CartBread implements Serializable {
    private String breadName;
    private int count;
    private int total;

    public CartBread (String breadName, int count, int total){
        this.breadName = breadName;
        this.count = count;
        this.total = total;
    }

    public void setBreadName(String breadName) { this.breadName = breadName; }
    public void setCount(int count) { this.count = count; }
    public void setTotal(int total) { this.total = total; }

    public int getCount() { return count; }
    public int getTotal() { return total; }
    public String getBreadName() { return breadName; }
}
