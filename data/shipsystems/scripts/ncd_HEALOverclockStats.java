package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class ncd_HEALOverclockStats extends BaseShipSystemScript {

	public static final float REGEN_BONUS = 3f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		float mult = 1f + REGEN_BONUS * effectLevel;
		stats.getEnergyAmmoRegenMult().modifyMult(id, mult);
		
//		ShipAPI ship = (ShipAPI)stats.getEntity();
//		ship.blockCommandForOneFrame(ShipCommand.FIRE);
//		ship.setHoldFireOneFrame(true);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getEnergyAmmoRegenMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + REGEN_BONUS * effectLevel;
		float bonusPercent = (int) ((mult - 1f) * 100f);
		if (index == 0) {
			return new StatusData("heal recharge +" + (int) bonusPercent + "%", false);
		}
		return null;
	}
}
