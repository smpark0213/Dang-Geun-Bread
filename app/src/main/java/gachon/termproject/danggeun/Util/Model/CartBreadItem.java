package gachon.termproject.danggeun.Util.Model;

public class CartBreadItem {
    String cartBreadName;
    String cartBreadPrice;
    String cartTotalPrice;
    int cartCount;

    // Generate > Constructor
    public CartBreadItem(String name, String price, String totalPrice, int count) {
        this.cartBreadName = name;
        this.cartBreadPrice = price;
        this.cartTotalPrice = totalPrice;
        this.cartCount = count;
    }

    // Generate > Getter and Setter
    public String getCartBreadName() {
        return cartBreadName;
    }
    public void setCartBreadName(String name) {
        this.cartBreadName = name;
    }

    public String getCartBreadPrice() {
        return cartBreadPrice;
    }
    public void setCartBreadPrice(String price) {
        this.cartBreadPrice = price;
    }

    public String getCartTotalPrice() {
        return cartTotalPrice;
    }
    public void setCartTotalPrice(String totalPrice) {
        this.cartTotalPrice = totalPrice;
    }

    public int getCartCount() {
        return cartCount;
    }
    public void setCartCount(int count) {
        this.cartCount = count;
    }



}
