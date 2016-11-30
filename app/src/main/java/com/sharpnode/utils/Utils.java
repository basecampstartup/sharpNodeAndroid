package com.sharpnode.utils;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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

import com.sharpnode.LandingPageActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.sprefs.AppSPrefs;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        activity.finish();
        Intent intent = new Intent(activity, LandingPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void exitFromApp(Activity activity){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
