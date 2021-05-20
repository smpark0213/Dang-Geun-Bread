package gachon.termproject.danggeun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {
    private AppCompatButton btn_reserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btn_reserve = findViewById(R.id.btn_reserve);
        ArrayList<BreadInfo> breadInfos = new ArrayList<>();

        // TODO : 가게 정보 가져오기
        // TODO : Firebase에서 해당 가게의 빵 정보 읽어오기
        for(int i = 0; i < 10; ++i){
            breadInfos.add(new BreadInfo("소라방", i + ""));
        }

        // 리사이클러뷰에 linearlayout 객체 지정
        RecyclerView recyclerView = findViewById(R.id.store_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 adapter지정
        BreadAdpater breadAdpater = new BreadAdpater(breadInfos);
        recyclerView.setAdapter(breadAdpater);
    }
}