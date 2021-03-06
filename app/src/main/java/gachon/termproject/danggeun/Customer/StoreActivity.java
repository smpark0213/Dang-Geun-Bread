package gachon.termproject.danggeun.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import gachon.termproject.danggeun.Adapter.BreadAdpater;
import gachon.termproject.danggeun.LoginActivity;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firebase;
import gachon.termproject.danggeun.Util.Model.BreadInfo;

public class StoreActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    private AppCompatButton btn_reserve;
    private AppCompatTextView store_name;
    private String storeId;
    SharedPreferences sp2;
    // 빵 리스트
    private static ArrayList<BreadDTO> breadList = new ArrayList<>();
    private static String cartStoreId = null;

    private void setCartStore(String sotreId){
        cartStoreId = sotreId;
    }

    public static String getCartStore(){
        return cartStoreId;
    }
    public static void clearCart(){
        breadList.clear();
    }
    public static boolean isCart(){
        return !(breadList.isEmpty() || breadList.size() == 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            BreadDTO returnBr = (BreadDTO) data.getSerializableExtra("toCart");
            Log.d("CART", "Bread name for cart : " + returnBr.getBreadName());
            breadList.add(returnBr);
            setCartStore(storeId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        store_name = findViewById(R.id.store_name);
        ArrayList<BreadInfo> breadInfos = new ArrayList<BreadInfo>();
        Intent intent = getIntent();

        sp2 = getSharedPreferences("sp2", MODE_PRIVATE);
        String textutils = sp2.getString("save2", "1");

        // intent null 체크
        // 무조건 storeId가 있어야 접근 가능함!
        if(!TextUtils.isEmpty(intent.getStringExtra("id"))){
            storeId = intent.getStringExtra("id");
            store_name.setText(intent.getStringExtra("title"));
        }
        else {
            Toast.makeText(getApplicationContext(), "현재 가게에 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        save(textutils);

        // 현재 가게에서의 빵 list
        Firebase.getBreadList(storeId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


        //장바구니 버튼
        Button btn_cart = (Button) findViewById(R.id.btn_cart);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCart = new Intent(StoreActivity.this, Cart.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("title", intent.getStringExtra("title"));
                toCart.putExtras(bundle2);
                toCart.putExtra("storeId", storeId);
                toCart.putExtra("breadList", breadList);
                startActivity(toCart);
                finish();
            }
        });
    }


    public void save(String textutils) {
        sp2 = getSharedPreferences("sp2", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp2.edit();
        editor.putString("save2", textutils);
        editor.commit();
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
            case R.id.action_logout :
                // TODO : process the click event for action_search item.
                fAuth= FirebaseAuth.getInstance();
                fAuth.signOut();
                Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true ;
            // ...
            // ...
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}