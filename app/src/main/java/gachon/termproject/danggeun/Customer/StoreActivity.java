package gachon.termproject.danggeun.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import gachon.termproject.danggeun.Adapter.BreadAdpater;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firestore;
import gachon.termproject.danggeun.Util.Model.BreadInfo;
import gachon.termproject.danggeun.Util.Model.CartBread;

public class StoreActivity extends AppCompatActivity {
    private AppCompatButton btn_cart;
    private AppCompatTextView store_name;

    private String storeId; // 현재 들어온 가게 id
    private String userId;  // 현재 로그인한 유저 id
    private static ArrayList<CartBread> breadlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        store_name = findViewById(R.id.store_name);
        btn_cart = findViewById(R.id.btn_cart);
        ArrayList<BreadInfo> breadInfos = new ArrayList<BreadInfo>();
        Intent intent = getIntent();

        // intent null 체크
        if(!TextUtils.isEmpty(intent.getStringExtra("id"))){
            storeId = intent.getStringExtra("id"); // 현재 가게 id
            store_name.setText(intent.getStringExtra("title")); // 현재 가게 이름
            userId = Firestore.getFirebaseUser().toString();   // 현재 로그인한 유저 id
        }
        else {
            Toast.makeText(getApplicationContext(), "현재 가게에 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        // 현재 가게에서의 빵 list
        Firestore.getBreadList(storeId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot d : task.getResult()){
                        Log.d(getLocalClassName(), "Success read bread list");
                        // 빵 이름, 빵 가격
                        String breadName = d.getData().get("breadName").toString();
                        Long breadPrice = Long.parseLong(d.getData().get("price").toString());
                        String breadId = d.getId();
                        String storeID= d.getData().get("storeID").toString();
                        String photoUrl=d.getData().get("photoURL").toString();
                        Long count=Long.parseLong(d.getData().get("count").toString());

                        breadInfos.add(new BreadInfo(storeID,breadId,breadName,breadPrice,count,photoUrl));
                    }

                    // 리사이클러뷰에 linearlayout 객체 지정
                    RecyclerView recyclerView = findViewById(R.id.store_recycle);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // 리사이클러뷰에 adapter지정
                    BreadAdpater breadAdpater = new BreadAdpater(StoreActivity.this,breadInfos, store_name.getText().toString());
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
                    //객체? 번들로 넘기는거 구현하기 (가게 이름 같이 넘겨줘야함)

                    Bundle bundle = new Bundle();
                    bundle.putString("title", intent.getStringExtra("title"));
                    bundle.putString("storeId", storeId);
                    bundle.putString("userId", userId);
                    bundle.putSerializable("bradList", breadlist);
                    toCart.putExtras(bundle);
                    startActivity(toCart);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(resultCode == RESULT_OK){
                Log.d(getLocalClassName(), "onActivityResult RESULT OK");
                String breadName = data.getStringExtra("BreadName");
                // 값 없으면, 1로 디폴트
                int count = data.getIntExtra("Count", 1);
                int totalPrice = data.getIntExtra("TotalPrice",1);
                Toast.makeText(getApplicationContext(), breadName+" "+count+"개"+totalPrice+"원", Toast.LENGTH_SHORT).show();

                // static 빵 list로 추가
                breadlist.add(new CartBread(breadName, count, totalPrice));
            }
            else{
                Log.d(getLocalClassName(), "onActivityResult RESULT NOT OK");
            }
        }
        else{
            Log.d(getLocalClassName(), "onActivityResult INTENT IS NULL");
        }
    }
}