package com.aariyan.pickingplan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.aariyan.pickingplan.Constant.Constant;
import com.aariyan.pickingplan.Database.S_Preferences;
import com.aariyan.pickingplan.Interface.LogInInterface;
import com.aariyan.pickingplan.Model.AuthenticationModel;
import com.aariyan.pickingplan.Networking.NetworkingFeedback;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Instance variable of logIn btn:
    private Button logInBtn;
    //Instance variable of Edit Text of code entering:
    private EditText enterCodeEdtText;

    //Instance variable of Constraint layout for SnackBar:
    private ConstraintLayout snackBarLayout;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate UI variables
        initUI();
    }

    private void initUI() {
        //instantiating the log In btn:
        logInBtn = findViewById(R.id.logInBtn);
        //Instantiating the Edit Text Field for entering the code:
        enterCodeEdtText = findViewById(R.id.enterCode);

        //Instantiating the snackBar layout:
        snackBarLayout = findViewById(R.id.snackBarLayout);

        progressBar = findViewById(R.id.progressbar);

        //instantiating the onClick for button listener, later we will implement the interface for triggering:
        logInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.logInBtn:
                logInValidation();
                break;
        }
    }

    //Checking the log-In validation whether the entered data is correct or not:
    private void logInValidation() {
        progressBar.setVisibility(View.VISIBLE);
        S_Preferences s_preferences = new S_Preferences(MainActivity.this);
        Constant.BASE_URL = s_preferences.getBaseUrl(Constant.SHARED_PREFERENCE_ROOT_NAME_FOR_BASE_URL);

        //Entered value from edit Text:
        String enteredPinCode = enterCodeEdtText.getText().toString().trim();
        //checking, String is empty (User inputted any code or not):
        if (TextUtils.isEmpty(enteredPinCode)) {
            //if the pin is empty or no pin inputted by the use:
            enterCodeEdtText.setError("Please enter pin code to continue!");
            enterCodeEdtText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        NetworkingFeedback networking = new NetworkingFeedback(MainActivity.this, MainActivity.this);
        networking.postLogInResponse(new LogInInterface() {
            @Override
            public void checkLogIn(List<AuthenticationModel> list) {
                if (list.size() > 0) {
                    Intent barcodeIntent = new Intent(MainActivity.this, BarcodeActivity.class);
                    barcodeIntent.putExtra("name", list.get(0).getPickingTeams());
                    startActivity(barcodeIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Snackbar.make(snackBarLayout, "Invalid Credential!", Snackbar.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(snackBarLayout, "" + errorMessage, Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, enteredPinCode);
    }
}