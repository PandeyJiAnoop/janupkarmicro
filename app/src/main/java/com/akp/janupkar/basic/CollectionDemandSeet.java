package com.akp.janupkar.basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import com.akp.janupkar.NetworkConnectionHelper;
import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class CollectionDemandSeet extends AppCompatActivity {
    String UserId, RoleType;
    ImageView menuImg;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private LegalListAdapter pdfAdapTer;
    RecyclerView cust_recyclerView;
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String username;


    TextView search_tv,title_tv;

    public static TextView tvDisplayDate, tvDisplayDate2;
    SearchView et_search;
    ImageView norecord_tv;



    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarr = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendarr.set(Calendar.YEAR, year);
            myCalendarr.set(Calendar.MONTH, monthOfYear);
            myCalendarr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }
    };
    private int year,year1;
    private int month,month1;
    private int day,day1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demand_seet);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("UserName", "");
//        Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_LONG).show();
        et_search = findViewById(R.id.search);
        menuImg = findViewById(R.id.menuImg);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);
        norecord_tv = findViewById(R.id.norecord_tv);
        search_tv = findViewById(R.id.search_tv);
        title_tv = findViewById(R.id.title_tv);

        setCurrentDateOnView();

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(CollectionDemandSeet.this)) {
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
                    Toast.makeText(CollectionDemandSeet.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Member_EmpList("", "", "", username);

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrLegalList.clear();
//                pdfAdapTer.notifyDataSetChanged();
                Member_EmpList(tvDisplayDate2.getText().toString(), "", tvDisplayDate.getText().toString(), username);
                Log.d("sdsdsdsdsd", tvDisplayDate.getText().toString() + "\n\n" + tvDisplayDate2.getText().toString() + "\n\n" + username);
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
                    arrLegalList.clear();
                    pdfAdapTer.notifyDataSetChanged();
                    Member_EmpList("", newText, "", username);
                } else {
                    arrLegalList.clear();
                    pdfAdapTer.notifyDataSetChanged();
                    Member_EmpList("", "", "", username);
                }
                return true;
            }
        });

    }

    private void Member_EmpList(String enddate, String Profarma, String startdate, String id) {
        String otp1 = new GlobalAppApis().CollectionDemandSeetAPI(enddate, Profarma, startdate, id);
        Log.v("otpi", otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.CollectionAndDemandSheetAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    Log.v("responsememberdetails", result);
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("objdash");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("Collection & Demand Sheet("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("EMIAmount", jsonObject.getString("EMIAmount"));
                            hashlist.put("InstallmentNo", jsonObject.getString("InstallmentNo"));
                            hashlist.put("MemberId", jsonObject.getString("MemberId"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("PlanName", jsonObject.getString("PlanName"));
                            hashlist.put("ProfarmaNo", jsonObject.getString("ProfarmaNo"));
                            hashlist.put("Status", jsonObject.getString("Status"));
                            hashlist.put("PrincipleAmount", jsonObject.getString("PrincipleAmount"));
                            hashlist.put("InstDate", jsonObject.getString("InstDate"));
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new LegalListAdapter(CollectionDemandSeet.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, CollectionDemandSeet.this, call1, "", true);
    }


    public class LegalListAdapter extends RecyclerView.Adapter<CollectionDemandSeet.LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }

        public CollectionDemandSeet.LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CollectionDemandSeet.LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_collectiondemandseet, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final CollectionDemandSeet.LegalList holder, final int position) {
            holder.t1.setText(data.get(position).get("MemberName") + "(" + data.get(position).get("MemberId") + ")");
            holder.t2.setText(data.get(position).get("EMIAmount"));
            holder.t3.setText(data.get(position).get("InstallmentNo"));
            holder.t4.setText(data.get(position).get("ProfarmaNo"));
            holder.t5.setText(data.get(position).get("Status"));

            holder.t6.setText(data.get(position).get("PrincipleAmount"));
            holder.date.setText("Inst.Date:- " + data.get(position).get("InstDate"));


        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class LegalList extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4, t5, t6, date, t8, t9, t10, t11, t12;

        public LegalList(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.tv1);
            t2 = itemView.findViewById(R.id.tv2);
            t3 = itemView.findViewById(R.id.tv3);
            t4 = itemView.findViewById(R.id.tv4);
            t5 = itemView.findViewById(R.id.tv5);
            t6 = itemView.findViewById(R.id.tv6);
            date = itemView.findViewById(R.id.date);

        }
    }


    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.mStartTime);
        tvDisplayDate2 = (TextView) findViewById(R.id.mEndTime);

        tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker;
                year = myCalendar.get(Calendar.YEAR);
                month = myCalendar.get(Calendar.MONTH);
                day = myCalendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(CollectionDemandSeet.this, date, year, month, day);
                // This akp is used for disable previous date but you can select the date
//                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                // This akp is used to prevent the date selection
                datePicker.getDatePicker().setCalendarViewShown(false);
                datePicker.show();
            }
        });
        tvDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker1;
                year1 = myCalendarr.get(Calendar.YEAR);
                month1 = myCalendarr.get(Calendar.MONTH);
                day1 = myCalendarr.get(Calendar.DAY_OF_MONTH);
                datePicker1 = new DatePickerDialog(CollectionDemandSeet.this, date1, year1, month1, day1);
                // This akp is used for disable previous date but you can select the date
                datePicker1.getDatePicker().setMinDate(System.currentTimeMillis());
                // This akp is used to prevent the date selection
                datePicker1.getDatePicker().setCalendarViewShown(true);
                datePicker1.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDisplayDate2.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDisplayDate.setText(sdf.format(myCalendarr.getTime()));
    }


}


  /*
    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showtvDisplayDate2PickerDialog(View v) {
        DialogFragment newFragment = new tvDisplayDate2PickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(),this, year,
                    month,day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

//            if (month ==1){
//                newmonth="Jan";
//            }
//            else if (month ==2){
//                newmonth="Fab";
//            }
//            else if (month ==3){
//                newmonth="Mar";
//            }
//            else if (month ==4){
//                newmonth="Apr";
//            }
//            else if (month ==5){
//                newmonth="May";
//            }
//            else if (month ==6){
//                newmonth="Jun";
//            }
//            else if (month ==7){
//                newmonth="Jul";
//            }
//            else if (month ==8){
//                newmonth="Aug";
//            }
//            else if (month ==9){
//                newmonth="Sep";
//            }
//            else if (month ==10){
//                newmonth="Oct";
//            }
//            else if (month ==11){
//                newmonth="Nov";
//            }
//            else if (month ==12){
//                newmonth="Dec";
//            }

            // Do something with the date chosen by the user
            tvDisplayDate.setText(day + "-" + month  + "-" + year);
        }

    }

    public static class tvDisplayDate2PickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            String gettvDisplayDate = tvDisplayDate.getText().toString().trim();
            String getfrom[] = gettvDisplayDate.split("-");
            int year,month,day;
            year= Integer.parseInt(getfrom[2]);
            month = Integer.parseInt(getfrom[1]);
            Log.d("afdafa",""+month);
            day = Integer.parseInt(getfrom[0]);
            final Calendar c = Calendar.getInstance();
            c.set(year,month,day+1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year,month,day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {

            tvDisplayDate2.setText(day + "-" + month  + "-" + year);
        }
    }
      
    }*/
