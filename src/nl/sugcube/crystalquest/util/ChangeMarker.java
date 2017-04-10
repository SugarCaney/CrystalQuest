package nl.sugcube.crystalquest.util;

/**
 * @author SugarCaney
 */
public class ChangeMarker {

    private boolean changed = false;

    public void markChange() {
        this.changed = true;
    }

    public boolean isChanged() {
        return changed;
    }
}
