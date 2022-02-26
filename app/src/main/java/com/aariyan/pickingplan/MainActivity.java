package com.aariyan.pickingplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Instance variable of logIn btn:
    private Button logInBtn;
    //Instance variable of Edit Text of code entering:
    private EditText enterCodeEdtText;

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

        //instantiating the onClick for button listener, later we will implement the interface for triggering:
        logInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.logInBtn:
                Intent barcodeIntent = new Intent(MainActivity.this,BarcodeActivity.class);
                startActivity(barcodeIntent);
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        }
    }
}