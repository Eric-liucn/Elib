package com.github.ericliucn.ericlib.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.concurrent.CompletableFuture;

public class Utils {

    public static Text toText(String str){
        return TextSerializers.FORMATTING_CODE.deserialize(str);
    }

    public static void runCommand(String cmd, CommandSource source){
        Sponge.getCommandManager().process(source, cmd);
    }

    public static void broadcast(String message){
        Sponge.getServer().getBroadcastChannel().send(toText(message));
    }

    public static void broadcast(Text message){
        Sponge.getServer().getBroadcastChannel().send(message);
    }

    public static void sendMessageToPlayer(Player player, String str){

    }
}
