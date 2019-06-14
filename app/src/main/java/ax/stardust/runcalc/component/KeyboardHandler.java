package ax.stardust.runcalc.component;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class KeyboardHandler implements View.OnFocusChangeListener, View.OnTouchListener {
    private final RunnersKeyboard runnersKeyboard;

    public KeyboardHandler(RunnersKeyboard runnersKeyboard) {
        this.runnersKeyboard = runnersKeyboard;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (KeyboardlessEditText.class.isAssignableFrom(view.getClass())) {
            KeyboardlessEditText keyboardlessEditText = (KeyboardlessEditText) view;
            if (hasFocus) {
                Editable editable = keyboardlessEditText.getText();
                InputConnection inputConnection = view.onCreateInputConnection(new EditorInfo());
                this.runnersKeyboard.show();
                this.runnersKeyboard.setSeparator(keyboardlessEditText.getInput().getSeparator());
                this.runnersKeyboard.enableDeleteButton(editable != null && !editable.toString().isEmpty());
                this.runnersKeyboard.setInputConnection(inputConnection);
            } else {
                this.runnersKeyboard.delayedHide();
            }
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (KeyboardlessEditText.class.isAssignableFrom(view.getClass())) {
            if (runnersKeyboard.getVisibility() != View.VISIBLE) {
                KeyboardlessEditText keyboardlessEditText = (KeyboardlessEditText) view;
                Editable editable = keyboardlessEditText.getText();
                InputConnection inputConnection = view.onCreateInputConnection(new EditorInfo());
                this.runnersKeyboard.show();
                this.runnersKeyboard.setSeparator(keyboardlessEditText.getInput().getSeparator());
                this.runnersKeyboard.enableDeleteButton(editable != null && !editable.toString().isEmpty());
                this.runnersKeyboard.setInputConnection(inputConnection);
            }
        }
        // let the rest of the framework handle this event also
        return false;
    }
}
