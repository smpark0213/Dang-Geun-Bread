package gachon.termproject.danggeun.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import gachon.termproject.danggeun.R;

public class Signup04Activity extends AppCompatActivity {
    public static String location = "";
    private Button nextButton;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup04_location);

        text = findViewById(R.id.signup04_edittext01);
        nextButton = findViewById(R.id.signup04_button01);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                location = text.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Signup06Activity.class);
                startActivity(intent);

            }
        });
    }
}