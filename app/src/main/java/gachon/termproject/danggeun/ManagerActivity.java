package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import gachon.termproject.danggeun.Util.Firestore;

public class ManagerActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        ImageView iv1, iv2, iv3, iv4;
        iv1 = findViewById(R.id.iv1); //매장 정보
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, StoreInfo.class);
                startActivity(intent);
            }
        });
        iv2 = findViewById(R.id.iv2); //메뉴 추가
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, MenuRegister.class);
                startActivity(intent);
            }
        });
        iv3 = findViewById(R.id.iv3); //주문 확인
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv4 = findViewById(R.id.iv4); //빵리스트
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //소비자 화면에서 뜨는 빵 리스트에서 예약하기 버튼?만 지우면 될듯
                Intent intent = new Intent(ManagerActivity.this, Bread_list_manager.class);
                startActivity(intent);

            }
        });

        Button btn = (Button) findViewById(R.id.userVersion); //사용자 버전으로 가기
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, CustomerActivity.class);
                startActivity(intent);
            }
        });



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
                fAuth=FirebaseAuth.getInstance();
                fAuth.signOut();
                Intent intent = new Intent(ManagerActivity.this, LoginActivity.class);
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
