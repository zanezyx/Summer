package com.deter.TaxManager.view.dateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by zhongqiusheng on 2017/9/25 0025.
 */
public class CalendarCard extends View {

    private static final int TOTAL_COL = 7; // 7列
    private static final int TOTAL_ROW = 6; // 6行

    private Paint mStrokeCirclePaint;
    private Paint mCirclePaint; // 绘制圆形的画笔
    private Paint mTextPaint; // 绘制文本的画笔
    private int mViewWidth; // 视图的宽度
    private int mViewHeight; // 视图的高度
    private int mColumnWidth; // 单元格宽度
    private int mColumnHeight;//单元格高度
    private Row rows[] = new Row[TOTAL_ROW]; // 行数组，每个元素代表一行
    private static CustomDate mShowDate; // 自定义的日期，包括year,month,day
    private OnCellClickListener mCellClickListener; // 单元格点击回调事件
    private int touchSlop; //
    private boolean callBackCellSpace;

    private static Cell mClickCell;
    private float mDownX;
    private float mDownY;


    /**
     * 单元格点击的回调接口
     */
    public interface OnCellClickListener {
        void exceeds(String nowDateStr);//超出当前指定时间
        void clickDate(CustomDate date); // 回调点击的日期
        void changeDate(CustomDate date); // 回调滑动ViewPager改变的日期
    }

    public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCard(Context context) {
        super(context);
        init(context);
    }

    public CalendarCard(Context context, OnCellClickListener listener) {
        super(context);
        this.mCellClickListener = listener;
        init(context);
    }

    private void init(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokeCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokeCirclePaint.setStyle(Paint.Style.STROKE);
        mStrokeCirclePaint.setColor(Color.parseColor("#F24949"));
        mStrokeCirclePaint.setStrokeWidth((float) 3.0);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#F24949")); // 红色圆形
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        initDate();
    }

    private void initDate() {
        mShowDate = new CustomDate();
        fillDate();//
    }

