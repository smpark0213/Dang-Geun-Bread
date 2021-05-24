package gachon.termproject.danggeun.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import gachon.termproject.danggeun.Customer.ReservationDetailActivity;
import gachon.termproject.danggeun.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private Activity activity;
    private Context context;
    private ArrayList<ReservationDetailActivity.ReservationDetailDTO> breadlist;

    public DetailAdapter(Activity activity, ArrayList<ReservationDetailActivity.ReservationDetailDTO> breadlist){
        this.activity = activity;
        this.breadlist = breadlist;
    }

    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_shop_detail, parent, false);
        final DetailAdapter.ViewHolder viewHolder = new DetailAdapter.ViewHolder(activity, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder holder, int position) {
        ReservationDetailActivity.ReservationDetailDTO rT = breadlist.get(position);

        holder.menuName.setText(rT.getBreadName());
        holder.count.setText(rT.getCount() + " 개");
        holder.totalPrice.setText(rT.getTotalPrice() + " 원");
    }

    @Override
    public int getItemCount() {
        return breadlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView menuName;
        AppCompatTextView count;
        AppCompatTextView totalPrice;

        public ViewHolder(Activity activity, @NonNull View itemView) {
            super(itemView);

            menuName = itemView.findViewById(R.id.item_menu_name);
            count = itemView.findViewById(R.id.item_menu_count);
            totalPrice = itemView.findViewById(R.id.item_menu_price);
        }
    }
}
