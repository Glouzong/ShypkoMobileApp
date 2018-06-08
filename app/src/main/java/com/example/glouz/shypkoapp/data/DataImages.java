package com.example.glouz.shypkoapp.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;

import com.example.glouz.shypkoapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class DataImages {

    static private File getPath(String fileName, Context context) {
        File directory = context.getDir("images", MODE_PRIVATE);
        return new File(directory, fileName);
    }

    public static void saveNewAvatar(Uri data, Context context) {
        FileOutputStream fileOutputStream;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(data);
            Bitmap avatar = BitmapFactory.decodeStream(inputStream);
            final String fileName = context.getString(R.string.fileNameAvatar);
            File path = getPath(context.getString(R.string.fileNameAvatar), context);
            fileOutputStream = new FileOutputStream(path);
            avatar.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getAvatar(boolean circle, Context context) {
        FileInputStream fileInputStream;
        Bitmap avatar = null;
        try {
            File path = getPath(context.getString(R.string.fileNameAvatar), context);
            if (!path.exists()) {
                return null;
            }
            fileInputStream = new FileInputStream(path);
            avatar = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return circle ? getCircleBitmap(avatar) : avatar;
    }

    private static Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
