package com.akp.janupkar.membershipregistration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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

public class CollectSecurityAmtList extends BaseActivity {
    RecyclerView rcvMemberLoanDetails;
    private ArrayList<HashMap<String, String>> arrMemberLoanDetails = new ArrayList<>();
    RelativeLayout rlHeader;
    String userid,getmemberId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    EditText edtName,edtTotalPaid,edt_Security;
    TextView tvPaymentDate;
    DatePickerDialog picker;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_security_amt_list);
        rcvMemberLoanDetails=findViewById(R.id.rcvMemberLoanDetails);
        rlHeader=findViewById(R.id.rlHeader);
        tvPaymentDate=findViewById(R.id.tvPaymentDate);
        edtName=findViewById(R.id.edtName);
        edtTotalPaid=findViewById(R.id.edtTotalPaid);
        edt_Security=findViewById(R.id.edt_Security);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_Security.getText().toString().equalsIgnoreCase("")){
                    edt_Security.setError("Fields can't be blank!");
                    edt_Security.requestFocus();
                }
                else if (tvPaymentDate.getText().toString().equalsIgnoreCase("")){
                    tvPaymentDate.setError("Fields can't be blank!");
                    tvPaymentDate.requestFocus();
                }
                else {
                    AddDocumentMember();
                }


            }
        });

        tvPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldrr = Calendar.getInstance();
                int dayy = cldrr.get(Calendar.DAY_OF_MONTH);
                int monthh = cldrr.get(Calendar.MONTH);
                int yearr = cldrr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CollectSecurityAmtList.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvPaymentDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                // getAge(dayOfMonth,monthOfYear,year);
                            }
                        }, yearr, monthh, dayy);
                picker.show();

            }
        });

        sharedPreferences =getSharedPreferences("login_preference",MODE_PRIVATE);
        userid = sharedPreferences.getString("UserName", "");
        getmemberId=getIntent().getStringExtra("akp_memberid");


        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollectSecurityamtlist();
    }
    public void CollectSecurityamtlist() {
        String otp1 = new GlobalAppApis().CollectSecurityamtlist(getmemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CollectSecurityamtlist(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    arrMemberLoanDetails.clear();
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    String MemberId = jsonObject.getString("MemberId");
                    String MemberName = jsonObject.getString("MemberName");
                    String TotalAmount = jsonObject.getString("TotalAmount");
                    edtName.setText(MemberName);
                    edtTotalPaid.setText(TotalAmount);

                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objlist");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("MemberName", arrayJSONObject.getString("MemberName"));
                            hashlist.put("MobileNo", arrayJSONObject.getString("MobileNo"));
                            hashlist.put("PaidAmount", arrayJSONObject.getString("PaidAmount"));
                            hashlist.put("PaymentDate", arrayJSONObject.getString("PaymentDate"));

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
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_member_collectlist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {


            holder.tvMemberName.setText(data.get(position).get("MemberName"));
            holder.tvMobileNo.setText(data.get(position).get("MobileNo"));
            holder.tvPaidAmount.setText(data.get(position).get("PaidAmount"));
            holder.tvPaymentDate.setText(data.get(position).get("PaymentDate"));




        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvPaidAmount,tvMemberName,tvMobileNo,tvPaymentDate;
        LinearLayout llPaid;
        public Holder(View itemView) {
            super(itemView);

            tvPaymentDate=itemView.findViewById(R.id.tvPaymentDate);
            tvPaidAmount=itemView.findViewById(R.id.tvPaidAmount);
            tvMemberName=itemView.findViewById(R.id.tvMemberName);
            tvMobileNo=itemView.findViewById(R.id.tvMobileNo);



        }
    }

    public  void  AddDocumentMember()
    {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/CollectionAddSerityAdd";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EntryBy",userid);
            jsonObject.put("MemberId",getmemberId);
            jsonObject.put("PaidAmount",edt_Security.getText().toString());
            jsonObject.put("PaymentDate",tvPaymentDate.getText().toString());
            Log.v("checkvAli", String.valueOf(jsonObject));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post( url ).addJSONObjectBody( jsonObject )
                .setPriority( Priority.HIGH ).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJsonn( response );
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.hideDialog();
                        } else {

                        }
                    }
                } );
    }

    private void parseJsonn(JSONObject response) {
        Log.v( "responseGetCat", response.toString() );
        try {
            String status = response.getString( "Status" );
            if (status.equals( "true" )) {
                Toast.makeText(this, "Collect Security Amount Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MemberShipDetailsActivity.class));
            } else {
                Toast.makeText(this, "Something  Wrong Try Again", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();
    }
}