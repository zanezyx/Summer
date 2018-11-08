package com.deter.TaxManager.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.deter.TaxManager.BackgroundService;
import com.deter.TaxManager.R;
import com.deter.TaxManager.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolu on 17-7-10.
 */

public class StarView extends View {

    public static final int TYPE_APP = 0;

    public static final int TYPE_STATION = 1;

    private static final String TAG = "StarView";

    private final static float SCALE_TOUCH_RATIO = 1.5f;

    private static float pointR;

    private static float triangleR;

    private final int STATUS_INIT = 0;

    private final int STATUS_ZOOM_IN = 1;

    private final int STATUS_ZOOM_OUT = 2;

    private final int STATUS_MOVE = 3;

    BitmapFactory.Options options = new BitmapFactory.Options();

    int degree = 0;

    Drawable rotateDrawable;

    private int mType = TYPE_APP;

    private Matrix matrix = new Matrix();

    private int currentStatus = STATUS_INIT;

    private float centerFingerPointX;

    private float centerFingerPointY;

    private float currentBitmapHeight;

    private float currentBitmapWidth;

    private float initx;

    private float inity;

    private float lastMoveX;

    private float lastMoveY;

    private float moveDistanceX;

    private float moveDistanceY;

    private float mTranslateX;

    private float mTranslateY;

    private float totalRatio = 1;

    private float scaledRatio;

    private float initRatio = 1;

    private double lastFingerDis;

    private boolean InScanAnimation = false;

    private int SCALE_TIMES = 4;

    private int width;

    private int height;

    private Paint viewPaint;

    private float maxR;

    private float[] centerCirclePoint;

    private Bitmap bitmap;

    private PopupWindow popupWindow;

    private DeviceShortCutRecyclerAdapter deviceShortCutRecyclerAdapter;

    private Activity context;

    private Object object = new Object();

    private ArrayList<Triangle> mTriangles = new ArrayList<>();

    private ArrayList<CirclePoint> mCirclePoints = new ArrayList<>();

