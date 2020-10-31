package com.prashant.simpleimagesearch.presenter_main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.google.gson.Gson;
import com.prashant.simpleimagesearch.Interfaces.VolleyCallback;
import com.prashant.simpleimagesearch.view.CommentsActivity;
import com.prashant.simpleimagesearch.model.ImageDetails;
import com.prashant.simpleimagesearch.services.FetchImages;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainPresenterImpl implements MainPresenter {
    Context context;
    int pageNo = 1;
    private MainView mainView;

    public MainPresenterImpl(Activity context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    public void onButtonClick(String link, String title, String id) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed(OnBackPressedCallback callback) {
        if (doubleBackToExitPressedOnce) {
            callback.handleOnBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void MakeAPICall(@NotNull String queryParams) {
//            String queryParams = searchView.getText().toString();
        FetchImages fetchImages = new FetchImages(context, queryParams, pageNo);
        fetchImages.sendRequest(new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {

                JSONObject jsonObject = null;

                JSONArray arrr = null;
                try {
                    jsonObject = new JSONObject(result);
                    arrr = jsonObject.getJSONArray("data");
                    for (int i = 0; i < arrr.length(); i++) {
                        JSONObject jsonObj = new JSONObject(arrr.getString(i));
                        JSONArray ar;
                        try {
                            String title = jsonObj.getString("title");
                            ar = jsonObj.getJSONArray("images");
                            Log.e("OuterLoop", jsonObj.toString());
                            for (int j = 0; j < ar.length(); j++) {
                                Gson gson = new Gson();
                                ImageDetails res = gson.fromJson(ar.getString(j), ImageDetails.class);
                                res.setTitle(title);
                                mainView.addData(res);
                                Log.e("InnerLoop", res.toString());
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }
                    }
                    mainView.notifyDataSetChanged();
                    pageNo++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void hideKeyboard(@NotNull IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(binder, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    @Override
    public void SetPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}