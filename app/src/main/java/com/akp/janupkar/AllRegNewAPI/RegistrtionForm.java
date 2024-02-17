package com.akp.janupkar.AllRegNewAPI;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.basic.MainActivity;
import com.akp.janupkar.view.BaseActivity;

public class RegistrtionForm extends BaseActivity implements View.OnClickListener {
    Button btn_BasicDeatils,btn_CustomerBusinessDetails,btn_LoanDetails;
    RelativeLayout rlHeader;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrtion_form);
        findViewById();
        setListner();
    }

    private void setListner() {
        btn_BasicDeatils.setOnClickListener(this);
        btn_CustomerBusinessDetails.setOnClickListener(this);
        btn_LoanDetails.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }

    private void findViewById() {
        btn_BasicDeatils=findViewById(R.id.btn_BasicDeatils);
        btn_CustomerBusinessDetails=findViewById(R.id.btn_CustomerBusinessDetails);
        btn_LoanDetails=findViewById(R.id.btn_LoanDetails);
        rlHeader=findViewById(R.id.rlHeader);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_BasicDeatils:
                startActivity(new Intent(mActivity, Reg_BasicDetails.class));
                break;
            case R.id.btn_LoanDetails:
                Toast.makeText(getApplicationContext(),"Firstly Add basic Details",Toast.LENGTH_LONG).show();
                startActivity(new Intent(mActivity, Reg_LoanDetails.class));
                break;
            case R.id.btn_CustomerBusinessDetails:
                Toast.makeText(getApplicationContext(),"Firstly Add Basic and Loan Details",Toast.LENGTH_LONG).show();
                startActivity(new Intent(mActivity, Reg_GuarantorDetails.class));
                break;
            case R.id.rlHeader:
                startActivity(new Intent(mActivity, MainActivity.class));
                break;
        } }


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(RegistrtionForm.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(RegistrtionForm.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(RegistrtionForm.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RegistrtionForm.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(RegistrtionForm.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }}
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RegistrtionForm.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistrtionForm.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }}}
}