package com.example.mg.todo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class BitmapUtil {
    static String mCurrentPhotoPath;

    static String encodedImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    static byte[] decodeImage(String mString) {
        return Base64.decode(mString.getBytes(), Base64.DEFAULT);

    }
    static File createTempImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

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

    static Bitmap resamplePic(String imagePath) {
        int newWidth = 400;
        int newHeight = 450;
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bm, newWidth, newHeight, true);
        bm.recycle();
        return Bitmap.createBitmap(scaledBitmap,
                0,
                0,
                scaledBitmap.getWidth(),
                scaledBitmap.getHeight(),
                matrix,
                true);
    }
}
