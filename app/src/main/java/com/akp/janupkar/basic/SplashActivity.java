package com.akp.janupkar.basic;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akp.janupkar.R;
import com.akp.janupkar.RetrofitAPI.ApiService;
import com.akp.janupkar.RetrofitAPI.ConnectToRetrofit;
import com.akp.janupkar.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.janupkar.customer.CustomerDashboard;
import com.akp.janupkar.login.WelcomActivity;
import com.akp.janupkar.view.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.akp.janupkar.RetrofitAPI.API_Config.getApiClient_ByPost;
import static com.akp.janupkar.common.apptools.showAlertDialog;

public class SplashActivity extends BaseActivity {
    String versionName = "", versionCode = "";
    private int DELAY = 2000;
    private SharedPreferences sharedPreferences;
    String UserId, MemberId,RoleType;
    private static final int permission_granted_code = 100;
    private static final String[] appPermission =
            {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById();

        hashkey();


    }

    private void findViewById() {
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("UserId", "");
        MemberId = sharedPreferences.getString("MemberId", "");
        RoleType = sharedPreferences.getString("RoleType", "");
        Log.v("checkdata", MemberId);
        Log.v("checkdata", "cddds" + UserId);
//        getPermission();
//        checkLogin();
        settingApi();
    }

    private void checkLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (RoleType.equalsIgnoreCase("4")){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("RoleType",RoleType);
                    startActivity(intent);
                }
                else if (RoleType.equalsIgnoreCase("8")){
                    Intent intent=new Intent(getApplicationContext(), CustomerDashboard.class);
                    intent.putExtra("RoleType",RoleType);
                    startActivity(intent);
//                    Intent intent=new Intent(getApplicationContext(), CustomerDashboard.class);

                }
                else {
                    startActivity(new Intent(SplashActivity.this, WelcomActivity.class));
                }



            }
        }, DELAY);

    }

    public void hashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.health.healthservice",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.v("sdhgsvdgsdf", String.valueOf(e));

        } catch (NoSuchAlgorithmException e) {
            Log.v("sdhgsvdgsdff", String.valueOf(e));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    public void AlertVersion() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_ok);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        TextView btnSubmit = dialog.findViewById(R.id.btnSubmit);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        tvMessage.setText(getString(R.string.newVersion));
        btnSubmit.setText(getString(R.string.update));
dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dialog.dismiss();
            }
        });
    }

    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = String.valueOf(packageInfo.versionCode);
            Log.v("vname", versionName + " ," + String.valueOf(versionCode));


            /*
             */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void settingApi() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Versionlistapp();
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result, Toast.LENGTH_LONG).show();
                try {
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        getVersionInfo();
                        if (versionName.equalsIgnoreCase(jsonObject.getString("VersionApp").toString())) {
                            checkLogin();
                        } else {
                            //checkLogin();
                            // checkLogin();
                            AlertVersion();



                            /*checkLogin();*/
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this, call1, "", true);


    }

    public boolean getPermission() {
        List<String> listPermissionsNeedeFor = new ArrayList<>();
        for (String permission : appPermission) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeedeFor.add(permission);

            }
        }
        if (!listPermissionsNeedeFor.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, listPermissionsNeedeFor.toArray(new String[listPermissionsNeedeFor.size()]), permission_granted_code);

            return false;
        }
        settingApi();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permission_granted_code) {
            HashMap<String, Integer> permisionResults = new HashMap<>();
            int deniedPermissionCount = 0;

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permisionResults.put(permissions[i], grantResults[i]);
                    deniedPermissionCount++;
                }
            }
            if (deniedPermissionCount == 0) {
                setInitals();
            } else {
                for (Map.Entry<String, Integer> entry : permisionResults.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permName)) {
                        showAlertDialog(mActivity, "", "Do allow or permission to make application work fine"
                                , "Yes, Grant Permission", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    getPermission();
                                }, "No, Exit app", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();

                                }, false);
                    } else {
                        showAlertDialog(mActivity, "", "You have denied some permissons. Allow all permissions at [Settings] > [Permissions]", "Go to settings"
                                , (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }, "No, Exit app", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();

                                }, false);
                        break;

                    }
                }
            }
        }
    }

    private void setInitals() {
//        memoryBox = new MemoryBox(this);
//        currentVersion = getAppVersion(mActivity);
//        dateFactory = new DateFactory("");
//        if(memoryBox.get(userId).equalsIgnoreCase(""))
//            startActivity(new Intent(mActivity, Login.class));
//        else if(memoryBox.get(userName).equalsIgnoreCase(""))
//            startActivity(new Intent(mActivity, PersonalDetails.class));
//        else

        /*getAppVersionApi();*/
        settingApi();
    }

}