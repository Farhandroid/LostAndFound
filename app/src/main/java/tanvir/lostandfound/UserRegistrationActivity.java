package tanvir.lostandfound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import tanvir.lostandfound.StartActivity.StartActivity;

public class UserRegistrationActivity extends AppCompatActivity {

    EditText emailET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        emailET=findViewById(R.id.enterEmailET);

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public void checkUserRegistrationInformation(View view) {
        String email = emailET.getText().toString();
        if (isValidEmail(email)==false)
        {
            emailET.requestFocus();
            emailET.setError("Invalid Email");
        }
        else {
            StartActivity startActivity = new StartActivity();
            startActivity.startPinVerificationActivity(UserRegistrationActivity.this);
        }

    }

}
