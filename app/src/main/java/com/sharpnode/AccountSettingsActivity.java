package com.sharpnode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.adapter.DialogAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.image.crop.Crop;
import com.sharpnode.model.AccountModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.permissions.PermissionManager;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Constants;
import com.sharpnode.utils.EmailSyntaxChecker;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 11/9/2016.
 */

public class AccountSettingsActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {
    private final String TAG = getClass().getSimpleName();
    protected int imageBitmapSize = 0;
    protected int imageBitmapHeight = 0;
    protected int imageBitmapWidth = 0;
    private Context mContext;
    private String selectedOption = "";
    private List<String> pictureOption;
    private ImageView ivProfilePicture;
    private String prevThumbnail = null;
    private boolean resetToDefaultClicked = false;
    private String pictureByteArray = "";
    //private ActionBar actionBar;
    private Toolbar mToolbar;
    private EditText edtName,edtEmail,edtPhone;
    Button btnSubmit, btnChange;

    private ProgressDialog loader = null;
    String strName,strPhone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        mContext = this;

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.AccountSettings));
        Utils.setTitleFontTypeface(mToolbar);

        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        ivProfilePicture.setOnClickListener(this);
        Bitmap bitmap = Utils.getBitmapFromBase64(AppSPrefs.getString(Commons.PHOTO));
        if(bitmap!=null){
            ivProfilePicture.setImageBitmap(bitmap);
        }else{
            ivProfilePicture.setImageResource(R.drawable.default_profile);
        }
        pictureOption = new ArrayList<>();
        initializeComponents();
    }


    /**
     * Initialize the UI Components.
     */
    public void initializeComponents() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);
        edtName=(EditText)findViewById(R.id.edtName);
        edtEmail=(EditText)findViewById(R.id.edtEmailID);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        btnSubmit.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnChange.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmail.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPhone.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmail.setEnabled(false);
        setModelData();
    }

    public void setModelData()
    {
        edtName.setText(AppSPrefs.getString(Commons.NAME));
        edtEmail.setText(AppSPrefs.getString(Commons.EMAIL));
        edtPhone.setText(AppSPrefs.getString(Commons.PHONE));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivProfilePicture:
                Utils.hideSoftKeyboard(this, ivProfilePicture);
                if (!"".equals(pictureByteArray) || prevThumbnail != null) {
                    if (pictureOption != null) {
                        pictureOption.clear();

                        pictureOption.add(getResources().getString(R.string.ChoosePhotoCapture));
                        pictureOption.add(getResources().getString(R.string.ChoosePhotoGallery));
                        if (!resetToDefaultClicked) {
                            pictureOption.add(getResources().getString(R.string.ChoosePhotoReset));
                        }
                        resetToDefaultClicked = false;
                    }
                } else {
                    if (pictureOption != null) {
                        pictureOption.clear();
                        pictureOption.add(getResources().getString(R.string.ChoosePhotoCapture));
                        pictureOption.add(getResources().getString(R.string.ChoosePhotoGallery));

                    }
                }
                showSelectImageDialog();
                break;
            case R.id.btnSubmit:
                strName=edtName.getText().toString().trim();
                String email=edtEmail.getText().toString().trim();
                strPhone=edtPhone.getText().toString().trim();

                if (!validate()) {
                    return;
                }


                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new Communicator(mContext, null, APIUtils.CMD_UPDATE_ACCOUNT,
                            getUpdateAccountRequestMap(APIUtils.CMD_UPDATE_ACCOUNT,email, strPhone, strName));
                } else {
                    Logger.i(TAG, "Not connected to Internet.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                        }
                    });
                }


                break;
            case R.id.btnChange:
                startActivity(new Intent(mContext, ChangePasswordActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
        }
    }


    public HashMap<String, String> getUpdateAccountRequestMap(String method,String email, String phone, String name) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.EMAIL, email);
        map.put(Commons.PHONE, phone);
        map.put(Commons.NAME, name);
        map.put(Commons.IMAGE, pictureByteArray);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return map;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here
        if (data != null) {
            switch (requestCode) {
                case Constants.REQUEST_FILE_RESULT_CODE:
                    if (data.getData() != null) {
                        String url = data.getData().toString();
                        if (url.startsWith(Constants.GOOGLE_PHOTO_URI)) {
                            //select photo from google photos
                            InputStream is = null;
                            try {
                                is = getContentResolver().openInputStream(Uri.parse(url));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
                            Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                            // start cropping
                            beginToCrop(Uri.parse(Utils.getImageUri(mContext, bmp).toString()));
                        } else {
                            //select photos from gallery and start cropping
                            beginToCrop(Uri.parse(data.getData().toString()));
                        }
                    }
                    break;
                case Crop.REQUEST_CROP:
                    ivProfilePicture.setImageBitmap(Crop.getOutput(data));
                    createByteStringOfBitmap(Crop.getOutput(data));
                    break;
            }
        }

        // in this case data will be null so I write this code outside of the condition
        if (requestCode == Constants.REQUEST_CAMERA_RESULT_CODE && resultCode != 0) {
          /* For issue of
          Uri uri = Utils.getOutputMediaFileUri();
            if (uri != null)
                beginToCrop(uri);*/
            new LoadImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**
     * method to start cropping
     */
    private void beginToCrop(Uri uri) {
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard.getAbsolutePath() + Constants.CROPPED_IMAGE_NAME);
        Uri destination = Uri.fromFile(file);
        Crop.of(uri, destination).asSquare().start(this);
    }

    /**
     * method to create String byte array of bitmap and calculate size, height and width of bitmap
     */
    private void createByteStringOfBitmap(Bitmap imageBitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
            byte[] image = stream.toByteArray();
            pictureByteArray = Base64.encodeToString(image, Base64.DEFAULT);
            // initialize global variable with valid value
            // size of bitmap
            imageBitmapSize = (Utils.sizeOf(imageBitmap)) / 1024; // image size in KB
            //height of image (getting from bitmap)
            imageBitmapHeight = imageBitmap.getHeight();
            //width of image (getting from bitmap)
            imageBitmapWidth = imageBitmap.getWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * to show dialog of photo option list
     */
    public void showSelectImageDialog() {
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView textView = (TextView) view.findViewById(R.id.text_view);
                String clickedAppName = textView.getText().toString();
                Utils.alertDialog.dismiss();
                selectedOption = clickedAppName;
                selectOption(clickedAppName);
            }
        };

        View.OnClickListener cancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.alertDialog.dismiss();
            }
        };
        //cListener to set white background of row after cancel dialog
        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        };
        DialogAdapter adapter = new DialogAdapter(mContext, pictureOption);
        Utils.openDialogSheet(this, adapter, itemClickListener, cancelListener, cancelClickListener);
    }

    /**
     * Method for select option for pick image.
     *
     * @param clickedAppName
     */
    private void selectOption(String clickedAppName) {
        //Checking read external storage permission
        if (PermissionManager.checkPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (clickedAppName.equalsIgnoreCase(getResources().getString(R.string.ChoosePhotoCapture))) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //uri to store capture image in storage
                Uri fileUri = Utils.getOutputMediaFileUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, Constants.REQUEST_CAMERA_RESULT_CODE);
            } else if (clickedAppName.equalsIgnoreCase(getResources().getString(R.string.ChoosePhotoGallery))) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, getResources().getString(R.string.ChoosePhoto)),
                        Constants.REQUEST_FILE_RESULT_CODE);

            } else if (clickedAppName.equalsIgnoreCase(getResources().getString(R.string.ChoosePhotoReset))) {
                pictureByteArray = "";
                resetToDefaultClicked = true;
                /*TextDrawable.builder().beginConfig();
                mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable drawable = mDrawableBuilder.build(employeeModel.getEmployee().getFirstName().toUpperCase().charAt(0) + "" + employeeModel.getEmployee().getLastName().toUpperCase().charAt(0), Utils.getProfileImageColor());
                profileImage.setImageDrawable(drawable);*/
            }
        } else {
            //Here requesting for read permission
            PermissionManager.requestPermission((Activity) mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    PermissionManager.PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionManager.PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectOption(selectedOption);
                    Logger.i(TAG, "Permission Enabled !");
                } else {
                    Logger.i(TAG, "Permission Denied !");
                }
                break;
        }
    }

    @Override
    public void onSuccess(String name, Object object) {
        loader.dismiss();

        try {
            Logger.i(TAG, "Response: " + object);
            if (APIUtils.CMD_UPDATE_ACCOUNT.equalsIgnoreCase(name)) {
                AccountModel model = ResponseParser.parseUpdateAccountResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    AppSPrefs.setString(Commons.PHOTO, pictureByteArray);
                    AppSPrefs.setString(Commons.NAME, strName);
                    AppSPrefs.setString(Commons.PHONE,strPhone);
                    String message=model.getResponseMsg();
                    Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(mContext, model.getResponseMsg(),
                            Toast.LENGTH_LONG).show();
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        loader.dismiss();
        Toast.makeText(mContext, "Profile Setting response Failure", Toast.LENGTH_LONG).show();
    }

    //Thread to LoadImage in background or to solve blank screen issue in sony device.
    public class LoadImage extends AsyncTask<Void, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Uri doInBackground(Void... params) {
            Uri uri = Utils.getOutputMediaFileUri();
            return uri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if (uri != null)
                beginToCrop(uri);
            super.onPostExecute(uri);
        }
    }



    /**
     * This method validate all the required fields.
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String name = edtName.getText().toString();
        //String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();

        if (name.isEmpty()) {
            edtName.setError(getString(R.string.SignUpNameRequired));
            valid = false;
        } else {
            edtName.setError(null);
        }

        /* if (email.isEmpty() || !EmailSyntaxChecker.check(email)) {
            edtEmail.setError(getString(R.string.SignUpEmailRequired));
            valid = false;
        } else {
            edtEmail.setError(null);
        }*/
        if (phone.isEmpty()) {
            edtPhone.setError("Enter a valid phone Number");
            valid = false;
        } else {
            edtName.setError(null);
        }
        return valid;
    }
}
