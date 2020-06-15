package org.pt3k;

/**
 * Klasa repezentujaca promiec z poczatku w pewnym kierunku.
 */
public class Ray {
    Vec3 origin;
    Vec3 direction;

    public Ray() {}

    public Ray(final Vec3 origin, final Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * Metoda obliczajaca punkt promienia w odleglosci t od poczatku.
     * @param t odleglosc od poczatku
     * @return punkt
     */
    public Vec3 at(float t) {
        Vec3 tdir = direction.mul(t);
        return origin.add(tdir);
    }

    public Vec3 getOrigin() { return origin; }
    public Vec3 getDirection() { return  direction; }
}
