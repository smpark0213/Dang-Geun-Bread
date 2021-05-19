package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.bundle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //빵 정보 번들로 보내기
                Bundle bundle = new Bundle();
                bundle.putString("store", "파리바게트 00점");
                bundle.putString("img", "Bread1");
                bundle.putString("name", "초코소라빵");
                bundle.putString("price", "1200");
                bundle.putString("maximum", "5");

                Intent intent = new Intent(MainActivity.this, Bread_Detail.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();


            }
        });
    }
}