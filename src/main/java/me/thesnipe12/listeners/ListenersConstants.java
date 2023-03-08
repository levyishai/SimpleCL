package me.thesnipe12.listeners;

import me.thesnipe12.PluginConstants;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ListenersConstants {
    static final TextComponent
            NOT_UP_TO_DATE_MESSAGE_PART1 = new TextComponent("There is a new version of the plugin available! Go to "),
            NOT_UP_TO_DATE_MESSAGE_PART2 = new TextComponent("\"" + PluginConstants.PLUGIN_LINK + ""),
            NOT_UP_TO_DATE_MESSAGE_PART3 = new TextComponent(" to download it!");

    static {
        NOT_UP_TO_DATE_MESSAGE_PART1.setColor(net.md_5.bungee.api.ChatColor.RED);
        NOT_UP_TO_DATE_MESSAGE_PART2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        NOT_UP_TO_DATE_MESSAGE_PART2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, PluginConstants.PLUGIN_LINK));
        NOT_UP_TO_DATE_MESSAGE_PART2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the link!")));
        NOT_UP_TO_DATE_MESSAGE_PART3.setColor(net.md_5.bungee.api.ChatColor.RED);
    }

}
