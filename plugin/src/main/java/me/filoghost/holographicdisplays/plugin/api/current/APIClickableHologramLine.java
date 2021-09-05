/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.ClickableHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.ClickCallbackProvider;
import org.bukkit.entity.Player;

public interface APIClickableHologramLine extends ClickableHologramLine, APIHologramLine, ClickCallbackProvider {

    @Override
    default boolean hasClickCallback() {
        return getClickListener() != null;
    }

    @Override
    default void invokeClickCallback(Player player) {
        try {
            ClickListener clickListener = getClickListener();
            if (clickListener != null) {
                clickListener.onClick(new SimpleHologramLineClickEvent(player));
            }
        } catch (Throwable t) {
            logClickCallbackException(getCreatorPlugin(), player, t);
        }
    }

}
