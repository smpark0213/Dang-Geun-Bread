package gachon.termproject.danggeun.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import gachon.termproject.danggeun.Adapter.AdapterActivity;
import gachon.termproject.danggeun.Cart_Receipt;
import gachon.termproject.danggeun.R;
import gachon.termproject.danggeun.Util.Firebase;
import gachon.termproject.danggeun.Util.Model.ReservatoinRequest;

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

    private int sMonth;
    private int sDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        String TAG = "CART";

        //앞 액티비티에서 받은 store 이름
        Bundle bundle = getIntent().getExtras();
        String storeName = bundle.getString("title");
        String getStoreId = getIntent().getStringExtra("storeId");
        ArrayList<BreadDTO> breadDTOArrayList = (ArrayList<BreadDTO>) getIntent().getSerializableExtra("breadList");

        // 제대로 들어오는 거 확인
        for(BreadDTO b : breadDTOArrayList){
            Log.d(TAG, b.getBreadName());
            Log.d(TAG, b.getBreadPrice());
            Log.d(TAG, b.getCount());
            Log.d(TAG, b.getTotalPrice());
        }

        /*스위치를 포함한 커스텀 adapterView 리스트 터치 오류 관련 문제 해결(Java code)
        switch.setFocusable(false);
        switch.setFocusableInTouchMode(false);*/

        // 카트에 담길 빵
        // recyclerView init
        recyclerView = findViewById(R.id.cart_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // StoreActivity로부터 전달받은 빵 리스트
        adapter = new CartAdapter(breadDTOArrayList);
        recyclerView.setAdapter(adapter);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = (TextView) findViewById(R.id.title);
        title.setText(storeName);

        // resltime db안해도 카트 구현 가능합니다!!
        /*
        mDatabase = Firebase.getFirebaseDatabaseInstance();
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
         */
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

        reserveBtn = (Button) findViewById(R.id.reserveBtn);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userId, storeId, time, breadList (breadId, count)

                String userId = Firebase.getFirebaseUser().getUid();
                String storeId = getStoreId;

                Calendar calendar = Calendar.getInstance();
                Timestamp timestamp = null;
                com.google.firebase.Timestamp rT = null;

                if(am_pm != null){
                    if(breadDTOArrayList.isEmpty() || breadDTOArrayList == null){
                        Toast.makeText(getApplicationContext(), "장바구니에 방이 없습니다.\n빵을 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // 24시간제로 변환
                        int hour24 = hour;
                        if(am_pm.equals("오후")) hour24 += 12;

                        // yyyy-mm-dd hh:mm:ss
                        String reservationTime = calendar.get(Calendar.YEAR)+ "-" + sMonth+ "-" + sDay+ " " + hour24+ ":" + minute + ":00";
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        try{
                            Log.d(getLocalClassName(), "In try block" + reservationTime);
                            //Date parsedData = dateFormat.parse(reservationTime);
                            //Log.d(getLocalClassName(), "parseData : " + String.valueOf(parsedData));
                            timestamp = Timestamp.valueOf(reservationTime);
                            rT = new com.google.firebase.Timestamp(timestamp);
                            Log.d(getLocalClassName(), "Timestamp : "+ String.valueOf(timestamp));
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "예약 시간 변환 실패..", Toast.LENGTH_SHORT).show();
                        }
                        Firebase.addReservation(new ReservatoinRequest(userId, storeId, breadDTOArrayList, rT)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "예약 성공", Toast.LENGTH_SHORT).show();
                                    Intent toReceipt = new Intent(Cart.this, Cart_Receipt.class);
                                    startActivity(toReceipt);
                                    // StoreActivity에서 저장한 Cart의 빵 리스트 비우기
                                    StoreActivity.clearCart();
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "예약 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                }

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
            sMonth = data.getIntExtra("stMonth", 1);
            sDay = data.getIntExtra("stDay", 1);

            date.setText("픽업 날짜:  "+result);
            // 시간

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