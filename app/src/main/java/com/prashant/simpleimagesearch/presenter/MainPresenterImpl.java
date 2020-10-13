package com.prashant.simpleimagesearch.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.prashant.simpleimagesearch.activities.CommentsActivity;

public class MainPresenterImpl implements MainPresenter {
    Context context;

    public MainPresenterImpl(Activity context) {
        this.context = context;
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

}