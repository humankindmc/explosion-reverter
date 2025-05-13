package me.rileycalhoun.explosionreverter.explosions;

import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;

public enum ExplosionCause {

    CREEPER, TNT, OTHER;

    public static ExplosionCause getExplosionCause(Entity exploder) {
        if (exploder instanceof Creeper) {
            return CREEPER;
        } if (exploder instanceof TNT) {
            return TNT;
        }

        return OTHER;
    }

}
