package tanvir.lostandfound.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.whalemare.sheetmenu.SheetMenu;
import tanvir.lostandfound.Adapter.RecyclerAdapterForUserProfile;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.UserLoginInformationSP;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.PojoClass.UserProfileItem;
import tanvir.lostandfound.R;

public class UserProfileActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerAdapterForUserProfile recyclerAdapterForUserProfile;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    private String cameFromWhere="";
    private int scrollingPosition=0;
    private CircleImageView userProfileImageInUP;
    private TextView nameTV , emailTV , phoneNOTV , noPostTV;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //setContentView(R.layout.activity_user_profile);
        setContentView(R.layout.activity_user_profile_test);
        //setContentView(R.layout.activity_user_profile_without_collapsing_toolbar);
        recyclerView = findViewById(R.id.recyclerViewInUserProfile);
        progressDialog = new ProgressDialog(this);
        userProfileImageInUP=findViewById(R.id.userProfileImageInUP);
        nameTV=findViewById(R.id.userNameInUP);
        emailTV=findViewById(R.id.emailInUP);
        phoneNOTV=findViewById(R.id.phoneInUP);
        noPostTV=findViewById(R.id.noPostTV);
        setUserInformation();
        ///retriveUsersAllPostDataFromServer();
        retriveCameFromWhereFromIntent();
    }

    private void setUserInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserLoginInformationSP.sharedPrefferenceName, Context.MODE_PRIVATE);
        String isUserUploadedProfilePicture=sharedPreferences.getString("isUserUploadedProfilePicture","");
        userName = sharedPreferences.getString("userName","");
        Log.i("userNameInProfile","userName : "+userName);
        Log.i("profilePicUserProfile",isUserUploadedProfilePicture);
        String email = sharedPreferences.getString("emailAddress","");
        String phoneNo = sharedPreferences.getString("phoneNumber","");
        phoneNOTV.setText(phoneNo);
        emailTV.setText(email);
        nameTV.setText(userName);
        if (!isUserUploadedProfilePicture.equals("0"))
        {
            RequestOptions options = new RequestOptions();
            options.signature(new ObjectKey(System.currentTimeMillis()));
            options.placeholder(getResources().getDrawable(R.drawable.image_blank_user_profile_icon));

            Glide.with(this)
                    .load("http://www.farhandroid.com/Lost&Found/ScriptRetrofit/UserProfilePic/"+userName+".jpg")
                    .apply(options)
                    .into(userProfileImageInUP);
        }
        else
        {
            Log.i("noProfileImage","noProfileImage");
            Glide.with(this)
                    .load(getResources().getDrawable(R.drawable.image_blank_user_profile_icon))
                    .into(userProfileImageInUP);
        }
    }

    public void retriveUsersAllPostDataFromServer() {
        progressDialog.showProgressDialog("Please Wait...");
        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ArrayList<UserProfileItem>> arrayListCall = apiConfig.getUsersAllPost(userName);
        arrayListCall.enqueue(new Callback<ArrayList<UserProfileItem>>() {
            @Override
            public void onResponse(Call<ArrayList<UserProfileItem>> call, retrofit2.Response<ArrayList<UserProfileItem>> response) {
                progressDialog.hideProgressDialog();

                ArrayList<UserProfileItem> usersAllPostArrayList = response.body();
                if (usersAllPostArrayList.size() > 0) {
                    Log.d("retrofitResponseUP", "" + usersAllPostArrayList.toString());
                    String message = "" + usersAllPostArrayList.get(0).getMessage();
                    Log.d("retrofitResponseUP : ", message);

                    if (message.contains("DataRetrive Success")) {
                        Log.d("detailedDescription", usersAllPostArrayList.get(0).getItemDetailedDescription());
                        Log.d("itemType", "" + usersAllPostArrayList.get(0).getItemType());
                        updateRecyclerView(usersAllPostArrayList);
                    } else Log.d("retrofitResponseUP : ", "dataRetriveFailed");
                } else Log.d("retrofitResponseUP : ", "UP ArrayList is empty");
            }

            @Override
            public void onFailure(Call<ArrayList<UserProfileItem>> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("retrofitError", "" + t.getMessage());
                Toast.makeText(UserProfileActivity.this, "retrofitError : " + "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateRecyclerView(ArrayList<UserProfileItem> usersAllPostArrayList) {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapterForUserProfile = new RecyclerAdapterForUserProfile(usersAllPostArrayList, this);
        recyclerView.setAdapter(recyclerAdapterForUserProfile);
        if (usersAllPostArrayList.size()==0)
            noPostTV.setText(Html.fromHtml(getString(R.string.you_didn_t_create_any_post_create_one)));;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                    scrollingPosition=linearLayoutManager.findLastVisibleItemPosition();

            }
        });
    }

    public void showSheetMenu() {
        SheetMenu.with(this).setTitle("Select Option").setMenu(R.menu.user_profile_option).setAutoCancel(true).setClick(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.editProfile) {
                    Intent intent = new Intent(UserProfileActivity.this,UserRegistrationActivity.class);
                    intent.putExtra("cameFromWhere","UserProfileActivity");
                    startActivity(intent);
                } else if (item.getItemId() == R.id.changePassword) {
                    showChangePasswordDialog();
                } else if (item.getItemId() == R.id.logOut) {
                    UserLoginInformationSP userLoginInformationSP=new UserLoginInformationSP(UserProfileActivity.this);
                    userLoginInformationSP.clearUserInformationSharedPrefference();
                    EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
                    enterOrBackFromActivity.startUserLoginActivity(UserProfileActivity.this);
                }
                return false;
            }
        }).show();
    }

    public void showProfileEditMenu(View view) {
        showSheetMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
        enterOrBackFromActivity.backToHomePageActivity(UserProfileActivity.this);
    }


    public void retriveCameFromWhereFromIntent()
    {
        try {
            cameFromWhere=getIntent().getStringExtra("cameFromWhere");
            if (cameFromWhere.contains("UserPostViewActivity"))
            {
                ArrayList<UserProfileItem> usersAllPostArrayList;
                usersAllPostArrayList= (ArrayList<UserProfileItem>) getIntent().getSerializableExtra("userAllPostArrayList");
                scrollingPosition = getIntent().getIntExtra("scrollingPosition",0);
                updateRecyclerView(usersAllPostArrayList);
                if (scrollingPosition>0)
                    recyclerView.scrollToPosition(scrollingPosition);
            }
            else retriveUsersAllPostDataFromServer();
        } catch (Exception e) {
            retriveUsersAllPostDataFromServer();
            e.printStackTrace();
            Log.d("errorcameFromWhere",e.toString());
        }
    }

    public int getScrollingPosition()
    {
        return scrollingPosition;
    }

    public void showChangePasswordDialog()
    {
        final View changePasswordDialogView;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        changePasswordDialogView=layoutInflater.inflate(R.layout.dialog_change_password,null);
        dialogBuilder.setView(changePasswordDialogView);
        Button changePasswordBTN=changePasswordDialogView.findViewById(R.id.changePasswordBTN);
        final AlertDialog alertDialog=dialogBuilder.create();
        changePasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText oldPasswordET = changePasswordDialogView.findViewById(R.id.oldPasswordInChangePassword);
                EditText newPasswordET =changePasswordDialogView.findViewById(R.id.newPasswordInChangePassword);
                EditText retypedNewPasswordET = changePasswordDialogView.findViewById(R.id.reenterNewPasswordInChangePassword);

                String oldPassword =oldPasswordET.getText().toString();
                String newPassword =newPasswordET.getText().toString();
                String retypedNewPassword =retypedNewPasswordET.getText().toString();
                Log.i("oldPassword",oldPassword);
                Log.i("newPassword",newPassword);
                Log.i("retypedNewPassword",retypedNewPassword);
                if (!newPassword.equals(retypedNewPassword))
                {
                    Toast.makeText(UserProfileActivity.this, "Password doesn't match ", Toast.LENGTH_SHORT).show();
                    newPasswordET.requestFocus();
                    newPasswordET.setError("Password doesn't match");
                    retypedNewPasswordET.requestFocus();
                    retypedNewPasswordET.setError("Password doesn't match");
                }
                else changePasswordInServer(oldPassword, newPassword,alertDialog);
            }
        });

        alertDialog.show();
    }
    public void changePasswordInServer(String oldPassword, String newPassword, final AlertDialog dialog)
    {
        progressDialog.showProgressDialog("Please Wait...");
        ApiConfig apiConfig=AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> changePasswordCall = apiConfig.changePassword(userName,oldPassword,newPassword);
        changePasswordCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                progressDialog.hideProgressDialog();
                ServerResponse serverResponse=response.body();
                String message = serverResponse.getMessage();
                Log.i("changePasswordRTFT",message);
                if (message.contains("User not found") || message.contains("Password update fail") || message.contains("User not found") || message.contains("Password doesn't match"))
                    Toast.makeText(UserProfileActivity.this, "Pasword change failed \nPlease try again", Toast.LENGTH_LONG).show();
                else
                {
                    dialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Pasword changed sucessfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.i("changePasswordRTFTError",t.getMessage());
            }
        });
    }

}
