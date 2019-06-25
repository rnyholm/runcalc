package ax.stardust.runcalc.component.watcher;

import android.widget.CompoundButton;

import ax.stardust.runcalc.interaction.container.InteractionContainer;

public class ReferencedSwitchWatcher implements CompoundButton.OnCheckedChangeListener {
    private final InteractionContainer interactionContainer;

    public ReferencedSwitchWatcher(InteractionContainer interactionContainer) {
        this.interactionContainer = interactionContainer;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        interactionContainer.calculateIfPossible();
    }
}
