package tanvir.lostandfound.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.MySingleton;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.UserLoginInformationSP;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.LoggedUserInformation;
import tanvir.lostandfound.R;

public class UserLoginActivity extends AppCompatActivity {

    EditText nameET , passwordET;
    ProgressDialog progressDialog;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        nameET=findViewById(R.id.userLoginNameET);
        passwordET=findViewById(R.id.userLoginPasswordET);
        context=UserLoginActivity.this;
        progressDialog=new ProgressDialog(context);
        checkIfUserAlreadyLoggedIn();

    }

    public void sendUserLoginInformationToServer(View view) {
        getLoggedUserInformationFromServer();
    }

    public void getLoggedUserInformationFromServer()
    {
        final String name , password;
        name=nameET.getText().toString();
        password=passwordET.getText().toString();
        if (name.length()>0 && password.length()>0) {
            progressDialog.showProgressDialog("Please Wait....");
            ApiConfig apiConfig = AppConfig.getRetrofit().create(ApiConfig.class);
            retrofit2.Call<LoggedUserInformation> loggedUserInformationCall = apiConfig.getLoggedUserInformation(name, password);

            loggedUserInformationCall.enqueue(new Callback<LoggedUserInformation>() {
                @Override
                public void onResponse(retrofit2.Call<LoggedUserInformation> call, retrofit2.Response<LoggedUserInformation> response) {
                    progressDialog.hideProgressDialog();
                    LoggedUserInformation loggedUserInformation=response.body();
                    String message = loggedUserInformation.getMessage();
                    Log.d("Message : ",message);

                    if (message.contains("login_successful"))
                    {
                        UserLoginInformationSP userLoginInformationSP=new UserLoginInformationSP(context);
                        userLoginInformationSP.putUserInformationToSP(loggedUserInformation.getUserName(),loggedUserInformation.getEmailAddress(),loggedUserInformation.getPhoneNumber(),loggedUserInformation.getIsUserUploadedProfilePicture());
                        Toast.makeText(context, "Log In Successful ", Toast.LENGTH_SHORT).show();
                        EnterOrBackFromActivity enterOrBackFromActivity=new EnterOrBackFromActivity();
                        enterOrBackFromActivity.startHomePageActivity(UserLoginActivity.this);
                    }
                    else
                    {
                        Toast.makeText(context, "Login Failed\nPlease Check Your User Name and Password", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<LoggedUserInformation> call, Throwable t) {
                    progressDialog.hideProgressDialog();
                    Log.d("retrofitError",""+t.getMessage());
                    Toast.makeText(context, "retrofitError : "+""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(context, "Please Provide All Information", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkIfUserAlreadyLoggedIn()
    {
        Log.i("checkIfUserLoggedIn","checkIfUserAlreadyLoggedIn");
        SharedPreferences sharedPreferences = UserLoginActivity.this.getSharedPreferences("UserLoginInformation",Context.MODE_PRIVATE);
        String s =sharedPreferences.getString("userName","");
        Log.d("userNameSP",s);
        if (s.length()>0)
        {
            EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
            enterOrBackFromActivity.startHomePageActivity(this);
        }
    }
}
