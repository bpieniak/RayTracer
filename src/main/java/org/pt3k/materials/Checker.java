package org.pt3k.materials;

import org.pt3k.Vec3;

public class Checker implements Texture {

    Texture odd, even;

    public Checker(Texture t0, Texture t1) {
        odd = t0;
        even = t1;
    }

    @Override
    public Vec3 value(float u, double v, Vec3 p) {
        double sines = Math.sin(10*p.getX())*Math.sin(10*p.getY())
                *Math.sin(10*p.getZ());
        if(sines < 0) {
            return odd.value(u,v,p);
        }
        return even.value(u,v,p);
    }
}
