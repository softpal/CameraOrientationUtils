package com.softpal.cameraorientationutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.softpal.fileutils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.annotation.RequiresApi;

public class CameraOrientationUtils
{
	private static final String TAG = CameraOrientationUtils.class.getSimpleName();
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static void correctOrientation(Context mContext,Uri uri)
	{
		long start = Calendar.getInstance().getTimeInMillis();
		
		String path = FileUtils.getPath(mContext,uri);
		
		Log.v(TAG,"correctOrientation path=="+path);
		
		Log.d(TAG,"Time 1 = " + (Calendar.getInstance().getTimeInMillis() - start));
		start = Calendar.getInstance().getTimeInMillis();
		
		Bitmap loadedBitmap = BitmapFactory.decodeFile(path);
		
		Log.v(TAG,"correctOrientation loadedBitmap=="+loadedBitmap);
		
		if(loadedBitmap != null)
		{
			Log.d(TAG,"Time 2 = " + (Calendar.getInstance().getTimeInMillis() - start));
			start = Calendar.getInstance().getTimeInMillis();
			
			ExifInterface exif = null;
			try
			{
				File pictureFile = new File(path);
				exif = new ExifInterface(pictureFile.getAbsolutePath());
				
				Log.v(TAG,"correctOrientation > if(loadedBitmap != null) exif=="+exif);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				Log.d(TAG,"In Catch ExifInterface");
			}
			Log.d(TAG,"Time 3 = " + (Calendar.getInstance().getTimeInMillis() - start));
			start = Calendar.getInstance().getTimeInMillis();
			
			int orientation = ExifInterface.ORIENTATION_NORMAL;
			
			Log.v(TAG,"correctOrientation > if(loadedBitmap != null) orientation1=="+orientation);
			
			if(exif != null)
			{
				orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
				Log.v(TAG,"correctOrientation > if(loadedBitmap != null) orientation2=="+orientation);
			}
			
			switch(orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					loadedBitmap = rotateBitmap(loadedBitmap,90);
					Log.v(TAG,"correctOrientation > if(loadedBitmap != null) orientation loadedBitmap90=="+loadedBitmap);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					loadedBitmap = rotateBitmap(loadedBitmap,180);
					Log.v(TAG,"correctOrientation > if(loadedBitmap != null) orientation loadedBitmap180=="+loadedBitmap);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					loadedBitmap = rotateBitmap(loadedBitmap,270);
					Log.v(TAG,"correctOrientation > if(loadedBitmap != null) orientation loadedBitmap270=="+loadedBitmap);
					break;
			}
			
			if(loadedBitmap != null)
			{
				Log.d(TAG,"Time 4 = " + (Calendar.getInstance().getTimeInMillis() - start));
				start = Calendar.getInstance().getTimeInMillis();
				
				try(FileOutputStream out = new FileOutputStream(path))
				{
					Log.d(TAG,"before loadedBitmap.size = " + loadedBitmap.getByteCount());
					loadedBitmap.compress(Bitmap.CompressFormat.JPEG,100,out); // bmp is your Bitmap instance
					// PNG is a lossless format, the compression factor (100) is ignored
					int imgSize = loadedBitmap.getByteCount();
					Log.d(TAG,"after imgSize = " + imgSize);
				}
				catch(IOException e)
				{
					e.printStackTrace();
					Log.d(TAG,"In Catch compress");
				}
				start = Calendar.getInstance().getTimeInMillis();
			}
			else
			{
			
			}
		}
		else
		{
		
		}
	}
	
	private static Bitmap rotateBitmap(Bitmap bitmap,int degrees)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	}
}
