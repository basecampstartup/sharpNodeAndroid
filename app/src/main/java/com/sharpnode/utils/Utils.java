package com.sharpnode.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.HomeDashboardActivity;
import com.sharpnode.LandingPageActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.SplashActivity;
import com.sharpnode.WebviewOfflineActivity;
import com.sharpnode.commons.Commons;
import com.sharpnode.sprefs.AppSPrefs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 11/9/2016.
 */
public class Utils {

    private final String TAG = getClass().getSimpleName();
    public static AlertDialog alertDialog;
    public static long mLastClickTime = 0;
    public static ProgressDialog loader = null;
    public static int delay05Seconds = 5 * 1000;
    public static int delay30Seconds = 30 * 1000;
    public static int delay45Seconds = 45 * 1000;
    public static int delay60Seconds = 60 * 1000;
    public static String[] arrInterval = {"1-Min", "2-Min", "3-Min", "4-Min", "5-Min", "6-Min", "7-Min", "9-Min", "10-Min",
            "11-Min", "12-Min", "13-Min", "14-Min", "15-Min", "16-Min"
            , "17-Min", "18-Min", "19-Min", "20-Min", "21-Min", "22-Min", "23-Min", "24-Min", "25-Min", "26-Min", "27-Min",
            "28-Min", "29-Min", "30-Min", "31-Min", "32-Min", "33-Min"
            , "34-Min", "35-Min", "36-Min", "37-Min", "38-Min", "39-Min", "40-Min", "41-Min", "42-Min", "43-Min", "44-Min",
            "45-Min", "46-Min", "47-Min", "48-Min", "49-Min", "50-Min", "51-Min",
            "52-Min", "53-Min", "54-Min", "55-Min", "56-Min", "57-Min", "58-Min", "59-Min", "60-Min"};

    public static String[] arrAppliances = {"CFL", "Fan", "Lamp", "TV", "Music", "Washing Machine", "Security"};
    public static String[] arrAppliancesKey = {"switch-one", "switch-two", "switch-three", "switch-four", "switch-five", "switch-six", "switch-seven"};

    public static String[] arrRepeat = {"Everyday", "Weekly", "Monthly", "Yearly", "Never"};

    public static void showLoader(Context mContext, ProgressDialog loader1) {
        loader = loader1;
        loader.setMessage(mContext.getString(R.string.MessagePleaseWait));
        if (!loader.isShowing())
            loader.show();
    }

    public static void dismissLoader() {
        if (loader != null) {
            loader.dismiss();
        }
    }

    public static boolean multipleTapDelayLONG(){
        //This will check if your click on button successively.
        if (SystemClock.elapsedRealtime() - Utils.mLastClickTime < Commons.DELAY_LONG) {
            Log.i("Utils", "multipleTapDelayLONG: "+true);
            return true;
        }
        Utils.mLastClickTime = SystemClock.elapsedRealtime();
        Log.i("Utils", "multipleTapDelayLONG: "+false);
        return false;
    }

    public static boolean multipleTapDelaySHORT(){
        //This will check if your click on button successively.
        if (SystemClock.elapsedRealtime() - Utils.mLastClickTime < Commons.DELAY_SHORT) {
            Log.i("Utils", "multipleTapDelaySHORT: "+true);
            return true;
        }
        Utils.mLastClickTime = SystemClock.elapsedRealtime();
        Log.i("Utils", "multipleTapDelaySHORT: "+false);
        return false;
    }

