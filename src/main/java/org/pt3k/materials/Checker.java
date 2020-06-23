package org.pt3k.materials;

import org.pt3k.Vec3;

/**
 * Textura szachownicy skladajacej sie z 2 kolorow
 */
public class Checker implements Texture {

    private Texture odd, even;

    public Checker(Texture t0, Texture t1) {
        odd = t0;
        even = t1;
    }

    /**
     * Zwraca kolor szachownycy na podstawie kordynatow uv i punktu.
     * @param u pozycja u
     * @param v pozycja v
     * @param p Wektor punktu
     * @return kolor w danym punkcie
     */
    @Override
    public Vec3 value(float u, float v, Vec3 p) {
        double sines = Math.sin(10*p.getX())*Math.sin(10*p.getY())
                *Math.sin(10*p.getZ());
        if(sines < 0) {
            return odd.value(u,v,p);
        }
        return even.value(u,v,p);
    }
}
