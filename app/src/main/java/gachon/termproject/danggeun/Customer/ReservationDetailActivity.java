package gachon.termproject.danggeun.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;

import gachon.termproject.danggeun.Adapter.DetailAdapter;
import gachon.termproject.danggeun.R;

public class ReservationDetailActivity extends AppCompatActivity {

    private ArrayList<ReservationDetailDTO> breadDetail = new ArrayList<>();
    private int total = 0;

    public class ReservationDetailDTO{
        private String breadName;
        private int count;
        private int totalPrice;
        public ReservationDetailDTO(String breadName, int count, int totalPrice){
            this.breadName = breadName;
            this.count = count;
            this.totalPrice = totalPrice;
        }

        public int getCount() {
            return count;
        }
        public int getTotalPrice() {
            return totalPrice;
        }
        public String getBreadName() {
            return breadName;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        Intent intent = getIntent();
        String storeName;
        String time;
        ArrayList<BreadDTO> breadList = null;
        AppCompatTextView realTotal = findViewById(R.id.tx_total_price);
        AppCompatTextView tx_store_name = findViewById(R.id.tx_detail_store_name);
        AppCompatTextView reservationTime = findViewById(R.id.tx_detail_time);

        if(!TextUtils.isEmpty(intent.getStringExtra("storeName"))){
            storeName = intent.getStringExtra("storeName");
            breadList = (ArrayList<BreadDTO>) intent.getSerializableExtra("breadList");
            time = intent.getStringExtra("time");

            if(breadList.size() == 0 || breadList.isEmpty()){
                Toast.makeText(getApplicationContext(), "예약한 빵이 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            for(BreadDTO br : breadList){
                breadDetail.add(new ReservationDetailDTO(br.getBreadName(), Integer.parseInt(br.getCount()), Integer.parseInt(br.getTotalPrice())));
                total += Integer.parseInt(br.getTotalPrice());
            }

            RecyclerView recyclerView = findViewById(R.id.reservation_shop_detail_recycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            DetailAdapter detailAdapter = new DetailAdapter(this, breadDetail);
            recyclerView.setAdapter(detailAdapter);

            tx_store_name.setText(storeName);
            realTotal.setText(total + "원");
            reservationTime.setText("예약시간 : " + time);
        }
        else{
            Toast.makeText(getApplicationContext(), "예약 상세 화면 보기 실패", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}