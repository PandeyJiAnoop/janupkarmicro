package com.akp.janupkar.AllRegNewAPI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.akp.janupkar.R;
import com.akp.janupkar.basic.Utility;
import com.akp.janupkar.utils.AppUtils;
import com.akp.janupkar.view.BaseActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Reg_GuarantorDetails extends BaseActivity implements View.OnClickListener {
    RelativeLayout rlHeader;
    Button btnSubmit;
    private SharedPreferences sharedPreferences;
    String MemberId,profarmaNo;

    EditText buss_name_et,buss_year_et,mob_et,family_income_et,add_et,edt_name,edt_mobile,edt_Address,edt_Addhar;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask,userChoosenTask1;
    String temp;
    String temp2;
    LinearLayout aadhar_back_ll,aadhar_font_ll;
    ImageView aadhar_back_img,aadhar_font_img;
    String chosseimag;
    String GetCatSP,GetBussSP;
    Spinner cat_sp,buss_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_guarantor_details);
        findViewById();
        setListner();
    }

    private void setListner() {
        rlHeader.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void findViewById() {
        rlHeader=findViewById(R.id.rlHeader);


        buss_name_et=findViewById(R.id.buss_name_et);
        buss_year_et=findViewById(R.id.buss_year_et);
        mob_et=findViewById(R.id.mob_et);
        family_income_et=findViewById(R.id.family_income_et);
        add_et=findViewById(R.id.add_et);
        edt_name=findViewById(R.id.edt_name);
        edt_mobile=findViewById(R.id.edt_mobile);
        edt_Address=findViewById(R.id.edt_Address);
        edt_Addhar=findViewById(R.id.edt_Addhar);


        btnSubmit=findViewById(R.id.btnSubmit);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        MemberId = sharedPreferences.getString("MemberId", "");
        profarmaNo = sharedPreferences.getString("profarmaNo", "");


        aadhar_font_ll= findViewById(R.id.aadhar_font_ll);
        aadhar_back_ll= findViewById(R.id.aadhar_back_ll);

        aadhar_font_img= findViewById(R.id.aadhar_font_img);
        aadhar_back_img= findViewById(R.id.aadhar_back_img);

        cat_sp = (Spinner)findViewById(R.id.cat_sp);
        GetCatSP = cat_sp.getSelectedItem().toString();
        buss_sp = (Spinner)findViewById(R.id.buss_sp);
        GetBussSP = buss_sp.getSelectedItem().toString();


        aadhar_font_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseimag="1";
                selectImage();
            }
        });
        aadhar_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosseimag="2";
                selectImage1();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlHeader:
                finish();
                break;
            case R.id.btnSubmit:
                if (buss_name_et.getText().toString().equalsIgnoreCase("")){
                    buss_name_et.setError("Fields can't be blank!");
                    buss_name_et.requestFocus();
                }
                else if (buss_year_et.getText().toString().equalsIgnoreCase("")){
                    buss_year_et.setError("Fields can't be blank!");
                    buss_year_et.requestFocus();
                }
                else if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }

                else if (family_income_et.getText().toString().equalsIgnoreCase("")){
                family_income_et.setError("Fields can't be blank!");
                family_income_et.requestFocus();
                }
                else if (add_et.getText().toString().equalsIgnoreCase("")){
                add_et.setError("Fields can't be blank!");
                add_et.requestFocus();
                }
                else if (edt_name.getText().toString().equalsIgnoreCase("")){
                edt_name.setError("Fields can't be blank!");
                edt_name.requestFocus();
                }
            else if (edt_mobile.getText().toString().equalsIgnoreCase("")){
                edt_mobile.setError("Fields can't be blank!");
                edt_mobile.requestFocus();
            }
            else if (edt_Address.getText().toString().equalsIgnoreCase("")){
                edt_Address.setError("Fields can't be blank!");
                edt_Address.requestFocus();
            }
            else if (edt_Addhar.getText().toString().equalsIgnoreCase("")){
                edt_Addhar.setError("Fields can't be blank!");
                edt_Addhar.requestFocus();
            }
                else {
                    MemberGaranterAdd();
                }
                break;
        }
    }

    public void MemberGaranterAdd() {
        AppUtils.showRequestDialog(mActivity);
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/MemberGuarantorDetails_Temp";
        Log.v("#####", url);

        Log.v("urlApi", url);
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            jsonObject.put("AadharBack",temp2);
            jsonObject.put("AadharFront",temp);
            jsonObject.put("AadharNo",edt_Addhar.getText().toString());
            jsonObject.put("BusinessName",buss_name_et.getText().toString());
            jsonObject.put("Category",GetCatSP);
            jsonObject.put("CustomerAddress",add_et.getText().toString());
            jsonObject.put("CustomerMobileNo",mob_et.getText().toString());
            jsonObject.put("EntryBy",MemberId);
            jsonObject.put("Guarantor1Id",MemberId);
            jsonObject.put("Guarantor1MobileNo",edt_mobile.getText().toString());
            jsonObject.put("Guarantor1Name",edt_name.getText().toString());
            jsonObject.put("Gurantor1Address",edt_Address.getText().toString());
            jsonObject.put("MemberId",MemberId);
            jsonObject.put("ProfarmaNo",profarmaNo);
            jsonObject.put("YearInBusiness",buss_year_et.getText().toString());
            jsonObject.put("Yearlyincomeoffamily",family_income_et.getText().toString());
            jsonObject.put("YourBusiness",GetBussSP);
            Log.v("finddObjectgur", String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url).addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJsonloandetails(response);
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.hideDialog();
                            Log.v("tyu", error.getMessage());
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    private void parseJsonloandetails(JSONObject response) {
        Log.v("responseGetCat", response.toString());
        AppUtils.hideDialog();
        try {
            String status = response.getString("Status");
            if (status.equals("true")) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Reg_GuarantorDetails.this);
                builder.setTitle("Guarantor Details add successfully!")
                        .setMessage(response.getString("Msg"))
                        .setCancelable(false)
                        .setIcon(R.drawable.appicon)
                        .setPositiveButton("Final Submit Form", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(mActivity, RegistrtionForm.class));
                                finish();
                                dialog.dismiss();
                            }});
                builder.create().show();
                Toast.makeText(this, "Gurantor Details add successfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, response.getString("Msg"), Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AppUtils.hideDialog();
    }




    private void selectImage() {
        final CharSequence[] items = {"Choose from Library" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Reg_GuarantorDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Reg_GuarantorDetails.this);
                galleryIntent();
//                if (items[item].equals("Choose from Library")) {
//                    userChoosenTask ="Choose from Library";
//                    if(result)
//                        galleryIntent();
//
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (chosseimag.equalsIgnoreCase("1"))
        {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);
            }
