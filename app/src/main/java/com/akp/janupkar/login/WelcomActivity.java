package com.akp.janupkar.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.basic.ForgotActivity;
import com.akp.janupkar.basic.MainActivity;
import com.akp.janupkar.customer.OTPVerify;
import com.akp.janupkar.view.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class WelcomActivity extends BaseActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvForgot;
    EditText edtUserId,edt_password,edt_mobile;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    String[] logintype = new String[]{"Select Login Type","Customer", "Collector"};
    String selectedName="";
    Spinner tvSelectType;
    LinearLayout llCustomer,llCollectorLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        findViewById();
        setListner();
    }

    private void setListner() {
        btnLogin.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
    }

    private void findViewById() {
        btnLogin=findViewById(R.id.btnLogin);
        tvForgot=findViewById(R.id.tvForgot);
        edtUserId=findViewById(R.id.edtUserId);
        edt_password=findViewById(R.id.edt_password);
        tvSelectType=findViewById(R.id.tvSelectType);
        edt_mobile=findViewById(R.id.edt_mobile);
        llCustomer=findViewById(R.id.llCustomer);
        llCollectorLogin=findViewById(R.id.llCollectorLogin);
        final List<String> plantsList = new ArrayList<>(Arrays.asList(logintype));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        tvSelectType.setAdapter(spinnerArrayAdapter);

        tvSelectType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView spinnerText = (TextView) tvSelectType.getChildAt(0);

                spinnerText.setTextColor(Color.BLACK);
                if (tvSelectType.getSelectedItem().toString().trim() == "Select Login Type") {
                    selectedName="2";
                } else {


                    selectedName = String.valueOf(tvSelectType.getSelectedItem());
                    if(selectedName.equalsIgnoreCase("Customer"))
                    {
                        selectedName="1";
                        llCustomer.setVisibility(View.VISIBLE);
                        llCollectorLogin.setVisibility(View.GONE);
                    }
                    else if(selectedName.equalsIgnoreCase("Collector"))
                    {
                        selectedName="2";
                        llCustomer.setVisibility(View.GONE);
                        llCollectorLogin.setVisibility(View.VISIBLE);
                    }

                    Log.v( "easd", selectedName );


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
                if(selectedName.equalsIgnoreCase("2"))
                {
                    if (edtUserId.getText().toString().equalsIgnoreCase("")){
                        edtUserId.setError("Fields can't be blank!");
                        edtUserId.requestFocus();
                    }
                    else if (edt_password.getText().toString().equalsIgnoreCase("")){
                        edt_password.setError("Fields can't be blank!");
                        edt_password.requestFocus();
                    }
                    else {
                        getLoginAPI(edtUserId.getText().toString(),edt_password.getText().toString());
                    }
                }
                else
                {
                    if (edt_mobile.getText().toString().equalsIgnoreCase("")){
                        edt_mobile.setError("Fields can't be blank!");
                        edt_mobile.requestFocus();
                    }
                    else {
                        LoginCustomer(edt_mobile.getText().toString());

                    }
                }

                break;
            case R.id.tvForgot:
                startActivity(new Intent(mActivity, ForgotActivity.class));
                break;
        }

    }
    public  void  LoginCustomer(String MobileNo)
    {
        String otp1 = new GlobalAppApis().LoginCustomer(MobileNo);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.LoginCustomer(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                        login_editor = login_preference.edit();
                        login_editor.putString("OTP",jsonObject.getString("OTP"));
                        login_editor.putString("Mobile",edt_mobile.getText().toString());
                        login_editor.commit();
                        startActivity(new Intent(mActivity, OTPVerify.class));
                        finish();
                    }
                    else {
                        Toast.makeText(WelcomActivity.this, "Userid or Password is wrong..", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WelcomActivity.this, call1, "", true);

    }
    public void  getLoginAPI(String mobile,String deviceid){
        String otp1 = new GlobalAppApis().Login(mobile,deviceid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.LoginService(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                        login_editor = login_preference.edit();
                        login_editor.putString("UserId",jsonObject.getString("MobileNo"));
                        login_editor.putString("UserName",jsonObject.getString("UserName"));
                        login_editor.putString("RoleType",jsonObject.getString("RoleType"));
                        login_editor.commit();
                        startActivity(new Intent(mActivity,MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(WelcomActivity.this, "Userid or Password is wrong..", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, WelcomActivity.this, call1, "", true);
    }
}