package com.github.ericliucn.ericlib.config;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import ninja.leaping.configurate.objectmapping.serialize.ScalarSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public abstract class BasicConfigLoader<C extends BasicConfig> {

    public static BasicConfigLoader<? extends BasicConfig> instance;
    private Path configFile;
    private final ConfigurationOptions options = ConfigurationOptions.defaults();;
    public CommentedConfigurationNode node;
    protected C config;

    @SafeVarargs
    public <T> BasicConfigLoader(Path configDir, boolean shouldCopyDefault, ScalarSerializer<T>... serializers)
            throws IOException, ObjectMappingException {
        instance = this;
        this.setConfigFile(configDir);
        this.setOptions(shouldCopyDefault, serializers);
        this.setNode();
        this.load();
    }

    private void setConfigFile(Path configDir) throws IOException {
        if (!Files.exists(configDir)){
            Files.createDirectory(configDir);
        }
        configFile = configDir.resolve("setting.conf");
    }

    @SafeVarargs
    private final <T> void setOptions(boolean shouldCopyDefault, ScalarSerializer<T>... serializers){
        this.options.withShouldCopyDefaults(shouldCopyDefault);
        TypeSerializerCollection collection = options.getSerializers();
        Arrays.stream(serializers).map(collection::register).close();
        options.withSerializers(collection);
    }

    private void setNode() throws IOException {
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader
                .builder()
                .setPath(configFile)
                .setDefaultOptions(options)
                .build();
        node = loader.load();
    }

    public abstract void load() throws ObjectMappingException;

    public C getConfig() {
        return config;
    }

}
