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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

public class PaidCustomerListActivity extends BaseActivity {
    RecyclerView rcvCustomerLoanList;
    private ArrayList<HashMap<String, String>> arrPaidEmiList = new ArrayList<>();
    RelativeLayout rlHeader;
    String MemberId,ProformaNo;
    private SharedPreferences sharedPreferences;
    Spinner sp;
    SpinnerAdapter spinner;
    //ArrayList
    ArrayList<String> AddaddressList;
    ArrayList<String> addresslistId;
    ArrayList<HashMap<String, String>> addresslist;
    String address="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_customer_list);
        rcvCustomerLoanList=findViewById(R.id.rcvCustomerPaidEmi);
        rlHeader=findViewById(R.id.rlHeader);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        MemberId = sharedPreferences.getString("MemberId", "");
        sp=findViewById(R.id.sp);
        AddaddressList = new ArrayList<>();
        addresslistId = new ArrayList<>();
        addresslist = new ArrayList<>();
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        proformalist();
        CustomerPaidEMI("");
        sp.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp.getSelectedItem().toString().trim() == "Select Profarma Number") {
                    CustomerPaidEMI("");
                } else {
                    ProformaNo = AddaddressList.get( sp.getSelectedItemPosition() );

                    Log.v( "easd", ProformaNo );

                    CustomerPaidEMI(ProformaNo);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    private void CustomerPaidEMI(String proformaNo) {
        String otp1 = new GlobalAppApis().CustomerPaidEMI(MemberId,proformaNo);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CustomerPaidEMI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    arrPaidEmiList.clear();
                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("CustEmi");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();

                            hashlist.put("AddedDate", arrayJSONObject.getString("AddedDate"));
                            hashlist.put("ArearAmount", arrayJSONObject.getString("ArearAmount"));
                            hashlist.put("ChequeStatus", arrayJSONObject.getString("ChequeStatus"));
                            hashlist.put("Chequeno", arrayJSONObject.getString("Chequeno"));
                            hashlist.put("ClosedStatus", arrayJSONObject.getString("ClosedStatus"));
                            hashlist.put("EMIAmount", arrayJSONObject.getString("EMIAmount"));
                            hashlist.put("InstallmentDate", arrayJSONObject.getString("InstallmentDate"));
                            hashlist.put("InstallmentNo", arrayJSONObject.getString("InstallmentNo"));
                            hashlist.put("InterestAmount", arrayJSONObject.getString("InterestAmount"));
                            hashlist.put("MemberId", arrayJSONObject.getString("MemberId"));
                            hashlist.put("PaymentDate", arrayJSONObject.getString("PaymentDate"));
                            hashlist.put("PenaltyAMount", arrayJSONObject.getString("PenaltyAMount"));
                            hashlist.put("PrincipleAmount", arrayJSONObject.getString("PrincipleAmount"));
                            hashlist.put("ProfarmaNo", arrayJSONObject.getString("ProfarmaNo"));
                            hashlist.put("RefId", arrayJSONObject.getString("RefId"));
                            hashlist.put("Status", arrayJSONObject.getString("Status"));
                            hashlist.put("TotalPaid", arrayJSONObject.getString("TotalPaid"));


                            arrPaidEmiList.add(hashlist);

                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        MemberLoanDetailsAdapter adapter = new MemberLoanDetailsAdapter(mActivity, arrPaidEmiList);
                        rcvCustomerLoanList.setLayoutManager(layoutManager);
                        rcvCustomerLoanList.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No Data Found", Toast.LENGTH_SHORT).show();
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
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.emi_paid_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {

            holder.tvAddedDate.setText(data.get(position).get("AddedDate"));
            holder.tvArearAmount.setText("Rs. "+data.get(position).get("ArearAmount"));
            holder.tvChequeStatus.setText(data.get(position).get("ChequeStatus"));
            holder.tvChequeno.setText(data.get(position).get("Chequeno"));
            holder.tvClosedStatus.setText(data.get(position).get("ClosedStatus"));
            holder.tvEMIAmount.setText("Rs. "+data.get(position).get("EMIAmount"));
            holder.tvInstallmentDate.setText(data.get(position).get("InstallmentDate"));
            holder.tvInstallmentNo.setText(data.get(position).get("InstallmentNo"));
            holder.tvMemberId.setText(data.get(position).get("MemberId"));
            holder.tvPaymentDate.setText("Rs. "+data.get(position).get("PaymentDate"));
            holder.tvPenaltyAMount.setText("Rs. "+data.get(position).get("PenaltyAMount"));
            holder.tvPrincipleAmount.setText("Rs. "+data.get(position).get("PrincipleAmount"));
            holder.tvTotalPaid.setText("Rs. "+data.get(position).get("TotalPaid"));
            holder.tvProfarmaNo.setText(data.get(position).get("ProfarmaNo"));
            holder.tvRefId.setText(data.get(position).get("RefId"));
            holder.tvStatus.setText(data.get(position).get("Status"));




        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tvAddedDate,tvArearAmount,tvChequeStatus,tvChequeno,tvClosedStatus,
                tvEMIAmount,tvInstallmentDate,tvInstallmentNo,tvMemberId,tvPaymentDate,tvPenaltyAMount
                ,tvPrincipleAmount,tvProfarmaNo,tvRefId,tvStatus,tvTotalPaid;
        public Holder(View itemView) {
            super(itemView);

            tvAddedDate=itemView.findViewById(R.id.tvAddedDate);
            tvArearAmount=itemView.findViewById(R.id.tvArearAmount);
            tvChequeStatus=itemView.findViewById(R.id.tvChequeStatus);
            tvChequeno=itemView.findViewById(R.id.tvChequeno);
            tvClosedStatus=itemView.findViewById(R.id.tvClosedStatus);
            tvEMIAmount=itemView.findViewById(R.id.tvEMIAmount);
            tvInstallmentDate=itemView.findViewById(R.id.tvInstallmentDate);
            tvInstallmentNo=itemView.findViewById(R.id.tvInstallmentNo);
            tvMemberId=itemView.findViewById(R.id.tvMemberId);
            tvPaymentDate=itemView.findViewById(R.id.tvPaymentDate);
            tvPenaltyAMount=itemView.findViewById(R.id.tvPenaltyAMount);
            tvPrincipleAmount=itemView.findViewById(R.id.tvPrincipleAmount);
            tvProfarmaNo=itemView.findViewById(R.id.tvProfarmaNo);
            tvRefId=itemView.findViewById(R.id.tvRefId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvTotalPaid=itemView.findViewById(R.id.tvTotalPaid);

        }
    }
    public void proformalist() {
        String otp1 = new GlobalAppApis().proformanobymemberid(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.proformanobymemberid(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    JSONObject jsonObject = new JSONObject(result);
                    addresslist.clear();
                    AddaddressList.clear();
                    addresslistId.clear();
                    AddaddressList.add("Select Profarma Number");
                    addresslistId.add("Select Profarma Number");

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objdash");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                           hashlist.put("ProfarmaNo", arrayJSONObject.getString("ProfarmaNo"));

                            AddaddressList.add(arrayJSONObject.getString("ProfarmaNo") );

                        }
                        spinner = new CustomerDashboard.spinnerAdapter( mActivity, R.layout.spinner, (ArrayList<String>) AddaddressList );
                        //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                        sp.setAdapter( spinner );

                    } else {
                        Toast.makeText(mActivity, "No Member Loan Details", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }
}