package com.akp.janupkar.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.login.BaiscDetails;
import com.akp.janupkar.login.GuarantorDetailsActivity;
import com.akp.janupkar.login.LoanDeatilsActivity;
import com.akp.janupkar.view.BaseActivity;

public class Registration extends BaseActivity implements View.OnClickListener {
    Button btn_BasicDeatils,btn_LoanDetails,btn_Guarantor;
    RelativeLayout rlHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        findViewById();
        setListner();
    }

    private void setListner() {
        btn_BasicDeatils.setOnClickListener(this);
        btn_LoanDetails.setOnClickListener(this);
        btn_Guarantor.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }

    private void findViewById() {
        btn_BasicDeatils=findViewById(R.id.btn_BasicDeatils);
        btn_LoanDetails=findViewById(R.id.btn_LoanDetails);
        btn_Guarantor=findViewById(R.id.btn_Guarantor);
        rlHeader=findViewById(R.id.rlHeader);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_BasicDeatils:
                startActivity(new Intent(mActivity, BaiscDetails.class));
                break;
            case R.id.btn_LoanDetails:
                Toast.makeText(mActivity, "Firstly Add Basic Details!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, LoanDeatilsActivity.class));
                break;
            case R.id.btn_Guarantor:
                Toast.makeText(mActivity, "Firstly Add Basic Details and Loan Details!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, GuarantorDetailsActivity.class));
                break;
            case R.id.rlHeader:
                onBackPressed();
                finish();
                break;
        }
    }
}