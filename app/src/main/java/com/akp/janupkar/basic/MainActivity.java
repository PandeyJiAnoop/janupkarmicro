package com.akp.janupkar.basic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.akp.janupkar.AllRegNewAPI.RegistrtionForm;
import com.akp.janupkar.EMIDueReport;
import com.akp.janupkar.R;
import com.akp.janupkar.a_webview.LoanApprovedList;
import com.akp.janupkar.a_webview.LoanLUCReportWebview;
import com.akp.janupkar.a_webview.LoanRepaymentPageAdd;
import com.akp.janupkar.aadharcibil.AadharVerifyWebview;
import com.akp.janupkar.aadharcibil.CibilVerifyWebview;
import com.akp.janupkar.common.GetBackFragment;
import com.akp.janupkar.fragment.CalenderFragment;
import com.akp.janupkar.fragment.CommentFragment;
import com.akp.janupkar.membershipregistration.MemberHousehold;
import com.akp.janupkar.membershipregistration.MemberHouseholdSecondReport;
import com.akp.janupkar.membershipregistration.MemberLoanAppraisal;
import com.akp.janupkar.membershipregistration.MemberLoanAppraisalReport;
import com.akp.janupkar.membershipregistration.MemberShip;
import com.akp.janupkar.membershipregistration.MemberShipDetailsActivity;
import com.akp.janupkar.membershipregistration.MemberloanLUC;
import com.akp.janupkar.utils.AppUtils;
import com.akp.janupkar.view.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    ScrollView scrollView;
    DrawerLayout mDrawerLayout;
    Handler mhandler;
    ImageView ivMenu, ivLogout, ivImageone;
    RelativeLayout rlNewRegistration, rlDashBoard, rlLogout, rlProfile,rlMemberDetails,rlDacument,rlMemberloanLUC,rlMemberHousehold,luc_rl;
    private Fragment fragment;
    LinearLayout ll;
    RelativeLayout rlMemberloanApprisalReport,rlMemberloanApprisal,rlMemberHouseholdReport,rlLoanRepayment,rlMemberMembership,rlMemberMembershipListDetail,duereport_rl,demandseet_rl,rlaadharverify,rlcibilverify;
    private SharedPreferences sharedPreferences;
    TextView tvName;
    String UserName,Mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerleft);
        findViewById();
        setListner();
    }

    private void setListner() {
        ivMenu.setOnClickListener(this);
        rlDacument.setOnClickListener(this);

        rlNewRegistration.setOnClickListener(this);
        ll.setOnClickListener(this);
        rlDashBoard.setOnClickListener(this);
        rlLogout.setOnClickListener(this);

        ivImageone.setOnClickListener(this);
        rlMemberDetails.setOnClickListener(this);
        rlLoanRepayment.setOnClickListener(this);
        rlMemberMembership.setOnClickListener(this);
        rlMemberMembershipListDetail.setOnClickListener(this);
        duereport_rl.setOnClickListener(this);
        demandseet_rl.setOnClickListener(this);
        rlMemberloanLUC.setOnClickListener(this);
        rlMemberHousehold.setOnClickListener(this);
        rlMemberHouseholdReport.setOnClickListener(this);
        rlaadharverify.setOnClickListener(this);
        rlcibilverify.setOnClickListener(this);
        rlMemberloanApprisal.setOnClickListener(this);
        rlMemberloanApprisalReport.setOnClickListener(this);
        luc_rl.setOnClickListener(this);
    }

    private void findViewById() {
        //ScrollView
        luc_rl = findViewById(R.id.luc_rl);
        scrollView = findViewById(R.id.scroll_side_menu);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        ivMenu = findViewById(R.id.ivMenu);
        tvName = findViewById(R.id.tvName);
        rlDacument = findViewById(R.id.rlDacument);
        rlMemberloanLUC = findViewById(R.id.rlMemberloanLUC);
        rlMemberHousehold = findViewById(R.id.rlMemberHousehold);
        rlNewRegistration = findViewById(R.id.rlNewRegistration);
        ll = findViewById(R.id.ll);
        rlDashBoard = findViewById(R.id.rlDashBoard);
        rlLogout = findViewById(R.id.rlLogout);
        rlProfile = findViewById(R.id.rlProfile);
        ivImageone = findViewById(R.id.ivImageone);
        rlLoanRepayment = findViewById(R.id.rlLoanRepayment);
        rlMemberDetails = findViewById(R.id.rlMemberDetails);
        rlMemberMembership = findViewById(R.id.rlMemberMembership);
        rlMemberMembershipListDetail = findViewById(R.id.rlMemberMembershipListDetail);
        duereport_rl = findViewById(R.id.duereport_rl);
        demandseet_rl = findViewById(R.id.demandseet_rl);
        rlMemberHouseholdReport= findViewById(R.id.rlMemberHouseholdReport);
        rlaadharverify = findViewById(R.id.rlaadharverify);
        rlcibilverify = findViewById(R.id.rlcibilverify);
        rlMemberloanApprisal= findViewById(R.id.rlMemberloanApprisal);
        rlMemberloanApprisalReport= findViewById(R.id.rlMemberloanApprisalReport);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Mobile = sharedPreferences.getString("UserId", "");
        UserName = sharedPreferences.getString("UserName", "");
        tvName.setText("+91 "+Mobile);
        mhandler = new Handler();
        displayView(1);
        closeDrawer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlLogout:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage(getString(R.string.aresurelogout));
                alertDialogBuilder.setPositiveButton(getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent I = new Intent(MainActivity.this, SplashActivity.class);
                                startActivity(I);
                                arg0.dismiss();
                                onResume();
                            }});
                alertDialogBuilder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.rlDacument:
                startActivity(new Intent(mActivity, LoanRepaymentPageAdd.class));
                break;
            case R.id.ivImageone:
                displayView(3);
                break;
            case R.id.rlNewRegistration:
                startActivity(new Intent(mActivity, RegistrtionForm.class));
