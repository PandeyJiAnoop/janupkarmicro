package com.akp.janupkar.customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.view.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CustomerProfileActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlHeader;
    TextView tvName,tvContact,tvAddress,tvGender,tvAge,tvBranchName,
            tvBranchCode,tvDateofBirth,tvFatherHusbandName,tvMemberType,tvMemberId;
    private SharedPreferences login_preference;
    String EmpCode;
    TextView tv_first_letter_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        findViewById();
        setLisner();
    }

    private void setLisner() {
        rlHeader.setOnClickListener(this);
    }

    private void findViewById() {
        rlHeader=findViewById(R.id.rlHeader);
        tvName=findViewById(R.id.tvName);
        tvContact=findViewById(R.id.tvContact);
        tvAddress=findViewById(R.id.tvAddress);
        tvGender=findViewById(R.id.tvGender);
        tvAge=findViewById(R.id.tvAge);
        tvBranchName=findViewById(R.id.tvBranchName);
        tvBranchCode=findViewById(R.id.tvBranchCode);
        tvDateofBirth=findViewById(R.id.tvDateofBirth);
        tvFatherHusbandName=findViewById(R.id.tvFatherHusbandName);
        tvMemberType=findViewById(R.id.tvMemberType);
        tvMemberId=findViewById(R.id.tvMemberId);

        tv_first_letter_text=findViewById(R.id.tv_first_letter_text);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        EmpCode = login_preference.getString("MemberId", "");
        CustomerProfile();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlHeader:
                finish();
                break;
        }

    }


    private void CustomerProfile() {
        String otp1 = new GlobalAppApis().CustomerProfile(EmpCode);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CustomerProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);

                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        String Address=jsonObject.getString("Address");
                        String Age=jsonObject.getString("Age");
                        String BranchCode=jsonObject.getString("BranchCode");
                        String BranchName=jsonObject.getString("BranchName");
                        String DateofBirth=jsonObject.getString("DateofBirth");
                        String FatherHusbandName=jsonObject.getString("FatherHusbandName");
                        String Gender=jsonObject.getString("Gender");
                        String MemberId=jsonObject.getString("MemberId");
                        String MemberName=jsonObject.getString("MemberName");
                        String MemberType=jsonObject.getString("MemberType");
                        String MobileNo=jsonObject.getString("MobileNo");
                        tvName.setText(MemberName);
                        tvAddress.setText(Address);
                        tvAge.setText(Age);
                        tvBranchCode.setText(BranchCode);
                        tvBranchName.setText(BranchName);
                        tvDateofBirth.setText(DateofBirth);
                        tvFatherHusbandName.setText(FatherHusbandName);
                        tvGender.setText(Gender);
                        tvMemberId.setText(MemberId);
                        tvMemberType.setText(MemberType);
                        tvContact.setText("+91 "+MobileNo);
                        String frstLetter = tvName.getText().toString().substring(0, 1);
                        tv_first_letter_text.setText(frstLetter);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }
}