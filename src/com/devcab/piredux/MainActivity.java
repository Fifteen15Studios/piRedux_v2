package com.devcab.piredux;

//Import Android OS features
import android.os.*;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

//Import Android Application features
import android.app.Activity;
import android.app.AlertDialog;

//Import Android content features
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

//Import Android Database
import android.database.Cursor;

//Import Bitmap Info
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

//Import Android IO features
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//Import Language features
import java.lang.Math;
import java.lang.String;

//Import Java Math
import java.math.BigDecimal;

//Import Android URI
import android.net.Uri;

//Import Android Log feature
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//Import Android View Features
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

//Import Android Widget Features
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    //-----constants-----
    protected static final String SETTING_WIDTH = "imageWidth";
    protected static final String SETTING_HEIGHT = "imageHeight";
    protected static final String SETTING_QUALITY = "imageQuality";
    protected static final String SETTING_ASPECT_RATIO = "retainAspectRatio";
    protected static final String SETTING_NUMBER_OF_IMAGES = "numberOfImages";

    //Default Values
    protected static final int DEFAULT_QUALITY = 50;

    protected static final String SELECTED_BUTTON = "selectedButton";

    //Final int RADIO_BUTTON Selector
    /*protected static final int RADIO_BUTTON_1 = 1;
    protected static final int RADIO_BUTTON_2 = 2;
    protected static final int RADIO_BUTTON_3 = 3;
    protected static final int RADIO_BUTTON_4 = 4;*/

    //Final int
    private final int RQS_LOADMULTIPLEIMAGE = 10;
    private final int RQS_LOADIMAGE = 0;
    private final int RQS_SENDEMAIL = 1;
    private final int FINISHED_CONVERSION = 100;

    //standard values for radio buttons
    protected static final int BUTTON_MOBILE_WIDTH = 320;
    protected static final int BUTTON_MOBILE_HEIGHT = 480;
    protected static final int BUTTON_TABLET_WIDTH = 1024;
    protected static final int BUTTON_TABLET_HEIGHT = 768;
    protected static final int BUTTON_HD_WIDTH = 1920;
    protected static final int BUTTON_HD_HEIGHT = 1080;

    //Android URI
    private android.net.Uri androidURI;

    //features for Images
    private Button btnAddFile;
    private Button btnResizeSettings;
    private Button btnResizePhotos;

    //TextView for Display
    private TextView displayTextView;
    private TextView numPhotoTextView;
    private TextView totalSizeTextView;

    private ListView photoView;

    private Boolean loopFinished = true;

    //ArrayList URI
    private ArrayList<Uri> arrayUri = new ArrayList<Uri>();
    private ArrayList<String> sizeArrayList = new ArrayList<String>();
    private ArrayList<Double> sizeArrayInByte = new ArrayList<Double>();
    private ArrayList<Double> sizeArrayInKB = new ArrayList<Double>();
    private ArrayList<Double> sizeArrayInMB = new ArrayList<Double>();
    private ArrayList<Integer> sizeArrayWidth = new ArrayList<Integer>();
    private ArrayList<Integer> sizeArrayHeight = new ArrayList<Integer>();
    private ArrayList<String> sizeArrayMimeType = new ArrayList<String>();
    private double totalSizeInMB = 0.00;
    private int numOfPicsSelected = 0;

    private int identification_counter = 0, identification_ender = 0;
    private int setSampleScale = 0;
    private int imageRotation = 0, finalRotation = 0;

    //Global variable for Context
    private final Context context = this;

    //int radio button selection
    private int selectedRadioButton = 3;

    private int qualityCompressValue = 100;

    //Bitmap Factory Options Global
    private BitmapFactory.Options bounds;
    private Bitmap tempMap;
    private Bitmap rescaledBitmap;
    private Bitmap fixRotationBitmap;

    //String radio button selected String output
    private String selectedRadioButtonString = "HD (1920px x 1080px)";
    private int aspectRatioWidth = 0;
    private int aspectRatioHeight = 0;

    //int selected width and height (only will be used if selectedRadioButton equals to 4
    private int selectedWidth = 1;
    private int selectedHeight = 1;

    //Global Variables for duplicate files
    private int duplicateImage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create Bundle package (required before anything else)
        super.onCreate(savedInstanceState);
        //display Content View (required before anything else)
        setContentView(R.layout.activity_main);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = settings.edit();

        //set default settings if no settings are found
        if (settings.getInt(SETTING_HEIGHT, -1) == -1) {
            editor.putInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);
            editor.apply();
        }
        if (settings.getInt(SETTING_WIDTH, -1) == -1) {
            editor.putInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
            editor.apply();
        }

        //Intent features (necessary features needed to go to another layout)
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();

        //Functions that initialize the app

        //Initialize layout elements
        btnAddFile = (Button) findViewById(R.id.addphoto);
        btnResizeSettings = (Button) findViewById(R.id.setResizeSettings);
        btnResizePhotos = (Button) findViewById(R.id.resizePhotos);
        displayTextView = (TextView) findViewById(R.id.displayView);
        numPhotoTextView = (TextView) findViewById(R.id.numPhotosView);
        totalSizeTextView = (TextView) findViewById(R.id.totalSizeView);
        photoView = (ListView)findViewById(R.id.photoList);

        btnResizeSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //SharedPreferences.Editor editor = settings.edit();

                //Create first intent to call resizeSettings
                Intent request = new Intent(MainActivity.this, resizeSettings.class);
                //request.putExtra("selectedRadioButton", selectedRadioButton);
                //request.putExtra("selectedRadioButtonString", selectedRadioButtonString);

                //editor.putInt(SETTING_WIDTH, selectedWidth);
                //request.putExtra("selectedWidth", selectedWidth);
                //editor.putInt(SETTING_HEIGHT, selectedHeight);
                //request.putExtra("selectedHeight", selectedHeight);
                //editor.putBoolean(SETTING_ASPECT_RATIO, aspectRatioSelected);
                //request.putExtra("aspectRatioSelected", aspectRatioSelected);
                //editor.putInt(SETTING_QUALITY, qualityCompressValue);
                //request.putExtra("qualityCompressValue", qualityCompressValue);

                //editor.apply();

                //Start new Resizing Settings and wait for its results
                startActivityForResult(request, selectedRadioButton);
            }
        });

        btnResizePhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startCompressingImages();
            }
        });

        btnAddFile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent
                        (
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        );
                startActivityForResult(intent, RQS_LOADIMAGE);
            }
        });

        //setAllButtonsVisible();


        //if ACTION_SEND_MULTIPLE is selected from the menu
        if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            if (extras != null && extras.containsKey(Intent.EXTRA_STREAM)) {
                ArrayList<Parcelable> list = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                for (Parcelable p : list) {
                    Uri imageUri = (Uri) p;
                    getFileSize(imageUri);
                    /*arrayUri.add(imageUri);
                    long fileSize = 0;
                    String fileSizeString = getImageSize(imageUri);
                    long fileSizeLong = Long.parseLong(fileSizeString);
                    fileSize = Integer.parseInt(fileSizeString);

                    double fileSizeInByte = Double.parseDouble(fileSizeString);
                    double fileSizeInKB = fileSizeLong / 1024;
                    double fileSizeInMB = fileSizeInKB / 1024;
                    double fileSizeInMBPrecision = roundMB(fileSizeInMB, 2, BigDecimal.ROUND_HALF_UP);
                    fileSizeString = Double.toString(fileSizeInMBPrecision);
                    if (fileSize == 0) {
                        sizeArrayList.add("N/A");
                        sizeArrayInByte.add(0.00);
                        sizeArrayInKB.add(0.00);
                        sizeArrayInMB.add(0.00);
                    } else {
                        sizeArrayList.add(fileSizeString + " MB");
                        sizeArrayInByte.add(fileSizeInByte);
                        sizeArrayInKB.add(fileSizeInKB);
                        sizeArrayInMB.add(fileSizeInMB);
                    }
                    totalSizeInMB += fileSizeInMBPrecision;*/
                }
                //updateNumPhotoTotalSizeViews(numOfPicsSelected, totalSizeInMB);
            }
        }
        // if this is from the share menu
        else if (Intent.ACTION_SEND.equals(action)) {
            if (extras != null && extras.containsKey(Intent.EXTRA_STREAM)) {
                try {
                    // Get resource path from intent called
                    Uri imageUri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                    getFileSize(imageUri);
                    /*arrayUri.add(imageUri);
                    long fileSize = 0;
                    String fileSizeString = getImageSize(imageUri);
                    long fileSizeLong = Long.parseLong(fileSizeString);
                    fileSize = Integer.parseInt(fileSizeString);

                    double fileSizeInByte = Double.parseDouble(fileSizeString);
                    double fileSizeInKB = fileSizeLong / 1024;
                    double fileSizeInMB = fileSizeInKB / 1024;
                    double fileSizeInMBPrecision = roundMB(fileSizeInMB, 2, BigDecimal.ROUND_HALF_UP);
                    fileSizeString = Double.toString(fileSizeInMBPrecision);
                    if (fileSize == 0) {
                        sizeArrayList.add("N/A");
                        sizeArrayInByte.add(0.00);
                        sizeArrayInKB.add(0.00);
                        sizeArrayInMB.add(0.00);
                    } else {
                        sizeArrayList.add(fileSizeString + " MB");
                        sizeArrayInByte.add(fileSizeInByte);
                        sizeArrayInKB.add(fileSizeInKB);
                        sizeArrayInMB.add(fileSizeInMB);
                    }
                    totalSizeInMB += fileSizeInMBPrecision;
                    numOfPicsSelected = arrayUri.size();
                    updateNumPhotoTotalSizeViews(numOfPicsSelected, totalSizeInMB);*/
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), e.toString());
                }

            }
        } else {
            //Function that sets all buttons visible
            setAllButtonsVisible();
        }
    }

    public void updateNumPhotoTotalSizeViews(int _numberOfPhotos, double _totalPhotoSizes) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(SETTING_NUMBER_OF_IMAGES, _numberOfPhotos);
        editor.apply();

        numPhotoTextView.setText(Integer.toString(_numberOfPhotos) + " photo(s)");
        //totalSizeTextView.setText((new DecimalFormat("##.##").format(_totalPhotoSizes)) + " MB");
        totalSizeTextView.setText(String.format("%.2f MB", _totalPhotoSizes));
        displayTextView.setText("Loaded 0/" + Integer.toString(numOfPicsSelected));
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void startCompressingImages() {
        if (arrayUri.size() == 0) {
            //Your code here
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set title
            alertDialogBuilder.setTitle("Custom filled but no width nor height filled");

            // set dialog message
            alertDialogBuilder
                    .setMessage("You have selected no photos. Would you like to choose from the gallery?")
                    .setCancelable(true)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            dialog.cancel();
                            Intent intent = new Intent
                                    (
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    );
                            startActivityForResult(intent, RQS_LOADIMAGE);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        } else {

            //disable all buttons until process is complete
            disableAllButtons();

            //call and assign the id counter and the id ender
            assignIDTasks();

            //call and assign the setDesired Width and Height of the newly created image(s)
            setDesiredDimensions();

            //call to assign the proper scaling
            setProperScalingForFirstImage();

            //call and assign the proper image width and height pending aspect ratio
            assignProperDimensionsForAllImages();

            //first time calling the asynchronous task
            new ImageCompressingTask().execute();
        }
    }

    public void assignProperDimensionsForAllImages() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        if (settings.getBoolean(SETTING_ASPECT_RATIO, true)) {
            editor.putInt(SETTING_WIDTH, aspectRatioWidth);
            editor.putInt(SETTING_HEIGHT, aspectRatioHeight);
        }
    }

    public void setProperScalingForFirstImage() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        setSampleScale = getSampleSize(sizeArrayWidth.get(0),
                sizeArrayHeight.get(0));
    }

    //todo: needed?
    public void setDesiredDimensions() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();

        switch (selectedRadioButton) {
            case 1:
                editor.putInt(SETTING_WIDTH, BUTTON_MOBILE_WIDTH);
                editor.putInt(SETTING_HEIGHT, BUTTON_MOBILE_HEIGHT);
                break;
            case 2:
                editor.putInt(SETTING_WIDTH, BUTTON_TABLET_WIDTH);
                editor.putInt(SETTING_HEIGHT, BUTTON_TABLET_HEIGHT);
                break;
            case 3:
                editor.putInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
                editor.putInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);
                break;
            case 4:
                editor.putInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
                editor.putInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);
                break;
            default:
                editor.putInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
                editor.putInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);
                break;
        }
    }

    public void assignIDTasks() {
        identification_counter = 0;
        identification_ender = arrayUri.size();
    }

    //why is passed image never used?
    public void saveImageCreated(Bitmap _tempBitmap) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        int setDesiredWidth = settings.getInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
        int setDesiredHeight = settings.getInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);

        String fileNameString = getFileNameFromURI(arrayUri.get(identification_counter)) + sizeArrayMimeType.get(identification_counter);

        try {
            saveToFileAndUri(rescaledBitmap, sizeArrayMimeType.get(identification_counter), fileNameString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //recycle the global bitmaps
        recycleBitmapImages();

        //clear the Garbage Collector
        clearGarbageCollector();

        //add one to the identification counter
        identification_counter++;
        if (identification_counter < identification_ender) {
            setSampleScale = getSampleSize(setDesiredWidth, setDesiredHeight);

            //call and assign the proper image width and height pending aspect ratio
            assignProperDimensionsForAllImages();

            new ImageCompressingTask().execute();
        } else {
            clearAllFeaturesAndArrayLists();
            setAllButtonsVisible();
            enableAllButtons();
        }
    }

    public void disableAllButtons() {
        btnAddFile.setEnabled(false);
        btnAddFile.setClickable(false);
        btnResizeSettings.setEnabled(false);
        btnResizeSettings.setClickable(false);
        btnResizePhotos.setEnabled(false);
        btnResizePhotos.setClickable(false);
    }

    public void enableAllButtons() {
        btnAddFile.setEnabled(true);
        btnAddFile.setClickable(true);
        btnResizeSettings.setEnabled(true);
        btnResizeSettings.setClickable(true);
        btnResizePhotos.setEnabled(true);
        btnResizePhotos.setClickable(true);
    }

    public void recycleBitmapImages() {
        rescaledBitmap.recycle();
        fixRotationBitmap.recycle();
    }

    //Method to clear all array list and adapters
    public void clearAllFeaturesAndArrayLists() {
        arrayUri.clear();
        sizeArrayList.clear();
        sizeArrayInByte.clear();
        sizeArrayInKB.clear();
        sizeArrayInMB.clear();
        sizeArrayWidth.clear();
        sizeArrayHeight.clear();
        sizeArrayMimeType.clear();
        totalSizeInMB = 0.00;
        numOfPicsSelected = 0;
        duplicateImage = 1;
        updateNumPhotoTotalSizeViews(numOfPicsSelected, totalSizeInMB);
    }

    //Method to set the aspectRatioWidth and aspectRatioHeight
    public void setDesiredWidthAndHeight(int desiredWidth, int desiredHeight) {
        aspectRatioWidth = desiredWidth;
        aspectRatioHeight = desiredHeight;
    }

    //Method to return the sample size and sets the desiredWidth and desiredHeight correctly if aspectRatio is selected
    public int getSampleSize(int imageWidth, int imageHeight) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        int desiredWidth = settings.getInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
        int desiredHeight = settings.getInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);

        if (settings.getBoolean(SETTING_ASPECT_RATIO, true)) {
            double accurateAspectRatio = (double) imageWidth / (double) imageHeight;
            double doubleDesiredHeight = 0;
            double doubleDesiredWidth = 0;

            if (imageWidth > imageHeight) {
                doubleDesiredHeight = desiredWidth / accurateAspectRatio;
                doubleDesiredWidth = desiredWidth;
            } else if (imageWidth < imageHeight) {
                doubleDesiredWidth = desiredHeight * accurateAspectRatio;
                doubleDesiredHeight = desiredHeight;
            } else if (imageWidth == imageHeight) {
                if (desiredWidth > desiredHeight) {
                    doubleDesiredWidth = desiredWidth;
                    doubleDesiredHeight = doubleDesiredWidth;
                } else if (desiredWidth < desiredHeight) {
                    doubleDesiredHeight = desiredHeight;
                    doubleDesiredWidth = doubleDesiredHeight;
                }
            }
            desiredWidth = (int) Math.round(doubleDesiredWidth);
            desiredHeight = (int) Math.round(doubleDesiredHeight);
            setDesiredWidthAndHeight(desiredWidth, desiredHeight);

            return (int) Math.round(accurateAspectRatio);
        } else {
            if (imageWidth > desiredWidth) {
                if (imageHeight > desiredHeight) {
                    return Math.max((imageWidth / desiredWidth), (imageHeight / desiredHeight));
                } else if (imageHeight < desiredHeight) {
                    return Math.max((imageWidth / desiredWidth), (desiredWidth / desiredHeight));
                }
            } else if (imageWidth < desiredWidth) {
                if (imageHeight > desiredHeight) {
                    return Math.max((desiredWidth / imageWidth), (imageHeight / desiredHeight));
                } else if (imageHeight < desiredHeight) {
                    return Math.max((desiredWidth / imageWidth), (desiredHeight / imageHeight));
                }
            } else if (imageWidth == desiredWidth) {
                if (imageHeight == desiredHeight) {
                    return 1;
                }
            }
        }
        return Math.max((imageWidth / desiredWidth), (imageHeight / desiredHeight));
    }

    //Function that saves the Bitmap to your gallery and returns the content that is stored into the URI created
    public Uri saveToFileAndUri(Bitmap bitmap, String mimeTypeString, String _fileName) throws Exception {
        String fileName = _fileName;
        if (mimeTypeString.equals(".jpeg") || mimeTypeString.equals(".jpg")) {
            fileName = _fileName;
        } else if (mimeTypeString.equals(".png")) {
            fileName = _fileName;
        }
        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsoluteFile() + "/piRedux");

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new Exception("Could not create directories, " + file.getAbsolutePath());
            }
        }
        String filePath = file.getAbsolutePath() + "/" + fileName;
        File checkIfExist = new File(filePath);
        if (checkIfExist.exists()) {
            filePath = file.getAbsolutePath() + "/" + duplicateImage + "_" + fileName;
            duplicateImage++;
        }
        FileOutputStream out = new FileOutputStream(filePath);

        if (mimeTypeString.equals(".jpeg") || mimeTypeString.equals(".jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, qualityCompressValue, out);
        } else if (mimeTypeString.equals("png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, qualityCompressValue, out);
        }
        out.flush();
        out.close();

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(7);
        values.put(Images.Media.TITLE, fileName);
        // That filename is what will be handed to Email when a user shares a
        // photo. Email gets the name of the picture attachment from the
        // "DISPLAY_NAME" field.
        values.put(Images.Media.DISPLAY_NAME, fileName);
        if (mimeTypeString.equals(".jpg") || mimeTypeString.equals(".jpeg")) {
            values.put(Images.Media.MIME_TYPE, "image/jpeg");
        } else if (mimeTypeString.equals(".png")) {
            values.put(Images.Media.MIME_TYPE, "image/png");
        }
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        return MainActivity.this.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    //Function that returns the file name of the URI being sent in
    public String getFileNameFromURI(Uri uriImg) {
        File file = new File(uriImg.toString());
        String uriString = file.getAbsolutePath();

        int fileNameInt = uriString.lastIndexOf("/");
        if (uriString.contains("/")) {
            fileNameInt = uriString.lastIndexOf("/");
        } else if (uriString.contains("\\")) {
            fileNameInt = uriString.lastIndexOf("\\");
        }

        return uriString.substring(fileNameInt + 1, uriString.length()) + "_resized";
    }

    //Function to clear the Garbage Collector for possible memory issues (OutOfMemoryException)
    public void clearGarbageCollector() {
        System.gc();
    }

    public int convertExif2Degress(int degreesConvert) {
        int returnValue;

        if (degreesConvert == 90) {
            returnValue = 90;
        } else if (degreesConvert == 180) {
            returnValue = 180;
        } else if (degreesConvert == 270) {
            returnValue = 270;
        } else {
            returnValue = 0;
        }

        return returnValue;
    }

    public int getOrientation(Context context, Uri photoUri) {
        int rotateIntValue;
        Cursor cursor = context.getContentResolver().query(photoUri, new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
        if (cursor.getCount() != 1) {
            rotateIntValue = -1;
        } else {
            cursor.moveToFirst();
            rotateIntValue = cursor.getInt(0);
        }
        cursor.close();
        return rotateIntValue;
    }

    //Function that is called when activityResult is called
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = null;

        if (data != null)
        {
            extras = data.getExtras();
            String action = data.getAction();

            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode >= 0)
            {
                switch (requestCode) {
                    case RQS_LOADMULTIPLEIMAGE:
                        if (Intent.ACTION_SEND_MULTIPLE.equals(action) && data.hasExtra(Intent.EXTRA_STREAM) && resultCode == RESULT_OK) {
                            if (extras != null && extras.containsKey(Intent.EXTRA_STREAM)) {
                                ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                                for (Parcelable p : list) {
                                    Uri imageUri = (Uri) p;
                                    getFileSize(imageUri);
                                    /*arrayUri.add(imageUri);
                                    long fileSize;
                                    String fileSizeString = getImageSize(imageUri);
                                    long fileSizeLong = Long.parseLong(fileSizeString);
                                    fileSize = Integer.parseInt(fileSizeString);

                                    double fileSizeInByte = Double.parseDouble(fileSizeString);
                                    double fileSizeInKB = fileSizeLong / 1024;
                                    double fileSizeInMB = fileSizeInKB / 1024;
                                    double fileSizeInMBPrecision = roundMB(fileSizeInMB, 2, BigDecimal.ROUND_HALF_UP);
                                    fileSizeString = Double.toString(fileSizeInMBPrecision);
                                    if (fileSize == 0) {
                                        sizeArrayList.add("N/A");
                                        sizeArrayInByte.add(0.00);
                                        sizeArrayInKB.add(0.00);
                                        sizeArrayInMB.add(0.00);
                                    } else {
                                        sizeArrayList.add(fileSizeString + " MB");
                                        sizeArrayInByte.add(fileSizeInByte);
                                        sizeArrayInKB.add(fileSizeInKB);
                                        sizeArrayInMB.add(fileSizeInMB);
                                    }
                                    totalSizeInMB += fileSizeInMBPrecision;
                                    numOfPicsSelected = arrayUri.size();
                                    updateNumPhotoTotalSizeViews(numOfPicsSelected, totalSizeInMB);*/
                                }
                            }
                            break;
                        }
                        break;

                    case RQS_LOADIMAGE:
                        if (resultCode == RESULT_OK) {
                            Uri imageUri = data.getData();
                            //arrayUri.add(imageUri);
                            getFileSize(imageUri);
                            /*long fileSize;
                            String fileSizeString = getImageSize(imageUri);
                            long fileSizeLong = Long.parseLong(fileSizeString);
                            fileSize = Integer.parseInt(fileSizeString);

                            double fileSizeInByte = Double.parseDouble(fileSizeString);
                            double fileSizeInKB = fileSizeLong / 1024;
                            double fileSizeInMB = fileSizeInKB / 1024;
                            double fileSizeInMBPrecision = roundMB(fileSizeInMB, 2, BigDecimal.ROUND_HALF_UP);
                            fileSizeString = Double.toString(fileSizeInMBPrecision);
                            if (fileSize == 0) {
                                sizeArrayList.add("N/A");
                                sizeArrayInByte.add(0.00);
                                sizeArrayInKB.add(0.00);
                                sizeArrayInMB.add(0.00);
                            } else {
                                sizeArrayList.add(fileSizeString + " MB");
                                sizeArrayInByte.add(fileSizeInByte);
                                sizeArrayInKB.add(fileSizeInKB);
                                sizeArrayInMB.add(fileSizeInMB);
                            }
                            totalSizeInMB += fileSizeInMBPrecision;
                            numOfPicsSelected = arrayUri.size();
                            updateNumPhotoTotalSizeViews(numOfPicsSelected, totalSizeInMB);*/
                        }
                        break;
                }

            }
        }
    }

    public static double roundMB(double unrounded, int precision, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    public String getImageSize(Uri ImageURI)
    {
        ContentResolver resolver = getContentResolver();
        InputStream is;
        int sizeOfImg = 0;
        try
        {
            is = resolver.openInputStream(ImageURI);
            bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is,null,bounds);
            int width = bounds.outWidth;
            int height = bounds.outHeight;
            String mimeTypeImg = bounds.outMimeType;


            sizeArrayWidth.add(width);
            sizeArrayHeight.add(height);
            int mimeTypeImgInt = mimeTypeImg.lastIndexOf("/");
            String subMimeType = mimeTypeImg.substring((mimeTypeImgInt + 1));
            String extensionString = "." + subMimeType;
            sizeArrayMimeType.add(extensionString);

            sizeOfImg = is.available();
            is.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return Integer.toString(sizeOfImg);
    }

    //Function that sets All the buttons visible
    public void setAllButtonsVisible()
    {
        btnAddFile.setVisibility(View.VISIBLE);
        btnResizeSettings.setVisibility(View.VISIBLE);
        btnResizePhotos.setVisibility(View.VISIBLE);
    }

    public class ImageCompressingTask extends AsyncTask<Void, Integer, Bitmap>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            int number = identification_counter + 1;
            displayTextView.setText("Compressing image " + number + "/" + identification_ender);
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            saveImageCreated(result);
        }

        @Override
        protected void onProgressUpdate(Integer... arguments)
        {
            int numFromArgs = arguments[0];

            switch(numFromArgs)
            {
                case 1:
                    imageRotation = getOrientation(getApplicationContext(), arrayUri.get(identification_counter));
                    finalRotation = convertExif2Degress(imageRotation);
                    break;
                case 2:
                    clearGarbageCollectorInTask();
                    break;
                case 3:
                    break;
                case 10:
                    int number = identification_counter + 1;
                    displayTextView.setText("Saving image " + number + "/" + identification_ender);
                    break;
                default:
                    break;
            }
        }

        @Override
        protected Bitmap doInBackground(Void... arg0)
        {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            int setFinalWidth = settings.getInt(SETTING_WIDTH, BUTTON_HD_WIDTH);
            int setFinalHeight = settings.getInt(SETTING_HEIGHT, BUTTON_HD_HEIGHT);

            //Call function to get the rescaled image
            publishProgress(1);
            rescaledBitmap = getBitmapRescaledInTask(arrayUri.get(identification_counter), setSampleScale, setFinalWidth, setFinalHeight, identification_counter);

            //run garbage collector
            publishProgress(2);

            //push progress to TextView
            publishProgress(10);

            return rescaledBitmap;
        }

        public void clearGarbageCollectorInTask()
        {
            System.gc();
        }

        //Method to resize the bitmap
        //Why is placement not used?
        public Bitmap getBitmapRescaledInTask(Uri imageUri, int sampleSizeInt, int setDesiredWidth, int setDesiredHeight, int indexPlacement)
        {
            imageRotation = getOrientationInTask(getApplicationContext(), imageUri);
            finalRotation = convertExif2DegressInTask(imageRotation);

            Matrix matrix = new Matrix();
            if (finalRotation != 0f)
            {
                matrix.postRotate(finalRotation);
            }
            ContentResolver resolver = getContentResolver();
            InputStream inputStream;
            try
            {
                inputStream = resolver.openInputStream(imageUri);
                bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = false;
                bounds.inSampleSize = sampleSizeInt;
                bounds.inPreferredConfig=Bitmap.Config.ARGB_8888;
                Bitmap bitmap_Source = BitmapFactory.decodeStream(inputStream,null,bounds);
                rescaledBitmap =  Bitmap.createScaledBitmap(bitmap_Source, setDesiredWidth, setDesiredHeight, true);
                if(bounds.outWidth == -1 || bounds.outHeight == -1)
                {
                    //bitmap is null
                    return null;
                }
                bitmap_Source.recycle();
                fixRotationBitmap = Bitmap.createBitmap(rescaledBitmap, 0, 0, rescaledBitmap.getWidth(), rescaledBitmap.getHeight(), matrix, true);
                rescaledBitmap.recycle();
                inputStream.close();
                return fixRotationBitmap;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        public int getOrientationInTask(Context context, Uri photoUri)
        {
            int rotateIntValue;
            Cursor cursor = context.getContentResolver().query(photoUri, new String[] {MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
            if(cursor.getCount() != 1)
            {
                rotateIntValue = -1;
            }
            else
            {
                cursor.moveToFirst();
                rotateIntValue = cursor.getInt(0);
            }
            cursor.close();
            return rotateIntValue;
        }

        public int convertExif2DegressInTask(int degreesConvert)
        {
            int returnValue;
            if(degreesConvert == 90)
            {
                returnValue = 90;
            }
            else if(degreesConvert == 180)
            {
                returnValue = 180;
            }
            else if(degreesConvert == 270)
            {
                returnValue = 270;
            }
            else
            {
                returnValue = 0;
            }

            return returnValue;
        }
    }

    //Function that is called when creating the menu/features
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    private void getFileSize(Uri uri)
    {
        //Uri imageUri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
        arrayUri.add(uri);
        long fileSize;
        String fileSizeString = getImageSize(uri);
        long fileSizeLong = Long.parseLong(fileSizeString);
        fileSize = Integer.parseInt(fileSizeString);

        double fileSizeInByte = Double.parseDouble(fileSizeString);
        double fileSizeInKB = fileSizeLong / 1024;
        double fileSizeInMB = fileSizeInKB / 1024;
        double fileSizeInMBPrecision = roundMB(fileSizeInMB, 2, BigDecimal.ROUND_HALF_UP);
        fileSizeString = Double.toString(fileSizeInMBPrecision);
        if(fileSize == 0)
        {
            sizeArrayList.add("N/A");
            sizeArrayInByte.add(0.00);
            sizeArrayInKB.add(0.00);
            sizeArrayInMB.add(0.00);
        }
        else
        {
            sizeArrayList.add(fileSizeString + " MB");
            sizeArrayInByte.add(fileSizeInByte);
            sizeArrayInKB.add(fileSizeInKB);
            sizeArrayInMB.add(fileSizeInMB);
        }
        numOfPicsSelected = arrayUri.size();
        updateNumPhotoTotalSizeViews(numOfPicsSelected, totalSizeInMB);

        setList();
    }

    //set ListView with selected images
    private void setList()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
        HashMap<String, Object> row = new HashMap<String, Object>();

        int numberOfImages = settings.getInt(SETTING_NUMBER_OF_IMAGES, 0);
        Bitmap image;
        ListView listView = (ListView)findViewById(R.id.photoList);

        for(int i=1; i<=numberOfImages; i++)
        {
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), arrayUri.get(i-1));

                if(i%2==1)
                    row.put("image1", image);
                else
                    row.put("image2", image);

                if(i==numberOfImages || i%2==1)
                    data.add(row);
            }
            catch(Exception e)
            {
                Toast.makeText(this, "Image Not Found", Toast.LENGTH_SHORT).show();
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this,
                data,
                R.layout.photo_list_row,
                new String[] {"image1","image2"},
                new int[] {R.id.photo1, R.id.photo2});



        listView.setAdapter(adapter);
        /*PhotoAdapter photoAdapter = new PhotoAdapter(this,
                data,
                R.layout.photo_list_row,
                new String[] {"image1", "image2"},
                new int[] { R.id.photo1, R.id.photo2,},
                listView);

        for(int i=-0; i < photoAdapter.getCount(); i++) {
            ImageView photo1 = (ImageView) photoAdapter.getView(i, null, null).findViewById(R.id.photo1);
            ImageView photo2 = (ImageView) photoAdapter.getView(i, null, null).findViewById(R.id.photo2);

            photo1.setImageBitmap();*/
        }

        //listView.setAdapter(photoAdapter);
    }