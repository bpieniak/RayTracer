package org.pt3k;

import org.pt3k.materials.*;
import org.pt3k.shapes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scene implements Hittable {

    private List<Hittable> list;

    public Scene(List<Hittable> list) {
        this.list = list;
    }

    /**
     * Metoda sprawdzajaca czy promien uderzyl w obiekt w scenie
     * @param r promien
     * @param t_min minimalna odleglosc trafienia
     * @param t_max maxymalna odleglosc trafienia
     * @param hitRecord informacje o trafieniu
     * @return czy promien trafil
     */
    @Override
    public boolean hit(Ray r, float t_min, float t_max, HitRecord hitRecord) {
         HitRecord rec = new HitRecord();
         boolean hit_anything = false;
         float closest = t_max;

         for(Hittable h : list) {
             if(h.hit(r,t_min,closest,rec)) {
                 hit_anything = true;
                 closest = rec.t;
                 hitRecord.normal = rec.normal;
                 hitRecord.t = rec.t;
                 hitRecord.front_face = rec.front_face;
                 hitRecord.p = rec.p;
                 hitRecord.material = rec.material;
                 hitRecord.u = rec.u;
                 hitRecord.v = rec.v;
             }
         }
         return hit_anything;
    }

    public static ArrayList<Hittable> fiveSpheres() throws IOException {
        ArrayList<Hittable> worldList = new ArrayList<>();

        Checker checker = new Checker(
                new SolidColor(0.2f,0.2f,0.2f),
                new SolidColor(0.9f,0.9f,0.9f)
        );

        Texture earthTexture = new ImageTexture("earth.jpg");
        Material earthSurface = new Lambertian(earthTexture);
        Texture marsTexture = new ImageTexture();
        Material marsSurface = new Lambertian(marsTexture);
        Texture neptuneTexture = new ImageTexture();
        Material neptuneSurface = new Lambertian(neptuneTexture);

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(checker)));
        worldList.add(new Sphere(new Vec3(-4,1,0),1,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(0,1,4),1,earthSurface));
        worldList.add(new Sphere(new Vec3(4,1,0),1,marsSurface));
        worldList.add(new Sphere(new Vec3(0,1,-4),1,neptuneSurface));
        worldList.add(new Sphere(new Vec3(0,1,0),1,new Metal(new Vec3(0.7f,0.6f,0.5f))));

        return worldList;
    }

    public static ArrayList<Hittable> randomScene() throws IOException {
        ArrayList<Hittable> worldList = new ArrayList<>();

        Checker checker = new Checker(
                new SolidColor(0.2f,0.8f,0.2f),
                new SolidColor(0.9f,0.9f,0.9f)
        );

        DiffuseLight diffuseLight = new DiffuseLight(new SolidColor(10,10,10));

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(checker)));

        int i = 1;
        for(int a = -6; a < 6; a = a+4) {
            for(int b = -6; b < 6; b = b+4) {
                float choose_mat = (float) Math.random();
                Vec3 center = new Vec3((float )(a + 2*Math.random()), 0.2f, (float )(b + 2*Math.random()));

                if(choose_mat < 0.9) {
                    Vec3 albedo = MyRandom.randomVector().mulvec(MyRandom.randomVector());
                    worldList.add(new Sphere(center,0.2f,new Lambertian(new SolidColor(albedo))));
                } else if(choose_mat < 0.95) {
                    Vec3 albedo = MyRandom.randomVector();
                    worldList.add(new Sphere(center, 0.2f, new Metal(albedo)));
                } else {
                    worldList.add(new Sphere(center,0.2f, new Dielectric(1.5f)));
                }
            }
        }

        worldList.add(new Sphere(new Vec3(-4,1,0),1,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(4,1,0),1,new Metal(new Vec3(0.7f,0.6f,0.5f))));

        /*
        worldList.add(new XZrectangle(-0.5f,0.5f,-0.5f,0.5f,0,diffuseLight));
        worldList.add(new XZrectangle(-0.5f,0.5f,-0.5f,0.5f,1,diffuseLight));
        worldList.add(new XYrectangle(-0.5f,0.5f,0f,1f,-0.5f,diffuseLight));
        worldList.add(new XYrectangle(-0.5f,0.5f,0f,1f,0.5f,diffuseLight));
        worldList.add(new YZrectangle(-0.5f,0.5f,0f,1f,-0.5f,diffuseLight));
        worldList.add(new YZrectangle(-0.5f,0.5f,0f,1f,0.5f,diffuseLight));
         */

        Hittable cube = new Cube(0.5f,0f,0.5f,1f,1f,2f,diffuseLight);
        cube = new RotateY(cube,30);

        worldList.add(cube);

        return worldList;
    }

    public static ArrayList<Hittable> cornellBox() throws IOException {
        ArrayList<Hittable> worldList = new ArrayList<>();

        Material red = new Lambertian(new SolidColor(0.8f,0.1f,0.1f));
        Material green = new Lambertian(new SolidColor(0.1f,0.8f,0.1f));
        Material white = new Lambertian(new SolidColor(0.8f,0.8f,0.8f));
        DiffuseLight diffuseLight = new DiffuseLight(new SolidColor(7,7,7));

        Texture earthTexture = new ImageTexture("earth.jpg");
        Material earthSurface = new Lambertian(earthTexture);

        worldList.add(new Sphere(new Vec3(138,100,300),100,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(138,100,300),-90,new Dielectric(1.5f)));
        worldList.add(new Sphere(new Vec3(138*2,240,350),100,new Metal(new Vec3(0.7f,0.6f,0.5f))));
        worldList.add(new Sphere(new Vec3(138*3,390,400),100, earthSurface));

        worldList.add(new YZrectangle(0,555,0,555,555,green));
        worldList.add(new YZrectangle(0,555,0,555,0,red));
        worldList.add(new XZrectangle(0,555,0,555,555,white));
        worldList.add(new XZrectangle(0,555,0,555,0,white));
        worldList.add(new XYrectangle(0,555,0,555,555,white));
        worldList.add(new XZrectangle(113,443,127,432,554,diffuseLight));

        return worldList;
    }

    public static ArrayList<Hittable> earthScene() throws IOException {
        ArrayList<Hittable> worldList = new ArrayList<>();

        DiffuseLight diffuseLight = new DiffuseLight(new SolidColor(80,80,80));

        Texture earthTexture = new ImageTexture();
        Material earthSurface = new Lambertian(earthTexture);

        worldList.add(new Sphere(new Vec3(0,0,0),4,earthSurface));
        worldList.add(new Sphere(new Vec3(30,1,0),2,diffuseLight));

        return worldList;
    }

    public static ArrayList<Hittable> rotatedCubes() throws IOException {
        ArrayList<Hittable> worldList = new ArrayList<>();

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(new ImageTexture())));

        Hittable cubeCenter = new Cube(0.5f,0f,0.5f,4f,4f,6f,new Lambertian(new ImageTexture()));
        worldList.add(cubeCenter);

        Hittable cubeRight = new Cube(5f,0f,0.5f,4f,4f,6f, new Lambertian(new ImageTexture()));
        cubeRight = new RotateY(cubeRight,45);
        worldList.add(cubeRight);

        Hittable cubeLeft = new Cube(-5f,0f,0.5f,4f,4f,6f, new Metal(new Vec3(0.5f,0.5f,0.9f)));
        cubeLeft = new RotateY(cubeLeft,-45);
        worldList.add(cubeLeft);

        return worldList;
    }

    public static ArrayList<Hittable> cylinder() throws IOException {
        ArrayList<Hittable> worldList = new ArrayList<>();

        Checker checker = new Checker(
                new SolidColor(0.2f,0.8f,0.2f),
                new SolidColor(0.9f,0.9f,0.9f)
        );

        worldList.add(new Sphere(new Vec3(0,-1000,0), 1000, new Lambertian(checker)));

        worldList.add(new Cylinder(new Vec3(0,0,0), 2, 5, new Lambertian(new ImageTexture())));

        return worldList;
    }

}
