package com.dcy.psychology.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.widget.ImageView;

public class AsyncImageCache {

    private Context mContext;

    private static AsyncImageCache instance;

    public LruCache<String, Bitmap> mMemoryCache;

    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
    private static final String DISK_CACHE_SUBDIR = "AsyncImageCache";
    public DiskLruCache mDiskCache;

    private Stack<AsyncImageLoadTask> mTaskStack = new Stack<AsyncImageLoadTask>();

    private static int sDisplayWidthPixels = 480;
    private static int sDisplayHeightPixels = 800;
    
    private static boolean sDiskCacheEnable = false;
    private static long sDiskCacheSize = DISK_CACHE_SIZE;
    private static int sDiskCacheCount = 64;
    private static String sDiskCacheDir = null;
    private static boolean sDiskCacheOriginal = false;
    
    
    private static int sMemoryCacheSize = 0;
    
    /**
     * Sets the disk cache enable.
     *
     * @param enable the new disk cache enable
     */
    public static void setDiskCacheEnable(boolean enable) {
        sDiskCacheEnable = enable;
    }
    
    /**
     * Sets the disk cache size.
     *
     * @param size the new disk cache size
     */
    public static void setDiskCacheSize(long size) {
        sDiskCacheSize = size;
    }
    
    /**
     * Sets the disk cache dir.
     *
     * @param dir the new disk cache dir
     */
    public static void setDiskCacheDir(String dir) {
        sDiskCacheDir = dir;
    }
    
    /**
     * Sets the disk cache original.
     *
     * @param enable the new disk cache original
     */
    public static void setDiskCacheOriginal(boolean enable) {
        sDiskCacheOriginal = enable;
    }

    public static void setDiskCacheCount(int count) {
        sDiskCacheCount = count;
    }

    public static void setMemoryCacheSize(int size) {
        sMemoryCacheSize = size;
    }
    
    /**
     * Get instance
     * 
     * @param context
     * @return
     */
    public static AsyncImageCache from(Context context) {

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("Cannot instantiate outside UI thread.");
        }

        if (instance == null) {
            instance = new AsyncImageCache(context.getApplicationContext());
        }

