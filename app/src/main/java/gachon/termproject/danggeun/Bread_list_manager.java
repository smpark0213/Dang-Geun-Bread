package gachon.termproject.danggeun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import gachon.termproject.danggeun.Adapter.FirstAdapter;
import gachon.termproject.danggeun.Util.Firestore;

public class Bread_list_manager extends AppCompatActivity {
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView freeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen_manager);
        //파이어베이스에서 데이터베이스를 가져옴.
        firebaseFirestore= FirebaseFirestore.getInstance();


        freeRecyclerView = findViewById(R.id.post1);
        freeRecyclerView.setHasFixedSize(true);
        freeRecyclerView.setLayoutManager(new LinearLayoutManager(Bread_list_manager.this));
    }

    @Override
    protected void onResume(){
        super.onResume();
        //현재 유저목록 가져오기
        FirebaseUser user = Firestore.getFirebaseUser();
        String userID=user.getUid();
        Log.v("사용자 id",userID);
        //BreadList 있는 data를 가져오기 위함.
        Firestore.getBreadList(userID)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<BreadInfo> Bread_list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //각 게시글의 정보를 가져와 arrayList에 저장.
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                Bread_list.add(new BreadInfo(
                                        document.getData().get("storeID").toString(),
                                        document.getData().get("breadId").toString(),
                                        document.getData().get("breadName").toString(),
                                        document.getData().get("price").toString(),
                                        (long) document.getData().get("count"),
                                        document.getData().get("photoURL").toString()
                                ));
                            }
                            //freeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
                            RecyclerView.Adapter mAdapter1 = new FirstAdapter(Bread_list_manager.this, Bread_list);
                            freeRecyclerView.setAdapter(mAdapter1);
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });


    }

    //만약 메뉴추가 버튼을 누르면 작성 화면으로 넘어가게 하기 위한 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };

    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
