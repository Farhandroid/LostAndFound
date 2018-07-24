package tanvir.lostandfound;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    private TextView phoneNumberCodeTV,verifyPhoneNumberTV,alreadyAMemberTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberCodeTV=findViewById(R.id.phoneNumberCodeTV);
        verifyPhoneNumberTV=findViewById(R.id.verifyPhoneNumberTV);
        alreadyAMemberTV=findViewById(R.id.alreadyAMemberTV);
        alreadyAMemberTV.setText(Html.fromHtml(getString(R.string.alreadyAMember)));
        verifyPhoneNumberTV.setText(Html.fromHtml(getString(R.string.verifyPhoneNumber)));
        phoneNumberCodeTV.setPaintFlags(phoneNumberCodeTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }



    public void startuserRegistrationActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), UserRegistrationActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }
}
