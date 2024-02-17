package com.akp.janupkar.basic;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
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

public class RepaymentListActivity extends BaseActivity {
    RecyclerView rcvMemberLoanDetails;
    private ArrayList<HashMap<String, String>> arrRepaymentList = new ArrayList<>();
    RelativeLayout rlHeader;
    String UserName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    TextView title_tv;
    SearchView et_search;
    MemberLoanDetailsAdapter adapter;
    Spinner spin1;
    ArrayList<String> CityName = new ArrayList<>();
    ArrayList<String> CityId = new ArrayList<>();
    String  cityid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_list);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserName = sharedPreferences.getString("UserName", "");
        rcvMemberLoanDetails=findViewById(R.id.rcvMemberLoanDetails);
        spin1 = findViewById(R.id.rupee_sp1);


        rlHeader=findViewById(R.id.rlHeader);
        title_tv=findViewById(R.id.title_tv);
        et_search = findViewById(R.id.search);
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCity(UserName);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= CityId.size(); j++) {
                        if (spin1.getSelectedItem().toString().equalsIgnoreCase(CityName.get(j))) {
                            cityid = CityId.get(j);
                            break;
                        }
                    }
                    LoanrepaymenttApi(UserName,cityid,"");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        et_search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    arrRepaymentList.clear();
//                    adapter.notifyDataSetChanged();
                    LoanrepaymenttApi(UserName,cityid,newText);
//                    Member_EmpList("",newText,"",username);
                }
                else {
                    arrRepaymentList.clear();
//                    adapter.notifyDataSetChanged();
                    LoanrepaymenttApi(UserName,cityid,"");
//                    Member_EmpList("","","",username);
                }
                return true;
            }
        });


    }
    public void LoanrepaymenttApi(String CollectorId,String centerid,String searchby) {
        String otp1 = new GlobalAppApis().Loanrepayment(CollectorId,centerid,searchby);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Loanrepayment(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    arrRepaymentList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objloan");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            title_tv.setText("Loan Repayment("+jsonArrayr.length()+")");
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
                            hashlist.put("CenterId", arrayJSONObject.getString("CenterId"));
                            hashlist.put("CenterName", arrayJSONObject.getString("CenterName"));
                            arrRepaymentList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        adapter = new MemberLoanDetailsAdapter(mActivity, arrRepaymentList);
                        rcvMemberLoanDetails.setLayoutManager(layoutManager);
                        rcvMemberLoanDetails.setAdapter(adapter);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RepaymentListActivity.this);
                        builder.setTitle("Data Not Found!")
                                .setMessage("No Member Loan Details")
                                .setCancelable(false)
                                .setIcon(R.drawable.appicon)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
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
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_repayment_list, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            holder.tvBranch.setText(data.get(position).get("BranchName"));
            holder.tvMemberName.setText(data.get(position).get("MemberName"));
            holder.tvRegDate.setText(data.get(position).get("RegDate"));
            holder.tvLoanPlan.setText(data.get(position).get("PlanName"));
            holder.tvLoanType.setText(data.get(position).get("LoanTypeName"));
            holder.tvLoanAmount.setText("Rs."+data.get(position).get("RequiredLoanAmount"));
            holder.tvLoanTenure.setText(data.get(position).get("LoanTenure"));
            holder.tvInstallmentMode.setText(data.get(position).get("InstallmentMode"));
            holder.tvCalculationMethod.setText(data.get(position).get("InterestCalMethod"));
            holder.tvPerformNo.setText(data.get(position).get("ProfarmaNo"));
            holder.tvCenterid.setText(data.get(position).get("CenterId"));
            holder.tvcenterName.setText(data.get(position).get("CenterName"));
            holder.personalinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                    login_editor = login_preference.edit();
                    login_editor.putString("ProfarmaNo",data.get(position).get("ProfarmaNo"));
                    login_editor.commit();
                    startActivity(new Intent(mActivity,RepaymentDetailsActivity.class));
                    finish();
                }});
        } public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvPerformNo,tvBranch,tvMemberName,tvRegDate,tvLoanPlan,tvLoanType,
                tvLoanAmount,tvLoanTenure,tvInstallmentMode,tvCalculationMethod,tvCenterid,tvcenterName;
        LinearLayout personalinfo;

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
            personalinfo=itemView.findViewById(R.id.personalinfo);
            tvCenterid=itemView.findViewById(R.id.tvCenterid);
            tvcenterName=itemView.findViewById(R.id.tvcenterName);
        }
    }

    public void getCity(String CollectorId) {
        String otp1 = new GlobalAppApis().CeneterList(CollectorId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CenterAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("Status") == true) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Centerlist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            CityId.add(jsonObject1.getString("CenterCode"));
                            String Cityname = jsonObject1.getString("CenterName");
                            CityName.add(Cityname);
                        }
                    } /*else {
                        sp_city.setHint("No city available");
                        sp_city.setHintColor(getResources().getColor(R.color.black));
                    }*/
                    spin1.setAdapter(new ArrayAdapter<String>(RepaymentListActivity.this, android.R.layout.simple_spinner_dropdown_item, CityName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }


}