package jp.kfujine.oadc;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fuji on 2015/11/23.
 */
public class PhotoUtil {
    public static Bitmap createBitmapFromUri(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = null;
        BitmapFactory.Options imageOptions;
        Bitmap imageBitmap = null;

        // メモリ上に画像を読み込まず、画像サイズ情報のみを取得する
        try {
            inputStream = contentResolver.openInputStream(uri);
            imageOptions = new BitmapFactory.Options();
            imageOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, imageOptions);
            assert inputStream != null;
            inputStream.close();
            // もし読み込む画像が大きかったら縮小して読み込む
            inputStream = contentResolver.openInputStream(uri);
            if (imageOptions.outWidth > 1024 && imageOptions.outHeight > 1024) {
                imageOptions = new BitmapFactory.Options();
                imageOptions.inSampleSize = 2;
                imageBitmap = BitmapFactory.decodeStream(inputStream, null, imageOptions);
            } else {
                imageBitmap = BitmapFactory.decodeStream(inputStream, null, null);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageBitmap;
    }

    public static int getOrientation(Uri uri) {
        ExifInterface exifInterface;

        try {
            exifInterface = new ExifInterface(uri.getPath());
        } catch (IOException e) {
            return 0;
        }

        int exifR = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        int orientation = 0;
        switch (exifR) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientation = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orientation = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                orientation = 270;
                break;
            default:
                orientation = 0;
                break;
        }
        return orientation;
    }

}
