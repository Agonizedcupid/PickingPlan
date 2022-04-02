package com.aariyan.pickingplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ExtrasActivity extends AppCompatActivity {

    private EditText checkerName,dunnages,straps,pallets,plasticComeres,tarps,stans,trail,belts,nets;
    private RadioButton completeYes, completeNo,secureYes, secureNo;
    private String complete = "", secure = "";
    private Button saveBtn;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);

        initUI();
    }

    private void initUI() {
        checkerName = findViewById(R.id.checkerName);
        dunnages = findViewById(R.id.dunnages);
        straps = findViewById(R.id.straps);
        pallets = findViewById(R.id.pallets);
        plasticComeres = findViewById(R.id.plasticComers);
        tarps = findViewById(R.id.tarps);
        stans = findViewById(R.id.stans);
        trail = findViewById(R.id.trailorNo);
        belts = findViewById(R.id.belts);
        nets = findViewById(R.id.nets);

        secureYes  = findViewById(R.id.yesSecureBtn);
        secureNo = findViewById(R.id.noSecureBtn);

        completeYes = findViewById(R.id.yesCompleteBtn);
        completeNo = findViewById(R.id.noCompleteBtn);
        saveBtn = findViewById(R.id.saveBtn);

        completeYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complete = "YES";
                completeYes.setChecked(true);
                completeNo.setChecked(false);
            }
        });

        completeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complete = "NO";
                completeYes.setChecked(false);
                completeNo.setChecked(true);
            }
        });

        secureYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secure = "YES";
                secureYes.setChecked(true);
                secureNo.setChecked(false);
            }
        });

        secureNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secure = "NO";
                secureYes.setChecked(false);
                secureNo.setChecked(true);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate() {
        if (TextUtils.isEmpty(checkerName.getText().toString())) {
            checkerName.setError("Please fill this");
            checkerName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dunnages.getText().toString())) {
            dunnages.setError("Please fill this");
            dunnages.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(straps.getText().toString())) {
            straps.setError("Please fill this");
            straps.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pallets.getText().toString())) {
            pallets.setError("Please fill this");
            pallets.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(plasticComeres.getText().toString())) {
            plasticComeres.setError("Please fill this");
            plasticComeres.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tarps.getText().toString())) {
            tarps.setError("Please fill this");
            tarps.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(stans.getText().toString())) {
            stans.setError("Please fill this");
            stans.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(trail.getText().toString())) {
            trail.setError("Please fill this");
            trail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(belts.getText().toString())) {
            belts.setError("Please fill this");
            belts.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nets.getText().toString())) {
            nets.setError("Please fill this");
            nets.requestFocus();
            return;
        }

        if (complete.equals("")) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (secure.equals("")) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Do the API staff:


    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}