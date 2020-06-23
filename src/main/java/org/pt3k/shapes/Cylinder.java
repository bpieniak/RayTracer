package org.pt3k.shapes;

import org.pt3k.HitRecord;
import org.pt3k.Ray;
import org.pt3k.Vec3;
import org.pt3k.materials.Material;

public class Cylinder implements Hittable {

    private Vec3 center;
    float radius;
    float height;
    private Material material;

    public Cylinder(Vec3 c, float r, float h, Material mat){

        center = c;
        radius = r;
        height = h;
        material = mat;
    }

    @Override
    public boolean hit(Ray r, float t_min, float t_max, HitRecord hitRecord) {

        Vec3 A = r.getOrigin();
        Vec3 B = r.getDirection();

        float a = B.getX()*B.getX() + B.getZ()*B.getZ();
        float b = 2*( -B.getX()*center.getX() + -B.getZ()*center.getZ() + A.getX()*B.getX() + A.getZ()*B.getZ() );
        float c = A.getX()*A.getX() + A.getZ()*A.getZ() + center.getX()*center.getX()+center.getZ()*center.getZ() -
                2*(A.getX()*center.getX()+A.getZ()*center.getZ()) - radius*radius;

        float discriminant = b*b-4*a*c;

        if(discriminant > 0) {
            float root = (float) Math.sqrt(discriminant);
            float temp = (-b-root)/(2.0f*a);
            if(temp > t_min && temp < t_max && (r.at(temp).getY() < (center.getY() + height)) && (r.at(temp).getY() > center.getY())) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(new Vec3(center.getX(),r.at(temp).getY(),center.getZ()))).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                hitRecord.material = material;
                getCylinderUV(outward_normal,hitRecord);
                return true;
            }
            temp = (-b+root)/(2.0f*a);
            if(temp > t_min && temp < t_max && (r.at(temp).getY() < (center.getY() + height)) && (r.at(temp).getY() > center.getY())) {
                hitRecord.t = temp;
                hitRecord.p = r.at(temp);
                Vec3 outward_normal = (r.at(temp).sub(new Vec3(center.getX(),r.at(temp).getY(),center.getZ()))).div(radius);
                hitRecord.set_front_face(r,outward_normal);
                hitRecord.material = material;
                getCylinderUV(outward_normal,hitRecord);
                return true;
            }
        }

        return false;
    }

    void getCylinderUV(Vec3 p, HitRecord hitRecord) {

        double phi = Math.atan2(p.getZ(),p.getX());
        hitRecord.u = (float) (1-(phi+Math.PI)/(2*Math.PI));
        hitRecord.v = (hitRecord.p.getY()-center.getY())/height;
    }
}
