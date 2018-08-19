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
import tanvir.lostandfound.Adapter.RecyclerAdapterForFoundItemFragment;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.Networking.ApiConfig;
import tanvir.lostandfound.Networking.AppConfig;
import tanvir.lostandfound.PojoClass.FoundItemPost;
import tanvir.lostandfound.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoundFragment extends Fragment {

    EnterOrBackFromActivity enterOrBackFromActivity;
    Activity activity;
    private RecyclerView recyclerView;
    private RecyclerAdapterForFoundItemFragment recyclerAdapterForFoundItemFragment;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;


    public FoundFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        enterOrBackFromActivity =new EnterOrBackFromActivity();
        activity=getActivity();
        recyclerView=view.findViewById(R.id.recyclerViewInFoundFragment);
        progressDialog=new ProgressDialog(activity);
        retriveFoundItemPostDataFromServer();

        return view;
    }

    public void updateRecyclerView( ArrayList<FoundItemPost> foundItemPostArrayList)
    {
        linearLayoutManager=new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerAdapterForFoundItemFragment =new RecyclerAdapterForFoundItemFragment(foundItemPostArrayList,activity);
        recyclerView.setAdapter(recyclerAdapterForFoundItemFragment);
    }

    public void retriveFoundItemPostDataFromServer()
    {
        progressDialog.showProgressDialog("Please Wait...");
        ApiConfig apiConfig= AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ArrayList<FoundItemPost>> arrayListCall=apiConfig.getAllFoundItemPost();

        arrayListCall.enqueue(new Callback<ArrayList<FoundItemPost>>() {
            @Override
            public void onResponse(Call<ArrayList<FoundItemPost>> call, retrofit2.Response<ArrayList<FoundItemPost>> response) {
                progressDialog.hideProgressDialog();
                ArrayList<FoundItemPost> foundItemPostArrayList =response.body();
                if (foundItemPostArrayList.size()>0)
                {
                    String message = foundItemPostArrayList.get(0).getMessage();
                    Log.d("retrofitResponse : ",message);

                    if (message.contains("DataRetrive Success"))
                    {
                        Log.d("detailedDescription", foundItemPostArrayList.get(0).getDetailedDescription());
                        Log.d("itemType", ""+foundItemPostArrayList.get(0).getWhatIsFound());
                        updateRecyclerView(foundItemPostArrayList);
                    }
                    else
                        Log.d("retrofitResponse : ","dataRetriveFailed");

                }
                Log.d("retrofitResponse : ","ArrayList is empty");

            }

            @Override
            public void onFailure(Call<ArrayList<FoundItemPost>> call, Throwable t) {
                progressDialog.hideProgressDialog();
                Log.d("retrofitError",""+t.getMessage());
                Toast.makeText(activity, "retrofitError : "+""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
