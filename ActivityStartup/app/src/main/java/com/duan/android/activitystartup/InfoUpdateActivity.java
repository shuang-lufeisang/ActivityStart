package com.duan.android.activitystartup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.base.Constant;
import com.duan.android.activitystartup.util.LogUtils;
import com.duan.android.activitystartup.widget.SoftKeyboardDetectLinearLayout;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改信息页面
 * 1. 价格 数字: 保留两位小数
 * 2. 处理键盘收放事件
 */
public class InfoUpdateActivity extends BaseActivity implements SoftKeyboardDetectLinearLayout.Listener{

    String TAG = "InfoUpdateActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_right) TextView saveBt;

    @BindView(R.id.edit) EditText editText;

    private String titleStr = "";
    private String contentStr = "";
    private boolean isHint = false;
    private int mode;

    private String mValue = null;    // 当前修改值

    static final String TITLE_INDEX = "title";
    static final String CONTENT_INDEX = "content";
    static final String HINT_INDEX = "hint";   // 是否为提示文字
    static final String MODE_INDEX = "mode";   // 用户信息；报价-单价；

    /**
     * @param context
     * @param mode
     * @param title
     * @param content
     * @param isHint
     */
    public static void startInfoUpdateActivity(Context context, int mode, String title, String content, boolean isHint) {
        LogUtils.printInfo("InfoUpdateActivity", "=================================== startInfoUpdateActivity ===================");
        Intent intent = new Intent(context, InfoUpdateActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(MODE_INDEX, mode);
        intent.putExtra(TITLE_INDEX, title);
        intent.putExtra(CONTENT_INDEX, content);
        intent.putExtra(HINT_INDEX, isHint);
        context.startActivity(intent);
    }

    // isHint=true 展示提示文字 setHint; 否则直接填值 setText;
    public static Intent getInfoUpdateIntent(Context context, int mode, String title, String content, boolean isHint) {
        LogUtils.printInfo("InfoUpdateActivity", "=================================== getInfoUpdateIntent ===================");
        Intent intent = new Intent(context, InfoUpdateActivity.class);
        intent.putExtra(MODE_INDEX, mode);
        intent.putExtra(TITLE_INDEX, title);
        intent.putExtra(CONTENT_INDEX, content);
        intent.putExtra(HINT_INDEX, isHint);

        return intent;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_info_update;
    }

    @Override
    public void initView() {
        initIntentData();  // 初始化 intent data (包含 title)
        initToolbar();
    }

    @Override
    public void initListener() {

        //setListenerToRootView(); // 键盘监听事件
    }

    @Override
    public void initData() {

    }

    private void initIntentData() {

        if (getIntent()!= null){

            mode = getIntent().getIntExtra(MODE_INDEX, 0);
            if (mode == Constant.InfoUpdate.INFO_QUOTE_PRICE){
                // 报价 设置输入 数字
                controlInput(2);
            }

            if(getIntent().getStringExtra(TITLE_INDEX) != null){
                titleStr = getIntent().getStringExtra(TITLE_INDEX);
            }

            isHint = getIntent().getBooleanExtra(HINT_INDEX, true);   // text or hint
            if (getIntent().getStringExtra(CONTENT_INDEX) != null){
                contentStr = getIntent().getStringExtra(CONTENT_INDEX);
                // isHint=true 展示提示文字 setHint; 否则直接填值 setText;
                if (isHint){
                    editText.setHint(contentStr);
                }else {
                    editText.setText(contentStr);
                }
            }

        }

    }


    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }
    public void initToolbar(){
        backBt.setVisibility(View.VISIBLE);
        saveBt.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }

