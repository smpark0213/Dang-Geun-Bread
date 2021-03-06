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


                //edittext??? img??? ??????????????? ?????????????????? ???????????? ?????????????????? ?????????
                if(breadname==null || breadpirce==0 || downloadUri==null || breadquantitiy==0)
                {
                    Log.v("????????????",breadname+breadpirce+downloadUri+breadquantitiy);
                    startToast("???????????? ????????? ???????????????!");
                }
                else {
                    //num ????????????
                    sp = getSharedPreferences("sp", MODE_PRIVATE);
                    String num = sp.getString("save", "1");

                    //editText ???????????? ????????? ?????????
                    databaseReference.child("storeBread").child("bread" + num).child("BreadName").setValue(breadname);
                    databaseReference.child("storeBread").child("bread" + num).child("BreadPrice").setValue(breadpirce);
                    databaseReference.child("storeBread").child("bread" + num).child("BreadQuantity").setValue(breadquantitiy);

                    //????????????????????? ??? ????????? ??????

                    //????????? ?????? ?????? ?????? --?????? ???????????? ????????? ?????????
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    //????????????????????? ??? ????????? ??????
                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                    final DocumentReference documentReference = firebaseFirestore.collection("Bread").document();
                    BreadInfo breadInfo=new BreadInfo(firebaseUser.getUid(),documentReference.getId(),breadname,breadpirce,breadquantitiy,downloadUri.toString());
                    documentReference.set(breadInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    startToast("??? ?????? ??????!");

                                    //??????????????????
                                    Drawable drawable = getResources().getDrawable(R.drawable.bread_example);
                                    iv.setImageDrawable(drawable);
                                    editBreadName.setText(null);
                                    editBreadPrice.setText(null);
                                    editBreadQuantity.setText(null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("??? ?????? ??????!");
                        }
                    });


                    //?????????????????? ?????????
                    Toast.makeText(MenuRegister.this, "??? ????????? ??????", Toast.LENGTH_SHORT).show();

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

    //???????????? ????????? ???????????? ??? ??????
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {

            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("Bread").document();
            String filename = documentReference.getId()+".jpg"; //????????? ?????????
            StorageReference storageRef = storage.getReferenceFromUrl("gs://dang-geun-bread.appspot.com");//storage ????????? ??????

            //Storage??? ?????? ??????
            final StorageReference riversRef = storageRef.child("Bread/"+filename);
            imagepath=getPath(data.getData());

            Log.v("?????? ??????2", getPath(data.getData()));

            try {
                // ????????? ??????????????? ????????? ??????
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                iv.setImageBitmap(img);
                //
                Log.v("?????? ??????3", getPath(data.getData()));

                //firestore??? storage uri ??????
                InputStream stream = new FileInputStream(new File(imagepath));
                UploadTask uploadTask=riversRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        Log.v("?????? ??????4", getPath(data.getData()));
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            Log.v("?????? ??????5", getPath(data.getData()));
                            geturi(uri);

//
                        } else {
                            Log.v("?????? ??????6", getPath(data.getData()));
                            startToast("??????????????? ???????????? ?????????????????????.");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //?????? ?????? ?????? ????????????!
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }


    //????????? ????????? ??????
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //Uri ????????????
    private void geturi(Uri uri){
        this.downloadUri= uri;
    }


    ////???????????? ?????? ????????? ????????? ??? ?????????!
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


