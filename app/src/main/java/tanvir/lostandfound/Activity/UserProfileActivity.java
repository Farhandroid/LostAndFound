package tanvir.lostandfound.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import tanvir.lostandfound.Adapter.RecyclerAdapterForUsersAllItem;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.UserProfileItem;
import tanvir.lostandfound.R;

public class UserProfileActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerAdapterForUsersAllItem recyclerAdapterForUsersAllItem;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        recyclerView=findViewById(R.id.recyclerViewInUserProfile);
        progressDialog=new ProgressDialog(this);
        retriveUsersAllPostDataFromServer();
    }

    public void retriveUsersAllPostDataFromServer()
    {
        progressDialog.showProgressDialog("Please Wait...");

        ApiConfig apiConfig= AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ArrayList<UserProfileItem>> arrayListCall=apiConfig.getUsersAllPost("farhan");

        arrayListCall.enqueue(new Callback<ArrayList<UserProfileItem>>() {
            @Override
            public void onResponse(Call<ArrayList<UserProfileItem>> call, retrofit2.Response<ArrayList<UserProfileItem>> response) {
                progressDialog.hideProgressDialog();

                ArrayList<UserProfileItem> usersAllPostArrayList =response.body();
                if (usersAllPostArrayList.size()>0)
                {
                    Log.d("retrofitResponseUP",""+usersAllPostArrayList.toString());
                    String message = ""+usersAllPostArrayList.get(0).getMessage();
                    Log.d("retrofitResponseUP : ",message);

                    if (message.contains("DataRetrive Success"))
                    {
                        Log.d("detailedDescription", usersAllPostArrayList.get(0).getItemDetailedDescription());
                        Log.d("itemType", ""+ usersAllPostArrayList.get(0).getItemType());
                        updateRecyclerView(usersAllPostArrayList);
                    }
                    else
                        Log.d("retrofitResponseUP : ","dataRetriveFailed");

                }
                else
                    Log.d("retrofitResponseUP : ","UP ArrayList is empty");

            }

            @Override
            public void onFailure(Call<ArrayList<UserProfileItem>> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("retrofitError",""+t.getMessage());
                Toast.makeText(UserProfileActivity.this, "retrofitError : "+""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateRecyclerView( ArrayList<UserProfileItem> usersAllPostArrayList)
    {
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapterForUsersAllItem =new RecyclerAdapterForUsersAllItem(usersAllPostArrayList,this);
        recyclerView.setAdapter(recyclerAdapterForUsersAllItem);
    }
}
