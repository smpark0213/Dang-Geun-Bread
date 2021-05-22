package gachon.termproject.danggeun.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

import gachon.termproject.danggeun.Adapter.AdapterActivity;
import gachon.termproject.danggeun.R;

public class Cart extends AppCompatActivity {

    public static final int REQUEST_CODE1 = 1000;
    public static final int REQUEST_CODE2 = 1001;
    private AdapterActivity arrayAdapter;
    private Button tpBtn, removeBtn;
    private ListView listView;
    private TextView textView;
    private int hour, minute;
    private String result, month, day, am_pm;
    private Handler handler;
    private SimpleDateFormat mFormat;
    private int adapterPosition;
    CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        /*스위치를 포함한 커스텀 adapterView 리스트 터치 오류 관련 문제 해결(Java code)
        switch.setFocusable(false);
        switch.setFocusableInTouchMode(false);*/

        // TODO : StoreActivity에서 Intent 받기
        Intent intent = getIntent();
        if(!TextUtils.isEmpty(intent.getStringExtra("storeId"))){
            intent.getExtras();

        }

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