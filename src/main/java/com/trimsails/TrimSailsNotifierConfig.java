package com.trimsails;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("trimsailsnotifier")
public interface TrimSailsNotifierConfig extends Config
{
    @ConfigItem(
            keyName = "enableNotification",
            name = "Enable notifications",
            description = "Send a desktop notification when 'Trim sails' becomes available"
    )
    default boolean enableNotification()
    {
        return true;
    }

    @ConfigItem(
            keyName = "notificationMessage",
            name = "Notification message",
            description = "Text to show in the notification when 'Trim sails' appears"
    )
    default String notificationMessage()
    {
        return "Trim sails is now available!";
    }
}
