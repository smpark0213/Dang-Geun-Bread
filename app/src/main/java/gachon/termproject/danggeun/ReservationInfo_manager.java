package gachon.termproject.danggeun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import gachon.termproject.danggeun.Adapter.VerticalAdapter;
import gachon.termproject.danggeun.Customer.BreadDTO;
import gachon.termproject.danggeun.Util.Firebase;
import gachon.termproject.danggeun.Util.Model.ReservatoinRequest;

public class ReservationInfo_manager extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth fAuth;
    private TextView textView;
    private String sotreId;
    private String username;
    private String Time;
    SharedPreferences sp2;
    // 빵 리스트




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rerservationinfo_manager);

//        textView = findViewById(R.id.reservationInfoTextView);

        ArrayList<ReservatoinRequest> reservationInfo_list = new ArrayList<ReservatoinRequest>();
        user=Firebase.getFirebaseUser();
        String storeId=user.getUid();
        // 현재 가게에서의 빵 list
        Firebase.getReservation(storeId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot d : task.getResult()) {
                        Log.d(getLocalClassName(), "Success read Reservation list");
                        // 빵 이름, 빵 가격
                        ArrayList<BreadDTO> breadList = new ArrayList<>();
                        ArrayList<HashMap<String,String>> breadListA = new ArrayList<>();
                        breadListA=(ArrayList<HashMap<String,String>>)d.getData().get("breadArrayList");
                        Timestamp timestamp=(Timestamp) d.getData().get("timestamp");
                        String userId=d.getData().get("userId").toString();
                        String storeId=d.getData().get("storeId").toString();
                        Boolean status=(Boolean) d.getData().get("status");
                        String storeName=d.getData().get("storeName").toString();

                        for(int i=0;i<breadListA.size();i++) {
                            String breadName = breadListA.get(i).get("breadName");
                            String BreadPrice=breadListA.get(i).get("breadPrice");
                            String Count = breadListA.get(i).get("count");
                            String TotalPrice=breadListA.get(i).get("totalPrice");
                            BreadDTO bread=new BreadDTO(breadName,BreadPrice,Count,TotalPrice);
                            breadList.add(bread);
                        }

                        reservationInfo_list.add(new ReservatoinRequest(userId,storeId,breadList,timestamp,storeName));
                    }

                    // 리사이클러뷰에 linearlayout 객체 지정
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewVertical);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // 리사이클러뷰에 adapter지정
                    VerticalAdapter verticalAdapter = new VerticalAdapter(ReservationInfo_manager.this, reservationInfo_list);
                    recyclerView.setAdapter(verticalAdapter);
                } else {
                    Log.d(getLocalClassName(), "Fail read bread list");
                    Toast.makeText(getApplicationContext(), "현재 가게에 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //예약자 이름 가져오기
    public void setUserName(String username){
        this.username=username;
    }


    //로그아웃 버튼 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //로그아웃 구현
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // TODO : process the click event for action_search item.
                fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                Intent intent = new Intent(ReservationInfo_manager.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            // ...
            // ...
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