    // 点击保存
    @OnClick(R.id.fl_title_menu_right)
    public void onSaveClicked(){
        Intent data = new Intent();
        hideKeyboard();
        String input = editText.getText().toString().trim();

        double number = Double.valueOf(input);
        input = new DecimalFormat("0.00").format(number);         // 两位小数

//        DecimalFormat decimalFormat = new DecimalFormat("###,###.00"); // 数字千分位 两位小数
//        input = decimalFormat.format(number);                       // 1002200999.22323 -> 1,002,200,999.22

        if (checkQuote(input)){
            mValue = input;
            // Constant.quotePrice = currentContent;
//            data.putExtra("value", mValue);
            data.putExtra("value", input);
            LogUtils.printCloseableInfo(TAG, "data: " + data);
            setResult(2, data);
            finish();// 关闭该窗口

        }

    }

    // hide keyboard
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm != null && imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showPromptMessage(String message) {
        Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
    }


    /**
     * 输入控制: 数字
     * @param digits 指定小数位数
     */
    private void controlInput(final int digits){
        //设置Input的类型两种都要
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);

        //设置字符过滤
        editText.setFilters(new InputFilter[]{new InputFilter() {
            /**
             *@param source 当前输入的字符
             *@param dest 当前已经输入的字符
             *@param dstart 当前输入开始时光标位置
             *@param dend  当前输入结束时光标位置
             */
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
               // LogUtils.printCloseableInfo(TAG, "source: " +source+ " start: " + start +" end: " +end + " dest: " +dest.toString() + " dstart: " + dstart + " dend: " +dend);
                if(source.equals(".") && dest.toString().length() == 0){
                    return "0.";
                }
                if(dest.toString().contains(".")){
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length(); // 小数点及其后的长度（length == 3 即小数点后有两位数）
                    if(length == (digits+1) && dstart > index){    // 当前小数点后有 digits 位数， 并且当前增加的位置 > 小数点的位置
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    // 校验报价 5000-40000
    private boolean checkQuote(String currentContent) {
        String quoteLowRemind = getResources().getString(R.string.quote_too_low);
        String quoteHighRemind = getResources().getString(R.string.quote_too_high);
        if (!TextUtils.isEmpty(currentContent)){
            double quote = Double.valueOf(currentContent);
            if (quote < 5000){
                showPromptMessage(quoteLowRemind);
                return false;
            }
            if (quote > 40000){
                showPromptMessage(quoteHighRemind);
                return false;
            }
            return true;
        }else {
            showPromptMessage(quoteLowRemind);
            return false;
        }
    }



    /** 监听键盘事件 */
    private void setListenerToRootView() {

        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LogUtils.printCloseableInfo(TAG, "[onGlobalLayout] .. in ..");
                boolean mKeyboardUp = isKeyboardShown(rootView);
                if (mKeyboardUp) {
                    //showPromptMessage("键盘弹出");
                    LogUtils.printCloseableInfo(TAG, "[onGlobalLayout] .. 键盘弹出");
                } else {
                    //showPromptMessage("键盘收起");
                    LogUtils.printCloseableInfo(TAG, "[onGlobalLayout] .. 键盘收起");
                }
            }
        });

    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        float measure = softKeyboardHeight * dm.density;
        LogUtils.printCloseableInfo(TAG, "=========== heightDiff: " + heightDiff);
        LogUtils.printCloseableInfo(TAG, "=========== measure: " + measure);
        return heightDiff > softKeyboardHeight * dm.density;
    }

//    @Override
//    public void onGlobalLayout() {
//        LogUtils.printCloseableInfo(TAG, "====== ViewTreeObserver.OnGlobalLayoutListener onGlobalLayout ========");
//
//        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//
//        int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
//        if (heightDiff > dpToPx(this, 200)) { // if more than 200 dp, it's probably a keyboard...
//            // ... do something here
//            LogUtils.printCloseableInfo(TAG, "====== heightDiff more than 200 dp, it's probably a keyboard... ========");
//        }
//
//    }

    /** dp -> px */
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onSoftKeyboardShown(boolean isShowing) {
        LogUtils.printCloseableInfo(TAG, "isShowing: "+ isShowing);
        if (isShowing){
        }else {
        }
    }
}
