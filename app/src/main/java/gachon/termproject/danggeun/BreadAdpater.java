package gachon.termproject.danggeun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.function.LongToDoubleFunction;

import gachon.termproject.danggeun.Util.Firestore;
import io.grpc.InternalNotifyOnServerBuild;

public class BreadAdpater extends RecyclerView.Adapter<BreadAdpater.ViewHolder> {
    private ArrayList<BreadInfo> breadData = null;
    private String storeName;
    private ArrayList<String> breadId = new ArrayList<>();
    private Context context;
    private AppCompatButton btn_bread_reservation;

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView bread_name;
        AppCompatTextView bread_price;

        ViewHolder(View itemView){
            super(itemView);
            bread_name = itemView.findViewById(R.id.bread_name);
            bread_price = itemView.findViewById(R.id.bread_price);
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
                        Firestore.getBreadInfo(breadId.get(pos)).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot d = task.getResult();
                                    bundle.putString("store", storeName);
                                    bundle.putString("img", d.getData().get("breadUrl").toString());
                                    bundle.putString("name", bread_name.getText().toString());
                                    bundle.putString("price", bread_price.getText().toString());
                                    bundle.putString("maximum", d.getData().get("breadLimit").toString());
                                }
                                else{
                                    Log.d(ViewHolder.this.getClass().toString(), "Fail to read breadInfo");
                                    Toast.makeText(context.getApplicationContext(), "빵 정보를 읽을 수 없습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        Intent intent = new Intent(context, Bread_Detail.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public BreadAdpater(ArrayList<BreadInfo> breadData,String storeName) {
        this.breadData = breadData;
        this.storeName = storeName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.bread_store, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreadAdpater.ViewHolder holder, int position) {

        BreadInfo currentBread = breadData.get(position);
        breadId.add(position,currentBread.getBreadId());

        holder.bread_name.setText(currentBread.getBreadName());
        holder.bread_price.setText(currentBread.getPrice());
    }

    @Override
    public int getItemCount() {
        return breadData.size();
    }
}
