package com.aariyan.pickingplan;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class BarcodeActivity extends AppCompatActivity {

    private Button tapToScan, getPlan, continueWithMyPlay;
    private TextView scannedCode;
    private ConstraintLayout snackBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        initUI(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BarcodeActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    }

    private void initUI(Bundle savedInstanceState) {
        snackBarLayout = findViewById(R.id.snackBarLayout);
        tapToScan = findViewById(R.id.tapToScanCode);
        scannedCode = findViewById(R.id.scannedCode);
        getPlan = findViewById(R.id.getPlan);
        continueWithMyPlay = findViewById(R.id.continueWithMyPLan);
        tapToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions().setCaptureActivity(ScannerActivity.class);
                barcodeLauncher.launch(options);
            }
        });

        //It will show the default data from local DB:
        getPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarcodeActivity.this, PlanActivity.class);
                intent.putExtra("qrCode", "nothing");
                intent.putExtra("userId", getIntent().getIntExtra("userId", 1));
                startActivity(intent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        //This will pull the new data from server:
        continueWithMyPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = scannedCode.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    Snackbar.make(snackBarLayout, "you didn't scan anything", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog alertDialog = new AlertDialog.Builder(BarcodeActivity.this).create();
                alertDialog.setTitle("Alert Message");
                alertDialog.setMessage("Are you sure you want to get new Data?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BarcodeActivity.this, PlanActivity.class);
                        intent.putExtra("qrCode", code);
                        intent.putExtra("userId", getIntent().getIntExtra("userId", 1));
                        startActivity(intent);
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BarcodeActivity.this, PlanActivity.class);
                        intent.putExtra("qrCode", "nothing");
                        intent.putExtra("userId", getIntent().getIntExtra("userId", 1));
                        startActivity(intent);
                    }
                });

                alertDialog.show();

            }
        });
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(BarcodeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(BarcodeActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "Scanned");
                    Toast.makeText(BarcodeActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    scannedCode.setText(result.getContents());
                    Log.d("SCANNED_RESULT", "" + result.getContents());
                }
            });

}