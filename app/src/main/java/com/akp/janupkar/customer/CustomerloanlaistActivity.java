package com.akp.janupkar.customer;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CustomerloanlaistActivity extends BaseActivity {
    RecyclerView rcvCustomerLoanList;
    private ArrayList<HashMap<String, String>> arrCustomerLoanList = new ArrayList<>();
    RelativeLayout rlHeader;
    String MemberId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerloanlaist);
        rcvCustomerLoanList=findViewById(R.id.rcvCustomerLoanList);
        rlHeader=findViewById(R.id.rlHeader);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        MemberId = sharedPreferences.getString("MemberId", "");
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CustomerLoanList();
    }

    private void CustomerLoanList() {
        String otp1 = new GlobalAppApis().CustomerLoanList(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CustomerLoanList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    arrCustomerLoanList.clear();
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objloan");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();

                            hashlist.put("BranchCode", arrayJSONObject.getString("BranchCode"));
                            hashlist.put("BranchName", arrayJSONObject.getString("BranchName"));
                            hashlist.put("FatherHusbandName", arrayJSONObject.getString("FatherHusbandName"));
                            hashlist.put("GroupName", arrayJSONObject.getString("GroupName"));
                            hashlist.put("InstallmentMode", arrayJSONObject.getString("InstallmentMode"));
                            hashlist.put("LoanTenure", arrayJSONObject.getString("LoanTenure"));
                            hashlist.put("LoanType", arrayJSONObject.getString("LoanType"));
                            hashlist.put("LoanTypeName", arrayJSONObject.getString("LoanTypeName"));
                            hashlist.put("InterestCalMethod", arrayJSONObject.getString("InterestCalMethod"));
                            hashlist.put("LoanPurposeName", arrayJSONObject.getString("LoanPurposeName"));
                            hashlist.put("MemberId", arrayJSONObject.getString("MemberId"));
                            hashlist.put("MemberName", arrayJSONObject.getString("MemberName"));
                            hashlist.put("PlanName", arrayJSONObject.getString("PlanName"));
                            hashlist.put("ProfarmaNo", arrayJSONObject.getString("ProfarmaNo"));
                            hashlist.put("RegDate", arrayJSONObject.getString("RegDate"));
                            hashlist.put("TotalInstallments", arrayJSONObject.getString("TotalInstallments"));
                            hashlist.put("RequiredLoanAmount", arrayJSONObject.getString("RequiredLoanAmount"));
                            hashlist.put("CollectorId", arrayJSONObject.getString("CollectorId"));
                            hashlist.put("MobileNo", arrayJSONObject.getString("MobileNo"));

                            arrCustomerLoanList.add(hashlist);

                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        MemberLoanDetailsAdapter adapter = new MemberLoanDetailsAdapter(mActivity, arrCustomerLoanList);
                        rcvCustomerLoanList.setLayoutManager(layoutManager);
                        rcvCustomerLoanList.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No Member Loan Details", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }
    private class MemberLoanDetailsAdapter extends RecyclerView.Adapter<Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public MemberLoanDetailsAdapter(Activity mActivity, ArrayList<HashMap<String, String>> arrRepaymentList) {
            data=arrRepaymentList;
        }


        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_loan_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {

            holder.tvBranchCode.setText(data.get(position).get("BranchCode"));
            holder.tvBranch.setText(data.get(position).get("BranchName"));
            holder.tvCollectorId.setText(data.get(position).get("CollectorId"));
            holder.tvMemberName.setText(data.get(position).get("MemberName"));
            holder.tvRegDate.setText(data.get(position).get("RegDate"));
            holder.tvLoanPlan.setText(data.get(position).get("PlanName"));
            holder.tvLoanType.setText(data.get(position).get("LoanTypeName"));
            holder.tvLoanAmount.setText("Rs."+data.get(position).get("RequiredLoanAmount"));
            holder.tvLoanTenure.setText(data.get(position).get("LoanTenure"));
            holder.tvInstallmentMode.setText(data.get(position).get("InstallmentMode"));

            holder.tvPerformNo.setText(data.get(position).get("ProfarmaNo"));
            holder.tvGroupName.setText(data.get(position).get("GroupName"));
            holder.tvLoanPurposeName.setText(data.get(position).get("LoanPurposeName"));
            holder.tvLoanTypeName.setText(data.get(position).get("LoanTypeName"));
            holder.tvMemberId.setText(data.get(position).get("MemberId"));
            holder.tvMobileNo.setText(data.get(position).get("MobileNo"));
            holder.tvTotalInstallments.setText(data.get(position).get("TotalInstallments"));
            holder.tvFatherHusbandName.setText(data.get(position).get("FatherHusbandName"));
            holder.tvInterestCalMethod.setText(data.get(position).get("InterestCalMethod"));



        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvInterestCalMethod,tvMemberId,tvBranchCode,tvCollectorId,tvFatherHusbandName,tvGroupName,tvMobileNo,tvTotalInstallments,tvLoanPurposeName,tvLoanTypeName,tvPerformNo,tvBranch,tvMemberName,tvRegDate,tvLoanPlan,tvLoanType,tvLoanAmount,tvLoanTenure,tvInstallmentMode,tvCalculationMethod;
        LinearLayout personalinfo;
        public Holder(View itemView) {
            super(itemView);

            tvBranch=itemView.findViewById(R.id.tvBranchName);
            tvMemberName=itemView.findViewById(R.id.tvMemberName);
            tvRegDate=itemView.findViewById(R.id.tvRegDate);
            tvLoanPlan=itemView.findViewById(R.id.tvPlanName);
            tvLoanType=itemView.findViewById(R.id.tvLoanType);
            tvLoanAmount=itemView.findViewById(R.id.tvLoanAmount);
            tvLoanTenure=itemView.findViewById(R.id.tvLoanTenure);
            tvInstallmentMode=itemView.findViewById(R.id.tvInstallmentMode);
            tvPerformNo=itemView.findViewById(R.id.tvProfarmaNo);
            tvBranchCode=itemView.findViewById(R.id.tvBranchCode);
            tvCollectorId=itemView.findViewById(R.id.tvCollectorId);
            tvFatherHusbandName=itemView.findViewById(R.id.tvFatherHusbandName);
            tvGroupName=itemView.findViewById(R.id.tvGroupName);
            tvLoanPurposeName=itemView.findViewById(R.id.tvLoanPurposeName);
            tvMemberId=itemView.findViewById(R.id.tvMemberId);
            tvMobileNo=itemView.findViewById(R.id.tvMobileNo);
            tvLoanTypeName=itemView.findViewById(R.id.tvLoanTypeName);
            tvTotalInstallments=itemView.findViewById(R.id.tvTotalInstallments);
            tvInterestCalMethod=itemView.findViewById(R.id.tvInterestCalMethod);





        }
    }
}