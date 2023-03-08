package me.thesnipe12.listeners;

import me.thesnipe12.PluginConstants;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ListenersConstants {
    static final TextComponent
            NUPD_MESSAGE_PART1 = new TextComponent("There is a new version of the plugin available! Go to "),
            NUPD_MESSAGE_PART2 = new TextComponent("\"" + PluginConstants.PLUGIN_LINK + ""),
            NUPD_MESSAGE_PART3 = new TextComponent(" to download it!");

    static {
        NUPD_MESSAGE_PART1.setColor(net.md_5.bungee.api.ChatColor.RED);
        NUPD_MESSAGE_PART2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        NUPD_MESSAGE_PART2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, PluginConstants.PLUGIN_LINK));
        NUPD_MESSAGE_PART2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the link!")));
        NUPD_MESSAGE_PART3.setColor(net.md_5.bungee.api.ChatColor.RED);
    }

}
