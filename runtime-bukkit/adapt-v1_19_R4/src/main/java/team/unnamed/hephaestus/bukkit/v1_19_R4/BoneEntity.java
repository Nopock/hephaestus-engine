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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftItemDisplay;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.hephaestus.Bone;
import team.unnamed.hephaestus.bukkit.BoneView;
import team.unnamed.hephaestus.util.Quaternion;

public class BoneEntity
    extends Display.ItemDisplay implements BoneView {

    private static final net.minecraft.world.item.ItemStack BASE_ITEM = getItemBase();

    private final MinecraftModelEntity view;
    private final Bone bone;

    protected final float modelScale;

    BoneEntity(
            MinecraftModelEntity view,
            Bone bone,
            Level world,
            Vector3Float initialPosition,
            Quaternion initialRotation,
            float modelScale
    ) {
        super(EntityType.ITEM_DISPLAY, world);
        this.view = view;
        this.bone = bone;
        this.modelScale = modelScale;

        initialize(initialPosition, initialRotation);
    }

    protected void initialize(Vector3Float initialPosition, Quaternion initialRotation) {
        super.setItemTransform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
        super.setInterpolationDuration(3);
        super.setViewRange(1000);
        super.setNoGravity(true);

        ItemStack stack = BASE_ITEM.copy().asBukkitCopy();

        stack.editMeta(itemMeta -> {
            itemMeta.setCustomModelData(bone.customModelData());
        });

        super.setItemStack(net.minecraft.world.item.ItemStack.fromBukkitCopy(stack));

        update(initialPosition, initialRotation, Vector3Float.ONE);
    }

    @Override
    public Bone bone() {
        return this.bone;
    }

    @Override
    public void update(Vector3Float position, Quaternion rotation, Vector3Float scale) {
        super.setDeltaMovement(Vec3.ZERO);
        super.setTransformation(
                new Transformation(
                        new Vector3f(position.x(), position.y(), position.z()).mul(modelScale * bone.scale()),
                        null,
                        new Vector3f(scale.x(), scale.y(), scale.z()),
                        new Quaternionf(rotation.x(), rotation.y(), rotation.z(), rotation.w())
                )
        );
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

    private static net.minecraft.world.item.ItemStack getItemBase() {
        ItemStack item = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setColor(Color.fromRGB(255, 255, 255));
        item.setItemMeta(meta);

        return CraftItemStack.asNMSCopy(item);
    }
}