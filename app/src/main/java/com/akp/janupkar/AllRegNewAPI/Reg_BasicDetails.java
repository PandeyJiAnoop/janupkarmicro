package com.akp.janupkar.AllRegNewAPI;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.basic.Utility;
import com.akp.janupkar.utils.AppUtils;
import com.akp.janupkar.view.BaseActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;

public class Reg_BasicDetails extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    RelativeLayout rlHeader;
    Button btn_signUp;
    TextInputEditText edtMemberName,edtMobile,edtFathersHusband,edtAddress,edt_age,edtHusband,edtaltMobile,
            edtDistrict,edtaadhar;
    TextInputEditText edt_bankname,edt_accountno,edt_ifsc,edt_accholdername,edt_branchname;
    TextView tvRegistrationDate,tvDOB;
    Spinner tvSelectType;
    ArrayList<String> branch_list;
    ArrayList<String> branch_listId;
    ArrayList<HashMap<String, String>> branchList;
    //////////////////
    String branchcity;
    SpinnerAdapter branchadapter;
    DatePickerDialog picker;
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;
    String  gender="";
    RadioButton rdbMale,rdbFemale,rdbOther;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    private SharedPreferences sharedPreferences;
    String UserName,profarmaNo;

    Spinner tvSelectType1;
    ArrayList<String> loan_list;
    ArrayList<String> loan_listId;
    ArrayList<HashMap<String, String>> loanList;
    String loan;
    Spinner tvLoanPlan;
    ArrayList<String> LoanPlan_list;
    ArrayList<String> LoanPlan_listId;
    ArrayList<HashMap<String, String>> LoanPlanList;
    String LoanPlan;
    LoanPlanadapter LoanPlanadapter;

    SpinnerAdapter1 branchadapter1;

//Image upload
private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String imgString = "",adhar ="",panfont_base = "",panback_base = "";
    String chosseimag="";
    LinearLayout aadhar_back_ll,aadhar_font_ll,profile_ll,passbook_ll;
    ImageView aadhar_back_img,aadhar_font_img,pan_back_img,pan_font_img;


//    String[] courses = {"Testing fields Officers~EMP0015","Pawan Kumar~EMP0017"};
    Spinner sp;
//    String SelectedValue;


    String stateid; ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_basic_details);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserName = sharedPreferences.getString("UserName", "");
        Log.d("UserNameid",UserName);
        findViewById();
        setListner();

        hitCity();
        GetFieldOfficerList();
    }

    private void setListner() {
        rlHeader.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        tvRegistrationDate.setOnClickListener(this);
        rdbMale.setOnClickListener(this);
        rdbFemale.setOnClickListener(this);
        rdbOther.setOnClickListener(this);
    }

    private void findViewById() {
        tvSelectType1 = findViewById(R.id.tvSelectType1);
        tvLoanPlan = findViewById(R.id.tvLoanPlan);

        edt_bankname=findViewById(R.id.edt_bankname);
        edt_accountno=findViewById(R.id.edt_accountno);
        edt_ifsc=findViewById(R.id.edt_ifsc);
        edt_accholdername=findViewById(R.id.edt_accholdername);
        edt_branchname=findViewById(R.id.edt_branchname);

        rlHeader=findViewById(R.id.rlHeader);
        btn_signUp=findViewById(R.id.btn_signUp);
        edtMemberName=findViewById(R.id.edtMemberName);
        edtFathersHusband=findViewById(R.id.edtFathersHusband);
        edtMobile=findViewById(R.id.edtMobile);
        tvSelectType=findViewById(R.id.tvSelectType);
        tvRegistrationDate=findViewById(R.id.tvRegistrationDatee);
        edtAddress=findViewById(R.id.edtAddress);
        tvDOB=findViewById(R.id.tvDOB);
        edt_age=findViewById(R.id.edt_age);
        edtHusband=findViewById(R.id.edtHusband);

        edtDistrict=findViewById(R.id.edtDistrict);
        edtaadhar=findViewById(R.id.edtaadhar);
        edtaltMobile=findViewById(R.id.edtaltMobile);
        rdbMale=findViewById(R.id.tvMale);
        rdbFemale=findViewById(R.id.tvFemale);
        rdbOther=findViewById(R.id.tvOther);


        aadhar_font_ll= findViewById(R.id.aadhar_font_ll);
        aadhar_back_ll= findViewById(R.id.aadhar_back_ll);

        aadhar_font_img= findViewById(R.id.aadhar_font_img);
        aadhar_back_img= findViewById(R.id.aadhar_back_img);

        profile_ll= findViewById(R.id.profile_ll);
        passbook_ll= findViewById(R.id.passbook_ll);
        pan_back_img= findViewById(R.id.profile_img);
        pan_font_img= findViewById(R.id.passbook_img);

        branch_list = new ArrayList<>();
        branch_listId = new ArrayList<>();
        branchList = new ArrayList<>();


        loan_list = new ArrayList<>();
        loan_listId = new ArrayList<>();
        loanList = new ArrayList<>();

        LoanPlan_list = new ArrayList<>();
        LoanPlan_listId = new ArrayList<>();
        LoanPlanList = new ArrayList<>();


        sp = findViewById(R.id.employee_sp);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j);
