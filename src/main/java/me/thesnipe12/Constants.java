package me.thesnipe12;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Constants {
    public static final TextComponent UPD_MESSAGE_PART1 = new TextComponent("There is a new version of the plugin available! Go to "),
    UPD_MESSAGE_PART2 = new TextComponent("\"https://www.spigotmc.org/resources/simplecl.101603/\""),
    UPD_MESSAGE_PART3 = new TextComponent(" to download it!");
    public static final
    String BORDER_DISABLED_FAIL = "BorderHopping is disabled but WGRegionEvents is not found! got to https://www.spigotmc.org/resources/worldguard-region-events-updated.61490/ to install it",
    BORDER_DISABLED_SUCCESS = "WGRegionEvents found! BorderHopping disabled!";
    static{
        Constants.UPD_MESSAGE_PART1.setColor(net.md_5.bungee.api.ChatColor.RED);
        Constants.UPD_MESSAGE_PART2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        Constants.UPD_MESSAGE_PART2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                "https://www.spigotmc.org/resources/simplecl.101603/"));
        Constants.UPD_MESSAGE_PART2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open URL")));
        Constants.UPD_MESSAGE_PART3.setColor(net.md_5.bungee.api.ChatColor.RED);
    }
}
