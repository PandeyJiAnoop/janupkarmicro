package com.akp.janupkar.basic;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.view.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class EmiChartActivity extends BaseActivity {
    RecyclerView rcvMemberLoanDetails;
    private ArrayList<HashMap<String, String>> arrRepaymentList = new ArrayList<>();
    RelativeLayout rlHeader;
    String UserName, Emiobj, extraPer, advanceAmt, ProfarmaNo;
    private SharedPreferences sharedPreferences;
    double totalPrice = 0, LateStatus = 0;
    Button btnCalculate;
    TextView tvAmountPaid, tvLateFee, tvPaymentMode;
    EditText tvPaidAmount;
    LinearLayout llCalculateDate;
    Button btnSave;
    Spinner spinner;
    JSONArray array = new JSONArray();
    VolleyService myvolleyservice;
    private ArrayList<String> arrRepaymentListt = new ArrayList<String>();

    String[] FootBallPlayers = new String[]{"Select Payment Mood", "Cash", "Cheque", "Online Payment"};
    String selectedName = "";
    LinearLayout llcheck, llRefrence;
    EditText edtChequeNo, edtRefernceNumber;
    TextView tvDate, tvChequeDate;
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
    private int year, year1;
    private int month, month1;
    private int day, day1;
    TextView tvadv;
    ImageView pdf_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi_chart);
        rcvMemberLoanDetails = findViewById(R.id.rcgEmi);
        rlHeader = findViewById(R.id.rlHeader);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);
        tvLateFee = findViewById(R.id.tvLateFee);
        spinner = findViewById(R.id.spinner);
        tvPaidAmount = findViewById(R.id.tvPaidAmount);
        llCalculateDate = findViewById(R.id.llCalculateDate);
        btnSave = findViewById(R.id.btnSave);
        llcheck = findViewById(R.id.llcheck);
        llRefrence = findViewById(R.id.llRefrence);
        tvDate = findViewById(R.id.tvDate);
        tvChequeDate = findViewById(R.id.tvChequeDate);
        edtChequeNo = findViewById(R.id.edtChequeNo);
        tvadv = findViewById(R.id.tvadv);

        edtRefernceNumber = findViewById(R.id.edtRefernceNumber);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserName = sharedPreferences.getString("UserName", "");
        Emiobj = sharedPreferences.getString("Emiobj", "");
        advanceAmt = sharedPreferences.getString("advanceAmt", "");
        extraPer = sharedPreferences.getString("extraPer", "");
        ProfarmaNo = sharedPreferences.getString("ProfarmaNo", "");
        tvadv.setText(advanceAmt);

        pdf_img = findViewById(R.id.pdf_img);


        final List<String> plantsList = new ArrayList<>(Arrays.asList(FootBallPlayers));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_value);
        spinner.setAdapter(spinnerArrayAdapter);
        tvChequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker;
                year = myCalendar.get(Calendar.YEAR);
                month = myCalendar.get(Calendar.MONTH);
                day = myCalendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(EmiChartActivity.this, date, year, month, day);
                // This akp is used for disable previous date but you can select the date
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                // This akp is used to prevent the date selection
                datePicker.getDatePicker().setCalendarViewShown(false);
                datePicker.show();
            }
        });


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker1;
                year1 = myCalendarr.get(Calendar.YEAR);
                month1 = myCalendarr.get(Calendar.MONTH);
                day1 = myCalendarr.get(Calendar.DAY_OF_MONTH);
                datePicker1 = new DatePickerDialog(EmiChartActivity.this, date1, year1, month1, day1);
                // This akp is used for disable previous date but you can select the date
                datePicker1.getDatePicker().setMinDate(System.currentTimeMillis());
                // This akp is used to prevent the date selection
                datePicker1.getDatePicker().setCalendarViewShown(true);
                datePicker1.show();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().trim() == "Select Payment Mood") {
                } else {
                    selectedName = String.valueOf(spinner.getSelectedItem());
                    if (selectedName.equalsIgnoreCase("Cash")) {
                        llcheck.setVisibility(View.GONE);
                        llRefrence.setVisibility(View.GONE);
                    } else if (selectedName.equalsIgnoreCase("Cheque")) {
                        llcheck.setVisibility(View.VISIBLE);
                        llRefrence.setVisibility(View.GONE);
                    } else if (selectedName.equalsIgnoreCase("Online Payment")) {
                        llcheck.setVisibility(View.GONE);
                        llRefrence.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {
            JSONArray array = new JSONArray(Emiobj);
            parseList(array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openpaymentdetail(advanceAmt, "0", totalPrice, LateStatus);
//      openpaymentdetail(advanceAmt, extraPer, totalPrice, LateStatus);
                Log.d("dsfgsdgsg", "\n\n" + advanceAmt + "\n\n" + extraPer + "\n\n" + totalPrice + "\n\n" + LateStatus);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDate.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please select Date!",Toast.LENGTH_LONG).show();
                }
                else {
                    PaymentaddEMI(edtChequeNo.getText().toString(), tvDate.getText().toString(), UserName, tvLateFee.getText().toString(), tvAmountPaid.getText().toString(), ProfarmaNo, tvChequeDate.getText().toString(), edtRefernceNumber.getText().toString());
                }
            }
        });
    }

    public void parseList(JSONArray array) {
        try {
            for (int n = 0; n < array.length(); n++) {
                JSONObject obj = array.getJSONObject(n);
                HashMap<String, String> hashlist = new HashMap();
                hashlist.put("ArearAmount", obj.getString("ArearAmount"));
                hashlist.put("BranchId", obj.getString("BranchId"));
                hashlist.put("EMIAmount", obj.getString("EMIAmount"));
                hashlist.put("InstDate", obj.getString("InstDate"));
                hashlist.put("InstallmentNo", obj.getString("InstallmentNo"));
                hashlist.put("InterestAmount", obj.getString("InterestAmount"));
                hashlist.put("MemberId", obj.getString("MemberId"));
                hashlist.put("PenaltyPerDayPer", obj.getString("PenaltyPerDayPer"));
                hashlist.put("PenaltyPerMonthPer", obj.getString("PenaltyPerMonthPer"));
                hashlist.put("PenaltyPerweekPer", obj.getString("PenaltyPerweekPer"));
                hashlist.put("PerdayLateFIne", obj.getString("PerdayLateFIne"));
                hashlist.put("PrincipleAmount", obj.getString("PrincipleAmount"));
                hashlist.put("ProfarmaNo", obj.getString("ProfarmaNo"));
                hashlist.put("Status", obj.getString("Status"));
                hashlist.put("LateStatus", obj.getString("LateStatus"));
                ProfarmaNo = obj.getString("ProfarmaNo");
                arrRepaymentList.add(hashlist);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 1);
                EMIAdapter adapter = new EMIAdapter(mActivity, arrRepaymentList);
                rcvMemberLoanDetails.setLayoutManager(layoutManager);
                rcvMemberLoanDetails.setAdapter(adapter);

//                pdf_img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        saveCsvFile(hashlist);
//                        Toast.makeText(getApplicationContext(), "Downloading Start...", Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class EMIAdapter extends RecyclerView.Adapter<Holder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public EMIAdapter(Activity mActivity, ArrayList<HashMap<String, String>> arrRepaymentList) {
            data = arrRepaymentList;
        }

        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.emi_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Holder holder, final int position) {
            holder.tvInstallmentDate.setText(data.get(position).get("InstDate"));
            holder.tvInstallmentNo.setText(data.get(position).get("InstallmentNo"));
            holder.tvPrincipleAmount.setText("Rs." + data.get(position).get("PrincipleAmount"));
            holder.tvInterestAmount.setText("Rs." + data.get(position).get("InterestAmount"));
            holder.tvEMIAmount.setText("Rs." + data.get(position).get("EMIAmount"));
            holder.tvLateFine.setText(data.get(position).get("LateStatus"));
            holder.tvStatus.setText(data.get(position).get("Status"));
            if (data.get(position).get("Status").equalsIgnoreCase("Paid")) {
                holder.checkbox.setChecked(true);
                holder.checkbox.setClickable(false);
                if (holder.checkbox.isChecked()) {
                    holder.checkbox.setEnabled(false);
                }
            }

            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkbox.isChecked()) {
                        totalPrice += Double.parseDouble(data.get(position).get("EMIAmount"));
                        LateStatus += Double.parseDouble(data.get(position).get("LateStatus"));
                        arrRepaymentListt.add(data.get(position).get("InstallmentNo"));
                    } else {
                        totalPrice -= Double.parseDouble(data.get(position).get("EMIAmount"));
                        LateStatus -= Double.parseDouble(data.get(position).get("LateStatus"));
                        arrRepaymentListt.remove(data.get(position).get("InstallmentNo"));
                    }
                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvInstallmentDate, tvInstallmentNo, tvPrincipleAmount, tvInterestAmount, tvEMIAmount, tvLateFine, tvStatus;
        CheckBox checkbox;

        public Holder(View itemView) {
            super(itemView);
            tvInstallmentDate = itemView.findViewById(R.id.tvInstallmentDate);
            tvInstallmentNo = itemView.findViewById(R.id.tvInstallmentNo);
            tvPrincipleAmount = itemView.findViewById(R.id.tvPrincipleAmount);
            tvInterestAmount = itemView.findViewById(R.id.tvInterestAmount);
            tvEMIAmount = itemView.findViewById(R.id.tvEMIAmount);
            tvLateFine = itemView.findViewById(R.id.tvLateFine);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

    public void openpaymentdetail(String advanceAmt, String extraPer, double totalPrice, double lateStatus) {
        String otp1 = new GlobalAppApis().openpaymentdetail(advanceAmt, extraPer, totalPrice, lateStatus);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.openpaymentdetail(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if (jsonObject.getString("Message").equalsIgnoreCase("Success")) {
                        llCalculateDate.setVisibility(View.VISIBLE);
                        tvAmountPaid.setText(jsonObject.getString("TotPayableAmt"));
                        tvLateFee.setText(jsonObject.getString("TotalLateFee"));
                        tvPaidAmount.setText(jsonObject.getString("totPaid"));
                    } else {
                        Toast.makeText(EmiChartActivity.this, "try again.", Toast.LENGTH_SHORT).show();
                        llCalculateDate.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, EmiChartActivity.this, call1, "", true);

    }

    public void PaymentaddEMI(String refrencenumber, String date, String UserName, String Latefee, String paidAmount, String ProfarmaNo, String ChequeDate, String refrenceId) {
        String otp1 = new GlobalAppApis().PaymentaddEMI("BranchCode", refrencenumber, "CollectionBy", date, UserName, "ExtraAmount", "ExtraPer", Latefee, "Loanclose", paidAmount, ProfarmaNo, "Remark", ChequeDate, refrenceId, arrRepaymentListt, selectedName);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.PaymentaddEMI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("responseeeee", String.valueOf(jsonObject));
                    if (jsonObject.getString("Status").equalsIgnoreCase("true")) {
                        Toast.makeText(EmiChartActivity.this, "EMI Add Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), RepaymentListActivity.class));
                    } else {
                        Toast.makeText(EmiChartActivity.this, "Something Wrong try again...", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, EmiChartActivity.this, call1, "", true);
    }

    public void SelectSpinnerValue(View view) {
        spinner.setSelection(2);
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvChequeDate.setText(sdf.format(myCalendarr.getTime()));
    }


//    private void saveCsvFile(HashMap<String, String> data) {
//        File csvFile = new File(getExternalFilesDir(null), "data.csv");
//        try {
//            // Create a CSVWriter object
//            CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
//
//            // Write data to CSV
//            writer.writeAll((List<String[]>) data);
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