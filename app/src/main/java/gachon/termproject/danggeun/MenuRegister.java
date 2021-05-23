package gachon.termproject.danggeun;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import gachon.termproject.danggeun.Util.Model.BreadInfo;
import gachon.termproject.danggeun.Util.Firebase;

public class MenuRegister extends AppCompatActivity {
    ImageView iv, back;
    Button uploadBtn;
    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser firebaseUser= Firebase.getFirebaseUser();
    SharedPreferences sp;
    private String imagepath;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_register);
        storage = FirebaseStorage.getInstance();

        verifyStoragePermissions(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
                String breadname=editBreadName.getText().toString();
                long breadpirce=Integer.parseInt(editBreadPrice.getText().toString());
                long breadquantitiy=Integer.parseInt(editBreadQuantity.getText().toString());


                //edittext나 img가 비어있으면 비어있으니까 채우라고 토스트메시지 띄우기
                if(breadname==null || breadpirce==0 || downloadUri==null || breadquantitiy==0)
                {
                    Log.v("메뉴정보",breadname+breadpirce+downloadUri+breadquantitiy);
                    startToast("사진이나 내용이 비어있어요!");
                }
                else {
                    //num 가져오기
                    sp = getSharedPreferences("sp", MODE_PRIVATE);
                    String num = sp.getString("save", "1");

                    //editText 가져와서 파베에 올리기
                    databaseReference.child("storeBread").child("bread" + num).child("BreadName").setValue(breadname);
                    databaseReference.child("storeBread").child("bread" + num).child("BreadPrice").setValue(breadpirce);
                    databaseReference.child("storeBread").child("bread" + num).child("BreadQuantity").setValue(breadquantitiy);

                    //파이어베이스에 빵 리스트 추가

                    //일단은 현재 시간 설정 --시간 쓰일수도 있어서 넣었음
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    //파이어베이스에 빵 리스트 추가
                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                    final DocumentReference documentReference = firebaseFirestore.collection("Bread").document();
                    BreadInfo breadInfo=new BreadInfo(firebaseUser.getUid(),documentReference.getId(),breadname,breadpirce,breadquantitiy,downloadUri.toString());
                    documentReference.set(breadInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    startToast("빵 등록 성공!");

                                    //초기화해주기
                                    Drawable drawable = getResources().getDrawable(R.drawable.bread_example);
                                    iv.setImageDrawable(drawable);
                                    editBreadName.setText(null);
                                    editBreadPrice.setText(null);
                                    editBreadQuantity.setText(null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("빵 등록 실패!");
                        }
                    });


                    //토스트메시지 띄우기
                    Toast.makeText(MenuRegister.this, "빵 업로드 성공", Toast.LENGTH_SHORT).show();

                }
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

    //앨범에서 사진을 클릭했을 때 실행
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {

            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("Bread").document();
            String filename = documentReference.getId()+".jpg"; //파일명 만들기
            StorageReference storageRef = storage.getReferenceFromUrl("gs://dang-geun-bread.appspot.com");//storage 서버로 이동

            //Storage에 사진 저장
            final StorageReference riversRef = storageRef.child("Bread/"+filename);
            imagepath=getPath(data.getData());

            Log.v("사진 경로2", getPath(data.getData()));

            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                iv.setImageBitmap(img);
                //
                Log.v("사진 경로3", getPath(data.getData()));

                //firestore에 storage uri 저장
                InputStream stream = new FileInputStream(new File(imagepath));
                UploadTask uploadTask=riversRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        Log.v("사진 경로4", getPath(data.getData()));
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            Log.v("사진 경로5", getPath(data.getData()));
                            geturi(uri);

//                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                            db.collection("Bread").document(documentReference.getId()).update("photoUrl",downloadUri.toString())
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            startToast("회원정보 등록을 성공하였습니다.");
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            startToast("회원정보 등록에 실패하였습니다.");
//
//                                        }
//                                    });
                        } else {
                            Log.v("사진 경로6", getPath(data.getData()));
                            startToast("회원정보를 보내는데 실패하였습니다.");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //앨범 사진 경로 가져오기!
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }


    //토스트 띄우는 함수
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Uri 저장하기
    private void geturi(Uri uri){
        this.downloadUri= uri;
    }


    ////외부에서 파일 읽기가 안되는 것 해결법!
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}