        return instance;
    }

    /**
     * Instantiates a new async image loader.
     * 
     * @param context
     *            the context
     */
    private AsyncImageCache(Context context) {
        mContext = context;
        int memClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024;
        
        if (sMemoryCacheSize ==0 ) {
            // default Heap * 1/8
            sMemoryCacheSize = memClass / 8;
        } else if (sMemoryCacheSize > memClass / 4) {
            // max Heap * 1/4
            sMemoryCacheSize = memClass / 4;
        }

        final int cacheSize = sMemoryCacheSize;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }

        };

        if (sDiskCacheEnable) {
            File cacheDir = null;
            if (sDiskCacheDir != null) {
                cacheDir = new File(sDiskCacheDir, DISK_CACHE_SUBDIR);
                cacheDir.mkdirs();
            }
            else
                cacheDir= DiskLruCache
                    .getDiskCacheDir(context, DISK_CACHE_SUBDIR);
            
            mDiskCache = DiskLruCache.openCache(context, cacheDir, sDiskCacheSize, sDiskCacheCount);
        }
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        sDisplayWidthPixels = dm.widthPixels;
        sDisplayHeightPixels = dm.heightPixels;
    }

    /**
     * Display Image (Drawable loading)
     * 
     * @param imageView
     * @param loadingImage
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Drawable loadingImage,
            ImageGenerator<?> imageGenerator, Runnable callback) {
        displayImage(imageView, loadingImage, 0, 0, imageGenerator, callback);
    }

    /**
     * Display Image (Drawable loading) by width & height
     * 
     * @param imageView
     * @param loadingImage
     * @param width
     * @param height
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Drawable loadingImage, int width,
            int height, ImageGenerator<?> imageGenerator, Runnable callback) {
        if (imageView == null) {
            if (callback != null) 
                callback.run();
            return;
        }
        
        if (loadingImage != null) {
            if (imageView.getBackground() == null) {
                imageView.setBackgroundDrawable(loadingImage);
            }
            imageView.setImageDrawable(null);
        }
        
        if (imageGenerator == null || imageGenerator.getTag() == null) {
            if (callback != null) 
                callback.run();
            return;
        }

        String tag = imageGenerator.getTag();
        imageView.setTag(tag);
        String key = (width != 0 && height != 0) ? tag + width + height : tag;
        Bitmap bitmap = null;
        synchronized (mMemoryCache) {
            bitmap = mMemoryCache.get(key);
        }
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap, false);
            if (callback != null) 
                callback.run();
            return;
        }
        
        synchronized (mTaskStack) {
            for (AsyncImageLoadTask asyncImageTask : mTaskStack) {
                if (asyncImageTask != null 
                        && asyncImageTask.mImageRef != null
                        && tag.equals(asyncImageTask.mImageRef.tag)) {
                    if (callback != null) 
                        callback.run();
                    return;
                }
            }
        }

        ImageRef imageRef = new ImageRef(imageView, tag, width, height,
                imageGenerator, callback);
        AsyncImageLoadTask asyncImageTask = new AsyncImageLoadTask();
        mTaskStack.push(asyncImageTask); 
        asyncImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageRef);
        
    }

    /**
     * Display Image (Bitmap loading)
     * 
     * @param imageView
     * @param loadingImage
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Bitmap loadingImage, ImageGenerator<?> imageGenerator, Runnable callback) {
        displayImage(imageView, loadingImage, 0, 0, imageGenerator, callback);        
    }
    
    /**
     * Display Image (Bitmap loading) by width & height
     * 
     * @param imageView
     * @param loadingImage
     * @param width
     * @param height
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Bitmap loadingImage, int width,
            int height, ImageGenerator<?> imageGenerator, Runnable callback) {
        displayImage(imageView, new BitmapDrawable(mContext.getResources(), loadingImage), width, height, imageGenerator, callback);        
    }
    
    /**
     * Display Image (resid loading)
     * 
     * @param imageView
     * @param loadingImageId
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, int loadingImageId,
            ImageGenerator<?> imageGenerator, Runnable callback) {
        displayImage(imageView, loadingImageId, 0, 0, imageGenerator, callback);
    }

    /**
     * Display Image (resid loading) by width & height
     * 
     * @param imageView
     * @param loadingImageId
     * @param width
     * @param height
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, int loadingImageId, int width,
            int height, ImageGenerator<?> imageGenerator, Runnable callback) {
        Drawable loadingImage = mContext.getResources().getDrawable(loadingImageId);
        displayImage(imageView, loadingImage, width, height, imageGenerator, callback);
        
    }

    /**
     * Display Image (Drawable loading)
     * 
     * @param imageView
     * @param loadingImage
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Drawable loadingImage,
            ImageGenerator<?> imageGenerator) {
        displayImage(imageView, loadingImage, 0, 0, imageGenerator, null);
    }

    /**
     * Display Image (Drawable loading)
     * 
     * @param imageView
     * @param loadingImage
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Drawable loadingImage, int width,
            int height, ImageGenerator<?> imageGenerator) {
        displayImage(imageView, loadingImage, width, height, imageGenerator, null);
    }
    
    /**
     * Display Image (Bitmap loading)
     * 
     * @param imageView
     * @param loadingImage
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Bitmap loadingImage, ImageGenerator<?> imageGenerator) {
        displayImage(imageView, loadingImage, 0, 0, imageGenerator, null);        
    }
    
    /**
     * Display Image (Bitmap loading) by width & height
     * 
     * @param imageView
     * @param loadingImage
     * @param width
     * @param height
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, Bitmap loadingImage, int width,
            int height, ImageGenerator<?> imageGenerator) {
        displayImage(imageView, new BitmapDrawable(mContext.getResources(), loadingImage), width, height, imageGenerator, null);        
    }
    
    /**
     * Display Image (resid loading)
     * 
     * @param imageView
     * @param loadingImageId
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, int loadingImageId,
            ImageGenerator<?> imageGenerator) {
        displayImage(imageView, loadingImageId, 0, 0, imageGenerator, null);
    }

    /**
     * Display Image (resid loading) by width & height
     * 
     * @param imageView
     * @param loadingImageId
     * @param width
     * @param height
     * @param imageGenerator
     */
    public void displayImage(ImageView imageView, int loadingImageId, int width,
            int height, ImageGenerator<?> imageGenerator) {
        Drawable loadingImage = mContext.getResources().getDrawable(loadingImageId);
        displayImage(imageView, loadingImage, width, height, imageGenerator, null);
        
    }
    
    /**
     * Get Image from cache
     *
     * @param tag
     */
    public Bitmap getCacheImage(String tag) {
        return getCacheImage(0, 0, tag);
    }
    
    /**
     * Get Image from cache by width & height
     * 
     * @param width
     * @param height
     * @param tag
     */
    public Bitmap getCacheImage(int width,
            int height, String tag) {

        String diskCachekey = MD5.Md5(tag);
        String memoryCachekey = tag;
        if (width != 0 && height != 0) {
            diskCachekey = MD5.Md5(tag + width + height);
            memoryCachekey = tag + width + height;
        }

        Bitmap bitmap = null;
        synchronized (mMemoryCache) {
            bitmap = mMemoryCache.get(memoryCachekey);
        }

        if (bitmap == null) {
            if (sDiskCacheEnable && mDiskCache != null) {
                synchronized (mDiskCache) {
                    bitmap = mDiskCache.get(diskCachekey);
                }
            }
        }
        
        return bitmap;
    }
    
    /**
     * Call by Activity#onDestroy
     */
    public void stop() {
        
        synchronized (mTaskStack) {
            while (!mTaskStack.empty()) {
                AsyncImageLoadTask asyncImageTask = mTaskStack.pop();
                asyncImageTask.cancel(true);
            }
        }
    }

    public void cleanDiskCache() {
        if (sDiskCacheEnable && mDiskCache != null) {
            synchronized (mDiskCache) {
                mDiskCache.clearCache();
            }
        }
    }
    
    /**
     * Image reference info
     */
    static class ImageRef {

        ImageView imageView;
        String tag;
        int width = 0;
        int height = 0;
        
        ImageGenerator<?> imageGenerator;
        boolean lazy = false;
        
        Runnable mCallback;

        /**
         * Constructor
         * 
         * @param imageView
         * @param tag
         * @param imageGenerator
         */
        public ImageRef(ImageView imageView, String tag, ImageGenerator<?> imageGenerator, Runnable callback) {
            this.imageView = imageView;
            this.tag = tag;
            
            this.imageGenerator = imageGenerator;
            
            this.mCallback = callback;
        }

        public ImageRef(ImageView imageView, String tag,
                int width, int height, ImageGenerator<?> imageGenerator, Runnable callback) {
            
            this(imageView, tag, imageGenerator, callback);
            
            this.width = width;
            this.height = height;
            
        }

    }

    /**
     * Sets the image bitmap.
     *
     * @param imageView the image view
     * @param bitmap the bitmap
     * @param isTran the is tran
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap,
            boolean isTran) {
        if (isTran) {
            final TransitionDrawable td = new TransitionDrawable(
                    new Drawable[] {
                            new ColorDrawable(android.R.color.transparent),
                            new BitmapDrawable(mContext.getResources(), bitmap) });
            td.setCrossFadeEnabled(true);
            imageView.setImageDrawable(td);
            td.startTransition(300);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static class MD5 {
        public static String Md5(String str) {
            if (str != null && !str.equals("")) {
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                            '9', 'a', 'b', 'c', 'd', 'e', 'f' };
                    byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < md5Byte.length; i++) {
                        sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
                        sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
                    }
                    str = sb.toString();
                } catch (NoSuchAlgorithmException e) {
                } catch (Exception e) {
                }
            }
            return str;
        }

    }

    /**
     * Abstract class ImageGenerator.
     */
    public static abstract class ImageGenerator<Params> {
        private String mTag;
        protected Params[] mParams;
        
        /**
         * Instantiates a new image generator.
         *
         * @param tag the tag
         * @param params the params
         */
        public ImageGenerator(String tag, Params... params) {
            mTag = tag;
            mParams = params;
        }

        /**
         * Generate.
         *
         * @return the bitmap
         */
        public abstract Bitmap generate();

        /**
         * Gets the tag.
         *
         * @return the tag
         */
        public String getTag() {
            return mTag;
        }
    }

    /**
     * Class NetworkImageGenerator, the image from network.
     */
    public static class NetworkImageGenerator extends ImageGenerator<String> {

        /**
         * Instantiates a new network image generator.
         *
         * @param tag the tag
         * @param params the params
         */
        public NetworkImageGenerator(String tag, String params) {
            super(tag, params);
        }

        /* (non-Javadoc)
         * @see com.android.internal.util.AsyncImageCache.ImageGenerator#generate()
         */
        @Override
        public Bitmap generate() { 
            Bitmap bitmap = null;
            byte[] data = loadByteArrayFromNetwork(mParams[0]);
            if (data != null) {

                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 1;

                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(data, 0,
                        data.length, opt);
                int bitmapSize = opt.outHeight * opt.outWidth
                        * 4;// pixels*3 if it's RGB and pixels*4
                            // if it's ARGB
                if (bitmapSize > sDisplayWidthPixels * sDisplayHeightPixels * 4)
                    opt.inSampleSize = 2;
                opt.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(data,
                        0, data.length, opt);

            }
            return bitmap;
        }
            
        /**
         * Load byte array from network.
         *
         * @param path the path
         * @return the byte[]
         */
        private byte[] loadByteArrayFromNetwork(String path) {

            ByteArrayOutputStream outputStream = null;
            try {
                URL url = new URL(path);
                InputStream inputStream = (InputStream) url.getContent();

                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                inputStream.close();

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return outputStream != null ? outputStream.toByteArray() : null;
        }
    }
    
    /**
     * Class NetworkImageGenerator, the image from exists bitmap.
     */
    public static class GeneralImageGenerator extends ImageGenerator<Bitmap> {
        
        /**
         * Instantiates a new General image generator.
         *
         * @param tag the tag
         * @param params the params
         */
        public GeneralImageGenerator(String tag, Bitmap params) {
            super(tag, params);
        }
        
        /* (non-Javadoc)
         * @see com.android.internal.util.AsyncImageCache.ImageGenerator#generate()
         */
        @Override
        public Bitmap generate() {
            return mParams[0];
        }
    }
    
    /**
     * Class AsyncImageLoadTask, the image load task.
     */
    class AsyncImageLoadTask extends AsyncTask<ImageRef, Integer, Bitmap> {

        private ImageRef mImageRef;

        @Override
        protected Bitmap doInBackground(ImageRef... params) {
            
            if (isCancelled())
                return null;
            
            mImageRef = params[0];
            
            Bitmap bitmap = null;
            Bitmap tBitmap = null;
            ImageRef imageRef = mImageRef;
            
            if (imageRef.tag == null)
                return null;
            
            // got from Disk Cache
            String diskCachekey = MD5.Md5(imageRef.tag);
            String memoryCachekey = imageRef.tag;
            if (imageRef.width != 0 && imageRef.height != 0) {
                diskCachekey = MD5.Md5(imageRef.tag + imageRef.width + imageRef.height);
                memoryCachekey = imageRef.tag + imageRef.width + imageRef.height;
            }
            
            if (sDiskCacheEnable && mDiskCache != null) {
                synchronized (mDiskCache) {
                    bitmap = mDiskCache.get(diskCachekey);
                }
            }

            if (bitmap != null) {
                // save to memory cache
                synchronized (mMemoryCache) {
                    if (mMemoryCache.get(memoryCachekey) == null)
                        mMemoryCache.put(memoryCachekey, bitmap);
                }
            } else {
                long startTime = System.currentTimeMillis();
                
                if (imageRef.imageGenerator != null) 
                    tBitmap = imageRef.imageGenerator.generate();
                
                if (tBitmap != null && imageRef.width != 0 && imageRef.height != 0) {
                    //save original bitmap
                    if (sDiskCacheEnable && mDiskCache != null
                            && sDiskCacheOriginal && tBitmap != null) {
                        synchronized (mDiskCache) {
                            mDiskCache.put(MD5.Md5(imageRef.tag), tBitmap);
                        }
                    }
                            
                    bitmap = ThumbnailUtils
                            .extractThumbnail(
                                    tBitmap,
                                    imageRef.width,
                                    imageRef.height,
                                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                } else {
                    bitmap = tBitmap;
                    tBitmap = null;
                }

                if (bitmap != null) {
                    // save to Disk Cache & memory cache
                    // save thumb bitmap
                    if (sDiskCacheEnable && mDiskCache != null) {
                        synchronized (mDiskCache) {
                            mDiskCache.put(diskCachekey, bitmap);
                        }
                    }
                    synchronized (mMemoryCache) {
                        mMemoryCache.put(memoryCachekey, bitmap);
                    }
                }
                
                long duration = System.currentTimeMillis() - startTime;
                imageRef.lazy = duration > 200;
                
            }
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            
            synchronized (mTaskStack) {
                mTaskStack.remove(this);
            }
            
            ImageRef imageRef = mImageRef;
            
            if (imageRef != null && imageRef.mCallback != null)
                imageRef.mCallback.run();
            
            do {
                if (isCancelled())
                    break;
                
                if (bitmap == null)
                    break;

                if (imageRef == null)
                    break;

                if (imageRef.imageView == null
                        || imageRef.imageView.getTag() == null
                        || imageRef.tag == null)
                    break;

                if (!(imageRef.tag).equals((String) imageRef.imageView
                        .getTag())) {
                    break;
                }

                setImageBitmap(imageRef.imageView, bitmap, imageRef.lazy);

            } while (false);
        }

    }

}
