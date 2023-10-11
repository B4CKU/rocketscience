package com.b4cku.rocketscience;

import net.minecraft.util.math.Vec3d;

public class RocketUtil {
    public static Vec3d rotateVectorHorizontalAxis (Vec3d rocket_vector, float angle) {
        if (angle == 0) {
            return rocket_vector;
        }


        final double vector_x = rocket_vector.x;
        final double vector_z = rocket_vector.z;

        if (Math.abs(vector_x) < Math.abs(vector_z)) {
            //i hope this isn't too performance-heavy, because i feel smart for coming up with this
            //despite it only being a workaround until i find a way to rotate vectors correctly
            return rocket_vector.rotateX((float)( angle * (vector_x / Math.abs(vector_x) )));
        } else {
            return rocket_vector.rotateZ((float)( angle * (vector_z / Math.abs(vector_z) )));
        }
    }
}
