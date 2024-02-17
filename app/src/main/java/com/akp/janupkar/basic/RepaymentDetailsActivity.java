package com.akp.janupkar.basic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class RepaymentDetailsActivity extends BaseActivity implements View.OnClickListener {
     TextView tvDate,tvFatherName,tvContact,tvAddress,tvLoanPlan,tvLoanTenure,tvTotalInterest,tvAnnualInterestRate,
             tvInstallmentMode,tvTotalRepaymentAmount,tvGracePeriod,tvLoanType,tvCalculationMethod,tvSecurityAmount
             ,tvDisbursementDate,tvTotalInstallments,tvProcessingFee,tvDisbursementAmount,tvHusbandName;
     RelativeLayout rlHeader;
    String ProfarmaNo;
    private SharedPreferences sharedPreferences;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    LinearLayout llEmiChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_details);
        findViewById();
        setListner();
    }

    private void setListner() {
        rlHeader.setOnClickListener(this);
        llEmiChart.setOnClickListener(this);
    }

    private void findViewById() {
        tvDate=findViewById(R.id.tvDate);
        tvHusbandName=findViewById(R.id.tvHusbandName);
        tvFatherName=findViewById(R.id.tvFatherName);
        tvContact=findViewById(R.id.tvContact);
        tvAddress=findViewById(R.id.tvAddress);
        tvLoanPlan=findViewById(R.id.tvLoanPlan);
        tvLoanTenure=findViewById(R.id.tvLoanTenure);
        tvTotalInterest=findViewById(R.id.tvTotalInterest);
        tvAnnualInterestRate=findViewById(R.id.tvAnnualInterestRate);
        tvInstallmentMode=findViewById(R.id.tvInstallmentMode);
        tvTotalRepaymentAmount=findViewById(R.id.tvTotalRepaymentAmount);
        tvGracePeriod=findViewById(R.id.tvGracePeriod);
        tvLoanType=findViewById(R.id.tvLoanType);
        tvCalculationMethod=findViewById(R.id.tvCalculationMethod);
        tvSecurityAmount=findViewById(R.id.tvSecurityAmount);
        tvDisbursementDate=findViewById(R.id.tvDisbursementDate);
        tvTotalInstallments=findViewById(R.id.tvTotalInstallments);
        tvProcessingFee=findViewById(R.id.tvProcessingFee);
        tvDisbursementAmount=findViewById(R.id.tvDisbursementAmount);
        rlHeader=findViewById(R.id.rlHeader);
        llEmiChart=findViewById(R.id.llEmiChart);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        ProfarmaNo = sharedPreferences.getString("ProfarmaNo", "");
        LoanrepaymenttApi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlHeader:
                finish();
                break;
            case R.id.llEmiChart:
                startActivity(new Intent(mActivity,EmiChartActivity.class));
                break;
        }

    }

    public void LoanrepaymenttApi() {
        String otp1 = new GlobalAppApis().GetEmIListbyproformano(ProfarmaNo);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.GetEmIListbyproformano(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);

                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        String AccountName=jsonObject.getString("AccountName");
                        String Address=jsonObject.getString("Address");
                        String CardType=jsonObject.getString("CardType");
                        String CashBackAmount=jsonObject.getString("CashBackAmount");
                        String CashClosingBal=jsonObject.getString("CashClosingBal");
                        String CashOpeningBal=jsonObject.getString("CashOpeningBal");
                        String CgstAmt=jsonObject.getString("CgstAmt");
                        String ContactNo=jsonObject.getString("ContactNo");
                        String CoupenAmount=jsonObject.getString("CoupenAmount");
                        String GracePeriodDays=jsonObject.getString("GracePeriodDays");
                        String InstallmentCount=jsonObject.getString("InstallmentCount");
                        String ItemCode=jsonObject.getString("ItemCode");
                        String ItemName=jsonObject.getString("ItemName");
                        String OfferType=jsonObject.getString("OfferType");
                        String OpenBal=jsonObject.getString("OpenBal");
                        String PayableAmt=jsonObject.getString("PayableAmt");
                        String Rate=jsonObject.getString("Rate");
                        String StatusN=jsonObject.getString("StatusN");
                        String eDate=jsonObject.getString("eDate");
                        String employee_father_name=jsonObject.getString("employee_father_name");
                        String HusbandName=jsonObject.getString("HusbandName");
                        String installmentmode=jsonObject.getString("installmentmode");
                        String interestcalmethod=jsonObject.getString("interestcalmethod");
                        String loanproccesingfeeper=jsonObject.getString("loanproccesingfeeper");
                        String loanproccessingfeeamount=jsonObject.getString("loanproccessingfeeamount");
                        String loanpurposeid=jsonObject.getString("loanpurposeid");
                        String loansecurityper=jsonObject.getString("loansecurityper");
                        String loantenure=jsonObject.getString("loantenure");
                        String mDate=jsonObject.getString("mDate");
                        String totalinstallments=jsonObject.getString("totalinstallments");
                        String totalrepaymentamount=jsonObject.getString("totalrepaymentamount");
                        String totinterestamount=jsonObject.getString("totinterestamount");
                        String Emiobj=jsonObject.getString("Emiobj");
                        login_editor = sharedPreferences.edit();
                        login_editor.putString("Emiobj",jsonObject.getString("Emiobj"));
                        login_editor.putString("advanceAmt",jsonObject.getString("CgstAmt"));
                        login_editor.putString("extraPer",jsonObject.getString("InstallmentCount"));
                        login_editor.commit();
                        tvHusbandName.setText(employee_father_name);
                        tvFatherName.setText(HusbandName);
                        tvContact.setText(ContactNo);
                        tvAddress.setText(Address);
                        tvDate.setText("Profarma No: #"+ProfarmaNo+" | "+AccountName+", Loan Amount: "+CashBackAmount+"\n"+mDate);
                        tvLoanPlan.setText(ItemName);
                        tvLoanTenure.setText(loantenure);
                        tvTotalInterest.setText(totinterestamount);
                        tvAnnualInterestRate.setText(Rate);
                        tvInstallmentMode.setText(installmentmode);
                        tvTotalRepaymentAmount.setText(totalrepaymentamount);
                        tvGracePeriod.setText(GracePeriodDays+" Days");
                        tvLoanType.setText(OfferType+" & "+loanpurposeid);
                        tvCalculationMethod.setText(interestcalmethod);
                        tvSecurityAmount.setText(CoupenAmount);
                        tvDisbursementDate.setText(eDate);
                        tvTotalInstallments.setText(totalinstallments);
                        tvProcessingFee.setText(PayableAmt);
                        tvDisbursementAmount.setText(CashBackAmount);

                        }

                     else {
                        Toast.makeText(mActivity, "No Member Loan Details", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }
}