//            Toast.makeText(getApplicationContext(),"one",Toast.LENGTH_LONG).show();

        }
        else  {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult1(data);
//                Toast.makeText(getApplicationContext(),"twooooooooo",Toast.LENGTH_LONG).show();
            }
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        aadhar_font_img.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.WEBP, 0, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }






    private void selectImage1() {
        final CharSequence[] items = {"Choose from Library"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Reg_GuarantorDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item1) {
                boolean result= Utility.checkPermission(Reg_GuarantorDetails.this);
                galleryIntent1();
//                if (items[item1].equals("Choose from Library")) {
//                    userChoosenTask1 ="Choose from Library";
//                    if(result)
//                        galleryIntent1();
//
//                } else if (items[item1].equals("Cancel")) {
//                    dialog.dismiss();
//                }
            }
        });
        builder.show();
    }

    private void galleryIntent1()
    {
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent1, "Select File"),SELECT_FILE);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult1(data);
//        }
//    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult1(Intent data1) {
        Bitmap bm1=null;
        if (data1 != null) {
            try {
                bm1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data1.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm1,Toast.LENGTH_LONG).show();
        aadhar_back_img.setImageBitmap(bm1);
        Bitmap immagex1=bm1;
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        immagex1.compress(Bitmap.CompressFormat.WEBP, 0, baos1);
        byte[] b1 = baos1.toByteArray();
        String image2 = Base64.encodeToString(b1,Base64.DEFAULT);
        temp2=image2;
        Log.d("image2", image2);
    }
}