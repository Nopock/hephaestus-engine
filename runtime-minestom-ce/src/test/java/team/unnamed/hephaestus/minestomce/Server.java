/*
 * This file is part of hephaestus-engine, licensed under the MIT license
 *
 * Copyright (c) 2021-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.hephaestus.minestomce;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.server.ResourcePackRequestHandler;
import team.unnamed.creative.server.ResourcePackServer;

public class Server {

    public static void main(String[] args) throws Exception {
        MinecraftServer server = MinecraftServer.init();
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        instance.setChunkSupplier(LightingChunk::new);

        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(instance);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        // Register model interaction listener
        ModelClickListener.register(eventHandler);

        ModelRegistry registry = new ModelRegistry();
        registry.loadModelFromResource("redstone_monstrosity.bbmodel");
        registry.loadModelFromResource("dragon.bbmodel");
        registry.loadModelFromResource("geometry.bbmodel");

        BuiltResourcePack resourcePack = ResourcePackFactory.create(registry);
        MinecraftServer.getCommandManager().register(new HephaestusCommand(registry, resourcePack));

        ModelEntity testView = MinestomModelEngine.minestom().createViewAndTrack(
                registry.model("dragon"),
                instance,
                new Pos(0, 43, 0),
                0.25f
        );

        registry.view("test", testView);

        // create resource pack server, start and schedule stop
        ResourcePackServer resourcePackServer = ResourcePackServer.builder()
                .address("127.0.0.1", 7270)
                .handler(ResourcePackRequestHandler.of(resourcePack))
                .build();

        resourcePackServer.start();
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> resourcePackServer.stop(0));

        // start minecraft server
        server.start("127.0.0.1", 25565);
    }
}
