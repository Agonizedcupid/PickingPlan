package com.aariyan.pickingplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aariyan.pickingplan.Adapter.PlanAdapter;
import com.aariyan.pickingplan.Interface.GetPLanInterface;
import com.aariyan.pickingplan.Model.PlanModel;
import com.aariyan.pickingplan.Networking.NetworkingFeedback;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PlanActivity extends AppCompatActivity {

    private String qrCode = "";
    private ConstraintLayout snackBarLayout;
    PlanAdapter adapter;
    private RecyclerView recyclerView;
    private TextView referenceNo, planName;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        initUI();

        if (getIntent() != null) {
            qrCode = getIntent().getStringExtra("qrCode");
            if (qrCode.equals("")) {
                Snackbar.make(snackBarLayout,"You didn't scan anything!", Snackbar.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loadPlan(qrCode);
                referenceNo.setText("Reference: "+qrCode);
            }
        }
    }

    private void loadPlan(String qrCode) {
        NetworkingFeedback feedback = new NetworkingFeedback(PlanActivity.this);
        feedback.getPlan(new GetPLanInterface() {
            @Override
            public void gotPlan(List<PlanModel> listOfPlan) {
                if (listOfPlan.size() > 0) {
                    adapter = new PlanAdapter(PlanActivity.this, listOfPlan);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Snackbar.make(snackBarLayout,"No data found!", Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(String errorMessage) {
                Snackbar.make(snackBarLayout,""+errorMessage, Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, qrCode);
    }

    private void initUI() {
        snackBarLayout = findViewById(R.id.snackBarLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressbar);

        referenceNo = findViewById(R.id.referenceTxtView);
        planName = findViewById(R.id.planNameTxtView);
        planName.setText("Plan Name");
    }
}