package com.dde.online;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import com.dde.online.utils.Tools;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.transition.MaterialContainerTransform;


import java.util.List;

public class BottomSheetTransform extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;

    private ExtendedFloatingActionButton bt_support;
    private View endView;
    private FrameLayout root;
    private boolean slow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_transform);
        parent_view = findViewById(android.R.id.content);

        initComponent();
    }



    public void initComponent() {
        root = findViewById(R.id.root_view);
        bt_support = findViewById(R.id.bt_support);
        endView = findViewById(R.id.bottom_sheet);

        ViewCompat.setTransitionName(bt_support, String.valueOf(bt_support.getId()));
        bt_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndView(v);
            }
        });

        (findViewById(R.id.bt_expand)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
  new Handler(this.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showEndView(bt_support);
            }
        }, 600);
    }

    private void showEndView(View startView) {
        Toast.makeText(this, slow ? "Slow" : "Fast", Toast.LENGTH_SHORT).show();
        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = buildContainerTransform(true);
        transition.setStartView(startView);
        transition.setEndView(endView);

        // Add a single target to stop the container transform from running on both the start and end view.
        transition.addTarget(endView);
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                Tools.setSystemBarColor(BottomSheetTransform.this, R.color.red_900);
                Tools.setSystemBarDark(BottomSheetTransform.this);
            }
        });

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(root, transition);
        startView.setVisibility(View.INVISIBLE);
        endView.setVisibility(View.VISIBLE);
    }

    private void showStartView(View endView) {
        View startView = bt_support;

        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = buildContainerTransform(false);
        transition.setStartView(endView);
        transition.setEndView(startView);

        // Add a single target to stop the container transform from running on both the start
        transition.addTarget(startView);

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(root, transition);
        startView.setVisibility(View.VISIBLE);
        endView.setVisibility(View.INVISIBLE);

        Tools.setSystemBarColor(this, R.color.grey_3);
        Tools.setSystemBarLight(this);

        slow = !slow;
    }

    @NonNull
    private MaterialContainerTransform buildContainerTransform(boolean entering) {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setTransitionDirection(entering ? MaterialContainerTransform.TRANSITION_DIRECTION_ENTER : MaterialContainerTransform.TRANSITION_DIRECTION_RETURN);
        transform.setDrawingViewId(root.getId());
        transform.setDuration(slow ? 1500 : 500);
        return transform;
    }

    @Override
    public void onBackPressed() {
        if (endView != null && endView.getVisibility() == View.VISIBLE) {
            showStartView(endView);
        } else {
            super.onBackPressed();
        }
    }



}