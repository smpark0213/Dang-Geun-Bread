package gachon.termproject.danggeun.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import gachon.termproject.danggeun.R;

public class Signup03Activity extends AppCompatActivity {
    private FirebaseFirestore fStore;
    private CollectionReference collectionReference;
    public static String nickname; // 회원가입을 위한 전역변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup03_nickname);

        Button nextButton = findViewById(R.id.signup03_button01);
        EditText nicknameText = findViewById(R.id.signup03_edittext01);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임을 입력받음
                String temp = nicknameText.getText().toString();

                //데이터베이스에서 중복되는 닉네임 있는지 확인!!!
                fStore = FirebaseFirestore.getInstance();
                collectionReference = fStore.collection("users");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> list = querySnapshot.getDocuments();

                            for (int i = 0; i < list.size(); i++) {
                                DocumentSnapshot snapshot = list.get(i);
                                String nicknameCheck = snapshot.getString("nickname");
                                if (temp.equals(nicknameCheck)) {
                                    Toast.makeText(getApplicationContext(), "중복된 닉네임 입니다", Toast.LENGTH_SHORT).show();
                                    break;
                                } else if (i == list.size() - 1) {
                                    // 닉네임 설정 후 다음 페이지로 이동
                                    nickname = temp;
                                    if(Signup00Activity.publicMan) {
                                        startActivity(new Intent(getApplicationContext(), Signup04Activity_user.class));
                                    }
                                    else{
                                        startActivity(new Intent(getApplicationContext(), Signup04Activity.class));
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}