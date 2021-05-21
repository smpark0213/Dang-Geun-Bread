package gachon.termproject.danggeun.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import gachon.termproject.danggeun.BreadInfo;
import gachon.termproject.danggeun.R;

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
                Glide.with(activity).load(breadImagePath).override(500).thumbnail(0.1f).into(titleImage);
            }
            TextView title = cardView.findViewById(R.id.bread_name);
            title.setText(mDataset.get(position).getBreadName());

            TextView userName = cardView.findViewById(R.id.price);
            userName.setText(mDataset.get(position).getPrice());

//            TextView createdAt = cardView.findViewById(R.id.bread_date);
//            createdAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(mDataset.get(position).getDate()));

            TextView recom = cardView.findViewById(R.id.count);
            recom.setText("개수 : " + (int) mDataset.get(position).getCount());

        }

        @Override
        public int getItemCount(){ return mDataset.size(); }

}
