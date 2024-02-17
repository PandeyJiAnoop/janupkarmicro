package com.akp.janupkar.membershipregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.basic.MainActivity;
import com.akp.janupkar.view.BaseActivity;

public class MemberShip extends BaseActivity implements View.OnClickListener {
    Button btn_BasicDeatils,btn_Guarantor;
    RelativeLayout rlHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship);
        findViewById();
        setListner();
    }

    private void setListner() {
        btn_BasicDeatils.setOnClickListener(this);
        btn_Guarantor.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }

    private void findViewById() {
        btn_BasicDeatils=findViewById(R.id.btn_BasicDeatils);
        btn_Guarantor=findViewById(R.id.btn_Guarantor);
        rlHeader=findViewById(R.id.rlHeader);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_BasicDeatils:
                startActivity(new Intent(mActivity, MemberShipBasicRegistrationActivity.class));
                break;
            case R.id.btn_Guarantor:
                Toast.makeText(getApplicationContext(),"Firstly Add basic Details",Toast.LENGTH_LONG).show();
                startActivity(new Intent(mActivity, MemberShipGurontorActivity.class));
                break;
            case R.id.rlHeader:
                startActivity(new Intent(mActivity, MainActivity.class));
                break;
        }
    }
}