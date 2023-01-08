package de.jonasmetzger.minecraft.commands;

import de.jonasmetzger.config.ConfigRepository;
import de.jonasmetzger.dependency.Inject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class SaveItemCommand extends PlayerCommand {

    @Inject
    ConfigRepository configRepository;

    public SaveItemCommand() {
        super("admin", "saveitem", "saves an itemstack in the mainhand", "/saveitem <key>");
    }

    @Override
    protected Feedback onCommand(Player player, String[] args) {
        if (args.length > 0) {
            final PlayerInventory inventory = player.getInventory();
            configRepository.save(args[0], inventory.getItemInMainHand());
            return Feedback.success().message("Sucessfully saved ItemStack to Database");
        }
        return Feedback.error().message("Please provide an key");
    }
}
