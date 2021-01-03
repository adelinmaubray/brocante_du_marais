package be.ami.maubray.brocantedumarais.brocscan.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import java.util.Objects;

import be.ami.maubray.brocantedumarais.brocscan.activity.MainActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
	
	private static final int MY_CAMERA_REQUEST_CODE = 100;
	boolean isOnPause;
	private ZXingScannerView mScannerView;
	
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setTitle("Scannez un QR Code");
		
		// Programmatically initialize the scanner view
		mScannerView = new ZXingScannerView(this);
		setContentView(mScannerView);
		
		// Register ourselves as a handler for scan results.
		mScannerView.setResultHandler(this);
		mScannerView.startCamera();
		
		isOnPause = false;
		
		if (checkSelfPermission(Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
		}
		
	}
	
	@Override
	public void handleResult(Result rawResult) {
		MainActivity.QR_CODE_READ = rawResult.getText();
		setResult(MainActivity.RESULT_CODE);
		this.finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mScannerView.stopCamera();   // Stop camera on pause
		isOnPause = true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (isOnPause) {
			mScannerView.startCamera();
		}
	}
}