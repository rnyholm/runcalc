package ax.stardust.runcalc.component.watcher;

import android.widget.RadioGroup;

import ax.stardust.runcalc.interaction.container.InteractionContainer;

public class ReferencedRadioGroupWatcher implements RadioGroup.OnCheckedChangeListener {
    private final InteractionContainer interactionContainer;

    public ReferencedRadioGroupWatcher(InteractionContainer interactionContainer) {
        this.interactionContainer = interactionContainer;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        interactionContainer.calculateIfPossible();
    }
}
