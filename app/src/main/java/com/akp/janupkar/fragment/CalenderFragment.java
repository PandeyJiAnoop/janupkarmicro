package com.akp.janupkar.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.view.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;
import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CalenderFragment extends BaseFragment {
    private SharedPreferences sharedPreferences;
    TextView tvName;
    String UserName,Mobile,ProformaNo;
    RecyclerView rcvDashBoard;
    private ArrayList<HashMap<String, String>> arrDashBoardList = new ArrayList<>();
    Spinner sp;
    SpinnerAdapter spinner;
    //ArrayList
    ArrayList<String> AddaddressList;
    ArrayList<String> addresslistId;
    ArrayList<HashMap<String, String>> addresslist;
    String address="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_calender, container, false);
        rcvDashBoard=view.findViewById(R.id.rcvDashBoard);
        sp=view.findViewById(R.id.sp);
        sharedPreferences = getActivity().getSharedPreferences("login_preference", MODE_PRIVATE);
        Mobile = sharedPreferences.getString("UserId", "");
        UserName = sharedPreferences.getString("UserName", "");
        AddaddressList = new ArrayList<>();
        addresslistId = new ArrayList<>();
        addresslist = new ArrayList<>();
        Dashboarddetail("");
        proformalist();
        sp.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp.getSelectedItem().toString().trim() == "Select Loan Id") {
                    Dashboarddetail("");
                } else {
                    ProformaNo = AddaddressList.get( sp.getSelectedItemPosition() );

                    Log.v( "easd", ProformaNo );

                    Dashboarddetail(ProformaNo);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );


        return view;
    }


    public void Dashboarddetail(String proformaNo) {
        String otp1 = new GlobalAppApis().Dashboarddetail(UserName,proformaNo);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Dashboarddetail(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    /*arrMemberLoanDetails.clear();*/
                    arrDashBoardList.clear();
                    JSONObject jsonObject = new JSONObject(result);


                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objdash");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("DisbursementDate", arrayJSONObject.getString("DisbursementDate"));
                            hashlist.put("DueAmount", arrayJSONObject.getString("DueAmount"));
                            hashlist.put("DueInstallment", arrayJSONObject.getString("DueInstallment"));
                            hashlist.put("InstallmentAmount", arrayJSONObject.getString("InstallmentAmount"));
                            hashlist.put("MemberId", arrayJSONObject.getString("MemberId"));
                            hashlist.put("ProfarmaNo", arrayJSONObject.getString("ProfarmaNo"));
                            hashlist.put("TotalPaid", arrayJSONObject.getString("TotalPaid"));
                            hashlist.put("MemberName", arrayJSONObject.getString("MemberName"));
                            arrDashBoardList.add(hashlist);


                        }

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        DashBoardLoanDetailsAdapter adapter = new DashBoardLoanDetailsAdapter(mActivity, arrDashBoardList);
                        rcvDashBoard.setLayoutManager(layoutManager);
                        rcvDashBoard.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No Member Loan Details", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }
    public void proformalist() {
        String otp1 = new GlobalAppApis().proformalist(UserName,"");
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.proformalist(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    /*arrMemberLoanDetails.clear();*/
                    arrDashBoardList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    addresslist.clear();
                    AddaddressList.clear();
                    addresslistId.clear();
                    AddaddressList.add("Select Loan Id");
                    addresslistId.add("Select Loan Id");

                    String status = jsonObject.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objdash");
                        for (int i = 0; i < jsonArrayr.length(); i++) {
                            JSONObject arrayJSONObject = jsonArrayr.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("DisbursementDate", arrayJSONObject.getString("DisbursementDate"));
                            hashlist.put("DueAmount", arrayJSONObject.getString("DueAmount"));
                            hashlist.put("DueInstallment", arrayJSONObject.getString("DueInstallment"));
                            hashlist.put("InstallmentAmount", arrayJSONObject.getString("InstallmentAmount"));
                            hashlist.put("MemberId", arrayJSONObject.getString("MemberId"));
                            hashlist.put("ProfarmaNo", arrayJSONObject.getString("ProfarmaNo"));
                            hashlist.put("TotalPaid", arrayJSONObject.getString("TotalPaid"));
                            hashlist.put("MemberName", arrayJSONObject.getString("MemberName"));
                            arrDashBoardList.add(hashlist);
                            AddaddressList.add(arrayJSONObject.getString("ProfarmaNo"));

//                            AddaddressList.add(arrayJSONObject.getString("ProfarmaNo")+"("+arrayJSONObject.getString("MemberName")+")");

                        }
                        spinner = new spinnerAdapter( getActivity(), R.layout.spinner, (ArrayList<String>) AddaddressList );
                        //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                        sp.setAdapter( spinner );
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        DashBoardLoanDetailsAdapter adapter = new DashBoardLoanDetailsAdapter(mActivity, arrDashBoardList);
                        rcvDashBoard.setLayoutManager(layoutManager);
                        rcvDashBoard.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No Member Loan Details", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mActivity, call1, "", true);
    }
    private class DashBoardLoanDetailsAdapter extends RecyclerView.Adapter<Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public DashBoardLoanDetailsAdapter(Activity mActivity, ArrayList<HashMap<String, String>> arrMemberLoanDetails) {
            data=arrMemberLoanDetails;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_liist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            holder.tvDistributerDate.setText("Disbursement on"+data.get(position).get("DisbursementDate"));
            holder.tvInstallmentt.setText("Due Installments : "+data.get(position).get("DueInstallment"));
            holder.tvMemberId.setText("Member Id: "+data.get(position).get("MemberId"));
            holder.tvLoanID.setText(data.get(position).get("ProfarmaNo"));
            holder.tvPaidAmount.setText("Inst.Amt\n"+data.get(position).get("InstallmentAmount"));
            holder.tvInstAmount.setText("Paid.Inst\n"+data.get(position).get("TotalPaid"));
            holder.tvdueAmount.setText("Due.Amt\n"+data.get(position).get("DueAmount"));
            holder.tvMemberName.setText("Member Name: "+data.get(position).get("MemberName"));

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class Holder extends RecyclerView.ViewHolder {

        TextView tvMemberId,tvLoanID,tvMemberName,tvInstAmount,tvPaidAmount,tvdueAmount,tvInstallmentt,tvDistributerDate;
        public Holder(View itemView) {
            super(itemView);
            tvMemberId=itemView.findViewById(R.id.tvMemberId);
            tvLoanID=itemView.findViewById(R.id.tvLoanID);
            tvMemberName=itemView.findViewById(R.id.tvMemberName);
            tvInstAmount=itemView.findViewById(R.id.tvInstAmount);
            tvPaidAmount=itemView.findViewById(R.id.tvPaidAmount);
            tvdueAmount=itemView.findViewById(R.id.tvdueAmount);
            tvInstallmentt=itemView.findViewById(R.id.tvInstallmentt);
            tvDistributerDate=itemView.findViewById(R.id.tvDistributerDate);






        }
    }
    public static class spinnerAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public spinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super( context, textViewResourceId, arraySpinner_time );

            this.data = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView( position, convertView, parent );
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
            View row = inflater.inflate( R.layout.spinner, parent, false );
            TextView label = (TextView) row.findViewById( R.id.tvName );
            label.setText( data.get( position ) );
            return row;
        }
    }


}