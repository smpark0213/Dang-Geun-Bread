package gachon.termproject.danggeun.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gachon.termproject.danggeun.Customer.ReservationDetailActivity;
import gachon.termproject.danggeun.Customer.StoreActivity;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firebase;
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
        AppCompatButton btn_item_cancel;

        public ViewHolder(Activity activity, @NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.tx_item_store_name);
            time = itemView.findViewById(R.id.tx_item_store_time);
            status = itemView.findViewById(R.id.tx_item_store_status);
            btn_item_store = itemView.findViewById(R.id.btn_item_store);
            btn_item_detail = itemView.findViewById(R.id.btn_item_detail);
            btn_item_cancel = itemView.findViewById(R.id.btn_item_cancel);
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


        Date d = new Date(String.valueOf(currentR.getTimestamp().toDate()));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rT = format.format(d);

        holder.shopName.setText(currentR.getShopName());
        holder.time.setText("???????????? : " + rT);    // Timestramp to String

        // ?????? ??????
        if(currentR.isStatus()){
            holder.status.setText("?????? ??????");
        } else{ // ?????? ??????
            holder.status.setText("?????? ??????");
            holder.status.setTextColor(0xAAef484a); // ?????? ??????
        }
        holder.btn_item_store.setText("????????????");
        holder.btn_item_detail.setText("????????????");
        holder.btn_item_cancel.setText("????????????");

        // ?????? ????????? ????????????
        holder.btn_item_store.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                Intent intent=new Intent(context, StoreActivity.class);
                intent.putExtra("id",currentR.getShopId());
                intent.putExtra("title", currentR.getShopName());
                context.startActivity(intent);
            }
        });

        // ?????? ???????????? ????????????
        holder.btn_item_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentR.isStatus()){
                    Intent intent = new Intent(context, ReservationDetailActivity.class);
                    intent.putExtra("storeName", currentR.getShopName());   // ?????? ??????
                    intent.putExtra("breadList", currentR.getBreadList());  // ??? ?????????
                    intent.putExtra("time", rT);    // ?????? ??????
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context.getApplicationContext(), "?????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ?????? ?????? ??????
        holder.btn_item_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // status == true??? ????????????
                if(currentR.isStatus()){
                    Firebase.updateReservationStatus(currentR.getReId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("StoreActivity", "Success");
                                Toast.makeText(context.getApplicationContext(), "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context.getApplicationContext(), "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                // ????????? ?????? ?????????
                else{
                    Toast.makeText(context.getApplicationContext(), "?????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return request.size();
    }
}
