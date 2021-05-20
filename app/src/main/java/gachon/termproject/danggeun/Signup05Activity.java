package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import gachon.termproject.danggeun.LoginActivity;
import gachon.termproject.danggeun.R;

public class Signup05Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup05_finishpublic);

        Button nextButton = findViewById(R.id.signup05_button01);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish(); // 자신을 죽이고 로그인 페이지로 이동

            }
        });

    }
}