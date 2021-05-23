package gachon.termproject.danggeun.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import gachon.termproject.danggeun.Adapter.StoreAdapter;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firebase;
import gachon.termproject.danggeun.Util.Model.ReservationDto;
import gachon.termproject.danggeun.Util.Model.ReservatoinRequest;

public class ReservationActivity extends AppCompatActivity {

    private ArrayList<ReservationDto> shopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        //  가게 id, 가게 이름, 예약 상태, 예약 시간, reservationId, 빵 리스트
        String userId = Firebase.getFirebaseUser().getUid();
        Firebase.getReservationShopList(userId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot d : task.getResult()) {
                        //d.getId(); // ReservationId
                        Log.d(getLocalClassName(), "들어왔음");
                        ReservatoinRequest r = d.toObject(ReservatoinRequest.class);
                        ReservationDto re = new ReservationDto(r.getStoreId(), r.getStoreName(),
                                r.isStatus(), r.getTimestamp(), d.getId(), r.getBreadArrayList());

                        Log.d(getLocalClassName(), re.getShopName() + re.getReId() + re.getReId());
                        shopList.add(re);
                        }

                        RecyclerView recyclerView = findViewById(R.id.reservation_shop_recycle);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        StoreAdapter storeAdapter = new StoreAdapter(ReservationActivity.this, shopList);
                        recyclerView.setAdapter(storeAdapter);
                    }
                else{
                    Log.d(getLocalClassName(), "Fail to read reservation shop list");
                    Toast.makeText(getApplicationContext(),"예약 정보 받오오기 실패.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}