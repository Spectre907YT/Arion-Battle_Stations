package data.hullmods;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class ncd_aegis extends BaseHullMod {

	public static float DAMAGE_BONUS = 50f;


	public static float MISSILE_SPEED_BONUS = 25f;
	public static float MISSILE_RANGE_MULT = 0.8f;
	public static float MISSILE_ACCEL_BONUS = 150f;
	public static float MISSILE_RATE_BONUS = 50f;
	public static float MISSILE_TURN_ACCEL_BONUS = 150f;
	
	public static float EW_PENALTY_MULT = 0.5f;
	public static float EW_PENALTY_REDUCTION = 5f;
	
	public static float ECCM_CHANCE = 0.5f;
	public static float GUIDANCE_IMPROVEMENT = 1f;
	
	public static float SMOD_ECCM_CHANCE = 1f;
	public static float SMOD_EW = 0f;


	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 1f);
		mag.put(HullSize.DESTROYER, 2f);
		mag.put(HullSize.CRUISER, 3f);
		mag.put(HullSize.CAPITAL_SHIP, 4f);
	}
	

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		boolean sMod = isSMod(stats);
		stats.getEccmChance().modifyFlat(id, sMod ? SMOD_ECCM_CHANCE : ECCM_CHANCE);
		stats.getMissileGuidance().modifyFlat(id, GUIDANCE_IMPROVEMENT);
		
		stats.getMissileMaxSpeedBonus().modifyPercent(id, MISSILE_SPEED_BONUS);
		stats.getMissileWeaponRangeBonus().modifyMult(id, MISSILE_RANGE_MULT);
		stats.getMissileAccelerationBonus().modifyPercent(id, MISSILE_ACCEL_BONUS);
		stats.getMissileMaxTurnRateBonus().modifyPercent(id, MISSILE_RATE_BONUS);
		stats.getMissileTurnAccelerationBonus().modifyPercent(id, MISSILE_TURN_ACCEL_BONUS);


		stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(id, (Float) mag.get(hullSize));
		

		if (sMod) {
			stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MOD).modifyMult(id, SMOD_EW);
		} else {
			stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MOD).modifyMult(id, EW_PENALTY_MULT);
		}

		stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
		
	}

	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		boolean sMod = isSMod(ship);
		
		if (sMod) {
			List weapons = ship.getAllWeapons();
			Iterator iter = weapons.iterator();
			while (iter.hasNext()) {
				WeaponAPI weapon = (WeaponAPI)iter.next();

				boolean sizeMatches = weapon.getSize() == WeaponSize.SMALL;
				
				if (sizeMatches && weapon.getType() != WeaponType.MISSILE && !weapon.hasAIHint(AIHints.STRIKE)) {
					weapon.setPD(true);
				}
			}
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(DAMAGE_BONUS) + "%";

		if (index == 1) return "" + (int) (ECCM_CHANCE * 100f) + "%";
		if (index == 2) return "" + (int) (MISSILE_SPEED_BONUS) + "%";
		if (index == 3) return "" + (int) (MISSILE_RATE_BONUS) + "%";
		if (index == 4) return "" + (int) ((1f - EW_PENALTY_MULT) * 100f) + "%";

		if (index == 5) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue() + "%";
		if (index == 6) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
		if (index == 7) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 8) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		return null;
	}


}




