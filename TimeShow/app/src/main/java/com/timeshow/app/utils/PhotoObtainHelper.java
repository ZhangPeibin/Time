package com.timeshow.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.timeshow.app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 1:显示弹框
 * 2:照相
 * 3:选取图片
 * Created by Administrator on 2016/8/3.
 */
public class PhotoObtainHelper {

    private static final String LOG_TAG = "PhotoObtainHelper";

    private final static int TAKE_PHOTO_REQUEST_CODE = 1006;
    private final static int PICK_PHOTO_REQUEST_CODE = 1007;
    private final static int CROP_PHOTO_REQUEST_CODE = 1008;

    private PopupWindow pop;

    private Activity mActivity;
    private OnBitmapBackListener onBitmapBackListener;

    private File picFile;

    private Uri photoUri;

    private String photoFileName;

    private boolean mCrop = true;

    private int mAspectX = 1;
    private int mAspectY = 1;

    public void show(Activity a, View v) {
        this.mActivity = a;

        pop = new PopupWindow(a);
        View view = a.getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new ColorDrawable(0x80000000));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        TextView takeP = (TextView) view.findViewById(R.id.t_p);
        TextView choseP = (TextView) view.findViewById(R.id.c_p);
        TextView cancel = (TextView) view.findViewById(R.id.cancel_action);
        view.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
        takeP.setOnClickListener(takePhotoListener);
        choseP.setOnClickListener(chosePhotoListener);
        cancel.setOnClickListener(cancelListener);
    }

    public void setBitmapBackListener(OnBitmapBackListener onBitmapBackListener) {
        this.onBitmapBackListener = onBitmapBackListener;
    }

    private View.OnClickListener takePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doTakePhoto();
        }
    };

    private View.OnClickListener chosePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPickPhoto();
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pop != null) {
                pop.dismiss();
                pop = null;
            }
        }
    };


    public void dismiss() {
        if (pop != null)
            pop.dismiss();
    }

    public interface OnBitmapBackListener {
        void onBitmap(Bitmap bitmap, String path);
    }

    public void setAspect(int aspectX, int aspectY) {
        this.mAspectX = aspectX;
        this.mAspectY = aspectY;
    }


    /**
     * 拍照获取图片
     */
    protected void doTakePhoto() {
        try {
            File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (!uploadFileDir.exists()) {
                uploadFileDir.mkdirs();
            }
            photoFileName = getPhotoFileName("/"+System.currentTimeMillis()+".jpg");

            picFile = new File(uploadFileDir, photoFileName);
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            photoUri = Uri.fromFile(picFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            mActivity.startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * doCropPhoto:(相册获取图片)
     */
    protected void doPickPhoto() {
        try {
            final Intent intent = getPickImageIntent();
            mActivity.startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doCopyPhoto(Intent data) {
        try {
            String picturePath = null;
            if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    String uriStr = selectedImage.toString();
                    String path = uriStr.substring(10, uriStr.length());
                    if (path.startsWith("com.sec.android.gallery3d")) {
                        Log.e(LOG_TAG, "It's auto backup pic path:" + selectedImage.toString());
                    }
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = mActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            } else {
                picturePath = getPath(mActivity, data.getData());
            }
            Log.d("fuck",picturePath);
            File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            photoFileName = getPhotoFileName(picturePath);
            picFile = new File(pictureFileDir, photoFileName);
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            copyFile(picturePath, picFile);
            photoUri = Uri.fromFile(picFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序
     */
    public Intent getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return intent;
    }

    public void setCrop(boolean crop) {
        this.mCrop = crop;
    }


    private void cropImageUriByTakePhoto() {
        if (mActivity != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");

            if ( Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileProvider", picFile);
                intent.setDataAndType(contentUri, "image/*");
            } else {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(photoUri, "image/*");
            }

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", mAspectX);
            intent.putExtra("aspectY", mAspectY);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection 不启用人脸探测
            mActivity.startActivityForResult(intent, CROP_PHOTO_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST_CODE:
                if (mCrop) {
                    cropImageUriByTakePhoto();
                } else {
                    doCopyPhoto(data);
                    backBitmap();
                }
                break;
            case PICK_PHOTO_REQUEST_CODE:
                if (mCrop) {
                    doCopyPhoto(data);
                    cropImageUriByTakePhoto();
                } else {
                    doCopyPhoto(data);
                    backBitmap();
                }
                break;
            case CROP_PHOTO_REQUEST_CODE:
                backBitmap();
                break;
            default:
                break;
        }
    }

    private void backBitmap() {
        if (photoUri != null) {
            Bitmap b = BitmapFactory.decodeFile(photoUri.getPath());
            if (onBitmapBackListener != null) {
                onBitmapBackListener.onBitmap(b, photoUri.getPath());
            }
        }
    }

    /**
     * 获取临时文件名
     *
     * @return
     * @param picturePath
     */
    private String getPhotoFileName (String picturePath) {
        if ( picturePath == null )
            return "temp_"+System.currentTimeMillis()+".jpg";

        String s[] = picturePath.split("/");
        if ( s.length == 0 ){
            return "temp_"+System.currentTimeMillis()+".jpg";
        }

        return s[s.length-1];
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void copyFile(String oldPath, File newPath) {
        InputStream inStream = null; //读入原文件
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "To copy a single file operation error");
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
                if (inStream != null) {
                    inStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint ("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
