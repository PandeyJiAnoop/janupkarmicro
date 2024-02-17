
package com.akp.janupkar.login;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class LoanDeatilsActivity extends BaseActivity implements View.OnClickListener {
    TextView tvdddLoanType,tvEligible,tvMinPlan,tvMaxLoan, tvTotalIntrest, tvInterestCalMethod, tvAnnual, tvPaymentAmount, tvLoanProcessing;
    TextInputEditText edtAsstValue, edtRquiredLoan, edtLoanDuration, edtAssetDescription,tvLoanSecurity;
    Button btnSubmit;
    Spinner tvSelectType;
    ArrayList<String> loan_list;
    ArrayList<String> loan_listId;
    ArrayList<HashMap<String, String>> loanList;
    String loan;
    SpinnerAdapter branchadapter;
    private SharedPreferences sharedPreferences;
    String MemberId;
    String ProfarmaNo;
    String BranchCode;
    String LoanProccesingFeePer="0";
    String TotalInstallments="0";

    Spinner tvLoanPlan;
    ArrayList<String> LoanPlan_list;
    ArrayList<String> LoanPlan_listId;
    ArrayList<HashMap<String, String>> LoanPlanList;
    String LoanPlan;
    LoanPlanadapter LoanPlanadapter;


    Spinner tvLoanPurchage;
    ArrayList<String>   LoanPlanpurpose_list;
    ArrayList<String>   LoanPlanpurpose_listId;
    ArrayList<HashMap<String, String>>   LoanPlanpurposeList;
    String   LoanPlanpurpose,LoanPlanpurposeee;
    LoanPlanpurposeadapter LoanPlanpurposeadapter;



    Spinner tvInstallmentMode;
    ArrayList<String>   InstallmentMode_list;
    ArrayList<String>   InstallmentMode_listId;
    ArrayList<HashMap<String, String>>   InstallmentModeList;
    String   InstallmentMode,InstallmentModee;
    InstallmentModeadapter installmentModeadapter;
    RelativeLayout rlHeader;
    double persecur=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_deatils);
        findViewById();
        setListner();
        LoanTypeApi();
        Installmentddl();
    }

    private void setListner() {
        btnSubmit.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }

    private void findViewById() {
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        MemberId = sharedPreferences.getString("MemberId", "");
        ProfarmaNo = sharedPreferences.getString("profarmaNo", "");
        BranchCode = sharedPreferences.getString("BranchCode", "");
        tvSelectType = findViewById(R.id.tvSelectType);
        tvLoanPlan = findViewById(R.id.tvLoanPlan);
        tvMinPlan = findViewById(R.id.tvMinPlan);
        tvEligible = findViewById(R.id.tvEligible);
        tvdddLoanType = findViewById(R.id.tvLoanType);
        tvMaxLoan = findViewById(R.id.tvMaxLoan);
        tvLoanSecurity = findViewById(R.id.tvLoanSecurity);
        tvLoanPurchage = findViewById(R.id.tvLoanPurchage);
        tvTotalIntrest = findViewById(R.id.tvTotalIntrest);
        tvInterestCalMethod = findViewById(R.id.tvInterestCalMethod);
        tvInstallmentMode = findViewById(R.id.tvInstallmentMode);
        tvAnnual = findViewById(R.id.tvAnnual);
        tvPaymentAmount = findViewById(R.id.tvPaymentAmount);
        tvLoanProcessing = findViewById(R.id.tvLoanProcessing);
        edtAsstValue = findViewById(R.id.edtAsstValue);
        edtRquiredLoan = findViewById(R.id.edtRquiredLoan);
        edtLoanDuration = findViewById(R.id.edtLoanDuration);
        edtAssetDescription = findViewById(R.id.edtAssetDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        rlHeader = findViewById(R.id.rlHeader);
        loan_list = new ArrayList<>();
        loan_listId = new ArrayList<>();
        loanList = new ArrayList<>();

        LoanPlan_list = new ArrayList<>();
        LoanPlan_listId = new ArrayList<>();
        LoanPlanList = new ArrayList<>();

        LoanPlanpurpose_list = new ArrayList<>();
        LoanPlanpurpose_listId = new ArrayList<>();
        LoanPlanpurposeList = new ArrayList<>();

        InstallmentMode_list = new ArrayList<>();
        InstallmentMode_listId = new ArrayList<>();
        InstallmentModeList = new ArrayList<>();
        tvLoanSecurity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("fsfsfffss", String.valueOf(s));
                if(tvLoanSecurity.getText().toString().equalsIgnoreCase(""))
                {

                }
                else {
                    double securityamt= Double.parseDouble(tvLoanSecurity.getText().toString());
                    double totalreq = Double.parseDouble(edtRquiredLoan.getText().toString());
                     persecur=(securityamt / totalreq) * 100;
                    Log.v("dadaddadaadda", String.valueOf(persecur));


                   /* int AssestValue= Integer.parseInt(tvLoanSecurity.getText().toString());
                    int minplan= Math.round(Float.parseFloat(tvMinPlan.getText().toString()));
                    int MaxLoan= Math.round(Float.parseFloat(tvMaxLoan.getText().toString()));*/


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        tvSelectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).toString() == "Select Loan Plan") {

                } else {
                    loan = loan_listId.get(tvSelectType.getSelectedItemPosition());
                    LoanPlann(loan);



                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvLoanPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                LoanPlan = LoanPlan_listId.get(tvLoanPlan.getSelectedItemPosition());
                LoanPlanDetail(LoanPlan);
                LoanPlanpurpose(loan);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvLoanPurchage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                InstallmentMode = LoanPlanpurpose_listId.get(tvLoanPurchage.getSelectedItemPosition());
                InstallmentModee = LoanPlanpurpose_list.get(tvLoanPurchage.getSelectedItemPosition());



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvInstallmentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                LoanPlanpurpose = InstallmentMode_listId.get(tvInstallmentMode.getSelectedItemPosition());
                //LoanPlanpurposeee = InstallmentMode_listId.get(tvInstallmentMode.getSelectedItemPosition());



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtAsstValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v("fsfsfffss", String.valueOf(s));
                if(edtAsstValue.getText().toString().equalsIgnoreCase(""))
                {

                }
                else {
                    int AssestValue= Integer.parseInt(edtAsstValue.getText().toString());

                    int minplan= Math.round(Float.parseFloat(tvMinPlan.getText().toString()));
                    int MaxLoan= Math.round(Float.parseFloat(tvMaxLoan.getText().toString()));

                    Log.v("adadaddaaa", String.valueOf(AssestValue));
                    Log.v("adadaddaaaminplan", String.valueOf(minplan));
                    Log.v("adadaddaaaMaxLoan", String.valueOf(MaxLoan));
                    if (AssestValue >= minplan && AssestValue <= MaxLoan) {

                        tvEligible.setText("Member eligible for Rs."+AssestValue+" Loan.");
                    } else {

                        tvEligible.setText("Member not eligible for Rs."+AssestValue+" Loan.");

                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        tvInterestCalMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertgendermode();
            }
        });
        tvdddLoanType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoanProcessingFee();
            }
        });

        edtRquiredLoan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CalculationAddMember(tvAnnual.getText().toString(),tvInterestCalMethod.getText().toString(),tvLoanProcessing.getText().toString()
                ,LoanProccesingFeePer,tvLoanSecurity.getText().toString(),edtLoanDuration.getText().toString(),edtRquiredLoan.getText().toString()
                ,tvdddLoanType.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtLoanDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CalculationAddMember(tvAnnual.getText().toString(),tvInterestCalMethod.getText().toString(),tvLoanProcessing.getText().toString()
                        ,LoanProccesingFeePer,tvLoanSecurity.getText().toString(),edtLoanDuration.getText().toString(),edtRquiredLoan.getText().toString()
                        ,tvdddLoanType.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlHeader:
                finish();

                break;
            case R.id.btnSubmit:
             /* ;
                TextInputEditText , edtRquiredLoan, edtLoanDuration, edtAssetDescription;

                if(tvdddLoanType.getText().toString().)*/
                if(tvdddLoanType.getText().toString().equalsIgnoreCase(""))
                {
                    tvdddLoanType.setError("Fields can't be blank!");
                    tvdddLoanType.requestFocus();
                }
                else if(tvEligible.getText().toString().equalsIgnoreCase(""))
                {
                    tvEligible.setError("Fields can't be blank!");
                    tvEligible.requestFocus();
                }
                else if(tvMinPlan.getText().toString().equalsIgnoreCase(""))
                {
                    tvMinPlan.setError("Fields can't be blank!");
                    tvMinPlan.requestFocus();
                }

                else if(tvMaxLoan.getText().toString().equalsIgnoreCase(""))
                {
                    tvMaxLoan.setError("Fields can't be blank!");
                    tvMaxLoan.requestFocus();
                }
                else if(tvLoanSecurity.getText().toString().equalsIgnoreCase(""))
                {
                    tvLoanSecurity.setError("Fields can't be blank!");
                    tvLoanSecurity.requestFocus();
                }

                else if(tvTotalIntrest.getText().toString().equalsIgnoreCase(""))
                {
                    tvTotalIntrest.setError("Fields can't be blank!");
                    tvTotalIntrest.requestFocus();
                }
                else if(tvInterestCalMethod.getText().toString().equalsIgnoreCase(""))
                {
                    tvInterestCalMethod.setError("Fields can't be blank!");
                    tvInterestCalMethod.requestFocus();
                }
                else if(tvAnnual.getText().toString().equalsIgnoreCase(""))
                {
                    tvAnnual.setError("Fields can't be blank!");
                    tvAnnual.requestFocus();
                }
                else if(tvPaymentAmount.getText().toString().equalsIgnoreCase(""))
                {
                    tvPaymentAmount.setError("Fields can't be blank!");
                    tvPaymentAmount.requestFocus();
                }
                else if(tvLoanProcessing.getText().toString().equalsIgnoreCase(""))
                {
                    tvLoanProcessing.setError("Fields can't be blank!");
                    tvLoanProcessing.requestFocus();
                }
                else if(edtAsstValue.getText().toString().equalsIgnoreCase(""))
                {
                    edtAsstValue.setError("Fields can't be blank!");
                    edtAsstValue.requestFocus();
                }
                else if(edtRquiredLoan.getText().toString().equalsIgnoreCase(""))
                {
                    edtRquiredLoan.setError("Fields can't be blank!");
                    edtRquiredLoan.requestFocus();
                }
                else if(edtLoanDuration.getText().toString().equalsIgnoreCase(""))
                {
                    edtLoanDuration.setError("Fields can't be blank!");
                    edtLoanDuration.requestFocus();
                }
                else if(edtAssetDescription.getText().toString().equalsIgnoreCase(""))
                {
                    edtAssetDescription.setError("Fields can't be blank!");
                    edtAssetDescription.requestFocus();
                }

                else if(LoanPlan_listId.isEmpty())
                {
                    Toast.makeText(this, "Please Select Loan Plan", Toast.LENGTH_SHORT).show();
                }
                else if(LoanPlanpurpose_listId.isEmpty())
                {
                    Toast.makeText(this, "Please Select Loan Plan Purpose", Toast.LENGTH_SHORT).show();
                }
                else if(InstallmentMode_listId.isEmpty())
                {
                    Toast.makeText(this, "Please Select InstallmentMode", Toast.LENGTH_SHORT).show();
                }
                else {
                    MemberLoanpurchase(tvdddLoanType.getText().toString(),tvEligible.getText().toString(),tvMinPlan.getText().toString(),tvMaxLoan.getText().toString(),tvLoanSecurity.getText().toString(),
                            tvTotalIntrest.getText().toString(),tvInterestCalMethod.getText().toString(),tvAnnual.getText().toString(),tvPaymentAmount.getText().toString(),
                            tvLoanProcessing.getText().toString(),edtAsstValue.getText().toString(),edtRquiredLoan.getText().toString(),edtLoanDuration.getText().toString(),edtAssetDescription.getText().toString()
                    );
                }
                break;

        }
    }
    //getLoanType
    public void LoanTypeApi() {
        AppUtils.showRequestDialog(mActivity);
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/LoanType";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();

        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonnn(response);

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

    private void parseJsonnn(JSONObject response) {

        Log.v("responseGetCat", response.toString());
        AppUtils.hideDialog();


        try {


            String status = response.getString("Status");
            loanList.clear();
            loan_list.clear();
            loan_listId.clear();

            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("Loanlist");


                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("LoanTypeName", block_object.get("LoanTypeName").toString());
                    map.put("SrNo", block_object.get("SrNo").toString());
                    loanList.add(map);
                    loan_list.add(loanList.get(i).get("LoanTypeName"));
                    loan_listId.add(loanList.get(i).get("SrNo"));
                    Log.d("CategorylistID11", loanList.get(i).get("SrNo"));


                }
                branchadapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinner, (ArrayList<String>) loan_list);
                tvSelectType.setAdapter(branchadapter);


            } else {


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }


    //getLoanType
    public void LoanPlann(String loan) {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/LoanPlan";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();

        JSONObject json = new JSONObject();
        try {
            jsonObject.put("SrNo", loan);

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
                }
            }
        });
    }

    private void parseJson(JSONObject response) {

        Log.v("responseGetCat", response.toString());


        try {


            String status = response.getString("Status");
            LoanPlanList.clear();
            LoanPlan_list.clear();
            LoanPlan_listId.clear();

            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("Loanlist");


                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("PlanCode", block_object.get("PlanCode").toString());
                    map.put("PlanName", block_object.get("PlanName").toString());
                    LoanPlanList.add(map);
                    LoanPlan_list.add(LoanPlanList.get(i).get("PlanName"));
                    LoanPlan_listId.add(LoanPlanList.get(i).get("PlanCode"));
                    Log.d("CategorylistID11", LoanPlanList.get(i).get("PlanCode"));


                }
                LoanPlanadapter = new LoanPlanadapter(getApplicationContext(), R.layout.spinner, (ArrayList<String>) LoanPlan_list);
                tvLoanPlan.setAdapter(LoanPlanadapter);


            } else {


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }

    public static class SpinnerAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super(context, textViewResourceId, arraySpinner_time);

            this.data = arraySpinner_time;

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
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(data.get(position));
            return row;
        }
    }

    public static class LoanPlanadapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public LoanPlanadapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super(context, textViewResourceId, arraySpinner_time);

            this.data = arraySpinner_time;

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
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(data.get(position));
            return row;
        }
    }


    public void LoanPlanDetail(String PlanCode) {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/LoanPlanDetail";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();

        JSONObject json = new JSONObject();
        try {
            jsonObject.put("PlanCode",PlanCode);

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


        try {


            String status = response.getString("Status");

            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("planobj");

                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject jsonObject1 = data_array.getJSONObject(i);
                    String AnnualIntRate=jsonObject1.getString("AnnualIntRate");
                    String LoanAmountOnAsset=jsonObject1.getString("LoanAmountOnAsset");
                     LoanProccesingFeePer=jsonObject1.getString("LoanProccesingFeePer");
                    String LoanProccessingFeeAmount=jsonObject1.getString("LoanProccessingFeeAmount");
                    String LoanSecurityPer=jsonObject1.getString("LoanSecurityPer");
                    String MaxLoanAmount=jsonObject1.getString("MaxLoanAmount");
                    String MinAge=jsonObject1.getString("MinAge");
                    String MinLoanAmount=jsonObject1.getString("MinLoanAmount");
                    tvAnnual.setText(AnnualIntRate);
                    tvLoanProcessing.setText(LoanProccessingFeeAmount);
                    tvMaxLoan.setText(MaxLoanAmount);
                    tvMinPlan.setText(MinLoanAmount);
                    tvLoanSecurity.setText(LoanSecurityPer);




                }



            } else {


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }



    //getLoanType
    public void LoanPlanpurpose(String loan) {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/LoanPlanpurpose";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();

        JSONObject json = new JSONObject();
        try {
            jsonObject.put("SrNo", loan);

            Log.v("finddObjecttttt", String.valueOf(jsonObject));
        } catch (JSONException e) {

            e.printStackTrace();
        }

        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonLoanPlanpurpose(response);

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
    private void parseJsonLoanPlanpurpose(JSONObject response) {

        Log.v("responseGetCat", response.toString());


        try {


            String status = response.getString("Status");
            LoanPlanpurposeList.clear();
            LoanPlanpurpose_list.clear();
            LoanPlanpurpose_listId.clear();

            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("Loanlist");


                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("LoanPurposeName", block_object.get("LoanPurposeName").toString());
                    map.put("SrNo", block_object.get("SrNo").toString());
                    LoanPlanpurposeList.add(map);
                    LoanPlanpurpose_list.add(  LoanPlanpurposeList.get(i).get("LoanPurposeName"));
                    LoanPlanpurpose_listId.add(  LoanPlanpurposeList.get(i).get("SrNo"));
                    Log.d("CategorylistID11",   LoanPlanpurposeList.get(i).get("LoanPurposeName"));


                }



            } else {


            }
            LoanPlanpurposeadapter = new LoanPlanpurposeadapter(getApplicationContext(), R.layout.spinner, (ArrayList<String>)   LoanPlanpurpose_list);
            tvLoanPurchage.setAdapter(LoanPlanpurposeadapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }
    public static class LoanPlanpurposeadapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public LoanPlanpurposeadapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super(context, textViewResourceId, arraySpinner_time);

            this.data = arraySpinner_time;

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
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(data.get(position));
            return row;
        }
    }


    //getLoanType
    public void Installmentddl() {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/Installmentddl";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();

        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonInstallmentddl(response);

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
    private void parseJsonInstallmentddl(JSONObject response) {

        Log.v("responseGetCat333", response.toString());


        try {


            String status = response.getString("Status");
            InstallmentModeList.clear();
            InstallmentMode_list.clear();
            InstallmentMode_listId.clear();

            if (status.equals("true")) {
                JSONArray data_array = response.getJSONArray("Loanlist");


                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Name", block_object.get("Name").toString());

                    InstallmentModeList.add(map);
                    InstallmentMode_list.add(  InstallmentModeList.get(i).get("Name"));
                    InstallmentMode_listId.add(  InstallmentModeList.get(i).get("Name"));
                    Log.d("CategorylistID11",   InstallmentModeList.get(i).get("Name"));


                }



            } else {


            }
            installmentModeadapter = new InstallmentModeadapter(getApplicationContext(), R.layout.spinner, (ArrayList<String>)   InstallmentMode_list);
            tvInstallmentMode.setAdapter(installmentModeadapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AppUtils.hideDialog();
    }
    public static class InstallmentModeadapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public InstallmentModeadapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super(context, textViewResourceId, arraySpinner_time);

            this.data = arraySpinner_time;

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
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(data.get(position));
            return row;
        }
    }
    public void alertgendermode() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.selectinterestcal,
                null, false);


        final RadioGroup genderRadioGroup = (RadioGroup) formElementsView
                .findViewById(R.id.genderRadioGroup);

        new AlertDialog.Builder(LoanDeatilsActivity.this).setView(formElementsView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int  selectedId = genderRadioGroup
                                .getCheckedRadioButtonId();
                        RadioButton selectedRadioButton = (RadioButton) formElementsView
                                .findViewById(selectedId);
                        tvInterestCalMethod.setText(selectedRadioButton.getText());
                        CalculationAddMember(tvAnnual.getText().toString(),tvInterestCalMethod.getText().toString(),tvLoanProcessing.getText().toString()
                                ,LoanProccesingFeePer,tvLoanSecurity.getText().toString(),edtLoanDuration.getText().toString(),edtRquiredLoan.getText().toString()
                                ,tvdddLoanType.getText().toString());

                        dialog.dismiss();


                    }

                }).show();
    }
    public void LoanProcessingFee () {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.selectinterestcall,
                null, false);


        final RadioGroup genderRadioGroup = (RadioGroup) formElementsView
                .findViewById(R.id.genderRadioGroupp);

        new AlertDialog.Builder(LoanDeatilsActivity.this).setView(formElementsView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int  selectedId = genderRadioGroup
                                .getCheckedRadioButtonId();
                        RadioButton selectedRadioButtonm = (RadioButton) formElementsView
                                .findViewById(selectedId);
                        tvdddLoanType.setText(selectedRadioButtonm.getText());
                        dialog.dismiss();


                    }

                }).show();
    }
    public void  CalculationAddMember(String anual, String InterestCalMethod,
                                      String LoanProcessing, String loanProccesingFeePer,
                                      String LoanSecurity, String LoanDuration, String RquiredLoan,
                                      String dddLoanType){
        String otp1 = new GlobalAppApis().CalculationAddMember(anual,LoanPlanpurpose,InterestCalMethod,
                LoanProcessing,loanProccesingFeePer,LoanSecurity,LoanDuration,RquiredLoan,dddLoanType);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CalculationAddMember(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                   tvTotalIntrest.setText(jsonObject.getString("TotalIntrest"));
                   tvLoanProcessing.setText(jsonObject.getString("LoanProcessingAmt"));
                   tvLoanSecurity.setText(jsonObject.getString("SecurityAmt"));
                   tvPaymentAmount.setText(jsonObject.getString("NetPayble"));
                  TotalInstallments= jsonObject.getString("TotalInstallments");
                    }
                    else {
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, LoanDeatilsActivity.this, call1, "", true);
    }



    public void  MemberLoanpurchase(String ddLoanType, String Eligible, String MinPlan, String MaxLoan, String LoanSecurity,
                                    String TotalIntrest, String InterestCalMethod, String Annual, String PaymentAmount,
                                    String LoanProcessing, String AsstValue, String RquiredLoan, String LoanDuration, String AssetDescription){

        String otp1 = new GlobalAppApis().MemberLoanpurchase( ddLoanType,  Eligible,  MinPlan,  MaxLoan,  LoanSecurity,
                 TotalIntrest,  InterestCalMethod,  Annual,  PaymentAmount,
                 LoanProcessing,  AsstValue,  RquiredLoan,  LoanDuration,  AssetDescription,loan,InstallmentMode,LoanPlan,MemberId,LoanPlanpurpose,ProfarmaNo,BranchCode,TotalInstallments,persecur );
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.MemberLoanpurchase(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeeett", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true"))
                    {
                        Toast.makeText(LoanDeatilsActivity.this, "Loan Details Add Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mActivity, GuarantorDetailsActivity.class));
                    }
                    else {
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, LoanDeatilsActivity.this, call1, "", true);
    }


}