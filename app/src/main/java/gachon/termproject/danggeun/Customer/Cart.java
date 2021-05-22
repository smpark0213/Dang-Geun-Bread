package gachon.termproject.danggeun.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gachon.termproject.danggeun.Adapter.AdapterActivity;
import gachon.termproject.danggeun.Cart_Receipt;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firestore;

public class Cart extends AppCompatActivity {

    public static final int REQUEST_CODE1 = 1000;
    public static final int REQUEST_CODE2 = 1001;
    private AdapterActivity arrayAdapter;
    private Button tpBtn, reserveBtn;
    private ListView listView;
    private TextView title;
    private int hour, minute;
    private String result, month, day, am_pm;
    private Handler handler;
    private SimpleDateFormat mFormat;
    private int adapterPosition;
    CalendarView calendarView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CartAdapter adapter;
    ArrayList<BreadDTO> bread_list = new ArrayList<BreadDTO>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        /*스위치를 포함한 커스텀 adapterView 리스트 터치 오류 관련 문제 해결(Java code)
        switch.setFocusable(false);
        switch.setFocusableInTouchMode(false);*/

        // recyclerView init
        recyclerView = findViewById(R.id.cart_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CartAdapter(bread_list);
        recyclerView.setAdapter(adapter);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        //앞 액티비티에서 받은 store 이름
        Bundle bundle = getIntent().getExtras();
        String storeName = bundle.getString("title");
        title = (TextView) findViewById(R.id.title);
        title.setText(storeName);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("BreadList");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bread_list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BreadDTO value = data.getValue(BreadDTO.class);
                    bread_list.add(value);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("Firebase DB Error ", error.toString());
            }
        });

        arrayAdapter = new AdapterActivity();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

        //List에 있는 항목들 눌렀을 때 시간변경 가능
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterPosition = position;
                arrayAdapter.removeItem(position);
                Intent intent = new Intent(Cart.this, TimePickerActivity.class);
                startActivityForResult(intent,REQUEST_CODE2);
            }
        });


        //TimePicker의 시간 셋팅값을 받기 위한 startActivityForResult()
        tpBtn = (Button) findViewById(R.id.addBtn);
        tpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tpIntent = new Intent(Cart.this, TimePickerActivity.class);
                startActivityForResult(tpIntent, REQUEST_CODE1);
            }
        });


        //예약하기 버튼 누르면 firestore에 올리기
        reserveBtn = (Button) findViewById(R.id.reserveBtn);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toReceipt = new Intent(Cart.this, Cart_Receipt.class);
                startActivity(toReceipt);
                finish();

            }
        });


    }

    //TimePicker 셋팅값 받아온 결과를 arrayAdapter에 추가
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView date = (TextView) findViewById(R.id.result);

        //시간 리스트 추가
        if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            hour = data.getIntExtra("hour", 1);
            minute = data.getIntExtra("minute", 2);
            am_pm = data.getStringExtra("am_pm");

            result = data.getStringExtra("date");
            date.setText("픽업 날짜:  "+result);

            arrayAdapter.addItem(hour, minute, am_pm, month, day);
            arrayAdapter.notifyDataSetChanged();
        }
        //시간 리스트 터치 시 변경된 시간값 저장
        if(requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null) {
            hour = data.getIntExtra("hour", 1);
            minute = data.getIntExtra("minute", 2);
            am_pm = data.getStringExtra("am_pm");
            month = data.getStringExtra("month");
            day = data.getStringExtra("day");

            arrayAdapter.addItem(hour, minute, am_pm, month, day);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}