package com.duan.android.activitystartup.util.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/21
 * desc :    侧边栏（字母）
 * version: 1.0
 * </pre>
 */
public class SideBar extends View {

    public static Character[] INDEX_CHARACTER = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', '#'};

    private OnLetterTouchChangedListener onTouchingLetterChangedListener;
    private List<Character> charList;     // 字母表
    private int choose = -1;
    private Paint paint = new Paint();
    private TextView mTextDialog;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        charList = Arrays.asList(INDEX_CHARACTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();// 获取对应高度
        int width = getWidth();  // 获取对应宽度
        int fontSize = width * 3 / 4;
        int singleHeight = height / charList.size();// 获取每一个字母的高度

        for (int i = 0; i < charList.size(); i++){
            paint.setColor(Color.parseColor("#606060"));
            paint.setTypeface(Typeface.MONOSPACE);//Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setFakeBoldText(true);
            paint.setTextSize(fontSize);

            // 选中状态
            if (i == choose){
                paint.setColor(Color.parseColor("#4F41FD"));
                if(fontSize + 10 <= width)
                    paint.setTextSize(fontSize + 10);
                else paint.setTextSize(width);
            }

            float ww = paint.measureText(String.valueOf(charList.get(i)));
            float hh = paint.descent() - paint.ascent();
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - ww / 2;
            float yPos = singleHeight * i + singleHeight / 2 + hh / 2;
            canvas.drawText(String.valueOf(charList.get(i)), xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    // 事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnLetterTouchChangedListener listener = onTouchingLetterChangedListener;
        // 点击y坐标所占总高度的比例*charList数组的长度就等于点击bar中的个数.
        final int index = (int) (y / getHeight() * charList.size());

        switch (action){
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.parseColor("#FFFFFF"));
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.GONE);
                }
                break;
            default:
                setBackgroundColor(Color.parseColor("#F0F0F0"));
                if (oldChoose != index) {
                    if (index >= 0 && index < charList.size()) {
                        if (listener != null) {
                            listener.onLetterTouchChanged(charList.get(index));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(String.valueOf(charList.get(index)));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = index;
                        invalidate();
                    }
                }
                break;
        }
        return true; //super.dispatchTouchEvent(event);
    }

    // 点击选择某个字母
    public interface OnLetterTouchChangedListener{
        void onLetterTouchChanged(char c);
    }

}
