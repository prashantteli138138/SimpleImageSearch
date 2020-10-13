package com.prashant.simpleimagesearch.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prashant.simpleimagesearch.adapters.ImageListAdapter;
import com.prashant.simpleimagesearch.Interfaces.OnItemClickListener;
import com.prashant.simpleimagesearch.Interfaces.VolleyCallback;
import com.prashant.simpleimagesearch.listeners.EndlessRecyclerOnScrollListener;
import com.prashant.simpleimagesearch.R;
import com.prashant.simpleimagesearch.model.ImageDetails;
import com.prashant.simpleimagesearch.presenter.MainPresenter;
import com.prashant.simpleimagesearch.presenter.MainPresenterImpl;
import com.prashant.simpleimagesearch.services.FetchImages;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView recyclerView;
    EditText searchView;
    ArrayList<ImageDetails> imageDetails;
    int pageNo = 1;
    ImageListAdapter myAdapter;
    private Timer timer = new Timer();
    private final long DELAY = 900; // in ms
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Search();
        MakeAPICall();
    }

    @Override
    public void onItemClick(@Nullable View view, int position) {
        presenter.onButtonClick(imageDetails.get(position).getLink(), imageDetails.get(position).getTitle(), imageDetails.get(position).getId());
    }

    public void SearchImages(View view) {
        imageDetails.clear();
        MakeAPICall();
        pageNo = 1;
    }

    public void MakeAPICall() {
        String queryParams = searchView.getText().toString();

        FetchImages fetchImages = new FetchImages(this, queryParams, pageNo);
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
                                imageDetails.add(res);
                                Log.e("InnerLoop", res.toString());
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                    pageNo++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Init() {
        presenter = new MainPresenterImpl(this);
        searchView = (EditText) findViewById(R.id.searchView);
        imageDetails = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        int columns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, columns);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new ImageListAdapter(MainActivity.this, imageDetails, MainActivity.this);
        recyclerView.setAdapter(myAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                System.out.println("load more");
                MakeAPICall();
            }
        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    imageDetails.clear();
                    MakeAPICall();
                    pageNo = 1;
                    return true;
                }
                return false;
            }
        });

    }

    public void Search() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 3) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            imageDetails.clear();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    hideKeyboard();
                                    imageDetails.clear();
                                    MakeAPICall();
                                    pageNo = 1;
                                }
                            });

                            pageNo = 1;
                        }

                    }, DELAY);
                } else if (s.length() == 0) {
                    imageDetails.clear();
                    MakeAPICall();
                    pageNo = 1;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.super.onBackPressed();
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
