package com.akp.janupkar;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.GlobalAppApis;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.basic.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;

public class UploadAddReceiptPicandSign extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask,userChoosenTask1;
    String temp;
   String temp2;
    LinearLayout aadhar_back_ll,aadhar_font_ll;
    ImageView aadhar_back_img,aadhar_font_img;

    Button submit;
    private SharedPreferences sharedPreferences;
String username,getPerforNo;
    String chosseimag,upload_cust_id;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_add_receipt_picand_sign);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("UserName", "");
        getPerforNo=getIntent().getStringExtra("perfno");
        upload_cust_id=getIntent().getStringExtra("upload_cust_id");

        aadhar_font_ll= findViewById(R.id.aadhar_font_ll);
        aadhar_back_ll= findViewById(R.id.aadhar_back_ll);

        aadhar_font_img= findViewById(R.id.aadhar_font_img);
        aadhar_back_img= findViewById(R.id.aadhar_back_img);
        submit= findViewById(R.id.submit);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ressss_img", "img2="+temp2);
                UploadLoanApproveList(upload_cust_id,getPerforNo,temp,temp2);

            }
        });

        ReceiptPicandSign_ListAPIService(upload_cust_id,getPerforNo);

    }



    private void UploadLoanApproveList(String mid,String pid,String ReceiptPic,String ReceiptSignPic) {
        String otp1 = new GlobalAppApis().ApprovedLoanListUpload(mid,pid,ReceiptPic,ReceiptSignPic);
        Log.d("otp1", otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.UploadLoanApproveListAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resnew", result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(result);
                    Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Error:- "+e,Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            }
        }, UploadAddReceiptPicandSign.this, call1, "", true);

    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadAddReceiptPicandSign.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(UploadAddReceiptPicandSign.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
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
        final CharSequence[] items = {"Choose from Library", "Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadAddReceiptPicandSign.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item1) {
                boolean result= Utility.checkPermission(UploadAddReceiptPicandSign.this);
                if (items[item1].equals("Choose from Library")) {
                    userChoosenTask1 ="Choose from Library";
                    if(result)
                        galleryIntent1();

                } else if (items[item1].equals("Cancel")) {
                    dialog.dismiss();
                }
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



    private void ReceiptPicandSign_ListAPIService(String mid,String pid) {
        String otp1 = new GlobalAppApis().ViewIploadImage(mid,pid);
        Log.d("otp1", otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.ReceiptPicandSign_ListAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resnew", result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("objdash");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String PaymentReceiptPic = jsonobject.getString("PaymentReceiptPic");
                            String PaymentSignPic = jsonobject.getString("PaymentSignPic");

                            if (jsonobject.getString("PaymentReceiptPic").equalsIgnoreCase("")){
                            }
                            else {
                                Glide.with(getApplicationContext()).load(jsonobject.getString("PaymentReceiptPic")).error(R.drawable.footerlogo).into(aadhar_font_img);
                            }
                            if (jsonobject.getString("PaymentSignPic").equalsIgnoreCase("")){
                            }
                            else {
                                Glide.with(getApplicationContext()).load(jsonobject.getString("PaymentSignPic")).error(R.drawable.footerlogo).into(aadhar_back_img);
                            }
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, UploadAddReceiptPicandSign.this, call1, "", true);

    }



}