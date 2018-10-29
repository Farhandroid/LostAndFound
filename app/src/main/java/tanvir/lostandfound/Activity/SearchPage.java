package tanvir.lostandfound.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import tanvir.lostandfound.R;

public class SearchPage extends AppCompatActivity {

    TextView selectCategoryTV, selectLoctionTV;
    private Toolbar searchToolBar;
    static final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        searchToolBar = findViewById(R.id.toolbarLayoutForSearchPageActivity);
        setSupportActionBar(searchToolBar);
        selectCategoryTV = findViewById(R.id.selectCategoryTV);
        selectLoctionTV = findViewById(R.id.selectLocationTV);
    }

    public void showCategoriPickerDialog(View view) {
        final View selectCategoryView;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selectCategoryView = layoutInflater.inflate(R.layout.dialog_select_category, null);
        dialogBuilder.setView(selectCategoryView);
        final Button lostBtn = selectCategoryView.findViewById(R.id.lostInSelectCategory);
        Button foundBTN = selectCategoryView.findViewById(R.id.foundInSelectCategory);
        final AlertDialog alertDialog = dialogBuilder.create();
        lostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryTV.setText("Lost");
                alertDialog.dismiss();
            }
        });

        foundBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryTV.setText("Found");
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showPlaceAutocompleteIntentInSearchpageActivity(View view) {

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
                Place place = PlacePicker.getPlace(this, data);
                selectLoctionTV.setText(place.getName().toString() + "\n" + place.getAddress().toString());

            }
            else Log.i("placeRqstcancelled","placeRqstcancelled");
        }
    }
}

