package org.pt3k.materials;

import org.pt3k.Vec3;

public class SolidColor implements Texture {

    Vec3 color;

    SolidColor() {};
    public SolidColor(Vec3 c) {
        color = c;
    }
    public SolidColor(float r, float g, float b) {
        color = new Vec3(r,g,b);
    }

    @Override
    public Vec3 value(float u, float v, Vec3 p) {
        return color;
    }
}
