package tanvir.lostandfound.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Callback;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.HelperClass.UserLoginInformationSP;
import tanvir.lostandfound.JavaMail.SendEmail;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.LoggedUserInformation;
import tanvir.lostandfound.PojoClass.ServerResponse;
import tanvir.lostandfound.R;

public class UserLoginActivity extends AppCompatActivity {

    EditText nameET , passwordET;
    ProgressDialog progressDialog;
    Context context;
    TextView dontHaveAccountTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///setContentView(R.layout.activity_user_login);
        setContentView(R.layout.activity_user_login_another_layout);
        nameET=findViewById(R.id.userLoginNameET);
        passwordET=findViewById(R.id.userLoginPasswordET);
        dontHaveAccountTV=findViewById(R.id.dontHaveAccountTV);
        dontHaveAccountTV.setText(Html.fromHtml(getString(R.string.dontHaveAccount)));
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
                        Log.i("LoggedUserInformation"," "+loggedUserInformation.getUserName());
                        Log.i("LoggedUserInfoName"," name : "+loggedUserInformation.getName());
                        UserLoginInformationSP userLoginInformationSP=new UserLoginInformationSP(context);
                        userLoginInformationSP.putUserInformationToSP(loggedUserInformation.getName(),loggedUserInformation.getUserName(),loggedUserInformation.getEmailAddress(),loggedUserInformation.getPhoneNumber(),loggedUserInformation.getIsUserUploadedProfilePicture());
                       //putUserInformationToSP(loggedUserInformation.getUserName(),loggedUserInformation.getEmailAddress(),loggedUserInformation.getPhoneNumber(),loggedUserInformation.getIsUserUploadedProfilePicture());
                        Toast.makeText(context, "Log In Successful ", Toast.LENGTH_SHORT).show();
                        EnterOrBackFromActivity enterOrBackFromActivity=new EnterOrBackFromActivity();
                        enterOrBackFromActivity.startHomePageActivity(UserLoginActivity.this);
                    }
                    else
                    {
                        Toast.makeText(context, "Login Failed . Please Check Your User Name and Password", Toast.LENGTH_LONG).show();
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
            Toast.makeText(context, "Please Provide All Information", Toast.LENGTH_SHORT).show();

    }

    public void checkIfUserAlreadyLoggedIn()
    {
        Log.i("checkIfUserLoggedIn","checkIfUserAlreadyLoggedIn");
        SharedPreferences sharedPreferences = getSharedPreferences("UserLoginInformation",Context.MODE_PRIVATE);
        String s =sharedPreferences.getString("userName","");
        Log.d("userNameSP",s);
        if (s.length()>0)
        {
            EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
            enterOrBackFromActivity.startHomePageActivity(this);
        }
    }

    public void forgotPassword(View view) {
        EnterOrBackFromActivity enterOrBackFromActivity=new EnterOrBackFromActivity();
        enterOrBackFromActivity.startuserRegistrationActivity(UserLoginActivity.this);
    }

    public void showForgotPasswordDialog(View view)
    {
        final View forgotPasswordDialogView;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        forgotPasswordDialogView=layoutInflater.inflate(R.layout.dialog_forgot_password,null);
        dialogBuilder.setView(forgotPasswordDialogView);
        Button forgotPasswordBTN=forgotPasswordDialogView.findViewById(R.id.sendMail);
        final AlertDialog alertDialog=dialogBuilder.create();
        forgotPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText emailET = forgotPasswordDialogView.findViewById(R.id.mailInForgotPassword);
                String email =emailET.getText().toString();
                sendEmailToUser(email,alertDialog);

            }
        });

        alertDialog.show();
    }

    public void sendEmailToUser(final String email, final AlertDialog alertDialog)
    {
        progressDialog.showProgressDialog("Please Wait...");
        ApiConfig apiConfig=AppConfig.getRetrofit().create(ApiConfig.class);
        retrofit2.Call<ServerResponse> sendEmailWithPasswordCall = apiConfig.sendPasswordUsingEmail(email);
        sendEmailWithPasswordCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progressDialog.hideProgressDialog();
                ServerResponse serverResponse = response.body();
                String message = serverResponse.getMessage();
                String password = serverResponse.getImageResponse();
                String userName = serverResponse.getQueryResponse();
                String mailMessage="\nDear "+userName+",\nI hope you’re doing well . Your password in Lost&Found is "+password+" . Please save it in secure place .\n\n\n-Regards\nLost&Found Team";
                Log.i("rspnseSendEmai",message);
                if (message.contains("Email exist"))
                {
                    progressDialog.hideProgressDialog();
                    SendEmail sm = new SendEmail(UserLoginActivity.this, email,"Password recovery ", mailMessage,alertDialog);
                    sm.execute();
                    ///alertDialog.dismiss();
                    ///Toast.makeText(context, "Please check your mail for password", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context, "Password recovery filed . Probable reason your provided email address is not valid", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(retrofit2.Call<ServerResponse> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.i("errorseSendEmai",t.getMessage());

            }
        });
    }
}
