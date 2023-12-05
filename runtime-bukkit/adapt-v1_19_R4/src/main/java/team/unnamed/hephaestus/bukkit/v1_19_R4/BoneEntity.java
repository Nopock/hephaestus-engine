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

import com.mojang.math.Transformation;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftItemDisplay;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.hephaestus.Bone;
import team.unnamed.hephaestus.bukkit.BoneView;
import team.unnamed.hephaestus.util.Quaternion;

public class BoneEntity
    extends Display.ItemDisplay implements BoneView {

    private final MinecraftModelEntity view;
    private final Bone bone;

    BoneEntity(MinecraftModelEntity view, Bone bone, Level world) {
        super(EntityType.ITEM_DISPLAY, world);
        this.view = view;
        this.bone = bone;
    }

    @Override
    public Bone bone() {
        return this.bone;
    }

    @Override
    public void update(Vector3Float position, Quaternion rotation, Vector3Float scale) {
        super.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void customName(Component customName) {
        super.setCustomName(PaperAdventure.asVanilla(customName));
    }

    @Override
    public Component customName() {
        return PaperAdventure.asAdventure(super.getCustomName());
    }

    @Override
    public void customNameVisible(boolean visible) {
        super.setCustomNameVisible(visible);
    }

    @Override
    public boolean customNameVisible() {
        return super.isCustomNameVisible();
    }

    @Override
    public void colorize(Color color) {
        ItemStack item = super.getItemStack().asBukkitCopy();
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);

        item.setItemMeta(meta);

        setItemStack(CraftItemStack.asNMSCopy(item));
    }
}