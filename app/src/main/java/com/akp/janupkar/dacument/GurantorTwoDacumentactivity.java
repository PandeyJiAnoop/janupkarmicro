package com.akp.janupkar.dacument;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.akp.janupkar.R;
import com.akp.janupkar.basic.Utility;
import com.akp.janupkar.utils.AppUtils;
import com.akp.janupkar.view.BaseActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GurantorTwoDacumentactivity extends BaseActivity implements View.OnClickListener {
    CardView crdDetails,cardUploadData;
    EditText edt_profermer,edt_DocumentNo;
    Spinner spinner;
    ArrayList<String> branch_list;
    ArrayList<String> branch_listId;
    ArrayList<HashMap<String, String>> branchList;
    GurantorTwoDacumentactivity.spinnerAdapter spinnerAdapter;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String imgString = "",adhar="";
    TextView tvChoose,tvDate;
    RecyclerView rcvList;
    private ArrayList<HashMap<String, String>> arrMemberList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String UserId,selectedName;
    RelativeLayout rlAdd,rlHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gurantor_two_dacumentactivity);
        findViewById();
        setListner();
        Documenttypelist();
    }
    private void setListner() {
        crdDetails.setOnClickListener(this);
        tvChoose.setOnClickListener(this);
        rlAdd.setOnClickListener(this);
        rlHeader.setOnClickListener(this);
    }
    private void findViewById() {
        crdDetails=findViewById(R.id.crdDetails);
        edt_profermer=findViewById(R.id.edt_profermer);
        spinner=findViewById(R.id.spinner);
        cardUploadData=findViewById(R.id.cardUploadData);
        tvChoose=findViewById(R.id.tvChoose);
        rcvList=findViewById(R.id.rcvList);
        tvDate=findViewById(R.id.tvDate);
        edt_DocumentNo=findViewById(R.id.edt_DocumentNo);
        rlAdd=findViewById(R.id.rlAdd);
        rlHeader=findViewById(R.id.rlHeader);
        branch_list = new ArrayList<>();
        branch_listId = new ArrayList<>();
        branchList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId = sharedPreferences.getString("UserId", "");
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().trim() == "Select Payment Mood") {
                } else {
                    selectedName = branch_listId.get(spinner.getSelectedItemPosition());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvChoose:
                selectImage();
                break;
            case R.id.rlHeader:
                finish();
                break;
            case R.id.crdDetails:
                if(edt_profermer.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Please Enter Profarma No.", Toast.LENGTH_SHORT).show();

                }

                else {
                    GetMemberdetailbyproformano();
                }
                break;
            case R.id.rlAdd:
                if(edt_profermer.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Please Enter Proferma Number ", Toast.LENGTH_SHORT).show();

                }
                else if(edt_DocumentNo.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Please Enter Dacument Number ", Toast.LENGTH_SHORT).show();

                }
                else if(selectedName.equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    AddDocumentMember();
                }

                break;
        }

    }
    public  void  AddDocumentMember()
    {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/AddDocumentMember";
        Log.v( "#####", url );

        Log.v( "urlApi", url );
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("AccountType","G2");
            jsonObject.put("DocumentNo",edt_DocumentNo.getText().toString());
            jsonObject.put("DocumentType",selectedName);
            jsonObject.put("EntryBy",UserId);
            jsonObject.put("MainImg",imgString);
            jsonObject.put("ProformaNumber",edt_profermer.getText().toString());
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

                            Log.v( "tyu", error.getMessage() );
                            Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                            Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        }
                    }
                } );
    }

    private void parseJsonn(JSONObject response) {

        Log.v( "responseGetCat", response.toString() );


        try {


            String status = response.getString( "Status" );


            if (status.equals( "true" )) {

                Toast.makeText(this, "Member Dacument Upload Successfully", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), DacumentUploadActivity.class));

            } else {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();

                cardUploadData.setVisibility(View.GONE);
                rcvList.setVisibility(View.GONE);

            }



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();
    }
    public  void  GetMemberdetailbyproformano()
    {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/GetMemberdetailbyproformano";
        Log.v( "#####", url );

        Log.v( "urlApi", url );
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("ProfarmaNo",edt_profermer.getText().toString());
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
                            parseJson( response );

                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        // handle error
                        if (error.getErrorCode() != 0) {
                            AppUtils.hideDialog();

                            Log.v( "tyu", error.getMessage() );
                            Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                            Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                        }
                    }
                } );
    }

    private void parseJson(JSONObject response) {

        Log.v( "responseGetCat", response.toString() );


        try {

            arrMemberList.clear();
            String status = response.getString( "Statusbool" );


            if (status.equals( "true" )) {
                cardUploadData.setVisibility(View.VISIBLE);
                rcvList.setVisibility(View.VISIBLE);
                String AccountName=response.getString("AccountName");
                String CashBackAmount=response.getString("CashBackAmount");
                String mDate=response.getString("mDate");
                tvDate.setText("Profarma No: #"+edt_profermer.getText().toString()+" | "+AccountName+", Loan Amount: "+CashBackAmount+"\n"+mDate);

                JSONArray jsonArray=response.getJSONArray("doclist");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject block_object = jsonArray.getJSONObject( i );
                    if(block_object.getString("DocumentFor").equalsIgnoreCase("G2"))
                    {

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put( "DocumentNo", block_object.get( "DocumentNo" ).toString() );
                        map.put( "filePath", block_object.get( "filePath" ).toString() );
                        map.put( "DocumentTitle", block_object.get( "DocumentTitle" ).toString() );
                        arrMemberList.add( map );
                    }

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    GurantorTwoDacumentactivity.MemberListAdapter adapter = new GurantorTwoDacumentactivity.MemberListAdapter(getApplicationContext(),arrMemberList);
                    rcvList.setLayoutManager(layoutManager);
                    rcvList.setAdapter(adapter);


                }



            } else {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();

                cardUploadData.setVisibility(View.GONE);
                rcvList.setVisibility(View.GONE);

            }



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();
    }

    public void Documenttypelist() {
        String url = "http://janupkarmicroapi.signaturesoftware.org/Service/Documenttypelist";
        Log.v( "#####", url );

        Log.v( "urlApi", url );
        JSONObject jsonObject = new JSONObject();


        AndroidNetworking.post( url ).addJSONObjectBody( jsonObject )
                .setPriority( Priority.HIGH ).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseJsonnn( response );

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(ANError error) {

                // handle error
                if (error.getErrorCode() != 0) {
                    AppUtils.hideDialog();

                    Log.v( "tyu", error.getMessage() );
                    Log.d( "onError errorCode ", "onError errorCode : " + error.getErrorCode() );
                    Log.d( "onError errorBody", "onError errorBody : " + error.getErrorBody() );
                    Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                } else {
                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                    Log.d( "onError errorDetail", "onError errorDetail : " + error.getErrorDetail() );
                }
            }
        } );
    }

    private void parseJsonnn(JSONObject response) {

        Log.v( "responseGetCat", response.toString() );


        try {


            String status = response.getString( "Status" );
            branchList.clear();
            branch_list.clear();
            branch_listId.clear();

            if (status.equals( "true" )) {
                JSONArray data_array = response.getJSONArray( "resdoc" );


                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject block_object = data_array.getJSONObject( i );

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put( "DocumentTitle", block_object.get( "DocumentTitle" ).toString() );
                    map.put( "SrNo", block_object.get( "SrNo" ).toString() );

                    branchList.add( map );
                    branch_list.add(  branchList.get( i ).get( "DocumentTitle" ) );
                    branch_listId.add(  branchList.get( i ).get( "SrNo" ) );
                    Log.d( "CategorylistID11",  branchList.get( i ).get( "DocumentTitle" ) );


                }
                spinnerAdapter = new GurantorTwoDacumentactivity.spinnerAdapter( getApplicationContext(), R.layout.spinner, (ArrayList<String>) branch_list );
                spinner.setAdapter( spinnerAdapter );


            } else {


            }



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }

        AppUtils.hideDialog();
    }

    public static class spinnerAdapter extends ArrayAdapter<String> {

        ArrayList<String> data;

        public spinnerAdapter(Context context, int textViewResourceId, ArrayList<String> arraySpinner_time) {

            super(context, textViewResourceId, arraySpinner_time);

            this.data = arraySpinner_time;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(data.get(position));
            return row;
        }
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(GurantorTwoDacumentactivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(GurantorTwoDacumentactivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getEncoded64ImageStringFromBitmap(thumbnail);




    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(GurantorTwoDacumentactivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        getEncoded64ImageStringFromBitmap(bm);



    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);

        Log.v("dadadadadaaaa", imgString);



        imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);


        return imgString;
    }
    private class MemberListAdapter extends RecyclerView.Adapter<GurantorTwoDacumentactivity.ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public MemberListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrTotalApplyList) {
            data=arrTotalApplyList;
        }


        public GurantorTwoDacumentactivity.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GurantorTwoDacumentactivity.ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.memberdacumentlist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final GurantorTwoDacumentactivity.ProductHolder holder, final int position) {

            holder.tvDacumentType.setText(data.get(position).get("DocumentTitle"));
            holder.tvDacumentNumber.setText(data.get(position).get("DocumentNo"));
            if(data.get(position).get("filePath").equalsIgnoreCase(""))
            {

            }
            else {
                Picasso.get().load(data.get(position).get("filePath")).into(holder.ivImage);

            }


        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        TextView tvDacumentNumber,tvDacumentType;
        ImageView ivImage;

        public ProductHolder(View itemView) {
            super(itemView);
            ivImage=itemView.findViewById(R.id.ivImage);
            tvDacumentType=itemView.findViewById(R.id.tvDacumentType);
            tvDacumentNumber=itemView.findViewById(R.id.tvDacumentNumber);






        }
    }
}