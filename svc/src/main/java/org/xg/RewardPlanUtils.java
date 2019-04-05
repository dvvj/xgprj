package org.xg;

import org.xg.dbModels.MRewardPlan;
import org.xg.dbModels.MRewardPlanMap;
import org.xg.gnl.CachedData;

import java.util.HashMap;
import java.util.Map;

import static org.xg.SvcUtils.getDbOps;

public class RewardPlanUtils {

  private final static CachedData<Map<String, MRewardPlan>> rewardPlansCache = CachedData.createJ(
    () -> getDbOps().allRewardPlansToMapJ()
  );

  public static Map<String, MRewardPlan> getRewardPlans() {
    return rewardPlansCache.getData();
  }

  public static void updateRewardPlans() {
    rewardPlansCache.update();
  }

  private final static CachedData<Map<String, MRewardPlanMap>> rewardPlanMapCache = CachedData.createJ(
    () -> getDbOps().allActiveRewardPlansJ()
  );

  public static Map<String, MRewardPlanMap> getRewardPlanMaps() {
    return rewardPlanMapCache.getData();
  }

  public static MRewardPlan[] getRewardPlansCreatedBy(String creatorId) {
    Map<String, MRewardPlan> plans = new HashMap<>(getRewardPlans());
    MRewardPlan[] res = plans.values().stream().filter(p -> p.creator().equals(creatorId))
      .toArray(MRewardPlan[]::new);
    return res;
  }

  public static boolean addRewardPlanMap(MRewardPlanMap rpm) {
    getDbOps().addRewardPlanMap(rpm);
    rewardPlanMapCache.update();
    return true;
  }
}
