package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerActivity extends AppCompatActivity {

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
}
