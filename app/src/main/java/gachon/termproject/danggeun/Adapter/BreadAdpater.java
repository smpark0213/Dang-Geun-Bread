package gachon.termproject.danggeun.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import gachon.termproject.danggeun.Util.Model.BreadInfo;
import gachon.termproject.danggeun.Customer.Bread_Detail;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firebase;

import static gachon.termproject.danggeun.Util.others.isStorageUrl;

public class BreadAdpater extends RecyclerView.Adapter<BreadAdpater.ViewHolder> {
    private ArrayList<BreadInfo> breadData = null;
    private String storeName;
    private ArrayList<String> breadId = new ArrayList<>();
    private Context context;
    private AppCompatButton btn_bread_reservation;
    private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView bread_name;
        AppCompatTextView bread_price;
        AppCompatImageView bread_img;

        ViewHolder(Activity activity, View itemView) {
            super(itemView);

            bread_name = itemView.findViewById(R.id.bread_name);
            bread_price = itemView.findViewById(R.id.bread_price);
            bread_img=itemView.findViewById(R.id.bread_img);
            btn_bread_reservation = itemView.findViewById(R.id.btn_bread_reservation);

            // 특정 빵 예약하기 버튼 이벤트
            // 여기서 번들로 Bread_Datail로 넘겨줌
            // 가게 이름, 빵 이름, 빵 이미지, 빵 가격, 빵 예약 가능 수량
            btn_bread_reservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Bundle bundle = new Bundle();
                        Log.d(BreadAdpater.this.getClass().toString(), "position = " + pos);
                        Firebase.getBreadInfo(breadId.get(pos)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot d = task.getResult();
                                    bundle.putString("store", d.getData().get("storeID").toString());
                                    bundle.putString("img", d.getData().get("photoURL").toString());
                                    bundle.putString("name", bread_name.getText().toString());
                                    bundle.putString("price", bread_price.getText().toString());
                                    bundle.putString("maximum", d.getData().get("count").toString());

                                    Intent intent = new Intent(context, Bread_Detail.class);
                                    intent.putExtras(bundle);
                                    //context.startActivity(intent);
                                    ((Activity)context).startActivityForResult(intent, 1000);
                                }
                                else{
                                    Log.d(ViewHolder.this.getClass().toString(), "Fail to read breadInfo");
                                    Toast.makeText(context.getApplicationContext(), "빵 정보를 읽을 수 없습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public BreadAdpater(Activity activity,ArrayList<BreadInfo> breadData, String storeName) {
        this.breadData = breadData;
        this.storeName = storeName;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.bread_store, parent, false);
        final ViewHolder viewHolder = new ViewHolder(activity, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        BreadInfo currentBread = breadData.get(position);
        breadId.add(position,currentBread.getBreadId());

        holder.bread_name.setText(currentBread.getBreadName());
        holder.bread_price.setText(String.valueOf(currentBread.getPrice()));
        String breadImagePath=currentBread.getPhotoURL();

        if(isStorageUrl(breadImagePath)){
            Glide.with(activity).load(breadImagePath).override(600).thumbnail(0.1f).into(holder.bread_img);
        }


    }

    @Override
    public int getItemCount() {
        return breadData.size();
    }
}