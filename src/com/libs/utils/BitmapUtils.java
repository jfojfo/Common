package com.libs.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtils {

    public static Bitmap transform(Matrix scaler, Bitmap source,
            int targetWidth, int targetHeight, boolean scaleUp) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
             * In this case the bitmap is smaller, at least in one dimension,
             * than the target. Transform it by placing as much of the image as
             * possible into the target and leaving the top/bottom or left/right
             * (or both) black.
             */
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);

            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
                    + Math.min(targetWidth, source.getWidth()), deltaYHalf
                    + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
                    - dstY);
            c.drawBitmap(source, src, dst, null);
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }

        Bitmap b1;
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to filter here.
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
                targetHeight);

        if (b1 != source) {
            b1.recycle();
        }

        return b2;
    }

    /**
     * Creates a centered bitmap of the desired size. Recycles the input.
     * 
     * @param source
     */
    public static Bitmap extractMiniThumb(Bitmap source, int width, int height) {
        return extractMiniThumb(source, width, height, true);
    }

    public static Bitmap extractMiniThumb(Bitmap source, int width, int height,
            boolean recycle) {
        if (source == null) {
            return null;
        }

        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = width / (float) source.getWidth();
        } else {
            scale = height / (float) source.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap miniThumbnail = transform(matrix, source, width, height, false);

        if (recycle && miniThumbnail != source) {
            source.recycle();
        }
        return miniThumbnail;
    }

    public static final Bitmap getMiniThumb(final Bitmap bitmap,
            final int thumbnailWidth, final int thumbnailHeight) {
        if (null == bitmap)
            return null;

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        // Detect faces to find the focal point, otherwise fall back to the
        // image center.
        int focusX = width / 2;
        int focusY = height / 2;
        // We have commented out face detection since it slows down the
        // generation of the thumbnail and screennail.

        // final FaceDetector faceDetector = new FaceDetector(width, height, 1);
        // final FaceDetector.Face[] faces = new FaceDetector.Face[1];
        // final int numFaces = faceDetector.findFaces(bitmap, faces);
        // if (numFaces > 0 && faces[0].confidence() >=
        // FaceDetector.Face.CONFIDENCE_THRESHOLD) {
        // final PointF midPoint = new PointF();
        // faces[0].getMidPoint(midPoint);
        // focusX = (int) midPoint.x;
        // focusY = (int) midPoint.y;
        // }

        // Crop to thumbnail aspect ratio biased towards the focus point.
        int cropX;
        int cropY;
        int cropWidth;
        int cropHeight;
        float scaleFactor;
        if (thumbnailWidth * height < thumbnailHeight * width) {
            // Vertically constrained.
            cropWidth = thumbnailWidth * height / thumbnailHeight;
            cropX = Math.max(0,
                    Math.min(focusX - cropWidth / 2, width - cropWidth));
            cropY = 0;
            cropHeight = height;
            scaleFactor = (float) thumbnailHeight / height;
        } else {
            // Horizontally constrained.
            cropHeight = thumbnailHeight * width / thumbnailWidth;
            cropY = Math.max(0,
                    Math.min(focusY - cropHeight / 2, height - cropHeight));
            cropX = 0;
            cropWidth = width;
            scaleFactor = (float) thumbnailWidth / width;
        }
        final Bitmap finalBitmap = Bitmap.createBitmap(thumbnailWidth,
                thumbnailHeight, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(finalBitmap);
        final Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawColor(0);
        canvas.drawBitmap(bitmap, new Rect(cropX, cropY, cropX + cropWidth,
                cropY + cropHeight), new Rect(0, 0, thumbnailWidth,
                thumbnailHeight), paint);
        bitmap.recycle();

        return finalBitmap;
    }

    public static Bitmap decodeFile(String file, int sampleSize) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(file, opt);
        // Bitmap bitmap = BitmapFactory.decodeFile(file);
        return bitmap;
    }

    public static Bitmap decodeStream(InputStream is, int sampleSize) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        // Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public static Bitmap makeRoundedCornerBitmap(Bitmap bm, int roundPx) {
        // LogUtil.d(TAG, "round px: " + String.valueOf(roundPx));
        Bitmap output = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, rect, rect, paint);

        if (output != bm) {
            bm.recycle();
        }
        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bm, double roundRatio) {
        int width = bm.getWidth();
        return makeRoundedCornerBitmap(bm, (int) (width * roundRatio));
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bm) {
        return getRoundedCornerBitmap(bm, 0.1);
    }

    // public void cropImage(int width, int height, File file) {
    // final Intent intent = new Intent("com.android.camera.action.CROP");
    // intent.setClassName("com.android.camera",
    // "com.android.camera.CropImage");
    // intent.setData(Uri.fromFile(file));
    // intent.putExtra("outputX", width);
    // intent.putExtra("outputY", height);
    // intent.putExtra("aspectX", width);
    // intent.putExtra("aspectY", height);
    // intent.putExtra("scale", true);
    // intent.putExtra("noFaceDetection", true);
    // intent.putExtra("output", Uri.parse("file:/" + file.getAbsolutePath()));
    // startActivityForResult(intent, REQUEST_CROP_IMAGE);
    // }

}