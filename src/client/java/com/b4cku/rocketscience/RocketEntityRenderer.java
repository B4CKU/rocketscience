package com.b4cku.rocketscience;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {

    private static final Identifier TEXTURE = new Identifier(RocketScience.MODID, "textures/entity/rocket.png");
    private final RocketEntityModel model;

    public RocketEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new RocketEntityModel(context.getPart(RocketScienceClient.MODEL_ROCKET_LAYER));
        //super(context, new CubeEntityModel(context.getPart(EntityTestingClient.MODEL_CUBE_LAYER)), 0.5f);
    }

    public void render(RocketEntity rocket, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rocket.getYaw()/* - 180.0F*/));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-180.0F - rocket.getPitch()));

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        //this.model.setAngles(rocket, 0.0F, 0.0F, 0.0F, rocket.getYaw(), rocket.getPitch());
        //this.model.animateModel(rocket, 0.0F, 0.0F, tickDelta);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
        super.render(rocket, f, tickDelta, matrixStack, vertexConsumerProvider, i);


       // this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        /*VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(rocket, f, tickDelta, matrixStack, vertexConsumerProvider, i);*/
    }


    @Override
    public Identifier getTexture(RocketEntity entity) {
        return TEXTURE;
    }
}