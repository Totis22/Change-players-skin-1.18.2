package com.totis.infinityg.extras.totis_player_skin_renderer;

import com.totis.infinityg.extras.totis_player_skin_renderer.helpers.ClassicPlayerRenderer;
import com.totis.infinityg.extras.totis_player_skin_renderer.helpers.FirstPersonHandRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;

@SuppressWarnings("all")
//All the credits goes to Kleiders Custom Renderer
public class TotisCustomSkinRenderer {

    public static void renderFirstPersonSkin(ResourceLocation texture, RenderHandEvent event) {
        // This method NEEDS to be in a "RenderHandEvent"
        event.setCanceled(true);
        event.getPoseStack().pushPose();
        if(Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;
            boolean isMainHand = event.getHand() == InteractionHand.MAIN_HAND;
            if (isMainHand && !player.isInvisible() && event.getItemStack().isEmpty()) {
                HumanoidArm enumhandside = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
                FirstPersonHandRenderer.renderArmFirstPersonLayered(texture, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getEquipProgress(), event.getSwingProgress(), enumhandside);
                //event.setCanceled(true);
            }
        }
        event.getPoseStack().popPose();
    }
    public static void renderSkin(ResourceLocation texture, RenderLivingEvent _evt, Entity entity, boolean hideBaseSkin, boolean ...conditions) {
        // This method NEEDS to be in a "RenderLivingEvent"
        if (entity == null) return;
        //RenderLivingEvent _evt = (RenderLivingEvent) event;
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dis = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(dis, mc.getItemRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.font);
        for(boolean valor : conditions) {
            if(!valor) return;
        }
        // If all the boolean conditions you put are true, you pass here!
        if (_evt.getRenderer() instanceof PlayerRenderer) {
            if (_evt instanceof RenderLivingEvent.Pre) {
                if (hideBaseSkin == true) {
                    _evt.setCanceled(true);
                }
            }
            new ClassicPlayerRenderer(new EntityRendererProvider.Context(dis, mc.getItemRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.font),
                    texture)
                    .render((AbstractClientPlayer) _evt.getEntity(), _evt.getEntity().getYRot(), _evt.getPartialTick(), _evt.getPoseStack(), _evt.getMultiBufferSource(), _evt.getPackedLight());
        }
    }
}
