package com.akp.janupkar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class EMIDueReport extends AppCompatActivity {
    String UserId,RoleType;
    ImageView menuImg;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private LegalListAdapter pdfAdapTer;
    RecyclerView cust_recyclerView;
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String username;
    private DatePicker dpResult;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID2 = 2;
    int cur = 0;
    private TextView tvDisplayDate, tvDisplayDate2;
    SearchView et_search;
    ImageView norecord_tv,pdf_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_m_i_due_report);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("UserName", "");
//     Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_LONG).show();
        et_search = findViewById(R.id.search);
        menuImg = findViewById(R.id.menuImg);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);
        norecord_tv= findViewById(R.id.norecord_tv);
        setCurrentDateOnView();
        addListenerOnButton();
         pdf_img=findViewById(R.id.pdf_img);

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(EMIDueReport.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(EMIDueReport.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Member_EmpList("","","",username);
        et_search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    arrLegalList.clear();
                    pdfAdapTer.notifyDataSetChanged();
                    Member_EmpList("",newText,"",username);
                }
                else {
                    arrLegalList.clear();
                    pdfAdapTer.notifyDataSetChanged();
                    Member_EmpList("","","",username);
                }
                return true;
            }
        });

    }

    private void Member_EmpList(String enddate,String Profarma,String startdate,String id) {
        String otp1 = new GlobalAppApis().EMIDUEReportAPI(enddate,Profarma,startdate,id);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.EMIDUEReportAPILink(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try { Log.v("responsememberdetails", result);
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("objdash");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);

//                            String[] row = new String[3];
//                            row[0] = jsonObject.getString("EMIAmount");
//                            row[1] = jsonObject.getString("InstallmentNo");
//                            row[2] = jsonObject.getString("MemberId");
//                            rows.add(row);
//                            // Save data as CSV file
//                            saveCsvFile(rows);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("EMIAmount", jsonObject.getString("EMIAmount"));
                            hashlist.put("InstallmentNo", jsonObject.getString("InstallmentNo"));
                            hashlist.put("MemberId", jsonObject.getString("MemberId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("PlanName", jsonObject.getString("PlanName"));
                            hashlist.put("ProfarmaNo", jsonObject.getString("ProfarmaNo"));
                            hashlist.put("Status", jsonObject.getString("Status"));
                            hashlist.put("TotalAmount", jsonObject.getString("TotalAmount"));
                            arrLegalList.add(hashlist);
//                            pdf_img.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    saveCsvFile(hashlist);
//                                Toast.makeText(getApplicationContext(),"Downloading Start...",Toast.LENGTH_LONG).show();
//                                }
//                            });
                        }

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new LegalListAdapter(EMIDueReport.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, EMIDueReport.this, call1, "", true);
    }



    public class LegalListAdapter extends RecyclerView.Adapter<EMIDueReport.LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }
        public EMIDueReport.LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EMIDueReport.LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_emiduereport, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final EMIDueReport.LegalList holder, final int position) {
            holder.t1.setText(data.get(position).get("MemberName")+"("+data.get(position).get("MemberId")+")");
            holder.t2.setText(data.get(position).get("EMIAmount"));
            holder.t3.setText(data.get(position).get("InstallmentNo"));
            holder.t4.setText(data.get(position).get("ProfarmaNo"));
            holder.t5.setText(data.get(position).get("Status"));
            holder.t6.setText(data.get(position).get("TotalAmount"));

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class LegalList extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;

        public LegalList(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.tv1);
            t2 = itemView.findViewById(R.id.tv2);
            t3 = itemView.findViewById(R.id.tv3);
            t4 = itemView.findViewById(R.id.tv4);
            t5 = itemView.findViewById(R.id.tv5);
            t6 = itemView.findViewById(R.id.tv6);
        }}

    // display current date
    public void setCurrentDateOnView() {
        tvDisplayDate = (TextView) findViewById(R.id.mStartTime);
        tvDisplayDate2 = (TextView) findViewById(R.id.mEndTime);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
        tvDisplayDate2.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
    }

    public void addListenerOnButton() {
        tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        tvDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID2);}
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = DATE_DIALOG_ID;
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,day);
            case DATE_DIALOG_ID2:
                cur = DATE_DIALOG_ID2;
                System.out.println("onCreateDialog2  : " + id);
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            if(cur == DATE_DIALOG_ID){
                // set selected date into textview
                tvDisplayDate.setText("Date1 : " + new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));
            }
            else{
                tvDisplayDate2.setText("Date2 : " + new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));
            }}};

//    private void saveCsvFile(HashMap<String, String> hashlist) {
//        File csvFile = new File(getExternalFilesDir(null), "data.csv");
//        try {
//            // Create a CSVWriter object
//            CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
//
//            // Write data to CSV
//            writer.writeAll((List<String[]>) hashlist);
//
//            // Close the writer
//            writer.close();
//
//            // Show a toast message to indicate the file has been saved
//            Toast.makeText(this, "Data saved to " + csvFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            Toast.makeText(this, "Error!!! " + e, Toast.LENGTH_SHORT).show();
//
//            e.printStackTrace();
//        }
//    }
}