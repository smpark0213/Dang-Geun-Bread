package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import gachon.termproject.danggeun.Customer.CustomerActivity;
import gachon.termproject.danggeun.Signup.Signup00Activity;
import gachon.termproject.danggeun.Util.Model.UserInfo;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private DocumentReference documentReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button_login = findViewById(R.id.login_button);
        TextView forgetPW = findViewById(R.id.login_text04_forgetPW);
        TextView toSignup = findViewById(R.id.login_text06_signup);
        EditText id = findViewById(R.id.login_editText_ID);
        EditText pw = findViewById(R.id.login_editText_PW);


        fAuth = FirebaseAuth.getInstance();

        // 이미 로그인한 경우 로그인 상태 유지
        // 로그아웃시키고 싶으면 요기 주석하고 재실행하쉐여
        if (fAuth.getCurrentUser() != null){
            setUserInfo();
        }


        //login버튼을 눌럿을때
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editText에서 아이디 비번 받아오기
                String ID = id.getText().toString().trim();
                String PW = pw.getText().toString().trim();


                if (ID.length() > 0 && PW.length() > 0) {
                    fAuth.signInWithEmailAndPassword(ID, PW)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        setUserInfo();
//                                        if(UserInfo.isConsumer==true){
//                                            Toast.makeText(getApplicationContext(), "자동 로그인 (고객)", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
//                                        }
//                                        else {
//                                            Toast.makeText(getApplicationContext(), "자동 로그인 (점주)", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(), ManagerActivity.class));
//                                        }
                                        finish();
                                    } else if (task.getException() != null)
                                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if (ID.length() == 0) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (PW.length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        //비밀번호 까묵엇을때
        forgetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // forget password 글자 눌렀을 때 넘어가는 화면넣기
                Toast.makeText(getApplicationContext(), "비밀번호를 까먹엇다고? 아직 비밀번호찾기페이지가 구현이 안되어서 찾을수없다..", Toast.LENGTH_LONG).show();

            }
        });


        //대망의 회원가입!!!
        toSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup00Activity.class));//액티비티 이동
            }
        });

    }

    // 나중에 쓸 일 많은 유저 고유 아이디, 닉네임, 프로필 사진 Url 정보 미리 저장
    public void setUserInfo() {
        UserInfo.userId = fAuth.getCurrentUser().getUid();
        documentReference = FirebaseFirestore.getInstance().collection("users").document(UserInfo.userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        UserInfo.isConsumer = document.getBoolean("isConsumer");
                        UserInfo.nickname = document.getString("nickname");
                        UserInfo.profileImg = document.getString("profileUrl");

                        //자동 로그인 여기로 옮겼더니 해결됏습니다
                        if(UserInfo.isConsumer){
                            Toast.makeText(getApplicationContext(), "자동 로그인 (고객)", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "자동 로그인 (점주)", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ManagerActivity.class));
                        }
                        finish();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("실패", "실패!");
                Toast.makeText(getApplicationContext(), "유저 정보 가져오기 실패", Toast.LENGTH_LONG).show();
            }
        });
    }
}