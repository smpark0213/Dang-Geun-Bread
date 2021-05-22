package gachon.termproject.danggeun.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import gachon.termproject.danggeun.R;

public class Bread_Detail extends AppCompatActivity {

    private int counter = 1;
    private int totalPrice = 0;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private String storeName;
    private String breadImg;
    private String breadName;
    private String breadPrice;
    private String maximum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bread_detail);

        //번들로 받아와야 하는 정보
        //빵사진, 빵이름, 가격, 최대수량
        //앞 액티비티에서 보낸 번들을 받음
        Intent passedIntent = getIntent();
        if(passedIntent!=null) {
            Bundle bundle = getIntent().getExtras();
            storeName = bundle.getString("store");
            breadImg = bundle.getString("img");
            breadName = bundle.getString("name");
            breadPrice = bundle.getString("price");
            maximum = bundle.getString("maximum");
            Toast.makeText(getApplicationContext(), breadImg, Toast.LENGTH_SHORT).show();


            //Toast.makeText(getApplicationContext(), breadName, Toast.LENGTH_SHORT).show();

        }

        //빵사진 파베 storage에 있는 빵 사진 가져와야함
        ImageView breadIV = (ImageView) findViewById(R.id.breadImageView);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://dang-geun-bread.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("Bread/"+breadImg+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(breadIV);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure (@NonNull Exception exception) {
                //실패시
                Toast.makeText(getApplicationContext(), "이미지 로드에 실패했습니다.", Toast.LENGTH_SHORT).show();

            }
        });



        //번들로 받은 빵 이름으로 변경해야함
        TextView bread_name = (TextView) findViewById(R.id.breadName);
        bread_name.setText(breadName);
        //번들로 받은 빵 가격으로 변경해야함
        TextView bread_price = (TextView) findViewById(R.id.breadPrice);
        bread_price.setText(breadPrice);
        //번들로 받은 빵 최대수량으로 변경
        TextView bread_maximum = (TextView) findViewById(R.id.maximum);
        bread_maximum.setText(maximum);

        Button cartBtn = (Button) findViewById(R.id.cartBtn);
        TextView count = (TextView) findViewById(R.id.count);



        if (counter == 1) {
            totalPrice = Integer.parseInt(breadPrice);
            cartBtn.setText(String.valueOf(counter)+"개 담기       "+totalPrice+"원");
        }



        //수량 체크 버튼
        ImageView minusBtn = (ImageView) findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //수량-1 (수량이 1이면 더이상 내려가지 않게)

                //밑에 담기 버튼에 띄우는 수량 가격 변경
                if (counter!=1) { //수량이 1일때 -를 누르면 안되니까
                    counter--;
                    count.setText(String.valueOf(counter));
                    totalPrice = Integer.parseInt(breadPrice)*counter;
                    cartBtn.setText(String.valueOf(counter)+"개 담기       "+totalPrice+"원");
                }

            }
        });

        ImageView plusBtn = (ImageView) findViewById(R.id.plus_btn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //수량+1
                counter++;
                if (counter <= Integer.parseInt(maximum)){
                    count.setText(String.valueOf(counter));
                    totalPrice = Integer.parseInt(breadPrice)*counter;
                    cartBtn.setText(String.valueOf(counter)+"개 담기       "+totalPrice+"원");
                }
                else{
                    Toast.makeText(getApplicationContext(), "최대수량을 초과할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //카트에 담기
        // 빵 이름, 빵 갯수, 총 가격
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("BreadName", breadName);
                returnIntent.putExtra("Count", counter);
                returnIntent.putExtra("TotalPrice", totalPrice);

                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}