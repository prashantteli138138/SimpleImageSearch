package com.prashant.simpleimagesearch.view;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.prashant.simpleimagesearch.adapters.ImageListAdapter;
import com.prashant.simpleimagesearch.Interfaces.OnItemClickListener;
import com.prashant.simpleimagesearch.listeners.EndlessRecyclerOnScrollListener;
import com.prashant.simpleimagesearch.R;
import com.prashant.simpleimagesearch.model.ImageDetails;
import com.prashant.simpleimagesearch.presenter_main.MainPresenter;
import com.prashant.simpleimagesearch.presenter_main.MainPresenterImpl;
import com.prashant.simpleimagesearch.presenter_main.MainView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, MainView {
    private RecyclerView recyclerView;
    EditText searchView;
    ArrayList<ImageDetails> imageDetails;

    ImageListAdapter myAdapter;
    private Timer timer = new Timer();
    private final long DELAY = 900; // in ms
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenterImpl(this, this);
        Init();
        Search();
        presenter.MakeAPICall(searchView.getText().toString());
    }

    @Override
    public void onItemClick(@Nullable View view, int position) {
        presenter.onButtonClick(imageDetails.get(position).getLink(), imageDetails.get(position).getTitle(), imageDetails.get(position).getId());
    }

    public void SearchImages(View view) {
        imageDetails.clear();
        presenter.MakeAPICall(searchView.getText().toString());
        presenter.SetPageNo(1);
    }

    public void Init() {
        presenter = new MainPresenterImpl(this, this);
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
                presenter.MakeAPICall(searchView.getText().toString());
            }
        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    presenter.hideKeyboard(searchView.getWindowToken());
                    imageDetails.clear();
                    presenter.MakeAPICall(searchView.getText().toString());
                    presenter.SetPageNo(1);
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
                                    presenter.hideKeyboard(searchView.getWindowToken());
                                    imageDetails.clear();
                                    presenter.MakeAPICall(searchView.getText().toString());

                                    presenter.SetPageNo(1);
                                }
                            });


                            presenter.SetPageNo(1);
                        }

                    }, DELAY);
                } else if (s.length() == 0) {
                    imageDetails.clear();
                    presenter.MakeAPICall(searchView.getText().toString());

                    presenter.SetPageNo(1);
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

    @Override
    public void notifyDataSetChanged() {
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void addData(@NotNull ImageDetails res) {
        imageDetails.add(res);
    }
}
