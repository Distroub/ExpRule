package com.exprule;

import org.bukkit.block.Block;
import org.bukkit.block.SculkCatalyst;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

final class PaperCatalystAccess {
    private final Method getBlockEntity;
    private final Method getListener;
    private final Method getSpreader;
    private final Method addCursors;
    private final Constructor<?> blockPos;

    PaperCatalystAccess() throws ReflectiveOperationException {
        Class<?> catalystState = Class.forName("org.bukkit.craftbukkit.block.CraftSculkCatalyst");
        getBlockEntity = catalystState.getMethod("getBlockEntity");
        Class<?> catalystEntity = Class.forName("net.minecraft.world.level.block.entity.SculkCatalystBlockEntity");
        getListener = catalystEntity.getMethod("getListener");
        getSpreader = getListener.getReturnType().getMethod("getSculkSpreader");
        getSpreader.setAccessible(true);
        Class<?> posType = Class.forName("net.minecraft.core.BlockPos");
        blockPos = posType.getConstructor(int.class, int.class, int.class);
        addCursors = getSpreader.getReturnType().getMethod("addCursors", posType, int.class);
    }

    void addCharge(SculkCatalyst catalyst, Block deathBlock, int charge) throws ReflectiveOperationException {
        Object entity = getListener.getDeclaringClass().cast(getBlockEntity.invoke(catalyst));
        Object listener = getListener.invoke(entity);
        Object spreader = getSpreader.invoke(listener);
        Object position = blockPos.newInstance(deathBlock.getX(), deathBlock.getY(), deathBlock.getZ());
        addCursors.invoke(spreader, position, charge);
    }
}