    /**
     * dialog sheet of contacts/set photo option
     *
     * @param context
     * @param adapter
     * @param itemClickListener
     * @param cancelListener
     * @param cancelClickListener
     */
    public static void openDialogSheet(Context context, BaseAdapter adapter, AdapterView.OnItemClickListener itemClickListener,
                                       DialogInterface.OnCancelListener cancelListener, View.OnClickListener cancelClickListener) {

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_list, null);
        ListView listView = (ListView) view.findViewById(R.id.list);
        TextView cancel = (TextView) view.findViewById(R.id.id_cancel_text);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListener);
        cancel.setOnClickListener(cancelClickListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnCancelListener(cancelListener);
        Utils.alertDialog = alertDialog;

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        //wlp.windowAnimations = R.style.PauseDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        alertDialog.show();
    }

    /**
     * Method for hide soft keyboard forcefully.
     *
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        try {
            if (view != null) {
                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method to get the size of bitmap
     *
     * @param data
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return data.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getByteCount();
        } else {
            return data.getRowBytes() * data.getHeight();
        }
    }

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), SNApplication.snApp.getString(R.string.app_name));
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("Utils", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + SNApplication.snApp.getString(R.string.app_name) + ".jpg");

        return mediaFile;
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFileNewPost() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), SNApplication.snApp.getString(R.string.app_name));
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("Utils", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpeg");

        return mediaFile;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        String path;
        if (inImage != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    /**
     * @param context context
     * @param fontId  fontid
     * @return Typeface
     * This method will return the font which apply on application text.
     */
    public static Typeface getTypeface(Context context, int fontId) {
        Typeface typeface = null;
        switch (fontId) {
            case 1:
                typeface = Typeface.createFromAsset(context.getAssets(), "avenir-roman.otf");
                break;
        }
        return typeface;
    }


    /*
     * Sets the font on all TextViews in the ViewGroup. Searches
     * recursively for all inner ViewGroups as well. Just add a
     * check for any other views you want to set as well (EditText,
     * etc.)
     */
    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView || v instanceof Button /*etc.*/)
                ((TextView)v).setTypeface(font);
            else if(v instanceof ViewGroup)
                setFont((ViewGroup)v, font);
        }
    }

    /**
     * This method is to hide android soft keyboard.
     * @param context
     * @param view
     */
    public static void hideSoftKeyboardWithoutReq(Context context, View view) {
        try {
            if (view != null) {
                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }



    /**
     * Method to return formatted time according to locale
     *
     * @param context
     * @param mDate
     * @param timeStyle
     * @return
     */
    public static String formatTime(Context context, Date mDate, TimeStyleEnum.StyleType timeStyle) {
        switch (timeStyle) {
            case SHORT:
//                DateFormat shortTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, getSysLocale(context));
//                return shortTimeFormat.format(mDate);
                SimpleDateFormat sdf = new SimpleDateFormat(Utils.getTimeFormat(context));
                return sdf.format(mDate);
            case LONG:
                DateFormat longTimeFormat = DateFormat.getTimeInstance(DateFormat.LONG, getSysLocale(context));
                return longTimeFormat.format(mDate);
            default:
                return "";

        }
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getTimeFormat(Context context) {
        Format systemTimeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return ((SimpleDateFormat) systemTimeFormat).toLocalizedPattern();
    }

    /**
     * method to return system locale
     *
     * @return
     */
    private static Locale getSysLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }


    public static void glowIcon(ImageView imageView){
        // An added margin to the initial image
        int margin = 24;
        int halfMargin = margin / 2;

        // the glow radius
        int glowRadius = 16;

        // the glow color
        int glowColor = Color.rgb(255,165,0);

        // The original image to use
        Bitmap src = null;
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                src = bitmapDrawable.getBitmap();
            }
        }
        // extract the alpha from the source image
        Bitmap alpha = src.extractAlpha();

        // The output bitmap (with the icon + glow)
        Bitmap bmp = Bitmap.createBitmap(src.getWidth() + 0,
                src.getHeight() + 0, Bitmap.Config.ARGB_8888);

        // The canvas to paint on the image
        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint();
        paint.setColor(glowColor);

        // outer glow
        paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER));
        canvas.drawBitmap(alpha, 0, 0, paint);

        // original icon
        canvas.drawBitmap(src, 0, 0, null);

        imageView.setImageBitmap(bmp);
    }

    public static void logoutFromApp(final Activity activity){
        AppSPrefs.clearAppSPrefs();
        Intent intent = new Intent(activity.getApplicationContext(), LandingPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        //activity.overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        //activity.finish();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        }, 1000);*/
    }

    public static void exitFromApp(Activity activity){
        /*activity.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(intent);*/
        activity.moveTaskToBack(true);
    }

    public static void setTitleFontTypeface(Toolbar mToolbar){
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateContactUs(String name, String email, String phone, String message) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SNApplication.snApp, "Enter your name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(SNApplication.snApp, "Enter your email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(SNApplication.snApp, "Enter your phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(message)) {
            Toast.makeText(SNApplication.snApp, "Enter your message", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static void okAlertDialog(Context mContext, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void offlineDeviceAlertDialog(final Context mContext, String title, String message, final String lastIP) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("Offline",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, WebviewOfflineActivity.class);
                        intent.putExtra("LAST_IP", lastIP);
                        ((Activity)mContext).startActivity(intent);
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static Bitmap getBitmapFromBase64(String base64Data){
        Bitmap bitmap = null;
        try{
            byte[] imageAsBytes = Base64.decode(base64Data.getBytes(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
