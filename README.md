# SoftpalCameraOrientationUtils

### Developed by

[Softpal](https://www.github.com/softpal)

 
 ### Why This Library?
 
 To use this library in an app which needs to support both landscape and portrait orientations.
 
 ## Installation
 
 Add repository url and dependency in application module gradle file:
 
 allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
### Gradle
[![](https://jitpack.io/v/softpal/SoftpalCameraOrientationUtils.svg)](https://jitpack.io/#softpal/SoftpalCameraOrientationUtils)
```javascript
dependencies {
    implementation 'com.github.softpal:SoftpalCameraOrientationUtils:1.1'
}
```
## Usage

### 1. Calling CameraOrientationUtils

public static void correctOrientation(Context mContext,Uri uri)
	{
		long start = Calendar.getInstance().getTimeInMillis();
		
		String path = FileUtils.getPath(mContext,uri);
		
		start = Calendar.getInstance().getTimeInMillis();
		
		Bitmap loadedBitmap = BitmapFactory.decodeFile(path);
		
		if(loadedBitmap != null)
		{
			start = Calendar.getInstance().getTimeInMillis();
			
			ExifInterface exif = null;
			try
			{
				File pictureFile = new File(path);
				exif = new ExifInterface(pictureFile.getAbsolutePath());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			start = Calendar.getInstance().getTimeInMillis();
			
			int orientation = ExifInterface.ORIENTATION_NORMAL;
			
			if(exif != null)
			{
				orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			}
			
			switch(orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					loadedBitmap = rotateBitmap(loadedBitmap,90);
					
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					loadedBitmap = rotateBitmap(loadedBitmap,180);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					loadedBitmap = rotateBitmap(loadedBitmap,270);
					break;
			}
			
			if(loadedBitmap != null)
			{
				start = Calendar.getInstance().getTimeInMillis();
				
				try(FileOutputStream out = new FileOutputStream(path))
				{
					loadedBitmap.compress(Bitmap.CompressFormat.JPEG,100,out); // bmp is your Bitmap instance
					// PNG is a lossless format, the compression factor (100) is ignored
					int imgSize = loadedBitmap.getByteCount();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				start = Calendar.getInstance().getTimeInMillis();
			}
		}
	}
		private static Bitmap rotateBitmap(Bitmap bitmap,int degrees)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	}
```



 
 
