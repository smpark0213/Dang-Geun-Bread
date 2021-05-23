package gachon.termproject.danggeun.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.termproject.danggeun.Customer.StoreActivity;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Model.ReservationDto;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList<ReservationDto> request;

    public StoreAdapter(Activity activity, ArrayList<ReservationDto> request){
        this.activity = activity;
        this.request = request;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView shopName;
        AppCompatTextView time;
        AppCompatTextView status;

        AppCompatButton btn_item_store;
        AppCompatButton btn_item_detail;

        public ViewHolder(Activity activity, @NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.tx_item_store_name);
            time = itemView.findViewById(R.id.tx_item_store_time);
            status = itemView.findViewById(R.id.tx_item_store_status);
            btn_item_store = itemView.findViewById(R.id.btn_item_store);
            btn_item_detail = itemView.findViewById(R.id.btn_item_detail);

        }
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.items_shop, parent, false);
        final StoreAdapter.ViewHolder viewHolder = new StoreAdapter.ViewHolder(activity, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        ReservationDto currentR = request.get(position);

        holder.shopName.setText(currentR.getShopName());
        holder.time.setText(currentR.getTimestamp().toString());
        // 예약 확정
        if(currentR.isStatus()){
            holder.status.setText("예약 확정");
        } else{ // 예약 취소
            holder.status.setText("예약 취소");
        }
        holder.btn_item_store.setText("가게보기");
        holder.btn_item_detail.setText("예약확인");

            // 해당 가게로 넘어가기
        holder.btn_item_store.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                Intent intent=new Intent(context, StoreActivity.class);
                intent.putExtra("id",currentR.getShopId());
                intent.putExtra("title", currentR.getShopName());
                context.startActivity(intent);
            }
        });

            // 상세 화면으로 넘어가기
        holder.btn_item_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return request.size();
    }
}
