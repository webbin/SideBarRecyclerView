package exp.webbin.siderecyclerviewlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by webbin on 2018/1/11.
 */

public class SideBar extends View {

    private Paint imagePaint;
    private Paint stringPaint;
    private int viewHeight, viewWidth;

    private int[] idData;
    private int currentIndex;
    private String[] stringData;
    private int stringColor = Color.BLACK;
    private TouchSideBarListener touchSideBarListener;

    public SideBar(Context context) {
        super(context);
        init();
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        log("-- on draw -- ");
        this.viewHeight = getHeight();
        this.viewWidth = getWidth();
//        drawImage(canvas,R.drawable.ic_drakeet);
        drawImageList(canvas);
        drawStrings(canvas);
    }


    private void drawImage(Canvas canvas, int id) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = drawableToBitmap(drawable);
//        canvas.drawBitmap(bitmap,0,0,imagePaint);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        float scale = 0.2f;
        matrix.setScale(scale, scale);
        shader.setLocalMatrix(matrix);
        imagePaint.setShader(shader);
        int drawableWidth = bitmap.getWidth();
        int drawableHeight = bitmap.getHeight();
        int radius = Math.min(drawableWidth, drawableHeight) / 2;
        radius = (int) (radius * scale);
        canvas.translate(viewWidth / 2 - radius, viewHeight / 2 - radius);
        canvas.drawCircle(radius, radius, radius, imagePaint);
//        log("get width = "+getWidth()+", get height = "+getHeight());
    }

    private void init() {
        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        imagePaint.setStrokeWidth(10);
        stringPaint = new Paint();
        stringPaint.setAntiAlias(true);
        stringPaint.setColor(stringColor);
//        stringData = letters;
//        stringPaint.strokewi
//        idData = new int[1];
//        stringData = new String[1];
//        this.idData = new int[]{R.drawable.ic_drakeet,R.drawable.ic_drakeet};
    }

    private void drawImageList(Canvas canvas) {
        if (idData == null || idData.length == 0) return;
        int size = idData.length;
        int itemDiameter = calculateItemDiameter(size);
        canvas.translate((this.viewWidth - itemDiameter) / 2, 0);
        for (int id : idData) {
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            Bitmap bitmap = drawableToBitmap(drawable);
            int drawableWidth = bitmap.getWidth();
            int drawableHeight = bitmap.getHeight();
            log("bitmap height = " + drawableHeight);
            float scale = itemDiameter / (float) drawableHeight;
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            shader.setLocalMatrix(matrix);
            imagePaint.setShader(shader);
            canvas.drawCircle(itemDiameter / 2, itemDiameter / 2, itemDiameter / 2, imagePaint);
            canvas.translate(0, itemDiameter);
//            canvas.restore();
        }
    }

    private void drawStrings(Canvas canvas) {
        if (stringData == null || stringData.length == 0) return;
        int size = stringData.length;
        int itemDiameter = calculateItemDiameter(size);
        int top = 0;
        stringPaint.setTextSize(itemDiameter * 0.7f);
        for (String s : stringData) {
            top += itemDiameter;
            canvas.drawText(s, 0, top, stringPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        float pointY = event.getY();
        int size = 0;
        if (stringData != null && stringData.length > 0)
            size = stringData.length;
        else if (idData != null && idData.length > 0)
            size = idData.length;
        int itemDiameter = calculateItemDiameter(viewHeight);
        int index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                index = (int) Math.floor(pointY / viewHeight * size);
                currentIndex = index;
                if (this.touchSideBarListener != null)
                    this.touchSideBarListener.onTouchItem(index);
                break;
            case MotionEvent.ACTION_MOVE:
//                log("on touch move");
                index = (int) Math.floor(pointY / viewHeight * size);
                if (currentIndex != index) {
                    if (this.touchSideBarListener != null)
                        this.touchSideBarListener.onTouchItem(index);
                    currentIndex = index;
                }
                break;
            case MotionEvent.ACTION_UP:
                index = (int) Math.floor(pointY / viewHeight * size);
                if (this.touchSideBarListener != null) {
                    this.touchSideBarListener.onTouchItemUp(index);
                }
        }
        return true;
    }

//    @Override
//    public void layout(@Px int l, @Px int t, @Px int r, @Px int b) {
//        super.layout(l, t, r, b);
//        log(" -- layout --, r = " + r + ", t = " + t + ", b = " + b);
//    }

    private int calculateItemDiameter(int size) {
        int itemDiameter = this.viewHeight / size;
        if (itemDiameter > this.viewWidth) itemDiameter = viewWidth;
        return itemDiameter;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = this.createBitmapSafely(width, height, Bitmap.Config.ARGB_8888, 1);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


    public Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }

    public void setDataList(int[] id) {
        idData = id;
        stringData = null;
        invalidate();
    }

    public void setDataList(String[] data) {
        stringData = data;
        idData = null;
        invalidate();
    }

    public TouchSideBarListener getTouchSideBarListener() {
        return touchSideBarListener;
    }

    public void setTouchSideBarListener(TouchSideBarListener touchSideBarListener) {
        this.touchSideBarListener = touchSideBarListener;
    }

    private void log(String log) {
        Log.e("side bar view", log);
    }

    interface TouchSideBarListener {
        void onTouchItem(int index);

        void onTouchItemUp(int index);
    }


}
