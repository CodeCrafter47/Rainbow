package joebkt;


import java.io.Serializable;
import java.util.HashMap;


public class _DynRewardInfo implements Serializable {

    public _SerializableLocation Loc = null;
    public String RewardName = "";
    public String AchievementName = "";
    public Boolean OneTimeOnly = Boolean.valueOf(false);
    public int delaySeconds = 60;
    public HashMap<Integer, Integer> Rewards = null;

    public _DynRewardInfo(int x, int y, int z, int dimension) {
        this.Loc = new _SerializableLocation((double) x, (double) y, (double) z,
                dimension, 0.0F, 0.0F);
    }
}
