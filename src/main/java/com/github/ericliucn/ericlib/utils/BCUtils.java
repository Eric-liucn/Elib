package com.github.ericliucn.ericlib.utils;

import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BCUtils {

    private static ChannelBinding.RawDataChannel channel;
    private static List<CompletableFuture<List<String>>> getServers = new ArrayList<>();

    private static ChannelBinding.RawDataChannel getChannel(Object plugin){
        if (channel == null){
            channel = Sponge.getChannelRegistrar().getOrCreateRaw(plugin, "BungeeCord");
            channel.addListener(new ChannelListener());
        }
        return channel;
    }

    public static void sendPlayerToServer(Object instance, Player player, String serverName){
        getChannel(instance).sendTo(player, channelBuf -> channelBuf.writeUTF("Connect").writeUTF(serverName));
    }

    public static void sendOtherPlayerToServer(Object instance,Player src, Player target, String serverName){
        getChannel(instance).sendTo(src, channelBuf -> channelBuf.writeUTF("ConnectOther").writeUTF(target.getName()).writeUTF(serverName));
    }

    public static CompletableFuture<List<String>> getServerList(Object instance, Player player){
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        getChannel(instance).sendTo(player, channelBuf -> channelBuf.writeUTF("GetServers"));
        getServers.add(completableFuture);
        return completableFuture;
    }

    public static class ChannelListener implements RawDataListener {

        @Override
        public void handlePayload(ChannelBuf data, RemoteConnection connection, Platform.Type side) {
            String channel = data.readUTF();
            if (channel.equals("BungeeCord")){
                String argument = data.readUTF();
                switch (argument){
                    case "GetSevers":
                        if (getServers.size() != 0){
                            getServers.get(0).complete(Arrays.asList(data.readUTF().split(", ")));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
