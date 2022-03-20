package com.aariyan.pickingplan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private RadioButton allBtn, loadedBtn, remainingBtn;

    private Button submitBtn;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        databaseAdapter = new DatabaseAdapter(PlanActivity.this);

        initUI();

        if (getIntent() != null) {
            qrCode = getIntent().getStringExtra("qrCode");
            progressBar.setVisibility(View.VISIBLE);
            if (qrCode.equals("nothing")) {
                loadPlan(qrCode);
                //Snackbar.make(snackBarLayout, "You didn't scan anything!", Snackbar.LENGTH_SHORT).show();
            } else {
                //progressBar.setVisibility(View.VISIBLE);
                //Drop the table before getting the data:
                databaseAdapter.dropPlanTable();
                loadPlan(qrCode);
                //referenceNo.setText("Reference: " + qrCode);
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
                    referenceNo.setText("Reference: " + listOfPlan.get(0).getReference());
                } else {
                    Snackbar.make(snackBarLayout, "No data found!", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(PlanActivity.this, "No Data found!", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(PlanActivity.this).create();
        alertDialog.setTitle("Alert Message");
        alertDialog.setMessage("Are you sure you want to return to the main page?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void initUI() {
//        showRemainingBtn = findViewById(R.id.showRemainingBtn);
//        showRemainingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadFilteredData();
//            }
//        });

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlanActivity.this, UploadActivity.class)
                        .putExtra("code", qrCode)
                        .putExtra("code", qrCode)
                        .putExtra("userId", getIntent().getIntExtra("userId", 1))
                );
            }
        });

        allBtn = findViewById(R.id.allRadioBtn);
        remainingBtn = findViewById(R.id.remainingRadioBtn);
        loadedBtn = findViewById(R.id.loadedRadioBtn);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlan("nothing");
                allBtn.setChecked(true);
                remainingBtn.setChecked(false);
                loadedBtn.setChecked(false);
            }
        });

        remainingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFilteredData(1);
                allBtn.setChecked(false);
                remainingBtn.setChecked(true);
                loadedBtn.setChecked(false);
            }
        });

        loadedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFilteredData(0);
                allBtn.setChecked(false);
                remainingBtn.setChecked(false);
                loadedBtn.setChecked(true);
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

//    private void loadFilteredData(int flag) {
//        NetworkingFeedback feedback = new NetworkingFeedback(PlanActivity.this, PlanActivity.this);
//        feedback.getPlan(new GetPLanInterface() {
//            @Override
//            public void gotPlan(List<PlanModel> listOfPlan) {
//                if (listOfPlan.size() > 0) {
////                    if (flag == 0) {
////                        filteredList.clear();
////                        filteredList = new Filter(PlanActivity.this).getFilteredForLoaded(listOfPlan);
////                        Toast.makeText(PlanActivity.this, listOfPlan.size()+"->"+filteredList.size(), Toast.LENGTH_SHORT).show();
////                    } else {
////                        filteredList.clear();
////                        filteredList = new Filter(PlanActivity.this).getFilteredForRemaining(listOfPlan);
////                    }
//
//                    filteredList.clear();
//                    filteredList = new Filter(PlanActivity.this).getFilteredData(listOfPlan, flag);
//
//                    adapter = new PlanAdapter(PlanActivity.this, filteredList, PlanActivity.this);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Snackbar.make(snackBarLayout, "No data found!", Snackbar.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void error(String errorMessage) {
//                Snackbar.make(snackBarLayout, "" + errorMessage, Snackbar.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        }, qrCode, snackBarLayout);
//    }

    private void loadFilteredData(int flag) {
        filteredList.clear();
        List<PlanModel> list = new NetworkingFeedback(PlanActivity.this, PlanActivity.this).getPlanForFilter();
        filteredList = new Filter(PlanActivity.this).getFilteredData(list, flag);
        adapter = new PlanAdapter(PlanActivity.this, filteredList, PlanActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

                long id = databaseAdapter.updatePlanToLoad(model.getDescription(), qrCode, finalQuantity, model.getStorename(), model.getLineNos(), 1);
                if (id > 0) {
                    Snackbar.make(snackBarLayout, "Updated To " + finalQuantity, Snackbar.LENGTH_SHORT).show();
                    loadPlan("nothing");
                } else {
                    Snackbar.make(snackBarLayout, "Failed to update", Snackbar.LENGTH_SHORT).show();
                }

                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

}