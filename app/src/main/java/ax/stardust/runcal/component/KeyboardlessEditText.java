package ax.stardust.runcal.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * EditText which suppresses IME show up.
 *
 * This is the same as an native EditText, except that no soft keyboard
 * will appear when user clicks on widget. This is modeled after the keyboard
 * in the default Android KitKat dialer app.
 * Proudly snatched from: https://github.com/danialgoodwin/android-simply-tone-generator/blob/master/app/src/main/java/net/simplyadvanced/simplytonegenerator/widget/KeyboardlessEditText.java
 *
 * As this stuff is copied and pasted(it was a pain to find any good stuff about this on stack overflow) I'm not 100%
 * sure of what every little code snippet are doing. Therefore this is to be used with a bit caution.
 */
public class KeyboardlessEditText extends AppCompatEditText {
    private static final Method SHOW_SOFT_INPUT_ON_FOCUS = getMethod(EditText.class, "setShowSoftInputOnFocus", boolean.class);

    public KeyboardlessEditText(Context context) {
        super(context);
        initialize();
    }

    public KeyboardlessEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public KeyboardlessEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        synchronized (this) {
            setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            setFocusableInTouchMode(true);
        }

        // Need to show the cursor when user interacts with EditText so that the edit operations
        // still work. Without the cursor, the edit operations won't appear.
        setOnClickListener(onClickListener);
        setOnLongClickListener(onLongClickListener);

        setShowSoftInputOnFocus(false); // This is a hidden method in TextView.
        reflexSetShowSoftInputOnFocus(false); // Workaround.

        // Ensure that cursor is at the end of the input box when initialized. Without this, the
        // cursor may be at index 0 when there is text added via layout XML.
        setSelection(getText().length());
    }

    private OnClickListener onClickListener = v -> setCursorVisible(true);

    private OnLongClickListener onLongClickListener = v -> {
        setCursorVisible(true);
        return false;
    };

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        hideKeyboard();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        final boolean ret = super.onTouchEvent(event);

        // Must be done after super.onTouchEvent()
        hideKeyboard();
        return ret;
    }

    private void hideKeyboard() {
        // Hide system keyboard
        final InputMethodManager inputMethodManager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null && inputMethodManager.isActive(this)) {
            inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        }
    }

    private void reflexSetShowSoftInputOnFocus(boolean show) {
        if (SHOW_SOFT_INPUT_ON_FOCUS != null) {
            invokeMethod(SHOW_SOFT_INPUT_ON_FOCUS, this, show);
        } else {
            // Use fallback method. Not tested.
            hideKeyboard();
        }
    }

    /**
     * Returns method if available in class or superclass (recursively),
     * otherwise returns null.
     */
    public static Method getMethod(Class<?> cls, String methodName, Class<?>... parametersType) {
        Class<?> superClass = cls.getSuperclass();
        while (superClass != Object.class) {
            try {
                return superClass.getDeclaredMethod(methodName, parametersType);
            } catch (NoSuchMethodException e) {
                // Just super it again
            }
            superClass = superClass.getSuperclass();
        }
        return null;
    }

    /**
     * Returns results if available, otherwise returns null.
     */
    public static Object invokeMethod(Method method, Object receiver, Object... args) {
        final String LOG_TAG = KeyboardlessEditText.class.getSimpleName();

        try {
            return method.invoke(receiver, args);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Safe invoke fail - Invalid args", e);
        } catch (IllegalAccessException e) {
            Log.e(LOG_TAG, "Safe invoke fail - Invalid access", e);
        } catch (InvocationTargetException e) {
            Log.e(LOG_TAG, "Safe invoke fail - Invalid target", e);
        }

        return null;
    }
}
