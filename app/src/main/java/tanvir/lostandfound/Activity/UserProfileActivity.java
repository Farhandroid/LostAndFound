package tanvir.lostandfound.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import ru.whalemare.sheetmenu.SheetMenu;
import tanvir.lostandfound.Adapter.RecyclerAdapterForUserrsProfile;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.UserLoginInformationSP;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.UserProfileItem;
import tanvir.lostandfound.R;

public class UserProfileActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerAdapterForUserrsProfile recyclerAdapterForUserrsProfile;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        recyclerView = findViewById(R.id.recyclerViewInUserProfile);
        progressDialog = new ProgressDialog(this);
        retriveUsersAllPostDataFromServer();
    }

    public void retriveUsersAllPostDataFromServer() {
        progressDialog.showProgressDialog("Please Wait...");

        ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ArrayList<UserProfileItem>> arrayListCall = apiConfig.getUsersAllPost("farhan");

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
        recyclerAdapterForUserrsProfile = new RecyclerAdapterForUserrsProfile(usersAllPostArrayList, this);
        recyclerView.setAdapter(recyclerAdapterForUserrsProfile);


    }

    public void showSheetMenu() {
        SheetMenu.with(this).setTitle("Select Option").setMenu(R.menu.user_profile_option).setAutoCancel(true).setClick(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.editProfile) {
                } else if (item.getItemId() == R.id.changePassword) {
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
}
