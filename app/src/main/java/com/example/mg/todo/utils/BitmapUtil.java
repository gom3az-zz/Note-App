package com.example.mg.todo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapUtil {
    public static String mCurrentPhotoPath;

    public static String encodedImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static byte[] decodeImage(String mString) {
        return Base64.decode(mString.getBytes(), Base64.DEFAULT);

    }

    public static File createTempImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.getDefault()).format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap resamplePic(String imagePath) {
        return resize(BitmapFactory.decodeFile(imagePath), .15f);
    }

    public static Bitmap resize(Bitmap bm, float factor) {
        int newWidth = bm.getWidth();
        int newHeight = bm.getHeight();
        int dstWidth = (int) (newWidth * factor); // scale to 15% from original size
        int dstHeight = (int) (newHeight * factor);

        return Bitmap.createScaledBitmap(
                bm, dstWidth, dstHeight, true);
    }

}