    private List<Device> tmplist = new ArrayList<>();

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.deter.wifimonitor.device.state.changed".equalsIgnoreCase(intent.getAction())) {
                if (deviceShortCutRecyclerAdapter != null) {
                    com.deter.TaxManager.utils.Log.d(TAG, "StarView --- onReceive----StarView");
                    Bundle bundle = intent.getExtras();
                    BackgroundService.updateDeviceState(bundle, tmplist);
                    deviceShortCutRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private BroadcastReceiver usbChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.hardware.usb.action.USB_STATE".equals(intent.getAction())) {
                if (!intent.getExtras().getBoolean("connected")) {
                    dissmissPopupWindow();
                }
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            degree++;
            degree = degree % 360;
            postInvalidate();
            if (InScanAnimation) {
                postDelayed(this, 10);
            }
        }
    };

    public StarView(Context context) {
        this(context, null);

    }

    public StarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.StarView);
        pointR = a.getFloat(R.styleable.StarView_pointR, 20);
        triangleR = a.getFloat(R.styleable.StarView_triangleR, 20);
        SCALE_TIMES = a.getInt(R.styleable.StarView_scaleTimes, 4);
        viewPaint = new Paint();
        viewPaint.setAntiAlias(true);
        viewPaint.setStrokeWidth(3);
        viewPaint.setColor(Color.RED);
        a.recycle();

        setWillNotDraw(false);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inScaled = false;
        centerCirclePoint = new float[2];
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
        bitmap = BitmapFactory.decodeResource(getResources(), mType == TYPE_APP ? R.drawable
                .star_map_ap : R.drawable.star_map_station, options).copy(Bitmap.Config
                .ARGB_8888, true);
    }

    public void setApData(ArrayList<CirclePoint> apData) {
        mCirclePoints.clear();
        mCirclePoints.addAll(apData);
        setType(TYPE_APP);
        if (centerCirclePoint != null) drawInitView(new Canvas());
        Log.d(TAG, "setApData view =" + this.toString() + ", apData size =" + apData.size()
                + ", mCirclePoints size =" + mCirclePoints.size());
        invalidate();
    }

    public void setStationData(ArrayList<Triangle> stationData) {
        mTriangles.clear();
        mTriangles.addAll(stationData);
        setType(TYPE_STATION);
        if (centerCirclePoint != null) drawInitView(new Canvas());
        Log.d(TAG, "setStationData view =" + this.toString() + ", stationData size =" +
                stationData.size() + ", mTriangles size =" + mTriangles.size());
        invalidate();
    }

    public void updatDeviceState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mType == TYPE_STATION) {
                    List<Triangle> tmp = new ArrayList<>();
                    synchronized (StarView.class) {
                        for (Triangle triangle : mTriangles) {
                            if (triangle.device.isInwhitelist()) {
                                tmp.add(triangle);
                            }
                        }
                    }
                    mTriangles.removeAll(tmp);
                }
                postInvalidate();
                final TextView textView = (TextView) getTag();
                if (textView != null) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mType == TYPE_STATION) {
                                textView.setText(getContext().getString(R.string
                                        .star_map_station_note2, mTriangles.size()));
                            } else {
                                textView.setText(getContext().getString(R.string
                                        .star_map_ap_note2, mCirclePoints.size()));
                            }
                        }
                    });
                }

            }
        }).start();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void resetState() {
        initx = 0;
        inity = 0;
        lastFingerDis = 0;
        lastMoveX = -1;
        lastMoveY = -1;
        scaledRatio = 1;
        totalRatio = 1;
        mTranslateX = 0;
        mTranslateY = 0;
        currentStatus = STATUS_INIT;
        //invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        bitmap = BitmapFactory.decodeResource(getResources(), mType == TYPE_APP ? R.drawable
                .star_map_ap : R.drawable.star_map_station, options).copy(Bitmap.Config
                .ARGB_8888, true);
        IntentFilter intentFilter = new IntentFilter("com.deter.wifimonitor.device.state.changed");
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
        context.registerReceiver(usbChangeReceiver, new IntentFilter("android.hardware.usb.action" +
                ".USB_STATE"));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
        context.unregisterReceiver(usbChangeReceiver);
        mTriangles.clear();
        mCirclePoints.clear();
        bitmap.recycle();
        bitmap = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        float paddingStart = getPaddingStart();
        float paddingTop = getPaddingTop();
        float paddingEnd = getPaddingEnd();
        float paddingBottom = getPaddingBottom();
        float centCirclePointX = (width + paddingStart - paddingEnd) / 2.0f;
        float centCirclePointY = (height + paddingTop - paddingBottom) / 2.0f;
        centerCirclePoint[0] = centCirclePointX;
        centerCirclePoint[1] = centCirclePointY;
        maxR = Math.min(width - paddingStart - paddingEnd, height - paddingTop - paddingBottom) /
                2.0f;
        Log.d(TAG, "bitmap.width =" + bitmap.getWidth() + ", bitmap.height =" + bitmap.getHeight
                () + ", width =" + width + ", height=" + height);
    }

    public void startScanAnimation() {
        resetState();
        InScanAnimation = true;
        rotateDrawable = getContext().getDrawable(R.drawable.starview_scan);
        post(runnable);
    }

    public boolean isInScanAnimation() {
        return InScanAnimation;
    }

    public void stopScanAnimation() {
        InScanAnimation = false;
        if (rotateDrawable != null) {
            rotateDrawable.setCallback(null);
            if (rotateDrawable instanceof BitmapDrawable) {
                ((BitmapDrawable) rotateDrawable).getBitmap().recycle();
            }
            rotateDrawable = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d(TAG, "xiaolu onDraw" + ", degree =" + degree + ", view =" + this.toString());
        super.onDraw(canvas);
        switch (currentStatus) {
            case STATUS_ZOOM_IN:
            case STATUS_ZOOM_OUT:
                zoom(canvas);
                break;
            case STATUS_MOVE:
                move(canvas);
                break;
            case STATUS_INIT:
                drawInitViewToBitmap();
            default:
                matrix.reset();
                canvas.drawBitmap(bitmap, matrix, null);
                drawDeviceViewWithMatrix(canvas, matrix);
                //canvas.save();
                if (InScanAnimation && rotateDrawable != null && !((BitmapDrawable)
                        rotateDrawable).getBitmap().isRecycled()) {
                    canvas.rotate(degree, centerCirclePoint[0], centerCirclePoint[1]);
                    rotateDrawable.setBounds(0, 0, getWidth(), getHeight());
                    rotateDrawable.draw(canvas);
                    //invalidate();
                }
                //canvas.restore();

        }
    }

    private void zoom(Canvas canvas) {
        matrix.reset();
        matrix.postScale(totalRatio, totalRatio);
        float scaledWidth = width * totalRatio;
        float scaledHeight = height * totalRatio;
        float translateX;
        float translateY;
        if (currentBitmapWidth < width) {
            translateX = (width - scaledWidth) / 2f;
        } else {
            translateX = mTranslateX * scaledRatio + centerFingerPointX * (1 - scaledRatio);
            if (translateX > 0) {
                translateX = 0;
            } else if (width - translateX > scaledWidth) {
                translateX = width - scaledWidth;
            }
        }
        if (currentBitmapHeight < height) {
            translateY = (height - scaledHeight) / 2.0f;
        } else {
            translateY = mTranslateY * scaledRatio + centerFingerPointY * (1 - scaledRatio);
            if (translateY > 0) {
                translateY = 0;
            } else if (height - translateY > scaledHeight) {
                translateY = height - scaledHeight;
            }
        }
        matrix.postTranslate(translateX, translateY);
        mTranslateX = translateX;
        mTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(bitmap, matrix, null);
        drawDeviceViewWithMatrix(canvas, matrix);
        scaledRatio = 1;
    }

    private void move(Canvas canvas) {
        matrix.reset();
        float translateX = mTranslateX + moveDistanceX;
        float translateY = mTranslateY + moveDistanceY;
        matrix.postScale(totalRatio, totalRatio);
        matrix.postTranslate(translateX, translateY);
        mTranslateX = translateX;
        mTranslateY = translateY;
        canvas.drawBitmap(bitmap, matrix, null);
        drawDeviceViewWithMatrix(canvas, matrix);
        moveDistanceY = 0;
        moveDistanceY = 0;
    }

    private Bitmap drawInitViewToBitmap() {
        Canvas canvas = new Canvas(bitmap);
        drawInitView(canvas);
        currentBitmapWidth = bitmap.getWidth();
        currentBitmapHeight = bitmap.getHeight();
        return bitmap;
    }

    private void drawInitView(Canvas canvas) {
        drawDeviceView(canvas);
    }

    private void drawDeviceView(Canvas canvas) {
        if (mType == TYPE_APP) {
            for (CirclePoint circlePoint : mCirclePoints) {
                drawPoint(canvas, circlePoint);
            }
        }
        if (mType == TYPE_STATION) {
            for (Triangle triangle : mTriangles) {
                drawTriangle(canvas, triangle);
            }
        }
    }

    private void drawDeviceViewWithMatrix(Canvas canvas, Matrix matrix) {
        if (mType == TYPE_APP) {
            for (CirclePoint circlePoint : mCirclePoints) {
                drawPoint(canvas, circlePoint, matrix);
            }
        }
        if (mType == TYPE_STATION) {
            for (Triangle triangle : mTriangles) {
                drawTriangle(canvas, triangle, matrix);
            }
        }
    }

    private void drawPoint(Canvas canvas, CirclePoint circlePoint, Matrix matrix) {
        float[] rotateCenterPoint = circlePoint.getNewCirclePoint(matrix);
        Drawable drawable = getAPDrawable(circlePoint);
        drawable.setBounds((int) (rotateCenterPoint[0] - pointR), (int) (rotateCenterPoint[1] -
                pointR), (int) (rotateCenterPoint[0] + pointR), (int) (rotateCenterPoint[1] +
                pointR));
        drawable.draw(canvas);
    }

    private Drawable getAPDrawable(CirclePoint circlePoint) {
        if (circlePoint.device.getDeauthState() != 0 && circlePoint.device.getDeauthState() != 3) {
            return getContext().getDrawable(R.drawable.ap_force_disconnected);
        } else {
            return getContext().getDrawable(R.drawable.ap_online);
        }
    }

    private void drawTriangle(Canvas canvas, Triangle triangle, Matrix matrix) {
        float[] rotateCenterPoint = triangle.getNewCenterPoint(matrix);
        Drawable drawable = getStationDrawable(triangle);
        drawable.setBounds((int) (rotateCenterPoint[0] - triangleR), (int) (rotateCenterPoint[1]
                - triangleR), (int) (rotateCenterPoint[0] + triangleR), (int)
                (rotateCenterPoint[1] + triangleR));
        drawable.draw(canvas);
    }

    private Drawable getStationDrawable(Triangle triangle) {
        Drawable drawable = getContext().getDrawable(R.drawable.station_online);
        if (triangle.device.getDeauthState() != 0) {
            switch (triangle.device.getDeauthState()) {
                case 1:
                case 2:
                    if (triangle.device.isInblacklist()) {
                        drawable = getContext().getDrawable(R.drawable
                                .station_black_and_force_disconnected);
                    } else {
                        drawable = getContext().getDrawable(R.drawable.station_force_disconnected);
                    }
                    break;
                case 3:
                    drawable = getContext().getDrawable(R.drawable.station_online);
                    break;
            }
        } else if (triangle.device.getMitmState() != 0) {
            switch (triangle.device.getMitmState()) {
                case 1:
                case 2:
                    if (triangle.device.isInblacklist()) {
                        drawable = getContext().getDrawable(R.drawable
                                .station_black_and_force_connected);
                    } else {
                        drawable = getContext().getDrawable(R.drawable.station_force_connected);
                    }
                    break;
                case 3:
                    drawable = getContext().getDrawable(R.drawable.station_online);
                    break;
            }
        } else {
            if (triangle.device.isInblacklist()) {
                drawable = getContext().getDrawable(R.drawable.station_blacklist);
            } else {
                drawable = getContext().getDrawable(R.drawable.station_online);
            }
        }
        return drawable;

    }

    private void drawPoint(Canvas canvas, CirclePoint circlePoint) {
        float centerPointerX = centerCirclePoint[0];
        float centerPointerY = maxR * (50 - circlePoint.distance) / 50f + getPaddingTop();
        float degree = (circlePoint.channel - 1) * 180 / 7f;
        float[] rotateCenterPoint = getRotatePoint(centerPointerX, centerPointerY,
                centerCirclePoint[0], centerCirclePoint[1], degree);
        if (circlePoint.state == 1) {
            viewPaint.setColor(Color.GREEN);
        } else {
            viewPaint.setColor(Color.RED);
        }
        //canvas.drawCircle(rotateCenterPoint[0], rotateCenterPoint[1], pointR, viewPaint);
        circlePoint.setCirclePoint(rotateCenterPoint, pointR * SCALE_TOUCH_RATIO);
    }

    private void drawTriangle(Canvas canvas, Triangle triangle) {
        float centerPointerX = centerCirclePoint[0];
        float centerPointerY = maxR * (50 - triangle.distance) / 50f + getPaddingTop();
        float degree = triangle.angle;
        Path path = new Path();
        float[] rotateCenterPoint = getRotatePoint(centerPointerX, centerPointerY,
                centerCirclePoint[0], centerCirclePoint[1], degree);
        float[] point1 = new float[]{(float) (rotateCenterPoint[0] - Math.sqrt(3.0) * triangleR /
                2f), rotateCenterPoint[1] + triangleR / 2f};
        float[] pointScale1 = new float[]{(float) (rotateCenterPoint[0] - Math.sqrt(3.0) *
                triangleR * SCALE_TOUCH_RATIO / 2f), rotateCenterPoint[1] + triangleR *
                SCALE_TOUCH_RATIO / 2f};
        path.moveTo(point1[0], point1[1]);
        float[] point2 = new float[]{rotateCenterPoint[0], rotateCenterPoint[1] - triangleR};
        float[] pointScale2 = new float[]{rotateCenterPoint[0], rotateCenterPoint[1] - triangleR
                * SCALE_TOUCH_RATIO};
        path.lineTo(point2[0], point2[1]);
        float[] point3 = new float[]{(float) (rotateCenterPoint[0] + Math.sqrt(3.0) * triangleR /
                2f), rotateCenterPoint[1] + triangleR / 2f};
        float[] pointScale3 = new float[]{(float) (rotateCenterPoint[0] + Math.sqrt(3.0) *
                triangleR * SCALE_TOUCH_RATIO / 2f), rotateCenterPoint[1] + triangleR *
                SCALE_TOUCH_RATIO / 2f};
        path.lineTo(point3[0], point3[1]);
        if (triangle.device.getState() == 2) {
            if (triangle.device.isInblacklist()) {
                viewPaint.setColor(Color.GREEN);
            } else {
                viewPaint.setColor(Color.YELLOW);
            }

        } else if (triangle.device.isInblacklist()) {
            viewPaint.setColor(Color.BLACK);
        } else {
            viewPaint.setColor(Color.RED);

        }
        //canvas.drawPath(path, viewPaint);

        triangle.setPoint(pointScale1, pointScale2, pointScale3, rotateCenterPoint);

    }

    private float[] getRotatePoint(float x1, float y1, float x0, float y0, float k) {
        //k = new Float(Math.toRadians(k));
        k = (float) Math.toRadians(k);
        float x2 = (float) ((x1 - x0) * Math.cos(k) - (y1 - y0) * Math.sin(k) + x0);
        float y2 = (float) ((x1 - x0) * Math.sin(k) + (y1 - y0) * Math.cos(k) + y0);
        float[] point;
        point = new float[]{x2, y2};
        return point;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (InScanAnimation) return false;

        Log.d(TAG, "event action =" + event.getActionMasked());
        if (currentStatus != STATUS_INIT) {
            getParent().getParent().requestDisallowInterceptTouchEvent(true);
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                initx = event.getX();
                inity = event.getY();
                Log.d(TAG, "initx =" + event.getX() + ", inity =" + event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    lastFingerDis = distanceBetweenFingers(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if (lastMoveX == -1 && lastMoveY == -1) {
                        lastMoveX = xMove;
                        lastMoveY = yMove;
                    }
                    if (totalRatio > initRatio) {
                        currentStatus = STATUS_MOVE;
                    }
                    moveDistanceX = xMove - lastMoveX;
                    moveDistanceY = yMove - lastMoveY;
                    if (mTranslateX + moveDistanceX > 0) {
                        moveDistanceX = -mTranslateX;
                    } else if (width - (mTranslateX + moveDistanceX) > currentBitmapWidth) {
                        moveDistanceX = width - currentBitmapWidth - mTranslateX;
                    }
                    if (mTranslateY + moveDistanceY > 0) {
                        moveDistanceY = -mTranslateY;
                    } else if (height - (mTranslateY + moveDistanceY) > currentBitmapHeight) {
                        moveDistanceY = height - currentBitmapHeight - mTranslateY;
                    }
                    Log.d(TAG, "mTranslateX =" + mTranslateX + ", mTranslateY =" + mTranslateY +
                            ", moveDistanceX =" + moveDistanceX + ", moveDistanceY =" +
                            moveDistanceY);
                    invalidate();
                    lastMoveX = xMove;
                    lastMoveY = yMove;
                } else if (event.getPointerCount() == 2) {
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);
                    if (fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                    } else {
                        currentStatus = STATUS_ZOOM_IN;
                    }
                    if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < SCALE_TIMES * initRatio)
                            || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
                        scaledRatio = (float) (fingerDis / lastFingerDis);
                        totalRatio = totalRatio * scaledRatio;
                        if (totalRatio > SCALE_TIMES * initRatio) {
                            totalRatio = SCALE_TIMES * initRatio;
                        } else if (totalRatio < initRatio) {
                            totalRatio = initRatio;
                        }
                        invalidate();
                        lastFingerDis = fingerDis;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    lastMoveX = -1;
                    lastMoveY = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                lastMoveX = -1;
                lastMoveY = -1;
                if (totalRatio == initRatio) {
                    currentStatus = STATUS_INIT;
                }
                if (initx == event.getX() && inity == event.getY()) {// just touch click event
                    float[] dst = new float[]{event.getX(), event.getY()};
                    findWhichArea(dst, matrix);
                }
                break;
            default:
                break;
        }
        return true;

    }

    private void findWhichArea(float[] point, Matrix matrix) {

        Log.d(TAG, "findWhichArea--------" + "pointx =" + point[0] + ", pointy =" + point[1]);
        ArrayList<Device> devices = new ArrayList<>();

        if (mType == TYPE_APP) {
            for (CirclePoint circlePoint : mCirclePoints) {
                if (circlePoint.isInCircleAreaNoMatrixScale(point, matrix)) {
                    Log.d(TAG, "findWhichArea is in this circlePoint =" + circlePoint.toString());
                    devices.add(circlePoint.device);
                }
            }
        } else if (mType == TYPE_STATION) {
            for (Triangle triangle : mTriangles) {
                if (triangle.isPointInTriangleAreaNoMatrixScale(point, matrix)) {
                    Log.d(TAG, "findWhichArea is in this triangle =" + triangle.toString());
                    devices.add(triangle.device);
                }
            }
        }
        if (devices.size() > 0) {
            showShotcutOptionDialog(devices);
        }
    }

    private void showShotcutOptionDialog(ArrayList<Device> devices) {
        if (popupWindow != null && popupWindow.isShowing() || context == null) {
            return;
        }
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_starview_popwindow,
                null, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        View close = v.findViewById(R.id.close);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        tmplist.clear();
        tmplist.addAll(devices);
        deviceShortCutRecyclerAdapter = new DeviceShortCutRecyclerAdapter(context, tmplist, this);
        recyclerView.setAdapter(deviceShortCutRecyclerAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(28));
        popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popWindowAnimation);
        popupWindow.showAtLocation((View) getParent(), Gravity.CENTER, 0, 0);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tmplist.clear();
                deviceShortCutRecyclerAdapter = null;
            }
        });

    }

    public void dissmissPopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerFingerPointX = (xPoint0 + xPoint1) / 2f;
        centerFingerPointY = (yPoint0 + yPoint1) / 2f;
    }

    public static class Triangle {

        private String mac;

        private int role;

        private int state;

        private int angle;

        private float distance;

        private Device device;

        private float[] pointA;

        private float[] pointB;

        private float[] pointC;

        private float[] centerPoint;

        public Triangle(String mac, int role, int state, int angle, float distance, Device device) {
            this.mac = mac;
            this.role = role;
            this.state = state;
            this.angle = angle;
            this.distance = distance;
            this.device = device;
        }

        private void setPoint(float[] point1, float[] point2, float[] point3, float[] centerPoint) {
            this.pointA = point1;
            this.pointB = point2;
            this.pointC = point3;
            this.centerPoint = centerPoint;

        }

        private float[] getNewCenterPoint(Matrix matrix) {
            float[] newCenterPoint = new float[2];
            matrix.mapPoints(newCenterPoint, centerPoint);
            return newCenterPoint;
        }

        private boolean isPointInTriangleAreaNoMatrixScale(float[] point, Matrix matrix) {

            float[] newCenterPoint = getNewCenterPoint(matrix);

            float left = newCenterPoint[0] - triangleR * 1.5f;
            float top = newCenterPoint[1] - triangleR * 1.5f;
            float right = newCenterPoint[0] + triangleR * 1.5f;
            float bottom = newCenterPoint[1] + triangleR * 1.5f;

            return point[0] > left && point[0] < right && point[1] > top && point[1] < bottom;

        }


        private boolean isPointInTriangleArea(float[] point, Matrix matrix) {
            float[] newPointA = new float[2];
            float[] newPointB = new float[2];
            float[] newPointC = new float[2];

            matrix.mapPoints(newPointA, pointA);
            matrix.mapPoints(newPointB, pointB);
            matrix.mapPoints(newPointC, pointC);

            float[] vectorA = new float[]{point[0] - newPointA[0], point[1] - newPointA[1]};
            float[] vectorB = new float[]{point[0] - newPointB[0], point[1] - newPointB[1]};
            float[] vectorC = new float[]{point[0] - newPointC[0], point[1] - newPointC[1]};

            float a, b, c;
            a = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0];
            b = vectorB[0] * vectorC[1] - vectorB[1] * vectorC[0];
            c = vectorC[0] * vectorA[1] - vectorC[1] * vectorA[0];

            return ((a <= 0 && b <= 0 && c <= 0) ||
                    (a > 0 && b > 0 && c > 0));

        }

        public String toString() {
            return "Triangle mac =" + this.mac + ", role =" + this.role + ", state =" + this
                    .state + ", angel =" + this.angle + ", distance =" + this.distance + ", " +
                    "isInblacklist =" + device.isInblacklist();
        }
    }

    public static class CirclePoint {

        private String mac;

        private int role;

        private int state;

        private int channel;

        private float distance;

        private Device device;

        private float[] centerPoint;

        private float newCircleR;

        private float circleR;

        public CirclePoint(String mac, int role, int state, int channel, float distance, Device
                device) {
            this.mac = mac;
            this.role = role;
            this.state = state;
            this.channel = channel;
            this.distance = distance;
            this.device = device;
        }

        private void setCirclePoint(float[] centerPoint, float circleR) {
            this.centerPoint = centerPoint;
            this.circleR = circleR;
        }

        private float[] getNewCirclePoint(Matrix matrix) {
            float[] newCenterPoint = new float[2];
            matrix.mapPoints(newCenterPoint, centerPoint);
            return newCenterPoint;
        }

        private boolean isInCircleAreaNoMatrixScale(float[] point, Matrix matrix) {

            float[] newCenterPoint = getNewCirclePoint(matrix);
            newCircleR = circleR * 1.5f;

            return isPointInCircle(point, newCenterPoint, newCircleR);
        }

        private boolean isInCircleArea(float[] point, Matrix matrix) {
            float[] newCenterPoint = new float[2];
            matrix.mapPoints(newCenterPoint, centerPoint);

            float[] values = new float[9];
            matrix.getValues(values);
            newCircleR = circleR * values[Matrix.MSCALE_X];

            return isPointInCircle(point, newCenterPoint, newCircleR);

        }

        private boolean isPointInCircle(float[] point, float[] newCenterPoint, float newCircleR) {
            float distance = (float) Math.sqrt((point[0] - newCenterPoint[0]) * (point[0] -
                    newCenterPoint[0]) + (point[1] - newCenterPoint[1]) * (point[1] -
                    newCenterPoint[1]));
            return distance < newCircleR;
        }

        public String toString() {
            return "CirclePoint mac =" + this.mac + ", role =" + this.role + ", state =" + this
                    .state + ", channel =" + this.channel + ", distance =" + this.distance;
        }

    }

}
