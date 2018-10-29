package tanvir.lostandfound.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
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
import retrofit2.Response;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.UserLoginInformationSP;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.R;

public class UserRegistrationActivity extends AppCompatActivity {

    EditText emailET, nameET, userNameET, passwordET, retypePasswordET;
    private TextView alreadyAMemberTV;
    private String name, email, password, userName;
    static final int REQUEST_IMAGE_CAPTURE_USING_CAMERA = 1;
    CircleImageView circleImageView;
    private ArrayList<Uri> imagePath;
    ArrayList<Bitmap> bitmapArrayList;
    Context context;
    Dialog dialog;
    File cameraFile;
    ProgressDialog progressDialog;
    private String cameFromWhere = "";
    private boolean isThereAnyProfilePictureSelected = false;
    private TextInputLayout passwordTIL, retypePasswordTIL;
    private String nameSP, userNameSP, emailSP, isUserUploadedProfilePictureSP;

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
        nameET = findViewById(R.id.userLoginNameET);
        userNameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.userLoginPasswordET);
        passwordTIL = findViewById(R.id.passwordTIL);
        retypePasswordET = findViewById(R.id.retypePasswordET);
        retypePasswordTIL = findViewById(R.id.retypePasswordTIL);
        circleImageView = findViewById(R.id.circleImageView);
        alreadyAMemberTV = findViewById(R.id.alreadyAMemberTV);
        checkPermissions();
        context = UserRegistrationActivity.this;
        imagePath = new ArrayList<>();
        bitmapArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
        Fresco.initialize(context);
        retrieveCameFromWhereIntent();

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void sendUserRegistrationInformationToServer(View view) {

        if (cameFromWhere.contains("UserProfileActivity"))
            sendUserInformationToServerForUpdate();
        else {
            email = emailET.getText().toString();
            password = passwordET.getText().toString();
            userName = userNameET.getText().toString();
            name = nameET.getText().toString();
            if (email.length() > 0 && password.length() > 0 && userName.length() > 0 && name.length() > 0) {
                if (isValidEmail(email) == false) {
                    emailET.requestFocus();
                    emailET.setError("Invalid Email");
                } else {
                    if (password.equals(retypePasswordET.getText().toString()))
                        sendUserRegistrationInformationToServer();
                    else {
                        passwordET.requestFocus();
                        passwordET.setError("Password Doesn't Match");
                        retypePasswordET.requestFocus();
                        retypePasswordET.setError("Password Doesn't Match");
                    }
                }
            } else
                Toast.makeText(UserRegistrationActivity.this, "Please Provide All Information", Toast.LENGTH_SHORT).show();
        }

    }

    public void showImagePickerDialog(View view) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_picker);
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
                cameraFile = file;
                try {
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Glide.with(this).load(bitmap).into(circleImageView);
                    isThereAnyProfilePictureSelected = true;
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
                else {
                    String filepath = getRealPathFromDocumentUri(context, imagePath.get(0));
                    cameraFile = new File(filepath);
                    Log.d("imageNameList", imagePath.toString());
                }
                isThereAnyProfilePictureSelected = true;
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
        EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
        enterOrBackFromActivity.startUserLoginActivity(this);
    }

    private void sendUserRegistrationInformationToServer() {
        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        MultipartBody.Part fileToUpload = null;
        RequestBody fileName = null;
        String profilePictureCounter = "";
        File file;

        if (isThereAnyProfilePictureSelected) {
            progressDialog.showProgressDialog("Please Wait...", true);
            if (cameraFile != null) {
                profilePictureCounter = "1";
                file = getCompressedFile();
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("file", userName, requestBody);
                fileName = RequestBody.create(MediaType.parse("text/plain"), userName);
            } else
                Log.d("cameraFileIsNull", "cameraFileIsNull");
        } else {
            progressDialog.showProgressDialog("Please Wait...");
            profilePictureCounter = "0";
            Log.d("noImageSelected", "noImageSelected");
        }

        RequestBody nameRB = RequestBody.create(MediaType.parse("text./plain"), name);
        RequestBody userNameRB = RequestBody.create(MediaType.parse("text./plain"), userName);
        RequestBody emailRB = RequestBody.create(MediaType.parse("text./plain"), email);
        RequestBody mobileNoRB = RequestBody.create(MediaType.parse("text./plain"), getRandomNumber());
        RequestBody paswordRB = RequestBody.create(MediaType.parse("text./plain"), password);
        RequestBody profilePicCounterRB = RequestBody.create(MediaType.parse("text/plain"), profilePictureCounter);

        //Call<ServerResponse> sendUserInformationCall= apiConfig.sendUserInformation(fileToUpload,fileName,name,userName,email,getRandomNumber(),password,profilePictureCounter);
        Call<ServerResponse> sendUserInformationCall = apiConfig.sendUserInformationRequestBody(fileToUpload, fileName, nameRB, userNameRB, emailRB, mobileNoRB, paswordRB, profilePicCounterRB);
        sendUserInformationCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progressDialog.hideProgressDialog();
                ServerResponse serverResponse = response.body();
                String message = serverResponse.getMessage();
                Log.d("responseRegstrtionRTFT", "" + serverResponse.getMessage());
                if (message.contains("Data insertion success")) {
                    Toast.makeText(context, "User registration success . You can login now", Toast.LENGTH_LONG).show();
                    EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
                    enterOrBackFromActivity.startUserLoginActivity(UserRegistrationActivity.this);
                } else if (message.contains("Data insertion Fail"))
                    Toast.makeText(context, "User registration failed . Please try again later", Toast.LENGTH_LONG).show();
                else if (message.contains("User alredy exist"))
                    Toast.makeText(context, "This username is already taken .  Please try another", Toast.LENGTH_LONG).show();
                else if (message.contains("Email already taken by a user"))
                    Toast.makeText(context, "This email is already taken . Please try another", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, "User registration failed . Please try again later", Toast.LENGTH_LONG).show();
                ///progressDialog.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("retrofitError", "" + t.getMessage());
                Toast.makeText(context, "Error : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hideProgressDialog();

            }
        });
    }

    public void retrieveCameFromWhereIntent() {
        cameFromWhere = getIntent().getStringExtra("cameFromWhere");
        if (cameFromWhere==null)
            cameFromWhere="";

        if (cameFromWhere.contains("UserProfileActivity")) {
            Log.i("EditProfile","EditProfile");
            retypePasswordET.setVisibility(View.GONE);
            passwordET.setVisibility(View.GONE);
            alreadyAMemberTV.setVisibility(View.GONE);
            passwordTIL.setVisibility(View.GONE);
            retypePasswordTIL.setVisibility(View.GONE);

            SharedPreferences sharedPreferences = getSharedPreferences(UserLoginInformationSP.sharedPrefferenceName, Context.MODE_PRIVATE);
            nameSP = sharedPreferences.getString(UserLoginInformationSP.nameSP, "");
            userNameSP = sharedPreferences.getString(UserLoginInformationSP.userNameSP, "");
            emailSP = sharedPreferences.getString(UserLoginInformationSP.emailAddressSP, "");
            isUserUploadedProfilePictureSP = sharedPreferences.getString(UserLoginInformationSP.isUserUploadedProfilePictureSP, "");

            Log.i("userNameSP","userNameSP : "+userNameSP);
            nameET.setText(nameSP);
            userNameET.setText(userNameSP);
            emailET.setText(emailSP);
            if (!isUserUploadedProfilePictureSP.equals("0"))
            {
                RequestOptions options = new RequestOptions();
                options.signature(new ObjectKey(System.currentTimeMillis()));
                Glide.with(this)
                        .load("http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserProfilePic/" + userNameSP + ".jpg")
                        .apply(options)
                        .into(circleImageView);
            }


        } else
            alreadyAMemberTV.setText(Html.fromHtml(getString(R.string.alreadyAMember)));
    }

    public void sendUserInformationToServerForUpdate() {
        final String newName = nameET.getText().toString();
        final String newUserName = userNameET.getText().toString();
        final String newEmail = emailET.getText().toString();

        String result = checkInformationForPostUpdate(newName, newUserName, newEmail);

        if (result.contains("nothing"))
            Toast.makeText(this, "There are nothing to update", Toast.LENGTH_LONG).show();
        else if (result.contains("empty"))
            Toast.makeText(this, "Please provide all information", Toast.LENGTH_LONG).show();
        if (result.contains("success")) {
            MultipartBody.Part fileToUpload = null;
            RequestBody fileName = null;
            String profilePictureCounter = "";
            File file;

            if (isThereAnyProfilePictureSelected) {
                progressDialog.showProgressDialog("Please Wait...", true);
                if (cameraFile != null) {
                    profilePictureCounter = "1";
                    file = getCompressedFile();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData("file", newUserName, requestBody);
                    fileName = RequestBody.create(MediaType.parse("text/plain"), newUserName);
                } else
                    Log.d("cameraFileIsNull", "cameraFileIsNull");
            } else {
                progressDialog.showProgressDialog("Please Wait...");
                profilePictureCounter = isUserUploadedProfilePictureSP;
                Log.d("noImageSelected", "noImageSelected");
            }
            final String finalProfilePictureCounter = profilePictureCounter;
            RequestBody nameRB = RequestBody.create(MediaType.parse("text./plain"), newName);
            RequestBody newUserNameRB = RequestBody.create(MediaType.parse("text./plain"), newUserName);
            RequestBody oldUserNameRB = RequestBody.create(MediaType.parse("text./plain"), userNameSP);
            RequestBody newEmailRB = RequestBody.create(MediaType.parse("text./plain"), newEmail);
            RequestBody oldEmailRB = RequestBody.create(MediaType.parse("text./plain"), emailSP);
            final RequestBody profilePicCounterRB = RequestBody.create(MediaType.parse("text/plain"), profilePictureCounter);

            ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<ServerResponse> userInformationUpdateServerResponseCall = apiConfig.updateUserInformation(fileToUpload,fileName,oldUserNameRB,newUserNameRB,nameRB,oldEmailRB,newEmailRB,profilePicCounterRB);
            userInformationUpdateServerResponseCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    progressDialog.hideProgressDialog();
                    ServerResponse serverResponse=response.body();
                    String message =serverResponse.getMessage();
                    Log.i("rspneUpdteUserInfoRTFT",message);
                    if (message.contains("UserInformation data update success"))
                    {
                        updateSP(newName,newUserName,newEmail,finalProfilePictureCounter);
                        Toast.makeText(context, "Profile successfully updated", Toast.LENGTH_LONG).show();
                        startUserProfileActivityAfterUserPostUpdate();
                    }
                    else
                        Toast.makeText(context, "Profile update failed . Please try again later", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    Toast.makeText(context, "error : "+t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("errorRtftUserInfoUpdate",t.getMessage());
                }
            });
        }
    }

    public String checkInformationForPostUpdate(String name, String userName, String email) {
        if (name.length() == 0 || userName.length() == 0 || email.length() == 0)
            return "empty";
        if (name.equals(nameSP) && userName.equals(userNameSP) && email.equals(emailSP) && isThereAnyProfilePictureSelected == false)
            return "nothing";

        return "success";
    }

    public File getCompressedFile() {
        File file = null;
        try {
            file = new Compressor(context)
                    .setQuality(80)
                    .setMaxHeight(640)
                    .setMaxHeight(480)
                    .compressToFile(cameraFile);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("compressError", e.toString());
            return file;
        }
    }

    public void updateSP(String name , String userName , String email ,String isUserUploadedProfilePicture)
    {
        Log.i("updateUserNameSP","userName : "+userName);
        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(UserLoginInformationSP.sharedPrefferenceName,Context.MODE_PRIVATE).edit();
        sharedPreferencesEditor.putString(UserLoginInformationSP.nameSP,name);
        sharedPreferencesEditor.putString(UserLoginInformationSP.userNameSP,userName);
        sharedPreferencesEditor.putString(UserLoginInformationSP.emailAddressSP,email);
        sharedPreferencesEditor.putString(UserLoginInformationSP.isUserUploadedProfilePictureSP,isUserUploadedProfilePicture);
        sharedPreferencesEditor.commit();
        sharedPreferencesEditor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (cameFromWhere.contains("UserProfileActivity"))
            startUserProfileActivityAfterUserPostUpdate();
    }

    public void startUserProfileActivityAfterUserPostUpdate()
    {
        Intent intent = new Intent(this,UserProfileActivity.class);
        intent.putExtra("cameFromWhere","UserRegistrationActivity");
        startActivity(intent);
    }
}
