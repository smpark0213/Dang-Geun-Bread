package gachon.termproject.danggeun.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import gachon.termproject.danggeun.BreadInfo;
import gachon.termproject.danggeun.Bread_list_manager;
import gachon.termproject.danggeun.MenuRegister;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.StoreRegister;
import gachon.termproject.danggeun.Util.Firestore;

import static gachon.termproject.danggeun.Util.others.isStorageUrl;

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.freeViewHolder>{

        //빵 리스트 데이터
        private ArrayList<BreadInfo> mDataset;
        private Activity activity;

        static class freeViewHolder extends RecyclerView.ViewHolder{
            public CardView cardView;
            freeViewHolder(Activity activity, CardView v, BreadInfo freePostInfo){
                super(v);
                cardView = v;
            }
        }

        public FirstAdapter(Activity activity, ArrayList<BreadInfo> freeDataset){
            mDataset = freeDataset;
            this.activity = activity;
        }

        //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
        @NotNull
        @Override
        public FirstAdapter.freeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
            CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_post, parent,false);
            final freeViewHolder freeViewHolder = new freeViewHolder(activity, cardView, mDataset.get(viewType));


            return freeViewHolder;
        }

        //카드뷰 안에 들어갈 목록
        //빵 사진, 빵 이름, 빵 개수,예약 마감 날짜,
        @Override
        public void onBindViewHolder(@NotNull final freeViewHolder holder, int position){
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            CardView cardView = holder.cardView;
            cardView.setLayoutParams(layoutParams);
            ImageView titleImage = cardView.findViewById(R.id.bread_img);
            String breadImagePath = mDataset.get(position).getPhotoURL();
            if(isStorageUrl(breadImagePath)){
                Glide.with(activity).load(breadImagePath).override(600).thumbnail(0.1f).into(titleImage);
            }
            TextView title = cardView.findViewById(R.id.bread_name);
            title.setText(mDataset.get(position).getBreadName());

            TextView userName = cardView.findViewById(R.id.price);
            userName.setText(mDataset.get(position).getPrice());

//            TextView createdAt = cardView.findViewById(R.id.bread_date);
//            createdAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(mDataset.get(position).getDate()));

            TextView recom = cardView.findViewById(R.id.count);
            recom.setText("개수 : " + (int) mDataset.get(position).getCount());
            //빵 매뉴 삭제
            //다이얼로그 띄우고 ok누르면 삭제
            String breadId= mDataset.get(position).getBreadId();
            Button deleteButton=cardView.findViewById(R.id.buttonDelete);
            FirebaseFirestore db=Firestore.getFirestoreInstance();
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                    alert.setTitle("정말 삭제하시겠습니까?");
                    alert.setMessage("Really want to delete?");

                    final EditText name = new EditText(activity.getApplicationContext());
                    alert.setView(name);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            db.collection("Bread").document(breadId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Log.v("삭제 성공","삭제 성공");
                                            Intent intent= new Intent(activity, Bread_list_manager.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            activity.startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.v("삭제 실패","삭제 성공");
//                                    Log.w(TAG, "Error deleting document", e);
                                        }
                                    });

                        }
                    });
                    alert.show();


                }
            });
        }

        @Override
        public int getItemCount(){ return mDataset.size(); }

}
