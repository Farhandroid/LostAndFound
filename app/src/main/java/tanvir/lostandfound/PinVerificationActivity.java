package tanvir.lostandfound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class PinVerificationActivity extends AppCompatActivity {

    TextView OTPverificationTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*setContentView(R.layout.activity_pin_verification);
        OTPverificationTV=findViewById(R.id.OTPverificationTV);
        OTPverificationTV.setText(Html.fromHtml(getString(R.string.OTPVerification)));*/
    }
}
