package com.trimsails;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ClientTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.Arrays;

@Slf4j
@PluginDescriptor(
        name = "Trim Sails Notifier",
        description = "Notifies you when the 'Trim sails' option becomes available",
        tags = {"sailing", "trim", "sails", "notification"}
)
public class TrimSailsNotifierPlugin extends Plugin
{
    private static final String TRIM_SAILS_OPTION = "Trim sails";

    @Inject
    private Client client;

    @Inject
    private Notifier notifier;

    @Inject
    private TrimSailsNotifierConfig config;

    /**
     * Track whether 'Trim sails' was available on the previous tick,
     * so we only notify on the transition from not-available -> available.
     */
    private boolean trimSailsAvailableLastTick = false;

    @Override
    protected void startUp() throws Exception
    {
        trimSailsAvailableLastTick = false;
        log.info("Trim Sails Notifier started");
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("Trim Sails Notifier stopped");
    }

    @Subscribe
    public void onClientTick(ClientTick event)
    {
        if (client == null || client.getMenuEntries() == null)
        {
            return;
        }

        boolean trimSailsAvailableNow = Arrays.stream(client.getMenuEntries())
                .map(MenuEntry::getOption)
                .filter(option -> option != null)
                .anyMatch(option -> option.equalsIgnoreCase(TRIM_SAILS_OPTION));

        // Only notify when it *becomes* available
        if (trimSailsAvailableNow && !trimSailsAvailableLastTick && config.enableNotification())
        {
            notifier.notify(config.notificationMessage());
        }

        trimSailsAvailableLastTick = trimSailsAvailableNow;
    }

    @Provides
    TrimSailsNotifierConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TrimSailsNotifierConfig.class);
    }
}
