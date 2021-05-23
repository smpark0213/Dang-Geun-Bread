package gachon.termproject.danggeun.Customer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import gachon.termproject.danggeun.R;

public class TimePickerActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button okBtn, cancelBtn;
    private int hour, minute;
    private String am_pm;
    private Date currentTime;
    private String stYear, stMonth, stDay, date;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        calendarView = (CalendarView) findViewById(R.id.calendarView1);
        calendarView.setVisibility(View.VISIBLE);
        java.util.Calendar curDate = java.util.Calendar.getInstance();
        curDate.setTimeInMillis(calendarView.getDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year+"년 "+(month + 1) + "월 " + dayOfMonth + "일";
            }
        });


        timePicker = (TimePicker)findViewById(R.id.time_picker);

        okBtn = (Button)findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            //안드로이드 버전별로 시간값 세팅을 다르게 해주어야 함. 여기선 Android API 23
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                am_pm = AM_PM(hour);
                hour = timeSet(hour);

                Intent sendIntent = new Intent(TimePickerActivity.this, Cart.class);

                sendIntent.putExtra("hour", hour);
                sendIntent.putExtra("minute", minute);
                sendIntent.putExtra("am_pm", am_pm);
                sendIntent.putExtra("date", date);

                setResult(RESULT_OK, sendIntent);
                finish();
            }
        });
        //취소버튼 누를 시 TimePickerAcitivity 종료
        cancelBtn = (Button) findViewById(R.id.cancleBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //24시 시간제 바꿔줌
    private int timeSet(int hour) {
        if(hour > 12) {
            hour-=12;
        }
        return hour;
    }
    //오전, 오후 선택
    private String AM_PM(int hour) {
        if(hour >= 12) {
            am_pm = "오후";
        }
        else {
            am_pm = "오전";
        }
        return am_pm;
    }

}