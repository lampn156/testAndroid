package com.example.lampn.week5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

public class CircularRevealActivity extends AppCompatActivity {
    private FloatingActionButton faAction;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_circular_reveal);
        faAction = (FloatingActionButton) findViewById(R.id.faAction);
        faAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExitAnimation();
            }
        });
        view = findViewById(R.id.activity_circular_reveal);

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                doEnterAnimation();
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });




    }

    private void doExitAnimation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x= (int) (faAction.getX() + faAction.getMeasuredWidth()/2);
            int y = (int) (faAction.getY() + faAction.getMeasuredHeight()/2);
            float startRadius = Math.max(view.getMeasuredWidth(), view.getMeasuredHeight());
            float endRadius = faAction.getMeasuredWidth()/2;
            Animator animator = ViewAnimationUtils.createCircularReveal(view,x,y,startRadius,endRadius);
            animator.setDuration(700);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    faAction.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    finish();

                    overridePendingTransition(0,0);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ContextCompat.getColor(CircularRevealActivity.this,R.color.colorAccent));
                    }
                    ActivityCompat.finishAfterTransition(CircularRevealActivity.this);
                }
            });

            animator.start();
        }
    }

    private void doEnterAnimation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x= (int) (faAction.getX() + faAction.getMeasuredWidth()/2);
            int y = (int) (faAction.getY() + faAction.getMeasuredHeight()/2);
            float startRadius = faAction.getMeasuredWidth()/2;
            float endRadius = Math.max(view.getMeasuredWidth(), view.getMeasuredHeight());
            Animator animator = ViewAnimationUtils.createCircularReveal(view,x,y,startRadius,endRadius);
            animator.start();
        }
    }

    @Override
    public void onBackPressed() {
        doExitAnimation();
    }
}
