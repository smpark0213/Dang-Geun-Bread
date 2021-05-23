package gachon.termproject.danggeun.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gachon.termproject.danggeun.R;
import io.grpc.InternalNotifyOnServerBuild;
import android.app.AlertDialog;

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
    private ImageView breadIV;

    private AlertDialog alertDialog = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

    public void createDialog(){
        alertDialog = new AlertDialog.Builder(Bread_Detail.this)
                .setTitle("같은 가게의 메뉴만 담을 수 있습니다.")
                .setMessage("주문할 가게를 변경하실 경우 이전에 담은 메뉴가 삭제됩니다.").setPositiveButton("새로 담기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 카트 클리어
                        StoreActivity.clearCart();
                        BreadDTO br = new BreadDTO(breadName, breadPrice, Integer.toString(counter), Integer.toString(totalPrice));
                        Intent reIntent = new Intent();
                        reIntent.putExtra("toCart", br);
                        setResult(RESULT_OK, reIntent);
                    }
                })
                .setNegativeButton("취소", null).setCancelable(false).create();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bread_detail);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
            //Toast.makeText(getApplicationContext(), breadImg, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), breadName, Toast.LENGTH_SHORT).show();
        }

        //빵사진 파베 storage에 있는 빵 사진 가져와야함
        //주소?로 가져오는걸로 고쳐야 하는데
        breadIV = findViewById(R.id.breadImageView);
        sendImageRequest(breadImg, breadIV);



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
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //파이어베이스에 올리기
                // 카트에 빵이 있다면,
                if(StoreActivity.isCart()){
                    // 같은 가게라면,
                    if(StoreActivity.getCartStore().equals(storeName)){
                        BreadDTO br = new BreadDTO(breadName, breadPrice, Integer.toString(counter), Integer.toString(totalPrice));
                        Intent reIntent = new Intent();
                        reIntent.putExtra("toCart", br);
                        setResult(RESULT_OK, reIntent);
                        finish();
                    }
                    else{
                        // 다른 가게,
                        // 카트를 비우고 현재 가게의 빵을 추가할 것인지,
                        // 카트를 비우지 않을 것인지 선택하게 할 것.
                        createDialog();
                        alertDialog.show();
                        finish();
                    }
                }
                // 카트에 빵이 없는 경우, 그냥 추가
                BreadDTO br = new BreadDTO(breadName, breadPrice, Integer.toString(counter), Integer.toString(totalPrice));
                Intent reIntent = new Intent();
                reIntent.putExtra("toCart", br);
                setResult(RESULT_OK, reIntent);
                finish();

                // RealTime DB 사용 안해도 됩니다!!
                /*
                databaseReference = firebaseDatabase.getReference("BreadList");
                BreadDTO bread_dto = new BreadDTO(breadName, breadPrice, counter+"", totalPrice+"");
                databaseReference.push().setValue(bread_dto);
                finish();
                 */
            }
        });

    }

    public void sendImageRequest(String breadImg, ImageView breadIV) {

        ImageLoadTask task = new ImageLoadTask(breadImg, breadIV);
        task.execute();
    }

}