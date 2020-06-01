package org.pt3k.materials;

import org.pt3k.Vec3;

public interface Texture {

    Vec3 value(float u, float v, Vec3 p);
}
