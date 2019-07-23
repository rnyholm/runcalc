package ax.stardust.runcalc.egg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;

import java.util.Objects;

import ax.stardust.runcalc.R;

public class EasterEgg {
    public static final String TRIGGER_TEXT = "06:10:86";

    public static void show(Context context) {
        View dialogView = View.inflate(context, R.layout.easter_egg_dialog, null);
        Dialog easterEggDialog = new Dialog(context, R.style.EasterEggDialogStyle);
        easterEggDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        easterEggDialog.setContentView(dialogView);
        easterEggDialog.setOnShowListener(dialogInterface -> revealShow(context, dialogView, true, null));
        easterEggDialog.setOnKeyListener((dialogInterface, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                revealShow(context, dialogView, false, easterEggDialog);
                return true; // consider back key pressed handled
            }
            return false;
        });

        ImageView closeImageView = easterEggDialog.findViewById(R.id.close_easter_egg_dialog_iv);
        closeImageView.setOnClickListener(view -> revealShow(context, dialogView, false, easterEggDialog));

        Objects.requireNonNull(easterEggDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        easterEggDialog.show();
    }

    private static void revealShow(Context context, View dialogView, boolean showDialog, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.easter_egg_dialog_rl);

        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        int centerX = context.getResources().getDisplayMetrics().widthPixels / 2;
        int centerY = context.getResources().getDisplayMetrics().heightPixels / 2;

        if (showDialog) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(700);
            revealAnimator.start();
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, endRadius, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            animator.setDuration(700);
            animator.start();
        }
    }
}
