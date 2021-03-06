/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.duckchat.bukkit.command;

import com.google.common.base.Charsets;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.cyberiantiger.minecraft.duckchat.bukkit.Main;
import org.jgroups.Address;

/**
 *
 * @author antony
 */
public class RexecSubCommand extends SubCommand<Main> {

    public RexecSubCommand(Main main) {
        super(main, "duckchat.rexec");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String... args) {
        return null;
    }

    @Override
    protected void doCommand(CommandSender sender, String... args) throws SubCommandException {
        if (!sender.hasPermission("duckchat.rexec")) {
            throw new PermissionException("duckchat.rexec");
        }
        if (args.length < 1) {
            throw new UsageException();
        }
        String target = args[0];
        Address targetAddress = plugin.getState().getServerAddress(target);
        if(targetAddress == null) {
            if (!"-g".equals(target)) {
                throw new UsageException();
            }
        }
        StringBuilder tmp = new StringBuilder();
        int last = args.length - 1;
        for (int i = 1; i < last; i++) {
            tmp.append(args[i]);
            tmp.append(' ');
        }
        if (args.length > 1) {
            tmp.append(args[last]);
        }
        sender.sendMessage("Executing command remotely at " + (targetAddress == null ? "all servers" : target) + ".");
        plugin.sendPluginMessage(targetAddress, "command", tmp.toString().getBytes(Charsets.UTF_8));
    }

    @Override
    public String getName() {
        return "rexec";
    }
    
}
