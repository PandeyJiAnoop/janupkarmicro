package com.akp.janupkar.basic;

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

public class MemberLoanDetailsActivity extends BaseActivity {
    RecyclerView rcvMemberLoanDetails;
    private ArrayList<HashMap<String, String>> arrMemberLoanDetails = new ArrayList<>();
    RelativeLayout rlHeader;
    String UserName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_loan_details);
        rcvMemberLoanDetails=findViewById(R.id.rcvMemberLoanDetails);
        rlHeader=findViewById(R.id.rlHeader);
        sharedPreferences =getSharedPreferences("login_preference",MODE_PRIVATE);
        UserName = sharedPreferences.getString("UserName", "");
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollectorInMemberListApi();
    }
    public void CollectorInMemberListApi() {
        String otp1 = new GlobalAppApis().CollectorInMemberList(UserName);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CollectorInMemberList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    arrMemberLoanDetails.clear();
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("LMem");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("BranchCode", arrayJSONObject.getString("BranchCode"));
                            hashlist.put("BranchName", arrayJSONObject.getString("BranchName"));
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
                            arrMemberLoanDetails.add(hashlist);

                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        MemberLoanDetailsAdapter adapter = new MemberLoanDetailsAdapter(mActivity, arrMemberLoanDetails);
                        rcvMemberLoanDetails.setLayoutManager(layoutManager);
                        rcvMemberLoanDetails.setAdapter(adapter);
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

        public MemberLoanDetailsAdapter(Activity mActivity, ArrayList<HashMap<String, String>> arrMemberLoanDetails) {
            data=arrMemberLoanDetails;
        }


        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_loan_details, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {

            holder.tvBranch.setText(data.get(position).get("BranchName"));
            holder.tvMemberName.setText(data.get(position).get("MemberName"));
            holder.tvRegDate.setText(data.get(position).get("RegDate"));
            holder.tvLoanPlan.setText(data.get(position).get("PlanName"));
            holder.tvLoanType.setText(data.get(position).get("LoanType"));
            holder.tvLoanAmount.setText("Rs."+data.get(position).get("RequiredLoanAmount"));
            holder.tvLoanTenure.setText(data.get(position).get("LoanTenure"));
            holder.tvInstallmentMode.setText(data.get(position).get("InstallmentMode"));
            holder.tvCalculationMethod.setText(data.get(position).get("InterestCalMethod"));

            holder.tvPerformNo.setText(data.get(position).get("ProfarmaNo"));



        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvPerformNo,tvBranch,tvMemberName,tvRegDate,tvLoanPlan,tvLoanType,tvLoanAmount,tvLoanTenure,tvInstallmentMode,tvCalculationMethod,tvInstallments;
        public Holder(View itemView) {
            super(itemView);

            tvBranch=itemView.findViewById(R.id.tvBranch);
            tvMemberName=itemView.findViewById(R.id.tvMemberName);
            tvRegDate=itemView.findViewById(R.id.tvRegDate);
            tvLoanPlan=itemView.findViewById(R.id.tvLoanPlan);
            tvLoanType=itemView.findViewById(R.id.tvLoanType);
            tvLoanAmount=itemView.findViewById(R.id.tvLoanAmount);
            tvLoanTenure=itemView.findViewById(R.id.tvLoanTenure);
            tvInstallmentMode=itemView.findViewById(R.id.tvInstallmentMode);
            tvCalculationMethod=itemView.findViewById(R.id.tvCalculationMethod);

            tvPerformNo=itemView.findViewById(R.id.tvPerformNo);




        }
    }
}