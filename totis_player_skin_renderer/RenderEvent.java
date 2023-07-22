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

    //pitch = xrot
    //yaw = yrot

    /*@SubscribeEvent
    public static void renderer(RenderLevelLastEvent event) {
        if(mc.player != null) {
            Player player = mc.player;
            PoseStack matrix = event.getPoseStack();
            MultiBufferSource vertexConsumerProvider = Minecraft.getInstance().renderBuffers().bufferSource();
            var launchHeight = BeamHelper.launchHeight(player);
            var offset = new Vec3(0.0, launchHeight, 0.5);
            Vec3 from = player.position().add(0,launchHeight,0);
            var lookVector = Vec3.ZERO;
            lookVector = Vec3.directionFromRotation(player.getXRot(), player.getYRot()); //polar rot
            lookVector = lookVector.normalize();
            var beamPos = TargetHelper.castBeam(player, lookVector, 32);
            lookVector = lookVector.scale(beamPos.length());
            Vec3 to = from.add(lookVector);
            RenderUtils.renderBeam(matrix, vertexConsumerProvider, new Spell.Release.Target.Beam(), from, to, offset, player.level.getGameTime(), event.getPartialTick());
        }
    }*/
    /*@SubscribeEvent
    public static void bakeItem(ModelBakeEvent event) {
        if(mc.player != null && mc.player.getMainHandItem().getItem() == ModItems.INFINITY_GAUNTLET.get()) {
            Map<ResourceLocation, BakedModel> model = event.getModelRegistry();

            ModelResourceLocation default_ = new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "infinity_gauntlet"), "inventory");
            ModelResourceLocation alternative = new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, "testitem"), "inventory");

            if (!mc.player.isShiftKeyDown()) {
                BakedModel alternativeModel = model.get(alternative);
                model.put(default_, alternativeModel);
            }
        }
    }*/

    @SubscribeEvent
    public static void renderSkinEvent(RenderLivingEvent event) {
        TotisCustomSkinRenderer.renderSkin(
                new ResourceLocation(Constants.MOD_ID, "textures/skins/spiderman.png"),
                event, event.getEntity(),
                false, true);
    }
    @SubscribeEvent
    public static void renderFirstPersonSkinEvent(RenderHandEvent event) {
        TotisCustomSkinRenderer.renderFirstPersonSkin(new ResourceLocation(Constants.MOD_ID, "textures/skins/spiderman.png"), event);
    }

    @SubscribeEvent
    public static void render(RenderLevelLastEvent event) {
        Entity re = mc.getCameraEntity();
        float partialTicks = event.getPartialTick();
        if(re != null) {
            Player player = mc.player;
            Level level = player.level;
            PoseStack matrix = event.getPoseStack();
            Vec3 proj = mc.gameRenderer.getMainCamera().getPosition();
            double d0 = proj.x;
            double d1 = proj.y;
            double d2 = proj.z;

            /*
            the update intervals of rendering and the player movement are different.
            The player position is changed each 1/20s, but the line rendering is updated every
            render frame (which is usually much faster than the 1/20s).
            Say you're doing a render at the half of a tick time passed.
            What you have to basically do is starting with the current position
            of the player at the current tick, and subtract the old position of the player
            from the previous (server) tick. if you then multiply this by how much
            you're 'in' the current tick  (0.5 in this case) you have the average of the two
             */
            double x = player.xOld + (player.getX() - player.xOld) * event.getPartialTick();
            double y = player.yOld + (player.getY() - player.yOld) * event.getPartialTick();
            double z = player.zOld + (player.getZ() - player.zOld) * event.getPartialTick();

            double length = 10;
            double radius = 20;

            /*matrix.pushPose();
            matrix.translate(-d3,-d4,-d5);*/
            /*Entity entity = EntityType.COW.create(player.level);
            RenderUtils.renderEntityStatic(entity,
                    13,-12,15,
                    0.0F,event.getPartialTick(),
                    matrix,
                    new ColorRenderTypeBuffer(
                            Minecraft.getInstance().renderBuffers().bufferSource(),
                            1.0F, 0.0F, 0.0F, 0.5F),240);*/

            Entity fakeEntity = EntityType.COW.create(level);
            fakeEntity.moveTo(-16,-59,33);
            /*fakeEntity.setYRot(player.getViewYRot(event.getPartialTick()));
            fakeEntity.setXRot(player.getViewXRot(event.getPartialTick()));*/
            MultiBufferSource.BufferSource multibuffersource = mc.renderBuffers().bufferSource();
            MultiBufferSource customBuffer = new ColorRenderTypeBuffer(
                    Minecraft.getInstance().renderBuffers().bufferSource(),
                    1.0F, 0.0F, 0.0F, 0.5F);
            RenderUtils.renderEntity(fakeEntity, event.getPartialTick(), matrix, multibuffersource);
            /*TotisPlayerUtils.getEntitiesInRadius(Entity.class, player.level,
                    player.getX(),player.getY(),player.getZ(), radius).forEach(entity -> {
                if(entity != null && entity.isAlive() && entity.getUUID() != player.getUUID()) {
                    //RenderUtils.renderSelectedEntity(entity, event);
                    AABB aabb = entity.getBoundingBoxForCulling();//entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());

                    *//*RenderUtils.renderEntity(entity, event.getPartialTick(), event.getPoseStack(),
                            new ColorRenderTypeBuffer(
                                    Minecraft.getInstance().renderBuffers().bufferSource(),1.0F, 0.0F, 0.0F, 0.5F));*//*
                    //RenderUtils.renderBox(matrix, minX, minY, minZ, maxX, maxY, maxZ, 3.0D, 91, 209, 252, 255);


                    RenderUtils.renderEntityBox(matrix, entity, event.getPartialTick(),
                            1.5D, 91, 209, 252, 255);
                    //RenderUtils.renderBox(matrix, aabb, 1.5D, 91, 209, 252, 255);
                    *//*RenderUtils.renderBox(matrix,
                            (float)entity.getX()-1,(float)entity.getY(),(float)entity.getZ()-1,
                            (float)entity.getX()+1,(float)entity.getY()+2,(float)entity.getZ()+1,
                            3.0D, 91, 209, 252, 255);*//*
                }
            });*/
            //Entity entity = TotisPlayerUtils.rayTraceEntities(mc.player, 30, Entity.class);

            //RenderUtils.renderLine(matrix, new Vec3(x,y,z), new Vec3(x-1,y,z-1), 135,9,0, 255);
            /*matrix.translate(0.0D,0.0D,0.0D);
            matrix.popPose();*/
            //renderLightning(player,d0 - d3, d1 - d4, d2 - d5, event.getPoseStack(), partialTicks);
        }
    }




    private static void renderLightning(LivingEntity entity, double x, double y, double z, PoseStack matrix, float partialTicks) {
        matrix.pushPose();
        matrix.translate(x, y, z);
        float[] colors = new float[]{1,1,1,0.7f};
        int lightmapX = 240;
        int lightmapY = 240;
        float w = 1.1f; //size
        float r2 = colors[0];
        float g2 = colors[1];
        float b2 = colors[2];
        float a2 = colors[3];
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        float a = 1.0F;
        r2 = 0.2F;
        g2 = 0.5F;
        b2 = 1.0F;
        a2 = 0.2F;
        RenderUtils.rotateQ(entity.getYRot(), 0.0F, 1.0F, 0.0F, matrix);
        RenderUtils.rotateQ(entity.getXRot(), 1.0F, 0.0F, 0.0F, matrix);
        boolean zap = true;
        if (zap) {
            RenderUtils.rotateQ((float)(-ClientHandlerEvent.clientTickCounter % 40L), 0.0F, 1.0F, 0.0F, matrix);
        } else {
            RenderUtils.rotateQ((float)(ClientHandlerEvent.clientTickCounter % 40L), 0.0F, 1.0F, 0.0F, matrix);
        }

        MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
        float th = 0.0F;

        matrix.pushPose();
        CullWrappedRenderLayer ringType2;
        float pit;
        float h;
        int i;
        int segments = 10;
        float[] pitchs = new float[segments];
        float[] prevpitchs = new float[pitchs.length];
        float[] reallengths = new float[segments];
        float[] prevreallengths = new float[reallengths.length];
        for(i = 0; i < segments; ++i) {
            ringType2 = new CullWrappedRenderLayer(TotisRenderType.createRunesRenderType(RenderUtils.beam, 0));
            pit = prevpitchs[i] + (pitchs[i] - prevpitchs[i]) * partialTicks;
            RenderUtils.rotateQ(pit, 1.0F, 0.0F, 0.0F, matrix);
            h = prevreallengths[i] + (reallengths[i] - prevreallengths[i]) * partialTicks;
            RenderUtils.renderRing(matrix, buf, 0.0D, 90.0F, w, h, 20, 240, 240, r, g, b, a, ringType2);
            buf.endBatch(ringType2);
            matrix.translate(0.0D, (double)(-h), 0.0D);
            th -= h;
            RenderUtils.rotateQ(-pit, 1.0F, 0.0F, 0.0F, matrix);
        }
        matrix.popPose();


        for(i = 0; i < segments; ++i) {
            ringType2 = new CullWrappedRenderLayer(TotisRenderType.createRunesRenderType(RenderUtils.beam, 1));
            pit = prevpitchs[i] + (pitchs[i] - prevpitchs[i]) * partialTicks;
            RenderUtils.rotateQ(pit, 1.0F, 0.0F, 0.0F, matrix);
            h = prevreallengths[i] + (reallengths[i] - prevreallengths[i]) * partialTicks;
            RenderUtils.renderRing(matrix, buf, 0.0D, 90.0F, w * 2.8F, h, 20, 240, 240, r2, g2, b2, a2, ringType2);
            buf.endBatch(ringType2);
            matrix.translate(0.0D, (double)(-h), 0.0D);
            th -= h;
            RenderUtils.rotateQ(-pit, 1.0F, 0.0F, 0.0F, matrix);
        }

        matrix.popPose();
    }
}
