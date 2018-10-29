package tanvir.lostandfound.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tanvir.lostandfound.Adapter.SwipeAdapterForPostCreaion;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.UserLoginInformationSP;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.R;

public class UserPostCreateAndEditActivity extends AppCompatActivity {

    private ViewPager viewPager;
    SwipeAdapterForPostCreaion customSwipeAdapterForPostCreaion;
    private Context context;
    private EditText itemTimeET,
            itemDateET,
            itemPlaceET,
            itemTypeET,
            itemRewardET,
            itemDetailedDescriptionET;
    private String itemTime,
            itemDate,
            itemFullAddress,
            itemType,
            itemReward,
            itemDetailedDescription,
            itemPlaceAdress,
            itemPlaceName,
            postDateAndTime,
            userName,
            oldPostDateAndTimeForPostUpdate;

    static final int PLACE_PICKER_REQUEST = 1;
    ProgressDialog progressDialog;
    private ArrayList<Uri> imagePath, imageToUploadForPostUpdate;
    private ArrayList<String> imageToDeleteForUpdatePost, imageNameListForPostUpdate, imagePathOrNameInString;
    Dialog dialog;
    private String cameFromWhere = "";
    static final int REQUEST_IMAGE_CAPTURE_USING_CAMERA = 2;
    private boolean isThisActivityForPostUpdate = false;
    private boolean isThreadForImageConvertStillRunning = false;
    private File cameraFile;
    private ArrayList<File> imageFileArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_creation);
        context = UserPostCreateAndEditActivity.this;
        itemTimeET = findViewById(R.id.lostItemTimeET);
        itemPlaceET = findViewById(R.id.lostItemPlaceET);
        itemDateET = findViewById(R.id.lostItemDateET);
        itemTypeET = findViewById(R.id.LostItemTypeET);
        itemRewardET = findViewById(R.id.LostItemRewardET);
        itemDetailedDescriptionET = findViewById(R.id.lostItemTimeETDetailedDescriptionET);
        itemDetailedDescriptionET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        imagePath = new ArrayList<>();
        imageToDeleteForUpdatePost = new ArrayList<>();
        imageToUploadForPostUpdate = new ArrayList<>();
        imageNameListForPostUpdate = new ArrayList<>();
        imagePathOrNameInString = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
        viewPager = findViewById(R.id.viewPagerInUserPostCreation);
        setUserName();
        getCameFromWhereFromIntent();
        initialView();
    }

    public void initialView() {
        if (cameFromWhere.contains("FoundFragmentUserProfileActivity") ||
                cameFromWhere.contains("LostFragmentUserProfileActivity")) {
            String itemCategory;
            if (cameFromWhere.contains("FoundFragmentUserProfileActivity"))
                itemCategory = "FoundItem";
            else
                itemCategory = "LostItem";
            Log.i("imagePathOrNameInString", Integer.toString(imagePathOrNameInString.size()));
            customSwipeAdapterForPostCreaion = new SwipeAdapterForPostCreaion(this, imagePathOrNameInString, true, itemCategory);
            //customSwipeAdapterForPostCreaion = new SwipeAdapterForPostCreaion(imageNameListForPostUpdate, this, true, itemCategory);
        } else {
            customSwipeAdapterForPostCreaion = new SwipeAdapterForPostCreaion(this, imagePathOrNameInString, false, "");
            ///customSwipeAdapterForPostCreaion = new SwipeAdapterForPostCreaion(this, imagePath, false);
        }
        viewPager.setAdapter(customSwipeAdapterForPostCreaion);
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
                String dayString, monthString;
                if (dayOfMonth < 10) dayString = "0" + Integer.toString(dayOfMonth);
                else dayString = Integer.toString(dayOfMonth);

                if (month + 1 < 10) monthString = "0" + Integer.toString(month + 1);
                else monthString = Integer.toString(month + 1);

                itemDateET.setText(dayString + "-" + monthString + "-" + Integer.toString(year));

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
                String hourString, minuteString;
                if (hourOfDay < 10) hourString = "0" + Integer.toString(hourOfDay);
                else hourString = Integer.toString(hourOfDay);

                if (minute < 10) minuteString = "0" + Integer.toString(minute);
                else minuteString = Integer.toString(minute);
                itemTimeET.setText(hourString + ":" + minuteString + " " + format);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String formattedDate = df.format(c.getTime());
        formattedDate = formattedDate.replace("a.m.", "AM").replace("p.m.", "PM");
        Log.i("formattedDate", formattedDate);
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
                imagePathOrNameInString.add("path: " + Uri.fromFile(file).toString());
                if (isThisActivityForPostUpdate)
                    imageToUploadForPostUpdate.add(Uri.fromFile(file));
                customSwipeAdapterForPostCreaion.notifyDataSetChanged();
            } else Log.d("fileNotExist", "fileNotExist");
        } else if (requestCode == Define.ALBUM_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                ArrayList<Uri> uriArrayList;
                uriArrayList = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                for (int i = 0; i < uriArrayList.size(); i++)
                    imagePathOrNameInString.add("path: " + uriArrayList.get(i).toString());

                imagePath.addAll(uriArrayList);
                if (isThisActivityForPostUpdate)
                    imageToUploadForPostUpdate.addAll(uriArrayList);
                customSwipeAdapterForPostCreaion.notifyDataSetChanged();
                if (uriArrayList == null) Log.d("uriArrayListIsNull", "uriArrayListIsNull");
                else Log.d("uriArrayList size : ", Integer.toString(uriArrayList.size()));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("errorInImagePath", e.toString());
            }
        } else Log.d("cameraCancelled", "cameraCancelled");
    }

    public void sendUserLostItemDataToServer(View view) {
        postDateAndTime = getCurrentDateAndTime();
        uploadUserLostItemPostImageToServer();
    }

    public void backToHomePageActivity() {
        EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
        enterOrBackFromActivity.BackToHomePageActivity(UserPostCreateAndEditActivity.this);
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
        dialog.setContentView(R.layout.dialog_image_picker);
        dialog.setCancelable(true);
        LinearLayout takePictureWithCamera = dialog.findViewById(R.id.takePictureWithCamera);
        takePictureWithCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) dialog.dismiss();
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
                if (dialog.isShowing()) dialog.dismiss();
                FishBun.with(UserPostCreateAndEditActivity.this).setImageAdapter(new GlideAdapter()).setMaxCount(10).setCamera(true).setActionBarColor(Color.parseColor("#607D8B"), Color.parseColor("#607D8B"), false).setActionBarTitleColor(Color.parseColor("#ffffff")).startAlbum();
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
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{imgId}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public String validateUserPostItemInformation() {
        itemReward = "";
        if (cameFromWhere.contains("LostFragment")) {
            itemReward = itemRewardET.getText().toString();
            if (!(itemReward.length() > 0)) {
                return "notFilled";
            }
        }
        itemFullAddress = itemPlaceET.getText().toString();
        itemDetailedDescription = itemDetailedDescriptionET.getText().toString();
        itemTime = itemTimeET.getText().toString();
        itemDate = itemDateET.getText().toString();
        itemType = itemTypeET.getText().toString();

        if (itemType.length() > 0
                && itemTime.length() > 0
                && itemDate.length() > 0
                && itemFullAddress.length() > 0
                && itemDetailedDescription.length() > 0) {
            return "filled";
        } else return "notFilled";
    }

    public void uploadUserLostItemPostImageToServer() {
        String result = validateUserPostItemInformation();
        if (result.equals("filled")) {
            if (isThisActivityForPostUpdate) updateUserPostData();
            else if (cameFromWhere.contains("LostFragment")) uploadLostItemPostData();
            else if (cameFromWhere.contains("FoundFragment")) uploadFoundItemPostData();

        } else Toast.makeText(context, "Please Provide All Data", Toast.LENGTH_LONG).show();

    }


    public ArrayList<File> getFileArrayList() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        final ArrayList<Uri> uriArrayList;
        Log.i("imageToUploadSize", Integer.toString(imageToUploadForPostUpdate.size()));
        if (isThisActivityForPostUpdate)
            uriArrayList = imageToUploadForPostUpdate;
        else
            uriArrayList = imagePath;


                if (uriArrayList != null) {
                    for (int i = 0; i < uriArrayList.size(); i++) {
                        Log.d("imagepathSize", Integer.toString(uriArrayList.size()));
                        File file;
                        if (cameraFile != null) {
                            file = cameraFile;
                        } else {
                            String filepath = getRealPathFromDocumentUri(context, uriArrayList.get(i));
                            file = new File(filepath);
                        }
                        try {
                            file = new Compressor(UserPostCreateAndEditActivity.this)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setQuality(80)
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
            cameFromWhere = "";
            cameFromWhere = getIntent().getStringExtra("cameFromWhere");
            Log.d("cameFromWhere", cameFromWhere);
            if (cameFromWhere.contains("FoundFragment") || cameFromWhere.contains("FoundFragmentUserProfileActivity")) {
                itemRewardET.setVisibility(View.GONE);
                if (cameFromWhere.contains("FoundFragmentUserProfileActivity")) {
                    isThisActivityForPostUpdate = true;
                    try {
                        FoundItemPost foundItemPost = (FoundItemPost) getIntent().getSerializableExtra("FoundItemData");
                        setFoundItemDataForPostUpdate(foundItemPost);
                        if (getIntent().getStringArrayListExtra("imageNameList") != null) {
                            imageNameListForPostUpdate = getIntent().getStringArrayListExtra("imageNameList");
                            imagePathOrNameInString.addAll(imageNameListForPostUpdate);
                            Log.i("imagePathString", Integer.toString(imageToUploadForPostUpdate.size()));
                            Log.d("imageNameListSize", Integer.toString(imageNameListForPostUpdate.size()));
                            ///customSwipeAdapterForPostCreaion.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("intentError", e.toString());
                    }

                }
            } else if (cameFromWhere.contains("LostFragment") || cameFromWhere.contains("LostFragmentUserProfileActivity")) {
                if (cameFromWhere.contains("LostFragmentUserProfileActivity")) {
                    isThisActivityForPostUpdate = true;
                }
                try {
                    LostItemPost lostItemPost = (LostItemPost) getIntent().getSerializableExtra("LostItemData");
                    setLostItemDataForPostUpdate(lostItemPost);
                    if (getIntent().getStringArrayListExtra("imageNameList") != null) {
                        imageNameListForPostUpdate = getIntent().getStringArrayListExtra("imageNameList");
                        imagePathOrNameInString.addAll(imageNameListForPostUpdate);
                        Log.d("imageNameListSize", Integer.toString(imageNameListForPostUpdate.size()));
                        ////customSwipeAdapterForPostCreaion.notifyDataSetChanged();
                        //initialView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("intentError", e.toString());
                }

                itemRewardET.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("errorCameFromWhere", e.toString());
        }
    }

    public void uploadLostItemPostData() {
        progressDialog.showProgressDialog("Uploding...", true);
        ArrayList<File> fileArrayList = getFileArrayList();
        ArrayList<MultipartBody.Part> partArrayList = new ArrayList<>();

        for (int i = 0; i < fileArrayList.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileArrayList.get(i));
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file" + Integer.toString(i + 1), fileArrayList.get(i).getName(), requestBody);
            partArrayList.add(fileToUpload);
        }

        RequestBody postDateAndTimeRB = RequestBody.create(MediaType.parse("text/plain"), postDateAndTime);
        RequestBody lostItemTypeRB = RequestBody.create(MediaType.parse("text/plain"), itemType);
        RequestBody userNameRB = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody placeNameRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceName);
        RequestBody placeAddressRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceAdress);
        RequestBody dateRB = RequestBody.create(MediaType.parse("text/plain"), itemDate);
        RequestBody timeRB = RequestBody.create(MediaType.parse("text/plain"), itemTime);
        RequestBody rewardRB = RequestBody.create(MediaType.parse("text/plain"), itemReward);
        RequestBody descriptionRB = RequestBody.create(MediaType.parse("text/plain"), itemDetailedDescription);

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        retrofit2.Call<ServerResponse> call = apiConfig.sendUserLostItemPostDataToServer(partArrayList, partArrayList.size(), userNameRB, postDateAndTimeRB, lostItemTypeRB, placeNameRB, placeAddressRB, dateRB, timeRB, rewardRB, descriptionRB);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                String RspnseInLostItemInsert = serverResponse.getMessage();
                Log.d("RspnseInLostItemInsert", "" + RspnseInLostItemInsert);
                progressDialog.hideProgressDialog();
                if (RspnseInLostItemInsert.contains("Data insertion Success")) {
                    Toast.makeText(context, "Data inserted successfully ", Toast.LENGTH_LONG).show();
                    EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
                    enterOrBackFromActivity.startHomePageActivity(UserPostCreateAndEditActivity.this);
                } else
                    Toast.makeText(context, "Error in data insertion . Please try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                Log.d("ErrorInLostItemInsert", "" + t.getMessage());
                ///Toast.makeText(context, "Error : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hideProgressDialog();
                Toast.makeText(context, "Error in data insertion . Please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadFoundItemPostData() {
        Log.d("EnterFound", "EnterFound");
        progressDialog.showProgressDialog("Uploding...", true);
        ArrayList<File> fileArrayList = getFileArrayList();
        ArrayList<MultipartBody.Part> partArrayList = new ArrayList<>();

        for (int i = 0; i < fileArrayList.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileArrayList.get(i));
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file" + Integer.toString(i + 1), fileArrayList.get(i).getName(), requestBody);
            partArrayList.add(fileToUpload);
        }

        RequestBody postDateAndTimeRB = RequestBody.create(MediaType.parse("text/plain"), postDateAndTime);
        RequestBody lostItemTypeRB = RequestBody.create(MediaType.parse("text/plain"), itemType);
        RequestBody userNameRB = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody placeNameRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceName);
        RequestBody placeAddressRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceAdress);
        RequestBody dateRB = RequestBody.create(MediaType.parse("text/plain"), itemDate);
        RequestBody timeRB = RequestBody.create(MediaType.parse("text/plain"), itemTime);
        RequestBody descriptionRB = RequestBody.create(MediaType.parse("text/plain"), itemDetailedDescription);

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        retrofit2.Call<ServerResponse> call = apiConfig.sendUserFoundItemPostDataToServer(partArrayList, partArrayList.size(), userNameRB, postDateAndTimeRB, lostItemTypeRB, placeNameRB, placeAddressRB, dateRB, timeRB, descriptionRB);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progressDialog.hideProgressDialog();
                ServerResponse serverResponse = response.body();
                String RspnseInFoundItemInsert = serverResponse.getMessage();
                Log.i("RspnseInFoundItemInsert", "" + serverResponse.getMessage());
                if (RspnseInFoundItemInsert.contains("Data insertion Success")) {
                    Toast.makeText(context, "Data inserted successfully ", Toast.LENGTH_LONG).show();
                    EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
                    enterOrBackFromActivity.startHomePageActivity(UserPostCreateAndEditActivity.this);
                } else
                    Toast.makeText(context, "Error in data insertion . Please try again later", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("ErrorInFoundItemInsert", "" + t.getMessage());
                ///Toast.makeText(context, "Error : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Error in data insertion . Please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendArrayListToServer() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("abul");
        stringArrayList.add("mofiz");
        stringArrayList.add("abul");
        stringArrayList.add("abul");
        stringArrayList.add("abul");
        stringArrayList.add("abul");
        stringArrayList.add("mofiz");

        ///String[] stringArray = {"abul", "mofiz", "karim"};
        String jsonString = new Gson().toJson(stringArrayList);
        ArrayList<String> fromJson = new Gson().fromJson(jsonString, ArrayList.class);
        Log.i("fromJson", fromJson.get(1));
       /* ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> serverResponseCall = apiConfig.testArrayList(jsonString);
        serverResponseCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                Log.d("arrayresponse : ", serverResponse.getMessage());
                Toast.makeText(context, "Response : " + "" + serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("retrofitErrorrray", "" + t.getMessage());
                Toast.makeText(context, "Error : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public void addImageToUploadForPostUpdate() {

    }

    public void addImageToDeleteForPostUpdate(String imageName, int position) {
        if (imageName.startsWith("path:") == false)
            imageToDeleteForUpdatePost.add(imageName);
        imagePathOrNameInString.remove(position);
        customSwipeAdapterForPostCreaion.notifyDataSetChanged();
        Log.i("imageToDeleteName", imageToDeleteForUpdatePost.get(imageToDeleteForUpdatePost.size() - 1));
        Log.i("imgToDlteForUpdatePost", Integer.toString(imageToDeleteForUpdatePost.size()));
    }

    public void setFoundItemDataForPostUpdate(FoundItemPost foundItemData) {
        itemType = foundItemData.getWhatIsFound();
        itemDate = foundItemData.getDayOfFound();
        itemFullAddress = foundItemData.getFoundItemPlaceName();
        itemPlaceAdress = foundItemData.getFoundItemAdress();
        itemPlaceName = foundItemData.getFoundItemPlaceName();
        itemTime = foundItemData.getTimeOfFound();
        oldPostDateAndTimeForPostUpdate = foundItemData.getPostDateAndTime();
        Log.i("postDateFound", "Found : " + oldPostDateAndTimeForPostUpdate);
        itemDetailedDescription = foundItemData.getDetailedDescription();
        itemReward = "0";

        itemTypeET.setText(foundItemData.getWhatIsFound());
        itemDateET.setText(foundItemData.getDayOfFound());
        itemTimeET.setText(foundItemData.getTimeOfFound());
        itemPlaceET.setText(foundItemData.getFoundItemPlaceName() + "\n" + foundItemData.getFoundItemAdress());
        itemDetailedDescriptionET.setText(foundItemData.getDetailedDescription());
    }

    public void setLostItemDataForPostUpdate(LostItemPost lostItemData) {
        itemType = lostItemData.getWhatIsLost();
        itemDate = lostItemData.getDayOfLost();
        itemFullAddress = lostItemData.getLostItemPlaceName();
        itemPlaceAdress = lostItemData.getLostItemAdress();
        itemPlaceName = lostItemData.getLostItemPlaceName();
        itemTime = lostItemData.getTimeOfLost();
        oldPostDateAndTimeForPostUpdate = lostItemData.getPostDateAndTime();
        Log.i("postDateLost", "Lost " + oldPostDateAndTimeForPostUpdate);
        itemDetailedDescription = lostItemData.getDetailedDescription();
        itemReward = lostItemData.getReward();

        itemTypeET.setText(lostItemData.getWhatIsLost());
        itemDateET.setText(lostItemData.getDayOfLost());
        itemTimeET.setText(lostItemData.getTimeOfLost());
        itemPlaceET.setText(lostItemData.getLostItemPlaceName() + "\n" + lostItemData.getLostItemAdress());
        itemDetailedDescriptionET.setText(lostItemData.getDetailedDescription());
        itemRewardET.setText(lostItemData.getReward());
    }


    public void updateUserPostData() {
        Log.i("updateUserPostData", "updateUserPostData");
        progressDialog.showProgressDialog("Updating...", true);
        ArrayList<File> fileArrayList = getFileArrayList();
        ArrayList<MultipartBody.Part> partArrayList = new ArrayList<>();
        for (int i = 0; i < fileArrayList.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileArrayList.get(i));
            MultipartBody.Part part = MultipartBody.Part.createFormData("file" + Integer.toString(i + 1), fileArrayList.get(i).getName(), requestBody);
            partArrayList.add(part);
        }

        String itemCategory;
        if (cameFromWhere.contains("LostFragmentUserProfileActivity"))
            itemCategory = "LostItem";
        else
            itemCategory = "FoundItem";

        Log.i("itemReward", itemReward);
        Log.i("imagePath_size", Integer.toString(imagePath.size()));
        Log.i("imageToUploadForPost", Integer.toString(imageToUploadForPostUpdate.size()));
        Log.i("itemCategory", itemCategory);
        Log.i("userName", userName);
        Log.i("itemPlaceAdress", itemPlaceAdress);
        Log.i("oldPostDateAndTimeFor", oldPostDateAndTimeForPostUpdate);
        Log.i("CurrentDateAndTime", getCurrentDateAndTime());
        Log.i("itemPlaceName", itemPlaceName);
        Log.i("itemDate", itemDate);
        Log.i("itemTime", itemTime);
        Log.i("itemDetailedDescription", itemDetailedDescription);
        Log.i("itemType", itemType);
        Log.i("imageToUploadForPost", Integer.toString(imageToUploadForPostUpdate.size()));
        //Log.i("imageToDeleteString",imageToDeleteString);

        RequestBody ItemCategoryRB = RequestBody.create(MediaType.parse("text/plain"), itemCategory);
        RequestBody UserNameRB = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody ItemAdressRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceAdress);
        RequestBody OldPostDateAndTimeRB = RequestBody.create(MediaType.parse("text/plain"), oldPostDateAndTimeForPostUpdate);
        RequestBody NewPostDateAndTimeRB = RequestBody.create(MediaType.parse("text/plain"), getCurrentDateAndTime());
        RequestBody ItemPlaceNameRB = RequestBody.create(MediaType.parse("text/plain"), itemPlaceName);
        RequestBody ItemDateRB = RequestBody.create(MediaType.parse("text/plain"), itemDate);
        RequestBody ItemTimeRB = RequestBody.create(MediaType.parse("text/plain"), itemTime);
        RequestBody ItemRewardRB = RequestBody.create(MediaType.parse("text/plain"), itemReward);
        RequestBody ItemDEtailedDescriptionRB = RequestBody.create(MediaType.parse("text/plain"), itemDetailedDescription);
        RequestBody ItemTypeRB = RequestBody.create(MediaType.parse("text/plain"), itemType);
        RequestBody HowManyImageRB = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(imagePathOrNameInString.size()));
        RequestBody NumberOfImageToUploadRB = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(imageToUploadForPostUpdate.size()));
        RequestBody ImageToDeleteRB = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(imageToDeleteForUpdatePost));
        //RequestBody ImageToDeleteRB = RequestBody.create(MediaType.parse("text/plain"),imageToDeleteString);

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> updateUserPostDataCall = apiConfig.updateUserPost(
                partArrayList,
                ItemCategoryRB,
                UserNameRB,
                OldPostDateAndTimeRB,
                NewPostDateAndTimeRB,
                ItemTypeRB,
                ItemPlaceNameRB,
                ItemAdressRB,
                ItemDateRB,
                ItemTimeRB,
                ItemRewardRB,
                ItemDEtailedDescriptionRB,
                HowManyImageRB,
                ImageToDeleteRB,
                NumberOfImageToUploadRB);

        updateUserPostDataCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                progressDialog.hideProgressDialog();
                ServerResponse serverResponse = response.body();
                String message = serverResponse.getMessage();
                Log.i("responsePstUpdteRTFT", "" + message);
                if (message.contains("Post updated successfully")) {
                    Toast.makeText(context, "Post updated successfully", Toast.LENGTH_LONG).show();
                    EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
                    enterOrBackFromActivity.backToUserProfileActivity(UserPostCreateAndEditActivity.this);
                } else
                    Toast.makeText(context, "Error in post update . Please try again later", Toast.LENGTH_LONG).show();

                ///Toast.makeText(context, "Response "+serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.i("errorPostUpdateRTFT", t.getMessage());
                Toast.makeText(context, "Error in post update . Please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserLoginInformationSP.sharedPrefferenceName, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "");
        if (userName.length() == 0)
            Log.i("userNameIsEmpty", "userNameIsEmpty");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isThisActivityForPostUpdate) {
            Intent intent = new Intent(UserPostCreateAndEditActivity.this, UserPostViewActivity.class);
            intent.putExtra("cameFromWhere", cameFromWhere);
            startActivity(intent);
            this.overridePendingTransition(R.anim.right_in, R.anim.right_out);
        } else {
            EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
            enterOrBackFromActivity.backToHomePageActivity(UserPostCreateAndEditActivity.this);
        }
    }

}
