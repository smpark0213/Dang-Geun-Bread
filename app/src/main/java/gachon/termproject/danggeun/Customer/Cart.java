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

        //??? ?????????????????? ?????? store ??????
        Bundle bundle = getIntent().getExtras();
        String storeName = bundle.getString("title");
        String getStoreId = getIntent().getStringExtra("storeId");
        ArrayList<BreadDTO> breadDTOArrayList = (ArrayList<BreadDTO>) getIntent().getSerializableExtra("breadList");

        // ????????? ???????????? ??? ??????
        for(BreadDTO b : breadDTOArrayList){
            Log.d(TAG, b.getBreadName());
            Log.d(TAG, b.getBreadPrice());
            Log.d(TAG, b.getCount());
            Log.d(TAG, b.getTotalPrice());
        }

        /*???????????? ????????? ????????? adapterView ????????? ?????? ?????? ?????? ?????? ??????(Java code)
        switch.setFocusable(false);
        switch.setFocusableInTouchMode(false);*/

        // ????????? ?????? ???
        // recyclerView init
        recyclerView = findViewById(R.id.cart_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // StoreActivity????????? ???????????? ??? ?????????
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

        // resltime db????????? ?????? ?????? ???????????????!!
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

        //List??? ?????? ????????? ????????? ??? ???????????? ??????
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterPosition = position;
                arrayAdapter.removeItem(position);
                Intent intent = new Intent(Cart.this, TimePickerActivity.class);
                startActivityForResult(intent,REQUEST_CODE2);
            }
        });

        //TimePicker??? ?????? ???????????? ?????? ?????? startActivityForResult()
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
                        Toast.makeText(getApplicationContext(), "??????????????? ?????? ????????????.\n?????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // 24???????????? ??????
                        int hour24 = hour;
                        if(am_pm.equals("??????")) hour24 += 12;

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
                            Toast.makeText(getApplicationContext(), "?????? ?????? ?????? ??????..", Toast.LENGTH_SHORT).show();
                        }
                        Firebase.addReservation(new ReservatoinRequest(userId, storeId, breadDTOArrayList, rT, storeName)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
                                    Intent toReceipt = new Intent(Cart.this, Cart_Receipt.class);
                                    startActivity(toReceipt);
                                    // StoreActivity?????? ????????? Cart??? ??? ????????? ?????????
                                    StoreActivity.clearCart();
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //TimePicker ????????? ????????? ????????? arrayAdapter??? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView date = (TextView) findViewById(R.id.result);

        //?????? ????????? ??????
        if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            hour = data.getIntExtra("hour", 1);
            minute = data.getIntExtra("minute", 2);
            am_pm = data.getStringExtra("am_pm");
            result = data.getStringExtra("date");
            sMonth = data.getIntExtra("stMonth", 1);
            sDay = data.getIntExtra("stDay", 1);

            date.setText("?????? ??????:  "+result);
            // ??????

            arrayAdapter.addItem(hour, minute, am_pm, month, day);
            arrayAdapter.notifyDataSetChanged();
        }
        //?????? ????????? ?????? ??? ????????? ????????? ??????
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