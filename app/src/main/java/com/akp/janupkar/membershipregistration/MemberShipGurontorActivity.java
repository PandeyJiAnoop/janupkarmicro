package com.akp.janupkar.membershipregistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.akp.janupkar.R;
import com.akp.janupkar.utils.AppUtils;
import com.akp.janupkar.view.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberShipGurontorActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlHeader;
    TextInputEditText edt_memberid,edt_name,edt_mobile,edt_Address,edt_memberid1,edt_name1,edt_mobile1,edt_Address1;
    Button btnSubmit;
    private SharedPreferences sharedPreferences;
    String MemberId,profarmaNo,getMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_gurontor);
        findViewById();
        setListner();
    }

    private void setListner() {
        rlHeader.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void findViewById() {
        rlHeader=findViewById(R.id.rlHeader);
        edt_memberid=findViewById(R.id.edt_memberid);
        edt_name=findViewById(R.id.edt_name);
        edt_mobile=findViewById(R.id.edt_mobile);
        edt_Address=findViewById(R.id.edt_Address);
        edt_memberid1=findViewById(R.id.edt_memberid1);
        edt_name1=findViewById(R.id.edt_name1);
        edt_mobile1=findViewById(R.id.edt_mobile1);
        edt_Address1=findViewById(R.id.edt_Address1);
        btnSubmit=findViewById(R.id.btnSubmit);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        MemberId = sharedPreferences.getString("UserName", "");
        profarmaNo = sharedPreferences.getString("profarmaNo", "");

        getMemberId=getIntent().getStringExtra("memberid");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlHeader:
                finish();
                break;
            case R.id.btnSubmit:
                if (edt_name.getText().toString().equalsIgnoreCase("")){
                    edt_name.setError("Fields can't be blank!");
                    edt_name.requestFocus();
                }
                else if (edt_mobile.getText().toString().equalsIgnoreCase("")){
                    edt_mobile.setError("Fields can't be blank!");
                    edt_mobile.requestFocus();
                }
                else if (edt_Address.getText().toString().equalsIgnoreCase("")){
                    edt_Address.setError("Fields can't be blank!");
                    edt_Address.requestFocus();
                }
                else if (edt_name1.getText().toString().equalsIgnoreCase("")){
                    edt_name1.setError("Fields can't be blank!");
                    edt_name1.requestFocus();
                }
                else if (edt_mobile1.getText().toString().equalsIgnoreCase("")){
                    edt_mobile1.setError("Fields can't be blank!");
                    edt_mobile1.requestFocus();
                }
                else if (edt_Address1.getText().toString().equalsIgnoreCase("")){
                    edt_Address1.setError("Fields can't be blank!");
                    edt_Address1.requestFocus();
                }
                else {

                    if (getMemberId.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Basic Details not Filled",Toast.LENGTH_LONG).show();
                    }
                    else {
                        MemberGaranterAdd();

                    }
                }
                break;
        }

    }

    public void MemberGaranterAdd() {
        AppUtils.showRequestDialog(mActivity);
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/MembershipMemberGaranterAdd";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {

            jsonObject.put("Guarantor1MobileNo",edt_mobile.getText().toString());
            jsonObject.put("Guarantor1Name",edt_name.getText().toString());
            jsonObject.put("Guarantor2MobileNo",edt_mobile1.getText().toString());
            jsonObject.put("Guarantor2Name",edt_name1.getText().toString());
            jsonObject.put("Gurantor1Address",edt_Address.getText().toString());
            jsonObject.put("Gurantor2Address",edt_Address1.getText().toString());
            jsonObject.put("Guarantor1Id",edt_memberid.getText().toString());
            jsonObject.put("Guarantor2Id",edt_memberid1.getText().toString());
            jsonObject.put("MemberId",getMemberId);

            Log.v("finddObject", String.valueOf(jsonObject));
        } catch (JSONException e) {

            e.printStackTrace();
        }

        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonloandetails(response);

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(ANError error) {

                // handle error
                if (error.getErrorCode() != 0) {
                    AppUtils.hideDialog();

                    Log.v("tyu", error.getMessage());
                    Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                    Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                    Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                } else {
                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                    Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                }
            }
        });
    }

    private void parseJsonloandetails(JSONObject response) {

        Log.v("responseGetCat", response.toString());
        AppUtils.hideDialog();


        try {


            String status = response.getString("Status");

            if (status.equals("true")) {
                Toast.makeText(this, "Gurantor Details add successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, MemberShip.class));
                finish();;




            } else {


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }

}