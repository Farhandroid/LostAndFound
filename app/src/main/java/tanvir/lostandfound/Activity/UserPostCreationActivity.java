package tanvir.lostandfound.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import tanvir.lostandfound.Adapter.SwipeAdapter;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.ServerResponse;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.R;

public class UserPostCreationActivity extends AppCompatActivity {

    private ViewPager viewPager;
    SwipeAdapter customSwipeAdapter;
    private Context context;
    private EditText itemTimeET, itemDateET, itemPlaceET, itemTypeET, itemRewardET, itemDetailedDescriptionET;
    private String itemTime, itemDate, itemPlace, itemType, itemReward, itemDetailedDescription;
    static final int PLACE_PICKER_REQUEST = 1;
    private String itemPlaceAdress, itemPlaceName;
    ProgressDialog progressDialog;
    private ArrayList<Uri> imagePath;
    Dialog dialog;
    private String postDateAndTime , cameFromWhere;
    static final int REQUEST_IMAGE_CAPTURE_USING_CAMERA = 2;
    private File cameraFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_creation);
        context = UserPostCreationActivity.this;
        itemTimeET = findViewById(R.id.lostItemTimeET);
        itemPlaceET = findViewById(R.id.lostItemPlaceET);
        itemDateET = findViewById(R.id.lostItemDateET);
        itemTypeET = findViewById(R.id.LostItemTypeET);
        itemRewardET = findViewById(R.id.LostItemRewardET);
        itemDetailedDescriptionET = findViewById(R.id.lostItemTimeETDetailedDescriptionET);
        itemDetailedDescriptionET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        imagePath = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
        viewPager = findViewById(R.id.viewPagerInUserPostCreation);
        getCameFromWhereFromIntent();
        initialView();
    }

    public void initialView() {
        customSwipeAdapter = new SwipeAdapter(this, imagePath);
        viewPager.setAdapter(customSwipeAdapter);
        CirclePageIndicator indicator = findViewById(R.id.CirclePageIndicatorInUserPostCreation);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
    }


    public void showDatePickerDialog(View view) {

        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("day", Integer.toString(dayOfMonth));
                itemDateET.setText(Integer.toString(dayOfMonth) + "-" + Integer.toString(month) + 1 + "-" + Integer.toString(year));

            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String format;
                if (hourOfDay == 0) {
                    hourOfDay += 12;
                    format = "AM";
                } else if (hourOfDay == 12) {
                    format = "PM";
                } else if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }
                itemTimeET.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute) + format);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:s");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public void showPlaceAutocompleteIntent(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                itemPlaceName = place.getName().toString();
                itemPlaceAdress = place.getAddress().toString();
                itemPlaceET.setText(place.getName().toString() + "\n" + place.getAddress().toString());
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_USING_CAMERA && resultCode == RESULT_OK) {
            String path = "/sdcard/lost_and_found/image.jpg";
            File file = new File(path);
            cameraFile = file;
            if (file.exists()) {
                imagePath.add(Uri.fromFile(file));
                customSwipeAdapter.notifyDataSetChanged();
            } else
                Log.d("fileNotExist", "fileNotExist");
        } else if (requestCode == Define.ALBUM_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                ArrayList<Uri> uriArrayList;
                uriArrayList = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                imagePath.addAll(uriArrayList);
                customSwipeAdapter.notifyDataSetChanged();
                if (uriArrayList == null)
                    Log.d("uriArrayListIsNull", "uriArrayListIsNull");
                else
                    Log.d("uriArrayList size : ", Integer.toString(uriArrayList.size()));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("errorInImagePath", e.toString());
            }

        } else
            Log.d("cameraCancelled", "cameraCancelled");
    }

    public void sendUserLostItemDataToServer(View view) {
        postDateAndTime = getCurrentDateAndTime();
        uploadUserLostItemPostImageToServer();
    }

    public void backToHomePageActivity() {
        EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
        enterOrBackFromActivity.BackToHomePageActivity(UserPostCreationActivity.this);
    }

    public File getFile() {
        File folder = new File("/sdcard/lost_and_found");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File imgFile = new File(folder, "image.jpg");
        return imgFile;
    }

    public void showImagePickerDialogInUserItemLostActivity(View view) {
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

                FishBun.with(UserPostCreationActivity.this)
                        .setImageAdapter(new GlideAdapter())
                        .setMaxCount(10)
                        .setCamera(true)
                        .setActionBarColor(Color.parseColor("#607D8B"), Color.parseColor("#607D8B"), false)
                        .setActionBarTitleColor(Color.parseColor("#ffffff"))
                        .startAlbum();

            }
        });
        dialog.show();
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

    public String validateUserPostItemInformation() {

        if(cameFromWhere.contains("LostFragment"))
        {
            itemReward = itemRewardET.getText().toString();
            if(!(itemReward.length() > 0))
            {
                return "notFilled";
            }
        }
        itemPlace = itemPlaceET.getText().toString();
        itemDetailedDescription = itemDetailedDescriptionET.getText().toString();
        itemTime = itemTimeET.getText().toString();
        itemDate = itemDateET.getText().toString();
        itemType = itemTypeET.getText().toString();

        if (itemType.length() > 0 && itemTime.length() > 0  && itemDate.length() > 0 && itemPlace.length() > 0 && itemDetailedDescription.length() > 0) {
            return "filled";
        } else
            return "notFilled";
    }


    public void uploadUserLostItemPostImageToServer() {

        String result = validateUserPostItemInformation();
        if (result.contains("filled")) {
            if (cameFromWhere.contains("LostFragment"))
                uploadLostItemPostData();
            else if (cameFromWhere.contains("FoundFragment"))
            {
                uploadFoundItemPostData();
            }

        } else {
            Toast.makeText(context, "Please Provide All Data", Toast.LENGTH_LONG).show();
        }
    }


    public ArrayList<File> getFileArrayList() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        if (imagePath != null) {
            for (int i = 0; i < imagePath.size(); i++) {
                Log.d("imagepathSize", Integer.toString(imagePath.size()));
                File file;
                if (cameraFile != null) {
                    file = cameraFile;
                } else {
                    String filepath = getRealPathFromDocumentUri(context, imagePath.get(i));
                    file = new File(filepath);
                }

                try {
                    file = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(60)
                            .compressToFile(file);
                    fileArrayList.add(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("compressError", e.toString());
                }
            }
        }
        return fileArrayList;
    }

    public void getCameFromWhereFromIntent() {
        try {
            cameFromWhere = getIntent().getStringExtra("cameFromWhere");
            Log.d("cameFromWhere",cameFromWhere);
            if (cameFromWhere.contains("FoundFragment"))
            {
                itemRewardET.setVisibility(View.GONE);
            }
            else if (cameFromWhere.contains("LostFragment"))
                itemRewardET.setVisibility(View.VISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("errorCameFromWhere",e.toString());
        }
    }

    public void uploadLostItemPostData()
    {
        progressDialog.showProgressDialog("Uploding...", true);
        ArrayList<File> fileArrayList = getFileArrayList();
        ArrayList<MultipartBody.Part> partArrayList = new ArrayList<>();

        for (int i = 0; i < fileArrayList.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileArrayList.get(i));
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file" + Integer.toString(i + 1), fileArrayList.get(i).getName(), requestBody);
            partArrayList.add(fileToUpload);
        }

        RequestBody postDateAndTimeRB = RequestBody.create(MediaType.parse("text/plain"),postDateAndTime);
        RequestBody lostItemTypeRB = RequestBody.create(MediaType.parse("text/plain"), itemType);
        RequestBody userNameRB = RequestBody.create(MediaType.parse("text/plain"),"farhan");
        RequestBody placeNameRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceName);
        RequestBody placeAddressRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceAdress);
        RequestBody dateRB = RequestBody.create(MediaType.parse("text/plain"), itemDate);
        RequestBody timeRB = RequestBody.create(MediaType.parse("text/plain"), itemTime);
        RequestBody rewardRB = RequestBody.create(MediaType.parse("text/plain"), itemReward);
        RequestBody descriptionRB = RequestBody.create(MediaType.parse("text/plain"), itemDetailedDescription);

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        ///retrofit2.Call<ServerResponse> call = apiConfig.sendUserLostItemPostImageToServer(partArrayList, partArrayList.size(), "farhan", postDateAndTime,itemType,itemPlaceName,itemPlaceAdress,itemDate,itemTime,itemReward,itemDetailedDescription);
        retrofit2.Call<ServerResponse> call = apiConfig.sendUserLostItemPostDataToServer(partArrayList, partArrayList.size(), userNameRB, postDateAndTimeRB,lostItemTypeRB,placeNameRB,placeAddressRB,dateRB,timeRB,rewardRB,descriptionRB);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                Log.d("retrofitResponse", "" + serverResponse.getImageResponse());
                Log.d("retrofitResponse2", "" + serverResponse.getQueryResponse());
                Toast.makeText(context, "Server Response : " + "" + serverResponse.getImageResponse(), Toast.LENGTH_SHORT).show();
                progressDialog.hideProgressDialog();
            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                Log.d("retrofitError", "" + t.getMessage());
                Toast.makeText(context, "Error : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hideProgressDialog();
            }
        });
    }

    public void uploadFoundItemPostData()
    {
        Log.d("EnterFound","EnterFound");
        progressDialog.showProgressDialog("Uploding...", true);
        ArrayList<File> fileArrayList = getFileArrayList();
        ArrayList<MultipartBody.Part> partArrayList = new ArrayList<>();

        for (int i = 0; i < fileArrayList.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileArrayList.get(i));
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file" + Integer.toString(i + 1), fileArrayList.get(i).getName(), requestBody);
            partArrayList.add(fileToUpload);
        }

        RequestBody postDateAndTimeRB = RequestBody.create(MediaType.parse("text/plain"),postDateAndTime);
        RequestBody lostItemTypeRB = RequestBody.create(MediaType.parse("text/plain"), itemType);
        RequestBody userNameRB = RequestBody.create(MediaType.parse("text/plain"),"farhan");
        RequestBody placeNameRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceName);
        RequestBody placeAddressRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceAdress);
        RequestBody dateRB = RequestBody.create(MediaType.parse("text/plain"), itemDate);
        RequestBody timeRB = RequestBody.create(MediaType.parse("text/plain"), itemTime);
        RequestBody descriptionRB = RequestBody.create(MediaType.parse("text/plain"), itemDetailedDescription);

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        retrofit2.Call<ServerResponse> call = apiConfig.sendUserFoundItemPostDataToServer(partArrayList, partArrayList.size(), userNameRB, postDateAndTimeRB,lostItemTypeRB,placeNameRB,placeAddressRB,dateRB,timeRB,descriptionRB);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progressDialog.hideProgressDialog();
                ServerResponse serverResponse = response.body();
                Log.d("retrofitResponse", "" + serverResponse.getImageResponse());
                Log.d("retrofitResponse2", "" + serverResponse.getQueryResponse());
                Toast.makeText(context, "Server Response : " + "" + serverResponse.getImageResponse(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("retrofitError", "" + t.getMessage());
                Toast.makeText(context, "Error : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
