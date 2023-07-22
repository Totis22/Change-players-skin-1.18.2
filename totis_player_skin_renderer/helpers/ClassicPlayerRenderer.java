package com.totis.infinityg.extras.totis_player_skin_renderer.helpers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("all")
@OnlyIn(Dist.CLIENT)
public class ClassicPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ResourceLocation PLAYER_SKIN;

    public ClassicPlayerRenderer(EntityRendererProvider.Context context, ResourceLocation skin) {
        this(context, false, skin);
    }
    public ClassicPlayerRenderer(EntityRendererProvider.Context context, boolean useSmallArms, ResourceLocation skin) {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), useSmallArms), 0.5F);
        this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
        this.addLayer(new PlayerItemInHandLayer(this));
        this.addLayer(new ArrowLayer(context, this));
        this.addLayer(new Deadmau5EarsLayer(this));
        this.addLayer(new CapeLayer(this));
        this.addLayer(new CustomHeadLayer(this, context.getModelSet()));
        this.addLayer(new ElytraLayer(this, context.getModelSet()));
        this.addLayer(new ParrotOnShoulderLayer(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer(this));
        this.PLAYER_SKIN = skin;
    }

    @Override
    public void render(AbstractClientPlayer pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.setModelProperties(pEntity);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public Vec3 getRenderOffset(AbstractClientPlayer entityIn, float partialTicks) {
        return entityIn.isCrouching() ? new Vec3(0.0D, -0.125D, 0.0D) : super.getRenderOffset(entityIn, partialTicks);
    }
    private void setModelProperties(AbstractClientPlayer clientPlayer) {
        PlayerModel<AbstractClientPlayer> playermodel = (PlayerModel)this.getModel();
        if (clientPlayer.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.hat.visible = clientPlayer.isModelPartShown(PlayerModelPart.HAT);
            playermodel.jacket.visible = clientPlayer.isModelPartShown(PlayerModelPart.JACKET);
            playermodel.leftPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.rightPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.leftSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            playermodel.rightSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.crouching = clientPlayer.isCrouching();
            HumanoidModel.ArmPose bipedmodel$armpose = getArmPose(clientPlayer, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose bipedmodel$armpose1 = getArmPose(clientPlayer, InteractionHand.OFF_HAND);
            if (bipedmodel$armpose.isTwoHanded()) {
                bipedmodel$armpose1 = clientPlayer.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }
            if (clientPlayer.getMainArm() == HumanoidArm.RIGHT) {
                playermodel.rightArmPose = bipedmodel$armpose;
                playermodel.leftArmPose = bipedmodel$armpose1;
            } else {
                playermodel.rightArmPose = bipedmodel$armpose1;
                playermodel.leftArmPose = bipedmodel$armpose;
            }
        }

    }
    private static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer clientPlayer, InteractionHand hand) {
        ItemStack itemstack = clientPlayer.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (clientPlayer.getUsedItemHand() == hand && clientPlayer.getUseItemRemainingTicks() > 0) {
                UseAnim useaction = itemstack.getUseAnimation();
                if (useaction == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }
                if (useaction == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
                if (useaction == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }
                if (useaction == UseAnim.CROSSBOW && hand == clientPlayer.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!clientPlayer.swinging && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }
            return HumanoidModel.ArmPose.ITEM;
        }
    }
    @Override
    public ResourceLocation getTextureLocation(AbstractClientPlayer pEntity) {
        return this.PLAYER_SKIN;
    }
    protected void scale(AbstractClientPlayer entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

    protected void renderNameTag(AbstractClientPlayer entityIn, Component displayNameIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        this.entityRenderDispatcher.distanceToSqr(entityIn);
        matrixStackIn.pushPose();
        super.renderNameTag(entityIn, displayNameIn, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    public void renderRightHand(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, AbstractClientPlayer playerIn) {
        this.renderHand(matrixStackIn, bufferIn, combinedLightIn, playerIn, ((PlayerModel)this.model).rightArm, ((PlayerModel)this.model).rightSleeve);
    }

    public void renderLeftHand(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, AbstractClientPlayer playerIn) {
        this.renderHand(matrixStackIn, bufferIn, combinedLightIn, playerIn, ((PlayerModel)this.model).leftArm, ((PlayerModel)this.model).leftSleeve);
    }

    private void renderHand(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, AbstractClientPlayer playerIn, ModelPart rendererArmIn, ModelPart rendererArmwearIn) {
        PlayerModel<AbstractClientPlayer> playermodel = (PlayerModel)this.getModel();
        this.setModelProperties(playerIn);
        playermodel.attackTime = 0.0F;
        playermodel.crouching = false;
        playermodel.swimAmount = 0.0F;
        playermodel.setupAnim(playerIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArmIn.xRot = 0.0F;
        rendererArmIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.entitySolid(playerIn.getSkinTextureLocation())), combinedLightIn, OverlayTexture.NO_OVERLAY);
        rendererArmwearIn.xRot = 0.0F;
        rendererArmwearIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(playerIn.getSkinTextureLocation())), combinedLightIn, OverlayTexture.NO_OVERLAY);
    }

    protected void setupRotations(AbstractClientPlayer entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = entityLiving.getSwimAmount(partialTicks);
        float f3;
        float f2;
        if (entityLiving.isFallFlying()) {
            super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
            f3 = (float)entityLiving.getFallFlyingTicks() + partialTicks;
            f2 = Mth.clamp(f3 * f3 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isAutoSpinAttack()) {
                matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f2 * (-90.0F - entityLiving.getXRot())));
            }

            Vec3 vector3d = entityLiving.getViewVector(partialTicks);
            Vec3 vector3d1 = entityLiving.getDeltaMovement();
            double d0 = vector3d1.horizontalDistanceSqr();
            double d1 = vector3d.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                matrixStackIn.mulPose(Vector3f.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
            f3 = entityLiving.isInWater() ? -90.0F - entityLiving.getXRot() : -90.0F;
            f2 = Mth.lerp(f, 0.0F, f3);
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f2));
            if (entityLiving.isVisuallySwimming()) {
                matrixStackIn.translate(0.0D, -1.0D, 0.30000001192092896D);
            }
        } else {
            super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        }
    }
}
