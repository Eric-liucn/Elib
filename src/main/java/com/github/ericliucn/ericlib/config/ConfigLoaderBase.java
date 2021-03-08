package com.github.ericliucn.ericlib.config;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.ScalarSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public abstract class ConfigLoaderBase {

    public static ConfigLoaderBase instance;
    private final Path configFile;
    private final ConfigurationOptions options;
    public ConfigurationLoader<CommentedConfigurationNode> loader;

    @SafeVarargs
    public <T> ConfigLoaderBase(Path configDir,
                                boolean shouldCopyDefault,
                                ScalarSerializer<T>... serializers) throws IOException {
        instance = this;
        configFile = configDir.resolve("setting.conf");
        createConfigDir(configDir);
        options = ConfigurationOptions.defaults();
        this.addCustomSerializer(serializers);
        options.withShouldCopyDefaults(shouldCopyDefault);
        this.setLoader();
    }

    private void createConfigDir(Path configDir) throws IOException {
        if (!Files.exists(configDir)){
            Files.createDirectory(configDir);
        }
    }

    @SafeVarargs
    private final <T> void addCustomSerializer(ScalarSerializer<T>... serializers){
        TypeSerializerCollection collection = options.getSerializers();
        Arrays.stream(serializers).map(collection::register).close();
        options.withSerializers(collection);
    }

    private void setLoader(){
        loader = HoconConfigurationLoader
                .builder()
                .setPath(configFile)
                .setDefaultOptions(options)
                .build();
    }

    public abstract void load();



}
