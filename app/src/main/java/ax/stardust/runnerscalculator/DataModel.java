package ax.stardust.runnerscalculator;

public class DataModel {
    private String paceToSpeed = "";

    private String paceToSpeedNew = "";

    public boolean hasChanged() {
        return !equals(paceToSpeed, paceToSpeedNew);
    }

    public String getPaceToSpeed() {
        return paceToSpeed;
    }

    public void setPaceToSpeedNew(String paceToSpeedNew) {
        this.paceToSpeedNew = paceToSpeedNew;
    }

    private boolean equals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        }

        return s1.equals(s2);
    }
}
