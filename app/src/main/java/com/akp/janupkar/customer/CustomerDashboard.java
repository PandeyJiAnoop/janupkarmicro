
package com.akp.janupkar.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.akp.janupkar.basic.SplashActivity;
import com.akp.janupkar.view.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CustomerDashboard extends BaseActivity {
    private SharedPreferences sharedPreferences;
    TextView tvName;
    String MemberId,BranchCode,ProformaNo,MobileNo;
    RecyclerView rcvDashBoard;
    private ArrayList<HashMap<String, String>> arrDashBoardList = new ArrayList<>();
    Spinner sp;
    SpinnerAdapter spinner;
    //ArrayList
    ArrayList<String> AddaddressList;
    ArrayList<String> addresslistId;
    ArrayList<HashMap<String, String>> addresslist;
    String address="";
    RelativeLayout rlLogout,rlCustomerlist,rlDashBoard,rlPaidEmiList,rlUnPaid;
ImageView ivMenu;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerleftmenu);
        rcvDashBoard=findViewById(R.id.rcvDashBoard);

        rlCustomerlist=findViewById(R.id.rlCustomerlist);
        rlLogout=findViewById(R.id.rlLogout);
        rlDashBoard=findViewById(R.id.rlDashBoard);
        rlPaidEmiList=findViewById(R.id.rlPaidEmiList);
        rlUnPaid=findViewById(R.id.rlUnPaid);
        tvName=findViewById(R.id.tvName);
        ivMenu=findViewById(R.id.ivMenu);
        sharedPreferences = mActivity.getSharedPreferences("login_preference", MODE_PRIVATE);
        BranchCode = sharedPreferences.getString("BranchCode", "");
        MemberId = sharedPreferences.getString("MemberId", "");
        MobileNo = sharedPreferences.getString("Mobile", "");
        sp=findViewById(R.id.sp);
        AddaddressList = new ArrayList<>();
        addresslistId = new ArrayList<>();
        addresslist = new ArrayList<>();
        tvName.setText("+91 "+MobileNo+"\nView Profile");
        mDrawerLayout=findViewById(R.id.mDrawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                mDrawer.openDrawer(Gravity.START);
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        Dashboarddetail("");
         proformalist();
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerProfileActivity.class));

            }
        });
        rlUnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UnPaidEmiActivity.class));

            }
        });
        rlPaidEmiList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PaidCustomerListActivity.class));

            }
        });
        rlDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerDashboard.class));

            }
        });
        rlCustomerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerloanlaistActivity.class));
            }
        });
        sp.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp.getSelectedItem().toString().trim() == "Select Profarma Number") {
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
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CustomerDashboard.this);
                alertDialogBuilder.setMessage(getString(R.string.aresurelogout));
                alertDialogBuilder.setPositiveButton(getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent I = new Intent(CustomerDashboard.this, SplashActivity.class);
                                startActivity(I);
                                arg0.dismiss();
                                onResume();
                            }
                        });

                alertDialogBuilder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }

    public void Dashboarddetail(String proformaNo ) {
        String otp1 = new GlobalAppApis().CustomerDashboard(proformaNo,BranchCode,MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CustomerDashboard(otp1);
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
                        JSONArray jsonArrayr = jsonObject.getJSONArray("objdah");
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
        String otp1 = new GlobalAppApis().proformanobymemberid(MemberId);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.proformanobymemberid(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                     arrDashBoardList.clear();

                    arrDashBoardList.clear();
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
                            hashlist.put("DisbursementDate", arrayJSONObject.getString("DisbursementDate"));
                            hashlist.put("DueAmount", arrayJSONObject.getString("DueAmount"));
                            hashlist.put("DueInstallment", arrayJSONObject.getString("DueInstallment"));
                            hashlist.put("InstallmentAmount", arrayJSONObject.getString("InstallmentAmount"));
                            hashlist.put("MemberId", arrayJSONObject.getString("MemberId"));
                            hashlist.put("ProfarmaNo", arrayJSONObject.getString("ProfarmaNo"));
                            hashlist.put("TotalPaid", arrayJSONObject.getString("TotalPaid"));
                            hashlist.put("MemberName", arrayJSONObject.getString("MemberName"));
                            arrDashBoardList.add(hashlist);
                            AddaddressList.add(arrayJSONObject.getString("ProfarmaNo") );

                        }
                        spinner = new spinnerAdapter( mActivity, R.layout.spinner, (ArrayList<String>) AddaddressList );
                        //  LocationSpinner.setAdapter(new spinnerAdapter(getApplicationContext(), R.layout.spinner_layout, (ArrayList<String>) Locationlist));
                        sp.setAdapter( spinner );
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                        DashBoardLoanDetailsAdapter adapter = new DashBoardLoanDetailsAdapter(mActivity, arrDashBoardList);
                        rcvDashBoard.setLayoutManager(layoutManager);
                        rcvDashBoard.setAdapter(adapter);
                    } else {
                        Toast.makeText(mActivity, "No Data Found", Toast.LENGTH_SHORT).show();
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
            holder.tvDistributerDate.setText("Disbursement on "+data.get(position).get("DisbursementDate"));
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