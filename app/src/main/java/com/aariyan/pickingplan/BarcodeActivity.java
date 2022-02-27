package com.aariyan.pickingplan;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    private Button tapToScan, getPlan;
    private TextView scannedCode;
    private ConstraintLayout snackBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {
        snackBarLayout = findViewById(R.id.snackBarLayout);
        tapToScan = findViewById(R.id.tapToScanCode);
        scannedCode = findViewById(R.id.scannedCode);
        getPlan = findViewById(R.id.getPlan);
        tapToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions().setCaptureActivity(ScannerActivity.class);
                barcodeLauncher.launch(options);
            }
        });

        getPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = scannedCode.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    Snackbar.make(snackBarLayout,"you didn't scan anything", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(BarcodeActivity.this, PlanActivity.class);
                intent.putExtra("qrCode", code);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                }
            });

}