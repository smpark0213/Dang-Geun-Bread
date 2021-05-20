package gachon.termproject.danggeun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

import gachon.termproject.danggeun.Util.Firestore;
import gachon.termproject.danggeun.Util.Model.CartBread;

public class StoreActivity extends AppCompatActivity {
    private AppCompatButton btn_reserve;
    private AppCompatButton btn_cart;
    private AppCompatTextView store_name;
    private String sotreId;
    private String userId;
    private static ArrayList<CartBread> breadlist = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "onActivityResult");

        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(getApplicationContext(), "RESULT_OK", Toast.LENGTH_SHORT).show();

            Bundle bundle = data.getExtras();   // 장바구니에 넣은 빵 받을거임
            String breadName = bundle.getString("BreadName");
            int count = bundle.getInt("Count");
            int total = bundle.getInt("TotalPrice");

            Toast.makeText(getApplicationContext(), "받아온 빵" + breadName + "  "+count + "  "+total, Toast.LENGTH_SHORT).show();
            breadlist.add(new CartBread(breadName, count, total));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btn_reserve = findViewById(R.id.btn_reserve);
        store_name = findViewById(R.id.store_name);
        btn_cart = findViewById(R.id.btn_cart);
        ArrayList<BreadInfo> breadInfos = new ArrayList<BreadInfo>();
        Intent intent = getIntent();

        // intent null 체크
        if(!TextUtils.isEmpty(intent.getStringExtra("id"))){
            sotreId = intent.getStringExtra("id");  // 현재 가게 id
            store_name.setText(intent.getStringExtra("title")); // 현재 가게 이름
            userId = Firestore.getFirebaseUser().toString();   // 현재 로그인한 유져 id
        }
        else {
            Bundle bundle = intent.getExtras();   // 장바구니에 넣은 빵 받을거임
            String breadName = bundle.getString("BreadName");
            int count = bundle.getInt("Count");
            int total = bundle.getInt("TotalPrice");

            Toast.makeText(getApplicationContext(), "받아온 빵" + breadName + "  "+count + "  "+total, Toast.LENGTH_SHORT).show();
            breadlist.add(new CartBread(breadName, count, total));
            Toast.makeText(getApplicationContext(), "현재 가게에 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
            //finish();
        }

        // 현재 가게에서의 빵 list
        Firestore.getBreadList(sotreId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot d : task.getResult()){
                        Log.d(getLocalClassName(), "Success read bread list");
                        // 빵 이름, 빵 가격
                        String breadName = d.getData().get("breadName").toString();
                        String breadPrice = d.getData().get("breadPrice").toString();
                        String breadId = d.getId();

                        breadInfos.add(new BreadInfo(breadName, breadPrice, breadId));
                    }

                    // 리사이클러뷰에 linearlayout 객체 지정
                    RecyclerView recyclerView = findViewById(R.id.store_recycle);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // 리사이클러뷰에 adapter지정
                    BreadAdpater breadAdpater = new BreadAdpater(breadInfos, store_name.getText().toString());
                    recyclerView.setAdapter(breadAdpater);
                }
                else{
                    Log.d(getLocalClassName(), "Fail read bread list");
                    Toast.makeText(getApplicationContext(), "현재 가게에 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 장바구니로 가기 이벤트
        // 현재 로그인한 유저, 현재 storeId
        // 빵 list
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breadlist.size() < 1) {
                    Toast.makeText(getApplicationContext(), "장바구니가 비어있습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent toCart = new Intent(getApplicationContext(), Cart.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", intent.getStringExtra("title"));
                    bundle.putString("userId", userId);
                    bundle.putSerializable("bradList", breadlist);
                    toCart.putExtras(bundle);
                    startActivity(toCart);
                }
            }
        });

        // 찐 예약하기 이벤트
        // 장바구니의 있는 내용들 전부 Firestore Reservation으로
        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}