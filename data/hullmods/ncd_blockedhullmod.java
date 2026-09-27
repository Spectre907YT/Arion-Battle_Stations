package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class ncd_blockedhullmod extends BaseHullMod
{
    @Override
    public String getDescriptionParam(int index, HullSize hullSize)
    {
        if (index == 0)
        {
            return "WARNING";
        }
        return null;
    }
}
