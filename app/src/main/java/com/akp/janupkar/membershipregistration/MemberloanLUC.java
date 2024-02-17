package com.akp.janupkar.membershipregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.a_webview.MemberLUCWebview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class MemberloanLUC extends AppCompatActivity {

    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberloan_l_u_c);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("UserName", "");
        RelativeLayout header= findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
        MemberLUCList();

    }


    public void MemberLUCList() {
        String otp1 = new GlobalAppApis().MemberLUCList(UserId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Loan_AppraisalListDetailsAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("MemberLUCList",result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("objdash");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("BranchCode", jsonObject.getString("BranchCode"));
                            hashlist.put("BranchName", jsonObject.getString("BranchName"));
                            hashlist.put("FatherHusbandName", jsonObject.getString("FatherHusbandName"));
                            hashlist.put("InstallmentMode", jsonObject.getString("InstallmentMode"));
                            hashlist.put("InterestCalMethod", jsonObject.getString("InterestCalMethod"));
                            hashlist.put("LoanAmount", jsonObject.getString("LoanAmount"));
                            hashlist.put("LoanPurposeName", jsonObject.getString("LoanPurposeName"));
                            hashlist.put("LoanTenure", jsonObject.getString("LoanTenure"));
                            hashlist.put("LoanTypeName", jsonObject.getString("LoanTypeName"));
                            hashlist.put("MemberId", jsonObject.getString("MemberId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("PlanName", jsonObject.getString("PlanName"));
                            hashlist.put("ProfarmaNo", jsonObject.getString("ProfarmaNo"));
                            hashlist.put("RegDate", jsonObject.getString("RegDate"));
                            hashlist.put("Status", jsonObject.getString("Status"));
                            hashlist.put("TotalInstallments", jsonObject.getString("TotalInstallments"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(MemberloanLUC.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }            }
        }, MemberloanLUC.this, call1, "", true);
    }





    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_memberluc, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, final int position) {
            holder.tv.setText(data.get(position).get("ProfarmaNo"));
            holder.tv1.setText(data.get(position).get("BranchName"));
            holder.tv2.setText(data.get(position).get("RegDate"));
            holder.tv3.setText(data.get(position).get("MemberName"));
            holder.tv4.setText(data.get(position).get("PlanName"));
            holder.tv5.setText(data.get(position).get("LoanAmount"));
            holder.tv6.setText(data.get(position).get("LoanTypeName"));
            holder.tv7.setText(data.get(position).get("LoanTenure"));
            holder.tv8.setText(data.get(position).get("InstallmentMode"));
            holder.tv9.setText(data.get(position).get("InterestCalMethod"));
            holder.tv10.setText(data.get(position).get("TotalInstallments"));
//            holder.sr_no.setText(position+1);
            holder.sr_no.setText(String.valueOf(" "+(position+1)));
            holder.approved_luc_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), MemberLUCWebview.class);
                    intent.putExtra("proforma_no",data.get(position).get("ProfarmaNo"));

                    startActivity(intent);
                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView sr_no,tv,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,approved_luc_tv;

        public FriendsList(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);

            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);


            tv5=itemView.findViewById(R.id.tv5);
            tv6=itemView.findViewById(R.id.tv6);

            tv7=itemView.findViewById(R.id.tv7);
            tv8=itemView.findViewById(R.id.tv8);


            tv9=itemView.findViewById(R.id.tv9);
            tv10=itemView.findViewById(R.id.tv10);

            sr_no=itemView.findViewById(R.id.sr_no);
            approved_luc_tv=itemView.findViewById(R.id.approved_luc_tv);

        }
    }

}