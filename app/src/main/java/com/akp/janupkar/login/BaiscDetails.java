package com.akp.janupkar.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.utils.AppUtils;
import com.akp.janupkar.view.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class BaiscDetails extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlHeader;
    Button btn_signUp;
    TextInputEditText edtMemberName,edtMobile,edtFathersHusband,edtAddress,edt_age;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        findViewById();
        setListner();
        hitCity();
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
        rdbMale=findViewById(R.id.tvMale);
        rdbFemale=findViewById(R.id.tvFemale);
        rdbOther=findViewById(R.id.tvOther);
        branch_list = new ArrayList<>();
        branch_listId = new ArrayList<>();
        branchList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserName = sharedPreferences.getString("UserName", "");
        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldrr = Calendar.getInstance();
                int dayy = cldrr.get(Calendar.DAY_OF_MONTH);
                int monthh = cldrr.get(Calendar.MONTH);
                int yearr = cldrr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(BaiscDetails.this,
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


                branchcity = branch_listId.get( tvSelectType.getSelectedItemPosition() );



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
                else if (gender.equalsIgnoreCase("")){
                    Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
                }
                else {
                    MemberBasicdetailadd(edtMemberName.getText().toString(),edtMobile.getText().toString(),edtFathersHusband.getText().toString(),tvRegistrationDate.getText().toString()
                    ,tvDOB.getText().toString(),edt_age.getText().toString(),edtAddress.getText().toString(),gender);
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

    public void  MemberBasicdetailadd(String member, String mobile, String fatherhusbend, String registrationdate, String dob, String age, String address, String gender){
        String otp1 = new GlobalAppApis().MemberBasicdetailadd(member,mobile,fatherhusbend,registrationdate,dob,age,address,gender,branchcity,UserName);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.MemberBasicdetailadd(otp1);
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
                        login_editor.putString("MemberId",jsonObject.getString("MemberId"));
                        login_editor.putString("profarmaNo",jsonObject.getString("profarmaNo"));
                        login_editor.putString("BranchCode",jsonObject.getString("BranchCode"));
                        Toast.makeText(BaiscDetails.this, "Basic Details Add Successfully", Toast.LENGTH_SHORT).show();
                        login_editor.commit();
                        startActivity(new Intent(mActivity, LoanDeatilsActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(BaiscDetails.this, "Some Thing Wrong tray again", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, BaiscDetails.this, call1, "", true);
    }

}