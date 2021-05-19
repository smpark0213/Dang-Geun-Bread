package gachon.termproject.danggeun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;

public class MenuRegister extends AppCompatActivity {
    ImageView iv;
    Button uploadBtn;
    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_register);
        storage = FirebaseStorage.getInstance();

        iv = findViewById(R.id.breadImg);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAlbum();
            }
        });

        EditText editBreadName = (EditText) findViewById(R.id.editBreadName);
        EditText editBreadPrice = (EditText) findViewById(R.id.editBreadPrice);
        EditText editBreadQuantity = (EditText) findViewById(R.id.editBreadQuantity);

        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edittext나 img가 비어있으면 비어있으니까 채우라고 토스트메시지 띄우기

                //num 가져오기
                sp = getSharedPreferences("sp", MODE_PRIVATE);
                String num = sp.getString("save", "1");
                Toast.makeText(MenuRegister.this, num, Toast.LENGTH_SHORT).show();

                //editText 가져와서 파베에 올리기
                databaseReference.child("storeBread").child("bread"+num).child("BreadName").setValue(editBreadName.getText().toString());
                databaseReference.child("storeBread").child("bread"+num).child("BreadPrice").setValue(editBreadPrice.getText().toString());
                databaseReference.child("storeBread").child("bread"+num).child("BreadQuantity").setValue(editBreadQuantity.getText().toString());

                //빵번호 +1
                num = (String.valueOf(Integer.parseInt(num)+1));

                //토스트메시지 띄우기
                Toast.makeText(MenuRegister.this, "빵 업로드 성공", Toast.LENGTH_SHORT).show();

                //초기화해주기
                Drawable drawable = getResources().getDrawable(R.drawable.bread_example);
                iv.setImageDrawable(drawable);
                editBreadName.setText(null);
                editBreadPrice.setText(null);
                editBreadQuantity.setText(null);
                save(num);

            }
        });
    }
    public void save(String num) {
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("save",num);
        editor.commit();
    }
    private void loadAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {

            //num 가져오기
            sp = getSharedPreferences("sp", MODE_PRIVATE);
            String num = sp.getString("save", "1");

            String filename = "Bread"+num+".jpg"; //파일명 만들기
            StorageReference storageRef = storage.getReferenceFromUrl("gs://dang-geun-bread.appspot.com");//storage 서버로 이동
            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("Photo/"+filename);
            UploadTask uploadTask = riversRef.putFile(file);

            try { // 선택한 이미지에서 비트맵 생성
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                iv.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //Toast.makeText(ImgSave.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(ImgSave.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

}


