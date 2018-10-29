package tanvir.lostandfound.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.R;

public class MainActivity extends AppCompatActivity {

    private TextView phoneNumberCodeTV, verifyPhoneNumberTV, alreadyAMemberTV;
    EnterOrBackFromActivity enterOrBackFromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterOrBackFromActivity = new EnterOrBackFromActivity();
        phoneNumberCodeTV = findViewById(R.id.phoneNumberCodeTV);
        verifyPhoneNumberTV = findViewById(R.id.verifyPhoneNumberTV);
        alreadyAMemberTV = findViewById(R.id.alreadyAMemberTV);
        alreadyAMemberTV.setText(Html.fromHtml(getString(R.string.alreadyAMember)));
        verifyPhoneNumberTV.setText(Html.fromHtml(getString(R.string.verifyPhoneNumber)));
        phoneNumberCodeTV.setPaintFlags(phoneNumberCodeTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


    public void startuserRegistrationActivity(View view) {
       ///enterOrBackFromActivity.startUserLoginActivity(this);
        //enterOrBackFromActivity.startuserRegistrationActivity(this);
        enterOrBackFromActivity.startUserPostCreationActivity(this);
        Log.i("av","ab");
        //sendEmail();
    }

    /*public void sendEmail() {
        ///String url = "http://www.farhandroid.com/testMail.php";
        String url = "http://www.farhandroid.com/phpMailer/testMail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyError",error.toString());
            }
        }
        );

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
*/
    public void startPinVerificationActivity(View view) {
        enterOrBackFromActivity.startHomePageActivity(this);
    }
}
