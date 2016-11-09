//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 04 Oct 2015
//===============================================================================
package com.sharpnode.image.cache;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * class to handle cache related task
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    // for image cache
    private Map<String, Bitmap> imageCache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));//Last argument true for LRU ordering
    //for modified date cache
    private Map<String, String> dateCache = Collections.synchronizedMap(
            new LinkedHashMap<String, String>(10, 1.5f, true));//Last argument true for LRU ordering

    private long size = 0;//current allocated size
    private long limit = 1000000;//max memory in bytes
    private long sizeDateCache = 0;//current allocated size for dateCache


    public MemoryCache() {
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    /**
     * method to set lilmit of cache
     *
     * @param new_limit
     */
    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    /**
     * method to get image bitmap from cache
     *
     * @param id
     * @return
     */
    public Bitmap get(String id) {
        try {
            if (!imageCache.containsKey(id))
                return null;
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            return imageCache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * method to put image bitmap to cache
     *
     * @param id
     * @param bitmap
     */
    public void put(String id, Bitmap bitmap) {
        try {
            if (imageCache.containsKey(id))
                size -= getSizeInBytes(imageCache.get(id));
            imageCache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * method to get modified date from cache
     *
     * @param id
     * @return
     */
    public String getModifiedDate(String id) {
        try {
            if (!dateCache.containsKey(id))
                return null;
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78
            return dateCache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * method to put modified date to cache
     *
     * @param id
     * @param modifiedDate
     */
    public void putModifiedDate(String id, String modifiedDate) {
        try {
            if (dateCache.containsKey(id))
                sizeDateCache -= getStringSizeInBytes(dateCache.get(id));
            dateCache.put(id, modifiedDate);
            sizeDateCache += getStringSizeInBytes(modifiedDate);
            checkDateSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * method to check cache size
     */
    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + imageCache.size());
        if (size > limit) {
            Iterator<Entry<String, Bitmap>> iter = imageCache.entrySet().iterator();//least recently accessed item will be the first one iterated
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size " + imageCache.size());
        }
    }

    /**
     * method to check date cache size
     */
    private void checkDateSize() {
        Log.i(TAG, "date cache size=" + sizeDateCache + " length=" + dateCache.size());
        if (sizeDateCache > limit) {
            Iterator<Entry<String, String>> iter = dateCache.entrySet().iterator();//least recently accessed item will be the first one iterated
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                sizeDateCache -= getStringSizeInBytes(entry.getValue());
                iter.remove();
                if (sizeDateCache <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size " + dateCache.size());
        }
    }

    /**
     * method to clear cache
     */
    public void clear() {
        try {
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            imageCache.clear();
            size = 0;
            dateCache.clear();
            sizeDateCache = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * method to get bitmap size in bytes
     *
     * @param bitmap
     * @return
     */
    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * method to get size of date string stored in cache
     *
     * @param modifiedDate
     * @return
     */
    long getStringSizeInBytes(String modifiedDate) {
        if (modifiedDate == null)
            return 0;
        return modifiedDate.length();
    }
}