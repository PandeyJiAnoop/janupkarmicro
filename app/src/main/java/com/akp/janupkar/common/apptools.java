package com.akp.janupkar.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.akp.janupkar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class apptools {
    public static final String appError = "Something went wrong";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static Toast mToast;
    static ProgressDialog progressDialog;
    static Dialog dialog;
    private static boolean doubleBackToExitPressedOnce;

    //****************Log****************************************
    public static void setLog(String title, String message) {
        try {
            Log.v(title, " " + message);
        } catch (Exception e) {
            handleCatch(e);
        }
    }

    public static void setLog(String title, String message, Throwable throwable) {
        try {
            Log.v(title, " " + message, throwable);
        } catch (Exception e) {
            handleCatch(e);
        }
    }

    public static void printMessage(String message) {
        System.out.println("Message :  " + message);
    }

    //****************Log****************************************
    //*******************************Youtube****************************************
    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        return "http://img.youtube.com/vi/" + getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg";
    }

    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        inUrl = inUrl.replace("&feature=youtu.be", "");
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    //*******************************Youtube****************************************
    //*****************************TimeSlots****************************************
    public static int checkTimeSlot() {
        try {
            Calendar c = Calendar.getInstance();
            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
            if (timeOfDay >= 0 && timeOfDay < 12) {
                return 1;
            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                return 2;
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                return 3;
            } else if (timeOfDay >= 21 && timeOfDay < 24) {
                return 4;
            }
        } catch (Exception e) {
            handleCatch(e);
        }
        return 0;
    }

    public static String checkTimeMessage(int code) {
        try {
            switch (code) {
                case 1:
                    return "Good Morning";
                case 2:
                    return "Good Afternoon";
                case 3:
                    return "Good Evening";
                case 4:
                    return "Good Night";
            }
        } catch (Exception e) {
            handleCatch(e);
        }
        return "error";
    }

    //*****************************TimeSlots****************************************
    //*****************************WhatsApp****************************************
    public static void openWhatsApp(Context context, String message, String reciverNumber) {
        try {
            if (apptools.isValidPhone(reciverNumber)) {
                String text = message;// Replace with your message.
                String toNumber = "91" + reciverNumber; // Replace with mobile phone number without +Sign or leading zeros, but with country akp
                //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                context.startActivity(intent);
            } else {
                showToast(context, "Phone number is invalid");
            }
        } catch (Exception e) {
            apptools.handleCatch(e);
        }
    }

    public static void openWhatsApp(Context context, String message) {
        PackageManager pm = context.getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = message; // Replace with your own message.
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then akp
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            handleCatch(e, context, "WhatsApp not Installed");
        }
    }

    //*****************************WhatsApp****************************************
    //*****************************Json****************************************
    public static String retrieveJSONString(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) ? jsonObject.get(key).toString() : "";
        } catch (JSONException e) {
            handleCatch(e);
        }
        return "";
    }

    public static String retrieveJSONString(JSONObject jsonObject, String key, String defaultValue) {
        try {
            return jsonObject.has(key) ? jsonObject.get(key).toString() : defaultValue;
        } catch (JSONException e) {
            handleCatch(e);
        }
        return defaultValue;
    }

    public static JSONObject retrieveJSONObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) ? jsonObject.getJSONObject(key) : null;
        } catch (JSONException e) {
            handleCatch(e);
        }
        return null;
    }

    public static JSONArray retrieveJSONArray(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) ? jsonObject.getJSONArray(key) : null;
        } catch (JSONException e) {
            handleCatch(e);
        }
        return null;
    }

    //*****************************Json****************************************
    //*****************************Handle Catch****************************************
    public static void handleCatch(Exception e) {
        e.printStackTrace();
    }

    public static void handleCatch(Exception e, Context context) {
        e.printStackTrace();
        showToast(context, appError);
    }

    public static void handleCatch(Exception e, Context context, String errorMessage) {
        e.printStackTrace();
        showToast(context, errorMessage);
    }

    //*****************************Handle Catch****************************************
    //*****************************Format Number****************************************
    public static String showDoubleDigit(int digit) {
        String finalDig = (digit < 10 ? "0" : "") + digit;
        return finalDig;
    }

    public static String showDoubleDigit(long digit) {
        String finalDig = (digit < 10 ? "0" : "") + digit;
        return finalDig;
    }

    public static String setPrice(String price) {
        return "₹ " + price;
    }

    public static String setPriceTotal(String price) {
        return "₹ " + price + " /-";
    }

    public static String setPriceTotal(int price) {
        return "₹ " + price + " /-";
    }

    public static String updateQuantity(String oldQuantity, int newQuantity) {
        if ((oldQuantity.equalsIgnoreCase("0") || oldQuantity.equalsIgnoreCase("")) && newQuantity <= 0)
            return "0";
        return Integer.toString(Integer.parseInt(oldQuantity) + newQuantity);
    }

    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void setSpannableColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i,
                i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    //*****************************Format Number****************************************
    //*****************************Back To Exit****************************************
    @SuppressLint("NewApi")
    public static void backToExit(Activity mActivity) {
        if (doubleBackToExitPressedOnce) {
            mActivity.finishAffinity();
            return;
        }
        doubleBackToExitPressedOnce = true;
        showToast(mActivity, "Press again to exit");
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);
    }

    @SuppressLint("NewApi")
    public static void backToExit(Activity mActivity, String message) {
        if (doubleBackToExitPressedOnce) {
            mActivity.finishAffinity();
            return;
        }
        doubleBackToExitPressedOnce = true;
        showToast(mActivity, message);
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);
    }

    //*****************************Back To Exit****************************************


    public static AlertDialog showAlertDialog(Context context, String title, String message, String positiveLabel,
                                              DialogInterface.OnClickListener positiveClick, String negativeLabel,
                                              DialogInterface.OnClickListener negativeClick, boolean isCancelable) {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle(title);
            dialogBuilder.setCancelable(isCancelable);
            dialogBuilder.setMessage(message);
            dialogBuilder.setPositiveButton(positiveLabel, positiveClick);
            dialogBuilder.setNegativeButton(negativeLabel, negativeClick);
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            return alertDialog;
        } catch (Exception e) {
            handleCatch(e);
        }
        return null;
    }


    public static void copyText(String text, Context context, String message) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text label", text);
            clipboard.setPrimaryClip(clip);
            showToast(context, message);
        } catch (Exception e) {
            handleCatch(e, context, "Unable to copy text");
        }
    }


    public static void showToast(Context context, String text) {
        try {
            if (context == null)
                return;
            if (mToast != null && mToast.getView().isShown()) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            mToast.show();
        } catch (Exception e) {
            handleCatch(e);
        }
    }


    public static void shareApplication(Context context, String appName) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "*" + appName + " App" + "*" + "\n" + "Hi There!\n" +
                "Download the " + appName + " app and register yourself. \n" +
                "Download link - https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n" +
                "Hava a nice day!\n" +
                appName + " Operation Team");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void rateApplication(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
    }

    public static void shareToBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);

    }


    public static boolean isEmailValid(String email) {
        try {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            handleCatch(e);
        }
        return false;
    }

    public static String getTextInputEditTextData(TextInputEditText textInputEditText) {
        try {
            return textInputEditText.getText().toString().trim();
        } catch (Exception e) {
            handleCatch(e);
        }
        return "";
    }

    public static String getTextInputEditTextData(EditText textInputEditText) {
        try {
            return textInputEditText.getText().toString().trim();
        } catch (Exception e) {
            handleCatch(e);
        }
        return "";
    }

    public static boolean isValidPhone(String phone) {
        try {
            return phone != null && phone.length() == 10;
        } catch (Exception e) {
            handleCatch(e);
        }
        return false;
    }

    public static void showRequestDialog(Activity activity) {
        try {
            if (!activity.isFinishing()) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(activity.getString(R.string.please_wait));
                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
            handleCatch(e);
        }
    }

    public static void showRequestDialog(Activity activity, String message) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(message);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.show();
            }
        } catch (Exception e) {
            handleCatch(e);
        }
    }

    public static void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static String toCamelCaseSentence(String sentence) {
        try {
            if (sentence == null) {
                return "";
            }
            String[] words = sentence.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for (String toCamelCaseWord : words) {
                stringBuilder.append(toCamelCaseWord(toCamelCaseWord));
            }
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            handleCatch(e);
        }
        return "";
    }

    public static String toCamelCaseWord(String word) {
        try {
            if (word == null) {
                return "";
            }
            switch (word.length()) {
                case 0:
                    return "";
                case 1:
                    return word.toUpperCase(Locale.getDefault()) + " ";
                default:
                    return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase(Locale.getDefault()) + " ";
            }
        } catch (Exception e) {
            handleCatch(e);
        }
        return "";
    }


    public static String getAppVersion(Activity context) {
        String latestVersion = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            latestVersion = String.valueOf(info.versionName);
        } catch (Exception e) {
            handleCatch(e, context);
        }
        return latestVersion;
    }

    public static void callToDial(String phoneNumber, Context context) {
        if (apptools.isValidPhone(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        } else {
            showToast(context, "Phone number is invalid");
        }
    }

    @SuppressLint("MissingPermission")
    public static String getUniqueIMEIId(Context context) {
        String myuniqueID = "";
        int myversion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (myversion < 23) {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            myuniqueID = info.getMacAddress();
            if (myuniqueID == null) {
                TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "Permission not granted";
                }
                myuniqueID = mngr.getDeviceId();
            }
        } else if (myversion > 23 && myversion < 29) {
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "Permission not granted";
            }
            myuniqueID = mngr.getDeviceId();
        } else {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            myuniqueID = androidId;
        }
        return myuniqueID;
    }

}
