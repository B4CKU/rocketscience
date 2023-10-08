package com.b4cku.rocketscience;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.8.3

public class RocketEntityModel<T extends RocketEntity> extends EntityModel<T> {
    private final ModelPart fins;
    //private final ModelPart fin_r1;
    private final ModelPart body;
    //private final ModelPart body_r1;
    public RocketEntityModel(ModelPart root) {
        this.fins = root.getChild("fins");
        this.body = root.getChild("body");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData fins = modelPartData.addChild("fins", ModelPartBuilder.create().uv(8, 5).cuboid(0.0F, -3.5F, 0.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 0.0F));

        ModelPartData fin_r1 = fins.addChild("fin_r1", ModelPartBuilder.create().uv(0, 5).cuboid(0.0F, -2.0F, 0.5F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.5F, -0.5F, 0.0F, 0.0F, 1.5708F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -3.0F, 0.0F));

        ModelPartData body_r1 = body.addChild("body_r1", ModelPartBuilder.create().uv(0, 13).cuboid(-1.0F, -1.0F, -4.5F, 2.0F, 2.0F, 2.0F, new Dilation(-0.25F))
                .uv(11, 0).cuboid(-1.0F, -1.0F, -5.5F, 2.0F, 2.0F, 2.0F, new Dilation(-0.5F))
                .uv(0, 0).cuboid(-1.0F, -1.0F, -3.5F, 2.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.5F, -0.5F, 0.0F, 0.0F, 0.7854F));
        return TexturedModelData.of(modelData, 32, 32);
    }
    @Override
    public void setAngles(RocketEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        fins.setAngles(headPitch, netHeadYaw, 0.0F);
        body.setAngles(headPitch, netHeadYaw, 0.0F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        fins.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
