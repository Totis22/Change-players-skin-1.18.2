package com.totis.infinityg.server.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.totis.infinityg.Constants;
import com.totis.infinityg.server.RenderUtils;
import com.totis.infinityg.server.render.*;
import com.totis.infinityg.extras.totis_player_skin_renderer.TotisCustomSkinRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class RenderEvent {

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void renderSkinEvent(RenderLivingEvent event) {
        TotisCustomSkinRenderer.renderSkin(
                new ResourceLocation("urmodID", "textures/somewhere/yourskin.png"),
                event, event.getEntity(),
                false, true);
    }
}
