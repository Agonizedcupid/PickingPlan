package com.aariyan.pickingplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.pickingplan.Adapter.PlanAdapter;
import com.aariyan.pickingplan.Database.DatabaseAdapter;
import com.aariyan.pickingplan.Filterable.Filter;
import com.aariyan.pickingplan.Interface.GetPLanInterface;
import com.aariyan.pickingplan.Interface.ToLoadClick;
import com.aariyan.pickingplan.Model.PlanModel;
import com.aariyan.pickingplan.Networking.NetworkingFeedback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity implements ToLoadClick {

    private String qrCode = "";
    private CoordinatorLayout snackBarLayout;
    PlanAdapter adapter;
    private RecyclerView recyclerView;
    private TextView referenceNo, planName;

    private ProgressBar progressBar;
    private TextView itemName;
    private EditText enteredQuantity;
    private Button saveQuantityBtn, showRemainingBtn;
    private View bottomSheet;
    private BottomSheetBehavior behavior;

    DatabaseAdapter databaseAdapter;
    List<PlanModel> filteredList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        databaseAdapter = new DatabaseAdapter(PlanActivity.this);

        initUI();

        if (getIntent() != null) {
            qrCode = getIntent().getStringExtra("qrCode");
            if (qrCode.equals("")) {
                Snackbar.make(snackBarLayout, "You didn't scan anything!", Snackbar.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loadPlan(qrCode);
                referenceNo.setText("Reference: " + qrCode);
            }
        }
    }

    private void loadPlan(String qrCode) {
        NetworkingFeedback feedback = new NetworkingFeedback(PlanActivity.this, PlanActivity.this);
        feedback.getPlan(new GetPLanInterface() {
            @Override
            public void gotPlan(List<PlanModel> listOfPlan) {
                if (listOfPlan.size() > 0) {
                    adapter = new PlanAdapter(PlanActivity.this, listOfPlan, PlanActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(snackBarLayout, "No data found!", Snackbar.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void error(String errorMessage) {
                Snackbar.make(snackBarLayout, "" + errorMessage, Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, qrCode, snackBarLayout);
    }

    private void initUI() {
        showRemainingBtn = findViewById(R.id.showRemainingBtn);
        showRemainingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFilteredData();
            }
        });

        snackBarLayout = findViewById(R.id.snackBarLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressbar);
        bottomSheet = findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        itemName = findViewById(R.id.itemName);
        enteredQuantity = findViewById(R.id.enterQuantityEdtText);
        saveQuantityBtn = findViewById(R.id.saveQuantity);

        referenceNo = findViewById(R.id.referenceTxtView);
        planName = findViewById(R.id.planNameTxtView);
        planName.setText("Plan Name");
    }

    private void loadFilteredData() {
        NetworkingFeedback feedback = new NetworkingFeedback(PlanActivity.this, PlanActivity.this);
        feedback.getPlan(new GetPLanInterface() {
            @Override
            public void gotPlan(List<PlanModel> listOfPlan) {
                if (listOfPlan.size() > 0) {
                    filteredList.clear();
                    filteredList = new Filter(PlanActivity.this).getFilteredData(listOfPlan);
                    adapter = new PlanAdapter(PlanActivity.this, filteredList, PlanActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(snackBarLayout, "No data found!", Snackbar.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void error(String errorMessage) {
                Snackbar.make(snackBarLayout, "" + errorMessage, Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, qrCode, snackBarLayout);
    }

    @Override
    public void onClick(PlanModel model) {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        itemName.setText("Item Name: " + model.getDescription());
        enteredQuantity.setText(model.getToLoad());
        saveQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalQuantity = enteredQuantity.getText().toString();
                if (TextUtils.isEmpty(finalQuantity)) {
                    enteredQuantity.setError("Quantity is empty!");
                    enteredQuantity.requestFocus();
                    return;
                }

                long id = databaseAdapter.updatePlanToLoad(model.getDescription(), qrCode, finalQuantity, model.getStorename(), model.getLineNos());
                if (id > 0) {
                    Snackbar.make(snackBarLayout, "Updated To " + finalQuantity, Snackbar.LENGTH_SHORT).show();
                    loadPlan(qrCode);
                } else {
                    Snackbar.make(snackBarLayout, "Failed to update", Snackbar.LENGTH_SHORT).show();
                }

                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

}