//                startActivity(new Intent(mActivity, RegistrationPageAdd.class));
                break;
            case R.id.rlDashBoard:
                displayView(2);
                break;
            case R.id.ll:
                displayView(1);
                break;
            case R.id.ivMenu:
                openDrawer();
                break;
            case R.id.rlMemberDetails:
//                startActivity(new Intent(mActivity, MemberLoanDetailsActivity.class));
                startActivity(new Intent(mActivity, LoanApprovedList.class));
                break;
            case R.id.rlLoanRepayment:
                startActivity(new Intent(mActivity, RepaymentListActivity.class));
                break;
            case R.id.rlMemberMembership:
                startActivity(new Intent(mActivity, MemberShip.class));
                break;
            case R.id.rlMemberMembershipListDetail:
                startActivity(new Intent(mActivity, MemberShipDetailsActivity.class));
                break;
            case R.id.duereport_rl:
                startActivity(new Intent(mActivity, EMIDueReport.class));
                break;
            case R.id.demandseet_rl:
                startActivity(new Intent(mActivity, CollectionDemandSeet.class));
                break;
            case R.id.rlMemberHousehold:
                startActivity(new Intent(mActivity, MemberHousehold.class));
                break;

            case R.id.rlMemberloanApprisal:
                startActivity(new Intent(mActivity, MemberLoanAppraisal.class));
                break;
            case R.id.rlMemberloanApprisalReport:
                startActivity(new Intent(mActivity, MemberLoanAppraisalReport.class));
                break;
            case R.id.rlMemberloanLUC:
                startActivity(new Intent(mActivity, MemberloanLUC.class));
                break;
            case R.id.rlaadharverify:
                startActivity(new Intent(mActivity, AadharVerifyWebview.class));
                break;
            case R.id.rlcibilverify:
                startActivity(new Intent(mActivity, CibilVerifyWebview.class));
                break;
            case R.id.rlMemberHouseholdReport:
                startActivity(new Intent(mActivity, MemberHouseholdSecondReport.class));
                break;
            case R.id.luc_rl:
                startActivity(new Intent(mActivity, LoanLUCReportWebview.class));
                break;
        }}
    public void openDrawer() {
        mDrawerLayout.openDrawer(scrollView);
    }
    //closeDrawer
    public void closeDrawer() {
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(scrollView);
            }
        }, 100);
    }
    private void displayView(int position) {
        switch (position) {
            case 1:
                //   hitFilterApi();
                GetBackFragment.Addpos(position);
                fragment = new CalenderFragment();
                break;
            case 2:
                //   hitFilterApi();
                GetBackFragment.Addpos(position);
                fragment = new CalenderFragment();
                closeDrawer();
                break;
            case 3:
                //   hitFilterApi();
                GetBackFragment.Addpos(position);
                fragment = new CommentFragment();
                break;
            default:
                break;
        }


        if (fragment != null) {
            AppUtils.hideSoftKeyboard(mActivity);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(fragment.toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction tx = fragmentManager.beginTransaction();
            // tx.add(R.id.container,fragment).addToBackStack(fragment.toString());
            tx.replace(R.id.container, fragment).addToBackStack(fragment.toString());
            tx.commit();
            // ====to clear unused memory==
            System.gc();
        } else {
            // error in creating fragment
            Log.e("ImageDataActivity", "Error in creating fragment");
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}