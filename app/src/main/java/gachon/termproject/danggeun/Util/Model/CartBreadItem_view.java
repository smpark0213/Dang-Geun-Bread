package gachon.termproject.danggeun.Util.Model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import gachon.termproject.danggeun.R;


public class CartBreadItem_view extends LinearLayout {

    TextView textView1, textView2, textView3, textView4;

    // Generate > Constructor

    public CartBreadItem_view(Context context) {
        super(context);

        init(context);
    }

    public CartBreadItem_view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // cart_bread_item.xml을 inflation
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cart_bread_item, this, true);

        textView1 = (TextView) findViewById(R.id.cartBreadName);
        textView2 = (TextView) findViewById(R.id.cartBreadPrice);
        textView3 = (TextView) findViewById(R.id.cartTotalPrice);
        textView4 = (TextView) findViewById(R.id.cartCount);

    }

    public void setCartBreadName(String name) {
        textView1.setText(name);
    }
    public void setCartBreadPrice(String price) {
        textView2.setText(price);
    }
    public void setCartTotalPrice(String totalPrice) {
        textView3.setText(totalPrice);
    }
    public void setCartCount(String count) {
        textView4.setText(count);
    }
}
