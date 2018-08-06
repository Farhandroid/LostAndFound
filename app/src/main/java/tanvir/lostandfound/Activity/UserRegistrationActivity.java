package tanvir.lostandfound.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.R;

public class UserRegistrationActivity extends AppCompatActivity {

    EditText emailET;
    static final int REQUEST_IMAGE_CAPTURE_USING_CAMERA = 1;
    CircleImageView circleImageView;
    private ArrayList<Uri> imagePath;
    ArrayList<Bitmap> bitmapArrayList;
    Context context;
    Dialog dialog;
    private boolean isThereAnyProfilePictureSelected=false;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        emailET = findViewById(R.id.enterEmailET);
        circleImageView = findViewById(R.id.circleImageView);
        checkPermissions();
        context = UserRegistrationActivity.this;
        imagePath = new ArrayList<>();
        bitmapArrayList=new ArrayList<>();
        Fresco.initialize(context);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmapArrayList.size() != 0) {
                    Log.d("enterImageViewer","enterImageViewer");
                    new ImageViewer.Builder(context, bitmapArrayList)
                            .setStartPosition(0).build();
                }
                else
                    Log.d("sizeZero","sizeZero");

            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void checkUserRegistrationInformation(View view) {
        String email = emailET.getText().toString();
        if (isValidEmail(email) == false) {
            emailET.requestFocus();
            emailET.setError("Invalid Email");
        } else {
            EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
            enterOrBackFromActivity.startPinVerificationActivity(UserRegistrationActivity.this);
        }

    }

    public void showImagePickerDialog(View view) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.image_picker_dialog_for_user_post);
        dialog.setCancelable(true);
        LinearLayout takePictureWithCamera = dialog.findViewById(R.id.takePictureWithCamera);
        takePictureWithCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE_USING_CAMERA);
            }
        });
        LinearLayout takePictureFromGallery = dialog.findViewById(R.id.takePictureFromGallery);
        takePictureFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();

                FishBun.with(UserRegistrationActivity.this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(1)
                        .setCamera(true)
                        .setActionBarColor(Color.parseColor("#607D8B"), Color.parseColor("#607D8B"), false)
                        .setActionBarTitleColor(Color.parseColor("#ffffff"))
                        .startAlbum();

            }
        });
        dialog.show();
    }

    public File getFile() {
        File folder = new File("/sdcard/lost_and_found");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File imgFile = new File(folder, "image.jpg");
        return imgFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE_USING_CAMERA && resultCode == RESULT_OK) {
            String path = "/sdcard/lost_and_found/image.jpg";
            File file = new File(path);
            if (file.exists()) {
                Bitmap bitmap = null;
                try {
                    imagePath.clear();
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Glide.with(this).load(bitmap).into(circleImageView);
                    isThereAnyProfilePictureSelected=true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("problemInBitmap", e.toString());
                }

            } else
                Log.d("fileNotExist", "fileNotExist");
        } else if (requestCode == Define.ALBUM_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                imagePath = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                if (imagePath == null)
                    Log.d("imagePathIsNull", "imagePathIsNull");
                else
                    Log.d("imagePath", imagePath.toString());
                isThereAnyProfilePictureSelected=true;
                Glide.with(context).load(imagePath.get(0)).into(circleImageView);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("errorInImagePath", e.toString());
            }

        } else
            Log.d("cameraCancelled", "cameraCancelled");
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void sendUserRegistartionDataToServer()
    {
        String url="";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
    }

}