    private void fillDate() {
        int monthDay = DateUtil.getCurrentMonthDay(); // 今天
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1); // 上个月的天数
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month); // 当前月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);//当前月1号所在位置
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }

        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {

            rows[j] = new Row(j);

            for (int i = 0; i < TOTAL_COL; i++) {
                int position = i + j * TOTAL_COL; // 单元格位置

                // 这个月的
                if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {
                    day++;
                    CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);

                    rows[j].cells[i] = new Cell(date, State.CURRENT_MONTH_DAY, i, j);

                    if (isCurrentMonth && day == monthDay) {// 今天
                        rows[j].cells[i] = new Cell(date, State.TODAY, i, j);
                    }

                    if (isCurrentMonth && day > monthDay) { // 如果比这个月的今天要大，表示还没到
                        rows[j].cells[i] = new Cell(date, State.UNREACH_DAY, i, j);
                    }

                    setClickCellState(rows[j].cells[i]);

                    // 过去一个月
                } else if (position < firstDayWeek) {
                    rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year,
                            mShowDate.month - 1, lastMonthDays
                            - (firstDayWeek - position - 1)),
                            State.PAST_MONTH_DAY, i, j);
                    // 下个月
                } else if (position >= firstDayWeek + currentMonthDays) {
                    rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year,
                            mShowDate.month + 1, position - firstDayWeek
                            - currentMonthDays + 1)),
                            State.NEXT_MONTH_DAY, i, j);
                }
            }
        }
        if (null != mCellClickListener) {
            mCellClickListener.changeDate(mShowDate);
        }
    }


    private void setClickCellState(Cell cell) {
        if (null != cell && null != mClickCell) {
            if (cell.date.getYear() == mClickCell.date.getYear()) {
                if (cell.date.getMonth() == mClickCell.date.getMonth()) {
                    if (cell.date.getDay() == mClickCell.date.getDay()) {
                        cell.state = State.SELECT_DAY;
                    }
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (int) (widthSize / TOTAL_COL * 0.7 * TOTAL_ROW);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null) {
                rows[i].drawCells(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        //mColumnWidth = Math.min(mViewHeight / TOTAL_ROW, mViewWidth / TOTAL_COL);
        mColumnWidth =mViewWidth / TOTAL_COL;
        mColumnHeight = (int) (mColumnWidth * 0.7);
        if (!callBackCellSpace) {
            callBackCellSpace = true;
        }
        mTextPaint.setTextSize(mColumnWidth / 4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mColumnWidth);
                    int row = (int) (mDownY / mColumnHeight);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 计算点击的单元格
     * @param col
     * @param row
     */
    private void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW) {
            return;
        }

        if (mClickCell != null) {
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
        }

        if (rows[row] != null) {
            mClickCell = new Cell(rows[row].cells[col].date,
                    rows[row].cells[col].state, rows[row].cells[col].i,
                    rows[row].cells[col].j);

            CustomDate selectDate = rows[row].cells[col].date;
            selectDate.week = col;

            if (selectDate.getMonth() == mShowDate.getMonth()) {
                CustomDate nowYearDate = new CustomDate();

                if (isExceeded(nowYearDate, selectDate) &&  null!=mCellClickListener) {
                    mCellClickListener.exceeds(nowYearDate.toString());
                    return;
                }

                if (null!=mCellClickListener){
                    mCellClickListener.clickDate(selectDate);
                }

                // 刷新界面
                update();
            }
        }
    }

    private boolean isExceeded(CustomDate nowYearDate, CustomDate selectDate) {
        //当前选中的日期超出今天的日期
        if (selectDate.getYear() > nowYearDate.getYear()) {
            return true;
        } else if (selectDate.getYear() == nowYearDate.getYear()) {
            if (selectDate.getMonth() > nowYearDate.getMonth()) {
                return true;
            }

            if (selectDate.getDay() > nowYearDate.getDay()) {
                if (selectDate.getMonth() == nowYearDate.getMonth()) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * 行
     */
    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public Cell[] cells = new Cell[TOTAL_COL];

        // 绘制单元格
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    cells[i].drawSelf(canvas);
                }
            }
        }

    }


    /**
     * 单元格
     */
    class Cell {
        public CustomDate date;
        public State state;
        public int i;
        public int j;

        public Cell(CustomDate date, State state, int i, int j) {
            super();
            this.date = date;
            this.state = state;
            this.i = i;//列
            this.j = j;//行
        }

        public void drawSelf(Canvas canvas) {
            switch (state) {
                case TODAY: // 今天
                    mTextPaint.setColor(Color.BLACK);
                    drawCircle(canvas, mStrokeCirclePaint);
                    break;

                case SELECT_DAY:
                    mTextPaint.setColor(Color.parseColor("#fffffe"));
                    drawCircle(canvas, mCirclePaint);
                    break;
                case CURRENT_MONTH_DAY: // 当前月日期
                    mTextPaint.setColor(Color.BLACK);
                    break;
                case PAST_MONTH_DAY: // 过去一个月
                case NEXT_MONTH_DAY: // 下一个月
                    mTextPaint.setColor(Color.parseColor("#fffffe"));
                    break;
                case UNREACH_DAY: // 还未到的天
                    mTextPaint.setColor(Color.GRAY);
                    break;
                default:
                    break;
            }
            // 绘制文字
            String content = date.day + "";
            int fontWidth = (int) mTextPaint.measureText(content);
            float textX = (float) (i * mColumnWidth + (mColumnWidth - fontWidth) / 2);
            float textY = (float) ((j + 0.7) * mColumnHeight - mTextPaint.measureText(content, 0, 1) / 2);
            canvas.drawText(content, textX, textY, mTextPaint);
        }

        private void drawCircle(Canvas canvas, Paint paint) {
            float circleX = (float) (mColumnWidth * (i + 0.5));
            float circleY = (float) ((j + 0.5) * mColumnHeight);
            float radius = mColumnHeight / 2;
            canvas.drawCircle(circleX, circleY, radius, paint);
        }

    }

    /**
     * 今天 单元格的状态 当前月日期，过去的月的日期，下个月的日期  选择的日期
     */
    enum State {
        TODAY, CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, UNREACH_DAY, SELECT_DAY;
    }

    // 从左往右划，上一个月
    public void leftSlide() {
        if (mShowDate.month == 1) {
            mShowDate.month = 12;
            mShowDate.year -= 1;
        } else {
            mShowDate.month -= 1;
        }
        update();
    }

    // 从右往左划，下一个月
    public void rightSlide() {
        if (mShowDate.month == 12) {
            mShowDate.month = 1;
            mShowDate.year += 1;
        } else {
            mShowDate.month += 1;
        }
        update();
    }

    public void update() {
        fillDate();
        invalidate();
    }

}
