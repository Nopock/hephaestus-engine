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
package team.unnamed.hephaestus.bukkit.v1_19_R4;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.BukkitModelEngine;
import team.unnamed.hephaestus.bukkit.ModelEntity;
import team.unnamed.hephaestus.view.track.ModelViewTracker;

public class BukkitV1_19_R4ModelEngine implements BukkitModelEngine {

    public ModelEntity createView(EntityType entityType, Model model, World world, Location position, float scale) {
        ModelEntity modelEntity = new ModelEntity(entityType, model, scale);
        modelEntity.setInstance(world, position);
        return modelEntity;
    }

    public ModelEntity createView(Model model, World instance, Location position, float scale) {
        return createView(EntityType.ARMOR_STAND, model, instance, position, scale);
    }

    public ModelEntity createView(Model model, World instance, Location position) {
        return createView(model, instance, position, 1);
    }

    public ModelEntity createViewAndTrack(Model model, World instance, Location position, float scale) {
        ModelEntity view = createView(model, instance, position, scale);
        tracker().startGlobalTracking(view);
        return view;
    }

    @Override
    public ModelEntity createView(Model model, Location location) {
        return createView(model, location.getWorld(), location);
    }

    @Override
    public ModelEntity createViewAndTrack(Model model, Location location) {
        return createViewAndTrack(model, location.getWorld(), location, 1);
    }

    @Override
    public ModelViewTracker<Player> tracker() {
        return MinestomModelViewTracker.INSTANCE;
    }

    public static MinestomModelEngine minestom() {
        return new MinestomModelEngine();
    }
}
