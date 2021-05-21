package gachon.termproject.danggeun;

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
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.util.ArrayList;
import java.util.Date;

import gachon.termproject.danggeun.Adapter.BreadAdpater;
import gachon.termproject.danggeun.Util.Firestore;

public class StoreActivity extends AppCompatActivity {
    private AppCompatButton btn_reserve;
    private AppCompatTextView store_name;
    private String sotreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btn_reserve = findViewById(R.id.btn_reserve);
        store_name = findViewById(R.id.store_name);
        ArrayList<BreadInfo> breadInfos = new ArrayList<BreadInfo>();
        Intent intent = getIntent();

        // intent null 체크
        if(!TextUtils.isEmpty(intent.getStringExtra("id"))){
            sotreId = intent.getStringExtra("id");
            store_name.setText(intent.getStringExtra("title"));
        }
        else {
            Toast.makeText(getApplicationContext(), "현재 가게에 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
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
                        String breadPrice = d.getData().get("price").toString();
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
    }
}