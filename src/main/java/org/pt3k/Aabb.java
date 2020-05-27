package org.pt3k;

public class Aabb {

    Vec3 min, max;

    public Aabb() {}
    public Aabb(Vec3 a, Vec3 b) {
        min = a;
        max = b;
    }

    boolean hit(final Ray r, float tmin, float tmax) {
        for(int a = 0; a < 3; a++) {

            float t0  = Math.min((min.getElement(a) - r.getOrigin().getElement(a))/r.getDirection().getElement(a),
                    (max.getElement(a) - r.getOrigin().getElement(a))/r.getDirection().getElement(a));

            float t1  = Math.max((min.getElement(a) - r.getOrigin().getElement(a))/r.getDirection().getElement(a),
                    (max.getElement(a) - r.getOrigin().getElement(a))/r.getDirection().getElement(a));

            tmin = Math.max(t0, tmin);
            tmax = Math.min(t1,tmax);
            if(tmax < tmin)
                return false;
        }
        return true;
    }

}
