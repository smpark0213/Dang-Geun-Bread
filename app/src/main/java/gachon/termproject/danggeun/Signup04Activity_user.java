package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signup04Activity_user extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup04_location_user);
        EditText text = findViewById(R.id.signup04_edittext01_user);

        Button nextButton = findViewById(R.id.signup04_button01_user);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = text.getText().toString();
                // 데이터베이스 가져오기
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                // 회원가입을 위한 전역 변수 가져오기
                String ID = Signup01Activity.identifier;
                String PW = Signup02Activity.password;
                String nickname = Signup03Activity.nickname;


                // 회원가입 프로세스
                fAuth.createUserWithEmailAndPassword(ID, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 유저 고유 아이디 생성 후 정보 저장할 데이터베이스 경로 생성
                            String userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            // 가입 유저 정보 맵에 모으기
                            // 필요한 정보 더 추가 가능
                            Map<String, Object> user = new HashMap<>();
                            user.put("ID", ID);
                            user.put("nickname", nickname);
                            user.put("isConsumer", true);
                            user.put("profileUrl", "None"); // 프로필 이미지 url
                            user.put("location", temp); // user위치


                            documentReference.set(user); // 데이터베이스에 정보 저장



                            startActivity(new Intent(getApplicationContext(), Signup05Activity.class));
                        } else {
                            Toast.makeText(Signup04Activity_user.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}