package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class ncd_advconstruction extends BaseHullMod {
	
	public static final float CASUALTY_REDUCTION = 80f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		stats.getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).modifyFlat(id, 1000f);
		stats.getBreakProb().modifyMult(id, 0f);

		stats.getCrewLossMult().modifyMult(id, 1f - (CASUALTY_REDUCTION) * 0.01f);

	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) CASUALTY_REDUCTION + "%";
		return null;
	}

}
