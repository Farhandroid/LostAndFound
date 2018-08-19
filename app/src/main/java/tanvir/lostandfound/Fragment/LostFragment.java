package tanvir.lostandfound.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import tanvir.lostandfound.Adapter.RecyclerAdapterForLostItemFragment;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.LostItemPost;
import tanvir.lostandfound.R;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LostFragment extends Fragment {


    EnterOrBackFromActivity enterOrBackFromActivity;
    Activity activity;
    private RecyclerView recyclerView;
    private RecyclerAdapterForLostItemFragment recyclerAdapterForLostItemFragment;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    public LostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost, container, false);
        enterOrBackFromActivity =new EnterOrBackFromActivity();
        activity=getActivity();
        recyclerView=view.findViewById(R.id.recyclerViewInLostFragment);
        progressDialog=new ProgressDialog(activity);
        retriveLostItemPostDataFromServer();
        return view;
    }


    public void updateRecyclerView( ArrayList<LostItemPost> lostItemPostArrayList)
    {
        linearLayoutManager=new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapterForLostItemFragment=new RecyclerAdapterForLostItemFragment(lostItemPostArrayList,activity);
        recyclerView.setAdapter(recyclerAdapterForLostItemFragment);
    }

    public void retriveLostItemPostDataFromServer()
    {
        progressDialog.showProgressDialog("Please Wait...");
        ApiConfig apiConfig= AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ArrayList<LostItemPost>> arrayListCall=apiConfig.getAllLostItemPost();

        arrayListCall.enqueue(new Callback<ArrayList<LostItemPost>>() {
            @Override
            public void onResponse(Call<ArrayList<LostItemPost>> call, retrofit2.Response<ArrayList<LostItemPost>> response) {
                progressDialog.hideProgressDialog();
                ArrayList<LostItemPost> lostItemPostArrayList=response.body();
                if (lostItemPostArrayList.size()>0)
                {
                    String message = lostItemPostArrayList.get(0).getMessage();
                    Log.d("retrofitResponse : ",message);

                    if (message.contains("DataRetrive Success"))
                    {
                        Log.d("detailedDescription",lostItemPostArrayList.get(0).getDetailedDescription());
                        Log.d("itemType",lostItemPostArrayList.get(0).getWhatIsLost());
                        updateRecyclerView(lostItemPostArrayList);
                    }
                    else
                        Log.d("retrofitResponse : ","dataRetriveFailed");

                }
                Log.d("retrofitResponse : ","ArrayList is empty");

            }

            @Override
            public void onFailure(Call<ArrayList<LostItemPost>> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("retrofitError",""+t.getMessage());
                Toast.makeText(activity, "retrofitError : "+""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
