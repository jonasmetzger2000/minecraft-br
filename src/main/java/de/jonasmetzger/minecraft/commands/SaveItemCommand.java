package de.jonasmetzger.minecraft.commands;

import de.jonasmetzger.config.ConfigRepository;
import de.jonasmetzger.dependency.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class SaveItemCommand extends PlayerCommand {

    @Inject
    ConfigRepository configRepository;

    public SaveItemCommand() {
        super("admin", "saveitem", "saves an itemstack in the mainhand", "/saveitem <key>");
    }

    @Override
    protected boolean onCommand(Player player, String[] args) {
        if (args.length > 0) {
            final PlayerInventory inventory = player.getInventory();
            configRepository.save(args[0], inventory.getItemInMainHand());
            player.sendMessage(Component.text("Sucessfully saved ItemStack to Database", NamedTextColor.GREEN));
            return true;
        }
        return false;
    }
}