//                            stateid = StateId.get(j - 1);
                            break; } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

//        sp.setOnItemSelectedListener(this);
//        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
//        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp.setAdapter(ad);

        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldrr = Calendar.getInstance();
                int dayy = cldrr.get(Calendar.DAY_OF_MONTH);
                int monthh = cldrr.get(Calendar.MONTH);
                int yearr = cldrr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Reg_BasicDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDOB.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                // getAge(dayOfMonth,monthOfYear,year);
                            }
                        }, yearr, monthh, dayy);
                picker.show();
            }
        });

        tvSelectType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString() == "Select Loan Plan") {
                } else {
                    branchcity = branch_listId.get( tvSelectType.getSelectedItemPosition() );
                    LoanTypeApi(branchcity);
                }}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        tvSelectType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                loan = loan_listId.get(tvSelectType1.getSelectedItemPosition());
//                LoanPlann(loan);
                if (parent.getItemAtPosition(position).toString() == "Select Loan Plan") {
                } else {
                    loan = loan_listId.get(tvSelectType1.getSelectedItemPosition());
                    LoanPlann(loan);
                }}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});
        tvLoanPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {@Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoanPlan = LoanPlan_listId.get(tvLoanPlan.getSelectedItemPosition());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        aadhar_font_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                chosseimag="1";
            }});
        aadhar_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseimag="2";
                selectImage();
            }});
        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseimag="3";
                selectImage();
            }});
        passbook_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseimag="4";
                selectImage();
            }});

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvMale:
                gender="Male";
                break;
            case R.id.tvFemale:
                gender="Female";
                break;
            case R.id.tvOther:
                gender="Other";
                break;
            case R.id.tvRegistrationDatee:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(mActivity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvRegistrationDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
                break;
            case R.id.rlHeader:
                finish();
                break;
            case R.id.btn_signUp:
                if (edtMemberName.getText().toString().equalsIgnoreCase("")){
                    edtMemberName.setError("Fields can't be blank!");
                    edtMemberName.requestFocus();
                }
                else if (edtMobile.getText().toString().equalsIgnoreCase("")){
                    edtMobile.setError("Fields can't be blank!");
                    edtMobile.requestFocus();
                }
                else if (edtFathersHusband.getText().toString().equalsIgnoreCase("")){
                    edtFathersHusband.setError("Fields can't be blank!");
                    edtFathersHusband.requestFocus();
                }
                else if (edtHusband.getText().toString().equalsIgnoreCase("")){
                    edtHusband.setError("Fields can't be blank!");
                    edtHusband.requestFocus();
                }
                else if (edtaltMobile.getText().toString().equalsIgnoreCase("")){
                    edtaltMobile.setError("Fields can't be blank!");
                    edtaltMobile.requestFocus();
                }

                else if (tvRegistrationDate.getText().toString().equalsIgnoreCase("")){
                    tvRegistrationDate.setError("Fields can't be blank!");
                    tvRegistrationDate.requestFocus();
                }
                else if (tvDOB.getText().toString().equalsIgnoreCase("")){
                    tvDOB.setError("Fields can't be blank!");
                    tvDOB.requestFocus();
                }
                else if (edt_age.getText().toString().equalsIgnoreCase("")){
                    edt_age.setError("Fields can't be blank!");
                    edt_age.requestFocus();
                }
                else if (edtAddress.getText().toString().equalsIgnoreCase("")){
                    edtAddress.setError("Fields can't be blank!");
                    edtAddress.requestFocus();
                }
                else if (edtDistrict.getText().toString().equalsIgnoreCase("")){
                    edtDistrict.setError("Fields can't be blank!");
                    edtDistrict.requestFocus();
                }
                else if (edtaadhar.getText().toString().equalsIgnoreCase("")){
                    edtaadhar.setError("Fields can't be blank!");
                    edtaadhar.requestFocus();
                }
                else if (gender.equalsIgnoreCase("")){
                    Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
                }
                else if (edt_bankname.getText().toString().equalsIgnoreCase("")){
                edt_bankname.setError("Fields can't be blank!");
                edt_bankname.requestFocus();
            }
            else if (edt_accountno.getText().toString().equalsIgnoreCase("")){
                edt_accountno.setError("Fields can't be blank!");
                edt_accountno.requestFocus();
            }
            else if (edt_ifsc.getText().toString().equalsIgnoreCase("")){
                edt_ifsc.setError("Fields can't be blank!");
                edt_ifsc.requestFocus();
            }
            else if (edt_accholdername.getText().toString().equalsIgnoreCase("")){
                edt_accholdername.setError("Fields can't be blank!");
                edt_accholdername.requestFocus();
            }
            else if (edt_branchname.getText().toString().equalsIgnoreCase("")){
                edt_branchname.setError("Fields can't be blank!");
                edt_branchname.requestFocus();
            }
                else if (aadhar_font_img.getDrawable() == null){
                    Toast.makeText(getApplicationContext(),"Image doesn´t exist.\nUpload Aadhar Front Image Less than 1 MB",Toast.LENGTH_LONG).show();
                    //Image doesn´t exist.
                }
                else if (aadhar_back_img.getDrawable() == null){
                    Toast.makeText(getApplicationContext(),"Image doesn´t exist.\nUpload Aadhar Back Image Less than 1 MB",Toast.LENGTH_LONG).show();
            }
                else if (pan_back_img.getDrawable() == null){
                    Toast.makeText(getApplicationContext(),"Image doesn´t exist.\nUpload Profile Image Less than 1 MB",Toast.LENGTH_LONG).show();
                }
                else if (pan_font_img.getDrawable() == null){
                    Toast.makeText(getApplicationContext(),"Image doesn´t exist.\nUpload Passbook Image Less than 1 MB",Toast.LENGTH_LONG).show();
                }
                else {
                    MemberBasicdetailadd(adhar,imgString,edtAddress.getText().toString(),
                            edt_accountno.getText().toString(),edt_accholdername.getText().toString(),
                            edtAddress.getText().toString(),edt_age.getText().toString(),
                            edtaltMobile.getText().toString(),edt_bankname.getText().toString(),
                            branchcity,"",loan,tvDOB.getText().toString(),
                            edtDistrict.getText().toString(),stateid,UserName,edtFathersHusband.getText().toString(),gender,
                            LoanPlan,edtHusband.getText().toString(),edt_ifsc.getText().toString(),
                            UserName,edtMemberName.getText().toString(),"",edtMobile.getText().toString(),
                            panback_base,tvRegistrationDate.getText().toString());
//                    MemberBasicdetailadd(edtMemberName.getText().toString(),edtMobile.getText().toString(),edtFathersHusband.getText().toString(),tvRegistrationDate.getText().toString()
//                            ,tvDOB.getText().toString(),edt_age.getText().toString(),edtAddress.getText().toString(),gender);
                }
                break;
        }
    }
    //getCity
    public void hitCity() {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/BranchList";
        Log.v( "#####", url );
        Log.v( "urlApi", url );
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        AndroidNetworking.post( url ).addJSONObjectBody( jsonObject )
                .setPriority( Priority.HIGH ).build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJsonnn( response );
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.hideDialog();
                            Log.v( "tyu", error.getMessage() );
                            Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                            Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        }
                    }
                } );
    }

    private void parseJsonnn(JSONObject response) {
        Log.v( "responseGetCat", response.toString() );
        try {
            String status = response.getString( "Status" );
            branchList.clear();
            branch_list.clear();
            branch_listId.clear();
            if (status.equals( "true" )) {
                JSONArray data_array = response.getJSONArray( "Branlist" );
                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject( i );
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put( "BranchCode", block_object.get( "BranchCode" ).toString() );
                    map.put( "Branch", block_object.get( "Branch" ).toString() );
                    branchList.add( map );
                    branch_list.add(  branchList.get( i ).get( "Branch" ) );
                    branch_listId.add(  branchList.get( i ).get( "BranchCode" ) );
                    Log.d( "CategorylistID11",  branchList.get( i ).get( "BranchCode" ) );
                }
                branchadapter = new SpinnerAdapter( getApplicationContext(), R.layout.spinner, (ArrayList<String>) branch_list );
                tvSelectType.setAdapter( branchadapter );
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }
        AppUtils.hideDialog();
    }

    public static class SpinnerAdapter extends ArrayAdapter<String> {
        ArrayList<String> data;
        public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {
            super( context, textViewResourceId, arraySpinner_time );
            this.data = arraySpinner_time;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }
        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
            View row = inflater.inflate( R.layout.spinner, parent, false );
            TextView label = (TextView) row.findViewById( R.id.tvName );
            label.setText( data.get( position ) );
            return row;
        }
    }

    public void  MemberBasicdetailadd(String AadharBackPic, String AadharFrontPic, String AadharNo,
                                      String AccountNo, String Accountholdername, String Address,
                                      String Age, String AlternateMobileNo, String BankName,
                                      String BranchCode, String BranchName, String CenterCode, String DateofBirth,
                                      String District, String EmployeeCode, String EntryBy, String FatherHusbandName, String Gender,
                                      String GroupCode, String HusbandName, String IFSCCode,
                                      String MemberId, String MemberName, String MemberType, String MobileNo,
                                      String Passbookfile, String RegistrationDate){
        String otp1 = new GlobalAppApis().MemberBasicdetailadd_New(AadharBackPic,AadharFrontPic,AadharNo,AccountNo,
                Accountholdername,Address,Age,AlternateMobileNo,BankName,BranchCode,BranchName,CenterCode,DateofBirth,District,
                EmployeeCode,EntryBy,FatherHusbandName,Gender,GroupCode,HusbandName,IFSCCode,MemberId,MemberName,MemberType,MobileNo,
                Passbookfile,RegistrationDate);
        Log.v("responseeeee_otpbasic", otp1);

        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.MemberBasicdetailTempAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeeebasic", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                        login_editor = login_preference.edit();
                        login_editor.putString("MemberId",jsonObject.getString("MemberId"));
                        login_editor.putString("profarmaNo",jsonObject.getString("profarmaNo"));
                        login_editor.putString("BranchCode",jsonObject.getString("BranchCode"));
                        Toast.makeText(Reg_BasicDetails.this, "Basic Details Add Successfully", Toast.LENGTH_SHORT).show();
                        login_editor.commit();

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Reg_BasicDetails.this);
                        builder.setTitle("Basic Details Add Successfully!")
                                .setMessage(jsonObject.getString("Msg"))
                                .setCancelable(false)
                                .setIcon(R.drawable.appicon)
                                .setPositiveButton("Next> Loan Details Form", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(mActivity, Reg_LoanDetails.class));
                                        dialog.dismiss();
                                    }});
                        builder.create().show();

                    }
                    else {
                        Toast.makeText(Reg_BasicDetails.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }} }, Reg_BasicDetails.this, call1, "", true);
    }


    //getLoanType
    public void LoanTypeApi(String branch_id) {
        AppUtils.showRequestDialog(mActivity);
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/CenterList_ByBranch";
        Log.v("#####", url);
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("BranchCode", branch_id);
            Log.v("finddObject", String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJsonnn1(response);
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
                        }}});
    }

    private void parseJsonnn1(JSONObject response) {
        Log.v("responseGetCatcenter", response.toString());
        AppUtils.hideDialog();
        try {
            String status = response.getString("Status");
            loanList.clear();
            loan_list.clear();
            loan_listId.clear();
            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("Centerlist");
                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("CenterName", block_object.get("CenterName").toString());
                    map.put("CenterCode", block_object.get("CenterCode").toString());
                    loanList.add(map);
                    loan_list.add(loanList.get(i).get("CenterName"));
                    loan_listId.add(loanList.get(i).get("CenterCode"));
                    Log.d("CategorylistID11", loanList.get(i).get("CenterCode"));
                }
                branchadapter1 = new SpinnerAdapter1(getApplicationContext(), R.layout.spinner1, (ArrayList<String>) loan_list);
                tvSelectType1.setAdapter(branchadapter1);
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AppUtils.hideDialog();
    }
    //getLoanType
    public void LoanPlann(String centercode) {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/GroupList_ByCenter";
        Log.v("#####", url);
        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CenterCode", centercode);
            Log.v("finddObject", String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJson(response);
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
                        }}});
    }

    private void parseJson(JSONObject response) {
        Log.v("responseGetCat", response.toString());
        try {
            String status = response.getString("Status");
            LoanPlanList.clear();
            LoanPlan_list.clear();
            LoanPlan_listId.clear();
            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("Grouplist");
                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("GroupCode", block_object.get("GroupCode").toString());
                    map.put("GroupName", block_object.get("GroupName").toString());
                    LoanPlanList.add(map);
                    LoanPlan_list.add(LoanPlanList.get(i).get("GroupName"));
                    LoanPlan_listId.add(LoanPlanList.get(i).get("GroupCode"));
                    Log.d("CategorylistID11", LoanPlanList.get(i).get("GroupCode"));
                }
                LoanPlanadapter = new LoanPlanadapter(getApplicationContext(), R.layout.spinner2, (ArrayList<String>) LoanPlan_list);
                tvLoanPlan.setAdapter(LoanPlanadapter);
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AppUtils.hideDialog();
    }

    public static class SpinnerAdapter1 extends ArrayAdapter<String> {
        ArrayList<String> data1;
        public SpinnerAdapter1(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {
            super(context, textViewResourceId, arraySpinner_time);
            this.data1 = arraySpinner_time;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View row1 = inflater.inflate(R.layout.spinner1, parent, false);
            TextView label1 = (TextView) row1.findViewById(R.id.tvName1);
            label1.setText(data1.get(position));
            return row1;
        }
    }

    public static class LoanPlanadapter extends ArrayAdapter<String> {

        ArrayList<String> data2;

        public LoanPlanadapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super(context, textViewResourceId, arraySpinner_time);

            this.data2 = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View row2 = inflater.inflate(R.layout.spinner2, parent, false);
            TextView label2 = (TextView) row2.findViewById(R.id.tvName2);
            label2.setText(data2.get(position));
            return row2;
        } }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break; }}

    private void selectImage() {
        final CharSequence[] items = {"Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Reg_BasicDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Reg_BasicDetails.this);
                galleryIntent();
//                if (items[item].equals("Choose from Gallery")) {
//                    userChoosenTask = "Choose from Gallery";
//                    if (result){
//                        galleryIntent();
//                    }
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(chosseimag.equalsIgnoreCase("1"))
        {
            aadhar_font_img.setImageBitmap(thumbnail);
            getEncoded64ImageStringFromBitmap(thumbnail);
        }
        else {
            aadhar_back_img.setImageBitmap(thumbnail);
            getEncoded64ImageStringFromBitmap(thumbnail);
        }


    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(chosseimag.equalsIgnoreCase("1"))
        {
            aadhar_font_img.setImageBitmap(bm);

        }
        else if(chosseimag.equalsIgnoreCase("2"))
        {
            aadhar_back_img.setImageBitmap(bm);

        }

        else if(chosseimag.equalsIgnoreCase("3"))
        {
            pan_back_img.setImageBitmap(bm);

        }
        else if(chosseimag.equalsIgnoreCase("4"))
        {
            pan_font_img.setImageBitmap(bm);

        }

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        Log.v("dadadadadaaaa", imgString);


        if(chosseimag.equalsIgnoreCase("1"))
        {
            imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        }
        else if(chosseimag.equalsIgnoreCase("2"))
        {
            adhar = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        }

        else if(chosseimag.equalsIgnoreCase("3"))
        {
            panback_base = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        }
        else if(chosseimag.equalsIgnoreCase("4"))
        {
            panfont_base = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        }
        return adhar;
    }




    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id)
    {
//        SelectedValue=sp.getSelectedItem().toString();
//        Toast.makeText(getApplicationContext(),spin.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView arg0)
    {
        // Auto-generated method stub
    }



    public void GetFieldOfficerList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.FieldOfficerList();
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_GetPackageList","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("Status")==false){
                        String msg       = object.getString("Message");
                        Toast.makeText(getApplicationContext(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONArray jsonArray = object.getJSONArray("Branlist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            StateId.add(jsonObject1.getString("EmpCode"));
                            String statename = jsonObject1.getString("EmpName");
                            StateName.add(statename); }
                        sp.setAdapter(new ArrayAdapter<String>(Reg_BasicDetails.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                    } } catch (JSONException e) {
                    e.printStackTrace();
                } }}, Reg_BasicDetails.this, call1, "", true);
    }
}