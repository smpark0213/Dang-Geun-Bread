package gachon.termproject.danggeun.Signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import gachon.termproject.danggeun.R;

public class Signup06Activity extends AppCompatActivity {
    private StorageReference storageReference; // 파일 저장소
    private EditText startgame;
    private EditText endgame;
    private Uri file; // 파일 담는 그릇

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup06_authexpert);

        storageReference = FirebaseStorage.getInstance().getReference(); // 파일 저장소 가져오기

        startgame = findViewById(R.id.signup06_edittext01);
        endgame = findViewById(R.id.signup06_edittext02);
        Button gogo = findViewById(R.id.signup06_button01);

        gogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = startgame.getText().toString();
                String end = endgame.getText().toString();
                int sstart = Integer.parseInt(start);
                int eend = Integer.parseInt(end);


                // 데이터베이스 가져오기
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();

                // 회원가입을 위한 전역 변수 가져오기
                String ID = Signup01Activity.identifier;
                String PW = Signup02Activity.password;
                String nickname = Signup03Activity.nickname;
                String location = Signup04Activity.location;


                // 회원가입 프로세스
                fAuth.createUserWithEmailAndPassword(ID, PW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(sstart < eend) { //오픈시간이 마감보단 작아야함.

                                String userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);

                                // 가입 유저 정보 맵에 모으기
                                // 필요한 정보 더 추가 가능
                                Map<String, Object> user = new HashMap<>();
                                user.put("ID", ID);
                                user.put("nickname", nickname);
                                user.put("isConsumer", true);
                                user.put("profileUrl", "None"); // 프로필 이미지 url
                                user.put("location", location); // user위치
                                user.put("openTime", start);
                                user.put("closeTime", end);

                                documentReference.set(user); // 데이터베이스에 정보 저장


                                Intent intent = new Intent(getApplicationContext(), Signup05Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전 액티비티들을 모두 kill
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Signup06Activity.this, "오픈시간과 마감시간을 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(Signup06Activity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

