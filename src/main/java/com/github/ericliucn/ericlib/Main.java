package com.github.ericliucn.ericlib;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "ericlib",
        name = "Ericlib",
        description = "Ericlib",
        authors = {
                "Eric12324"
        }
)
public class Main {

    public static Main instance;

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;
    }
}
