package ax.stardust.runcal.component;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

import ax.stardust.runcal.R;

public class RunnersKeyboard extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final int HIDE_DELAY = 25;
    private static final int CONTINUOUS_DELETE_DELAY = 60;

    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonSeparator;
    private Button buttonDelete;

    private final SparseArray<String> keyValues = new SparseArray<>();
    private InputConnection inputConnection;

    private final Handler hideDelayHandler = new Handler();
    private final Handler continuousDeleteHandler = new Handler();
    private final Runnable continuousDeleteAction = new Runnable() {
        @Override
        public void run() {
            actionDelete();
            continuousDeleteHandler.postDelayed(continuousDeleteAction, CONTINUOUS_DELETE_DELAY);
        }
    };

    public RunnersKeyboard(Context context) {
        this(context, null, 0);
    }

    public RunnersKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RunnersKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        inflateLayout(context);
        findViews();
        setListeners();
        setKeyValues();
    }

    private void inflateLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.runners_keyboard, this, true);
    }

    private void findViews() {
        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        buttonSeparator = findViewById(R.id.button_separator);
        buttonDelete = findViewById(R.id.button_del);
    }

    private void setListeners() {
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonSeparator.setOnClickListener(this);
        // delete button needs this many listeners in order to handle long press delete
        buttonDelete.setOnClickListener(this);
        buttonDelete.setOnLongClickListener(this);
    }

    private void setKeyValues() {
        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");
        // Defaults to ':' separator but it will be changed at runtime
        keyValues.put(R.id.button_separator, ":");
    }

    @Override
    public boolean onLongClick(View view) {
        if (inputConnection != null) {
            if (view != null) {
                if (R.id.button_del == view.getId()) {
                    continuousDeleteHandler.post(continuousDeleteAction);
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (inputConnection != null) {
            if (R.id.button_del == view.getId()) {
                continuousDeleteHandler.removeCallbacks(continuousDeleteAction);
                actionDelete();
            } else {
                String keyValue = keyValues.get(view.getId());
                inputConnection.commitText(keyValue, 1);
            }
        }
    }

    public void setInputConnection(InputConnection inputConnection) {
        this.inputConnection = inputConnection;
    }

    public void setSeparator(String separator) {
        keyValues.put(R.id.button_separator, separator);
        buttonSeparator.setText(separator);
    }

    public void enableDeleteButton(boolean enable) {
        buttonDelete.setEnabled(enable);
        // Nothing more to delete in field
        continuousDeleteHandler.removeCallbacks(continuousDeleteAction);
    }

    public void show() {
        hideDelayHandler.removeCallbacksAndMessages(null);
        setVisibility(View.VISIBLE);
    }

    public void delayedHide() {
        hideDelayHandler.postDelayed(this::hide, HIDE_DELAY);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    /**
     * DON'T EVER, EVER call this before you have checked that:
     * - inputConnection is not null
     * - calling method has a view that's not null
     * - calling method has a view with id: R.id.button_del
     */
    private void actionDelete() {
        CharSequence selectedText = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(selectedText)) {
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            inputConnection.commitText("", 1);
        }
    }
}