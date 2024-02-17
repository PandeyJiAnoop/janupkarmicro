package com.akp.janupkar.membershipregistration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

public class MemberShipDetailsActivity extends BaseActivity {
    RecyclerView rcvMemberLoanDetails;
    private ArrayList<HashMap<String, String>> arrMemberLoanDetails = new ArrayList<>();
    RelativeLayout rlHeader;
    String UserName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_details);
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
        String otp1 = new GlobalAppApis().MembershipMemberdetail(UserName);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.MembershipMemberdetail(otp1);
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
                        JSONArray jsonArrayr = jsonObject.getJSONArray("memlist");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("Address", arrayJSONObject.getString("Address"));
                            hashlist.put("Age", arrayJSONObject.getString("Age"));
                            hashlist.put("BranchName", arrayJSONObject.getString("BranchName"));
                            hashlist.put("DateofBirth", arrayJSONObject.getString("DateofBirth"));
                            hashlist.put("FatherHusbandName", arrayJSONObject.getString("FatherHusbandName"));
                            hashlist.put("Gender", arrayJSONObject.getString("Gender"));
                            hashlist.put("MemberId", arrayJSONObject.getString("MemberId"));
                            hashlist.put("MemberType", arrayJSONObject.getString("MemberType"));
                             hashlist.put("MobileNo", arrayJSONObject.getString("MobileNo"));
                            hashlist.put("RegistrationDate", arrayJSONObject.getString("RegistrationDate"));
                            hashlist.put("MemberName", arrayJSONObject.getString("MemberName"));
                            hashlist.put("HusbandName", arrayJSONObject.getString("HusbandName"));
                            arrMemberLoanDetails.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        MemberLoanDetailsAdapter adapter = new MemberLoanDetailsAdapter(mActivity, arrMemberLoanDetails);
                        rcvMemberLoanDetails.setLayoutManager(layoutManager);
                        rcvMemberLoanDetails.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No Membership Details", Toast.LENGTH_SHORT).show();
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
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_member_loandeatils, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            holder.tvHusbandName.setText(data.get(position).get("HusbandName"));
            holder.tvAddress.setText(data.get(position).get("Address"));
            holder.tvMemberName.setText(data.get(position).get("MemberName"));
            holder.tvAge.setText(data.get(position).get("Age"));
            holder.tvDob.setText(data.get(position).get("DateofBirth"));
            holder.tvFatherHusbandName.setText(data.get(position).get("FatherHusbandName"));
            holder.tvGender.setText(data.get(position).get("Gender"));
            holder.tvMobileNo.setText(data.get(position).get("MobileNo"));
            holder.tvRegistrationDate.setText(data.get(position).get("RegistrationDate"));
            holder.tvBranchName.setText(data.get(position).get("BranchName"));
            holder.llPaid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),CollectSecurityAmtList.class);
                    intent.putExtra("akp_memberid",data.get(position).get("MemberId"));
                    startActivity(intent);
                    finish();
                }
            });
        }
        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvRegistrationDate,tvBranchName,tvAddress,tvFatherHusbandName,tvGender,tvAge,tvDob,
                tvMobileNo,tvMemberName,tvHusbandName;
       LinearLayout llPaid;
        public Holder(View itemView) {
            super(itemView);
            tvRegistrationDate=itemView.findViewById(R.id.tvRegistrationDate);
            tvMemberName=itemView.findViewById(R.id.tvMemberName);
            tvBranchName=itemView.findViewById(R.id.tvBranchName);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            tvFatherHusbandName=itemView.findViewById(R.id.tvFatherHusbandName);
            tvGender=itemView.findViewById(R.id.tvGender);
            tvAge=itemView.findViewById(R.id.tvAge);
            tvDob=itemView.findViewById(R.id.tvDob);
            tvMobileNo=itemView.findViewById(R.id.tvMobileNo);
            llPaid=itemView.findViewById(R.id.llPaid);
            tvHusbandName=itemView.findViewById(R.id.tvHusbandName);
        }
    }
}