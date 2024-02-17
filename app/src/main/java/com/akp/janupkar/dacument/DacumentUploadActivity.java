package com.akp.janupkar.dacument;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.akp.janupkar.R;
import com.akp.janupkar.basic.MainActivity;
import com.akp.janupkar.view.BaseActivity;

public class DacumentUploadActivity extends BaseActivity implements View.OnClickListener {
    Button btn_MemberDocumentDetails,btn_GuarantorDocumentDetails,btn_GuarantortwoDocumentDetails;
   RelativeLayout rlHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dacument_upload);
        findViewById();
        setListner();
    }

    private void setListner() {
        btn_MemberDocumentDetails.setOnClickListener(this);
        btn_GuarantorDocumentDetails.setOnClickListener(this);
        btn_GuarantortwoDocumentDetails.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }
    private void findViewById() {
        btn_MemberDocumentDetails=findViewById(R.id.btn_MemberDocumentDetails);
        btn_GuarantorDocumentDetails=findViewById(R.id.btn_GuarantorDocumentDetails);
        btn_GuarantortwoDocumentDetails=findViewById(R.id.btn_GuarantortwoDocumentDetails);
        rlHeader=findViewById(R.id.rlHeader);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_MemberDocumentDetails:
                        startActivity(new Intent(getApplicationContext(), MemberDacumntsDetails.class));

                break;
            case R.id.btn_GuarantorDocumentDetails:
                startActivity(new Intent(getApplicationContext(), GurantoroneDacumentactivity.class));

                break;
            case R.id.btn_GuarantortwoDocumentDetails:
                startActivity(new Intent(getApplicationContext(), GurantorTwoDacumentactivity.class));

                break;
            case R.id.rlHeader:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                break;
        }

    }
}