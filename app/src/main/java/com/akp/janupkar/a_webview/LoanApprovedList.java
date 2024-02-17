package com.akp.janupkar.a_webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.UploadAddReceiptPicandSign;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class LoanApprovedList extends AppCompatActivity {
    RecyclerView rcvList;
    RelativeLayout rlHeader;
    private SharedPreferences sharedPreferences;
    String username;
    final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_approved_list);
        findViewById();
    }

    private void findViewById() {
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("UserName", "");
        Log.d("testsd", username);
        rcvList = findViewById(R.id.rcvList);
        rlHeader = findViewById(R.id.rlHeader);
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FavPostList(username);
    }

    private void FavPostList(String useid) {
        String otp1 = new GlobalAppApis().ApprovedLoanList(useid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.LoanApproveListAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("testsdres", result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray Response = object.getJSONArray("objdash");
                    for (int i = 0; i < Response.length(); i++) {
                        JSONObject jsonObject = Response.getJSONObject(i);
                        HashMap<String, String> hashlist = new HashMap();
                        hashlist.put("LoanAmount", jsonObject.getString("LoanAmount"));
                        hashlist.put("MemberId", jsonObject.getString("MemberId"));
                        hashlist.put("MemberName", jsonObject.getString("MemberName"));
                        hashlist.put("ProfarmaNo", jsonObject.getString("ProfarmaNo"));
                        hashlist.put("SanctionBy", jsonObject.getString("SanctionBy"));
                        hashlist.put("SanctionDate", jsonObject.getString("SanctionDate"));
                        hashlist.put("SanctionedAmount", jsonObject.getString("SanctionedAmount"));
                        hashlist.put("Status", jsonObject.getString("Status"));
                        hashlist.put("KYCStatus", jsonObject.getString("KYCStatus"));
                        arrFriendsList.add(hashlist);
                    }
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    LoanApprovedList.FriendsListAdapter pdfAdapTer = new LoanApprovedList.FriendsListAdapter(getApplicationContext(), arrFriendsList);
                    rcvList.setLayoutManager(layoutManager);
                    rcvList.setAdapter(pdfAdapTer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
        }, LoanApprovedList.this, call1, "", true);
    }


    public class FriendsListAdapter extends RecyclerView.Adapter<LoanApprovedList.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public LoanApprovedList.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LoanApprovedList.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_loan_list, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final LoanApprovedList.FriendsList holder, final int position) {
            holder.tv1.setText(data.get(position).get("MemberName") + "(" + data.get(position).get("MemberId") + ")");
            holder.tv5.setText(data.get(position).get("ProfarmaNo"));
            holder.tv2.setText("Sanctioned Amount \u20B9 " + data.get(position).get("SanctionedAmount") + "\nSanctionDate- " + data.get(position).get("SanctionDate"));
            holder.tv3.setText("LoanAmount \u20B9 " + data.get(position).get("LoanAmount"));
            holder.tv4.setText("Status:- " + data.get(position).get("Status"));
            holder.tv6.setText("KYC Status:- " + data.get(position).get("KYCStatus"));
            if (data.get(position).get("KYCStatus").equalsIgnoreCase("Pending")) {
                holder.collector_kyc_ll.setVisibility(View.VISIBLE);
                holder.tv6.setTextColor(Color.RED);
                holder.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UploadAddReceiptPicandSign.class);
                        intent.putExtra("perfno",data.get(position).get("ProfarmaNo"));
                        intent.putExtra("upload_cust_id",data.get(position).get("MemberId"));
                        startActivity(intent);
                    }});
            } else {
                holder.collector_kyc_ll.setVisibility(View.GONE);
            }
//            Picasso.get().load(data.get(position).get("Photo")).into(holder.ivImage);
//
        }
        public int getItemCount() {
            return data.size();
        }
    }

    public class FriendsList extends RecyclerView.ViewHolder {
        CircularImageView ivImage;
        TextView tv1, tv2, tv3, tv4, tv5, tv6;
        LinearLayout collector_kyc_ll;
        Button submit;

        public FriendsList(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
            tv6 = itemView.findViewById(R.id.tv6);
            submit = itemView.findViewById(R.id.submit);
            collector_kyc_ll = itemView.findViewById(R.id.collector_kyc_ll);
            ivImage = itemView.findViewById(R.id.user_profile_img);
        } }

}