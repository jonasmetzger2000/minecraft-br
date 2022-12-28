package de.jonasmetzger.config;

import de.jonasmetzger.dependency.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class DefaultConfiguration {

    @Inject
    ConfigRepository configRepository;

    public void setDefaults() {
        // game
        configRepository.save("game.secondsToStart", "600");

        // groups
        configRepository.save("group.default.prefix", Component.class, Component.empty().color(NamedTextColor.WHITE));
        configRepository.save("group.vip.prefix", Component.class, Component.empty().color(NamedTextColor.GREEN));
        configRepository.save("group.mvp.prefix", Component.class, Component.empty().color(NamedTextColor.BLUE));
        configRepository.save("group.pro.prefix", Component.class, Component.empty().color(NamedTextColor.GOLD));
        configRepository.save("group.media.prefix", Component.class, Component.empty().color(NamedTextColor.AQUA));
        configRepository.save("group.mod_trial.prefix", Component.class, Component.empty().color(NamedTextColor.DARK_PURPLE));
        configRepository.save("group.moderator.prefix", Component.class, Component.empty().color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC));
        configRepository.save("group.admin.prefix", Component.class, Component.empty().color(NamedTextColor.RED));
    }

}
