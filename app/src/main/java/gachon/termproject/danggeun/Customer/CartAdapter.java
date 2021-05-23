package gachon.termproject.danggeun.Customer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gachon.termproject.danggeun.R;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private ArrayList<BreadDTO> breadList;

    // 생성자
    public CartAdapter(ArrayList<BreadDTO> breadList) {
        this.breadList = breadList;
    }

    // 리사이클러 뷰의 각 뷰에 들어갈 아이템들을 지정, 각 뷰는 뷰 홀더가 관여한다. 연결이 어댑터
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cart_bread_name;
        private TextView cart_bread_price;
        private TextView cart_total_price;
        private TextView cart_count;

        public MyViewHolder(View view) {
            super(view);
            this.cart_bread_name = view.findViewById(R.id.cartBreadName);
            this.cart_bread_price = view.findViewById(R.id.cartBreadPrice);
            this.cart_total_price = view.findViewById(R.id.cartTotalPrice);
            this.cart_count = view.findViewById(R.id.cartCount);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_bread_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.cart_bread_name.setText(breadList.get(position).getBreadName());
        holder.cart_bread_price.setText(breadList.get(position).getBreadPrice());
        holder.cart_total_price.setText(breadList.get(position).getTotalPrice());
        holder.cart_count.setText(breadList.get(position).getCount());


    }

    @Override
    public int getItemCount() {
        return breadList.size();
    }


}