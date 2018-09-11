package tanvir.lostandfound.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.R;

public class UserRegistrationActivity extends AppCompatActivity {

    EditText emailET,nameET,userNameET,passwordET,retypePasswordET;
    private String name , email , password , userName;
    static final int REQUEST_IMAGE_CAPTURE_USING_CAMERA = 1;
    CircleImageView circleImageView;
    private ArrayList<Uri> imagePath;
    ArrayList<Bitmap> bitmapArrayList;
    Context context;
    Dialog dialog;
    File cameraFile;
    ProgressDialog progressDialog;
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
        emailET = findViewById(R.id.emailET);
        nameET=findViewById(R.id.userLoginNameET);
        userNameET=findViewById(R.id.userNameET);
        passwordET=findViewById(R.id.userLoginPasswordET);
        retypePasswordET=findViewById(R.id.retypePasswordET);
        circleImageView = findViewById(R.id.circleImageView);
        checkPermissions();
        context = UserRegistrationActivity.this;
        imagePath = new ArrayList<>();
        bitmapArrayList=new ArrayList<>();
        progressDialog=new ProgressDialog(context);
        Fresco.initialize(context);

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void sendUserRegistrationInformationToServer(View view) {
       email = emailET.getText().toString();
       password=passwordET.getText().toString();
       userName =userNameET.getText().toString();
       name=nameET.getText().toString();
       if (email.length()>0 && password.length()>0 && userName.length()>0 && name.length()>0)
       {
           if (isValidEmail(email) == false) {
               emailET.requestFocus();
               emailET.setError("Invalid Email");
           } else {
                if (password.equals(retypePasswordET.getText().toString()))
                {
                    sendUserRegistrationInformationToServer();
                }
                else
                {
                    passwordET.requestFocus();
                    passwordET.setError("Password Doesn't Match");
                    retypePasswordET.requestFocus();
                    retypePasswordET.setError("Password Doesn't Match");
                }
           }
       }
       else
       {
           Toast.makeText(UserRegistrationActivity.this, "Please Provide All Information", Toast.LENGTH_SHORT).show();
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
                cameraFile=file;
                try {
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
                {
                    String filepath = getRealPathFromDocumentUri(context, imagePath.get(0));
                    cameraFile = new File(filepath);
                    Log.d("imageNameList", imagePath.toString());
                }
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

    public String getRandomNumber() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000000000);
        String formatted = String.format("%09d", num);
        return formatted;
    }

    public static String getRealPathFromDocumentUri(Context context, Uri uri) {
        String filePath = "";
        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {

            return filePath;
        }
        String imgId = m.group();
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{imgId}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    public void startLoginActivity(View view) {
        EnterOrBackFromActivity enterOrBackFromActivity=new EnterOrBackFromActivity();
        enterOrBackFromActivity.startUserLoginActivity(this);
    }

    private void sendUserRegistrationInformationToServer()
    {
        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        MultipartBody.Part fileToUpload=null;
        RequestBody fileName=null;
        String profilePictureCounter="";
        File file=null;

        if (isThereAnyProfilePictureSelected)
        {
            progressDialog.showProgressDialog("Please Wait...",true);
            if (cameraFile!=null)
            {
                profilePictureCounter="1";
                try {
                    file=new Compressor(context)
                            .setQuality(60)
                            .setMaxHeight(640)
                            .setMaxHeight(480)
                            .compressToFile(cameraFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("compressError",e.toString());
                }

                RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),file);
                fileToUpload = MultipartBody.Part.createFormData("file", userName,requestBody);
                fileName = RequestBody.create(MediaType.parse("text/plain"), userName);
            }
            else
                Log.d("cameraFileIsNull", "cameraFileIsNull");
        }
        else
        {
            progressDialog.showProgressDialog("Please Wait...");
            profilePictureCounter="0";
            Log.d("noImageSelected","noImageSelected");
        }

        RequestBody nameRB = RequestBody.create(MediaType.parse("text./plain"),name);
        RequestBody userNameRB = RequestBody.create(MediaType.parse("text./plain"),userName);
        RequestBody emailRB = RequestBody.create(MediaType.parse("text./plain"),email);
        RequestBody mobileNoRB = RequestBody.create(MediaType.parse("text./plain"),getRandomNumber());
        RequestBody paswordRB = RequestBody.create(MediaType.parse("text./plain"),password);
        RequestBody profilePicCounterRB = RequestBody.create(MediaType.parse("text/plain"),profilePictureCounter);

        //Call<ServerResponse> sendUserInformationCall= apiConfig.sendUserInformation(fileToUpload,fileName,name,userName,email,getRandomNumber(),password,profilePictureCounter);
        Call<ServerResponse> sendUserInformationCall= apiConfig.sendUserInformationRequestBody(fileToUpload,fileName,nameRB,userNameRB,emailRB,mobileNoRB,paswordRB,profilePicCounterRB);
        sendUserInformationCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse=response.body();
                Log.d("retrofitResponse",""+serverResponse.getImageResponse());
                Log.d("retrofitResponse2",""+serverResponse.getQueryResponse());
                Toast.makeText(context, "Server Response : "+""+serverResponse.getImageResponse(), Toast.LENGTH_SHORT).show();
                progressDialog.hideProgressDialog();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("retrofitError",""+t.getMessage());
                Toast.makeText(context, "Error : "+""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hideProgressDialog();

            }
        });
    }


}
