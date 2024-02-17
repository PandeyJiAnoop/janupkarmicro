package com.akp.janupkar.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class OTPVerify extends AppCompatActivity {
    //TextView
    TextView tvMobileNo, tvResend;

    //EditText
    EditText etCode1, etCode2, etCode3, etCode4, etCode5, etCode6,etreferal;

    int check = 0;
    TextView tvTimerText,tvOtp;
    //Button
    Button btnSubmit;
    ImageView Timer;
    private SharedPreferences sharedPreferences;
    String OTP,Mobile;
    private SharedPreferences.Editor login_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verify);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        OTP = sharedPreferences.getString("OTP", "");
        Mobile = sharedPreferences.getString("Mobile", "");

        //Button
        btnSubmit = findViewById( R.id.btnSubmit );


        //EditText
        etCode1 = findViewById( R.id.etCode1 );
        etCode2 = findViewById( R.id.etCode2 );
        etCode3 = findViewById( R.id.etCode3 );
        etCode4 = findViewById( R.id.etCode4 );
        etCode5 = findViewById( R.id.etCode5 );
        etCode6 = findViewById( R.id.etCode6 );
        tvOtp = findViewById( R.id.tvOtp );
        tvMobileNo = findViewById( R.id.tvMobileNo );
        tvOtp.setText("Your Otp " +OTP );
        tvMobileNo.setText("+91 " +Mobile );

        etCode1.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    etCode2.requestFocus();
            }
        } );

        etCode2.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode3.requestFocus();
            }
        } );

        etCode3.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode4.requestFocus();
            }
        } );

        etCode4.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode5.requestFocus();
            }
        } );

        etCode5.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    etCode6.requestFocus();
            }
        } );

        etCode6.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        } );

        etCode1.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode1.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    }

                }
                return false;
            }
        } );

        etCode2.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode2.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    }

                }
                return false;
            }
        } );


        etCode3.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode3.getText().toString().trim().isEmpty()) {
                        etCode2.requestFocus();
                    }

                }
                return false;
            }
        } );


        etCode4.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode4.getText().toString().trim().isEmpty()) {
                        etCode3.requestFocus();
                    }

                }
                return false;
            }
        } );


        etCode5.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode5.getText().toString().trim().isEmpty()) {
                        etCode4.requestFocus();
                    }

                }
                return false;
            }
        } );

        etCode6.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace

                    if (etCode6.getText().toString().trim().isEmpty()) {
                        etCode5.requestFocus();
                    }

                }
                return false;
            }
        } );
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etCode1.getText().toString().trim() +
                        etCode2.getText().toString().trim() +
                        etCode3.getText().toString().trim() +
                        etCode4.getText().toString().trim() +
                        etCode5.getText().toString().trim() +
                        etCode6.getText().toString().trim();

                if (otp.length() != 6) {
                    AppUtils.showToastSort( getApplicationContext(),"Enter Valid OTP");
                } else {
                    Otpverify( otp );
                }
            }


        });
    }
    private void Otpverify(String otp) {
        String otp1 = new GlobalAppApis().Otpverify(Mobile,otp);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Otpverify(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
                        login_editor = sharedPreferences.edit();
                        login_editor.putString("BranchCode",jsonObject.getString("BranchCode"));
                        login_editor.putString("EmployeCode",jsonObject.getString("EmployeCode"));
                        login_editor.putString("MemberId",jsonObject.getString("MemberId"));
                        login_editor.putString("MemberName",jsonObject.getString("MemberName"));
                        login_editor.putString("Message",jsonObject.getString("Message"));
                        login_editor.putString("RoleType",jsonObject.getString("RoleType"));
                        login_editor.commit();
                        startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                        finish();
                    }
                    else {
                        Toast.makeText(OTPVerify.this, "Userid or Password is wrong..", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, OTPVerify.this, call1, "", true);

    }
}