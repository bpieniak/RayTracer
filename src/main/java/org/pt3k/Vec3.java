package org.pt3k;


/**
 * Klasa reprezentujaca trzy elementowy wektor i operacje ktore mozna na nim wykonac.
 */
public class Vec3 {

    private float x;
    private float y;
    private float z;

    /**
     * Konstruktor klasy Vec3
     * @param x parametr x
     * @param y parametr y
     * @param z parametr z
     */
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Tworzy wektor przeciwny
     * @return wektor przeciwny
     */
    public Vec3 inverse() {
        return new Vec3(-this.x,-this.y,-this.z);
    }

    /**
     * Dodawanie 2 wektorow
     * @param v wektor dodawany
     * @return suma wektorow
     */
    public Vec3 add(final Vec3 v) {
        Vec3 vec = new Vec3();
        vec.x = this.x+v.x;
        vec.y = this.y+v.y;
        vec.z = this.z+v.z;
        return vec;
    }

    /**
     * Odejmowanie 2 wektorow
     * @param v odejmowany wektor
     * @return roznica wektorow
     */
    public Vec3 sub(final Vec3 v) {
        Vec3 vec = new Vec3();
        vec.x = this.x-v.x;
        vec.y = this.y-v.y;
        vec.z = this.z-v.z;
        return vec;
    }

    /**
     * Mnozenie wektora przez skalar
     * @param t float przez ktory sie mnozy wektor
     * @return wynik mnozenia
     */
    public Vec3 mul(final float t) {
        Vec3 vec = new Vec3();
        vec.x = this.x*t;
        vec.y = this.y*t;
        vec.z = this.z*t;
        return vec;
    }

    /**
     * Iloraz wektorowy
     * @param v wektor mnozony
     * @return iloraz
     */
    public Vec3 mulvec(final Vec3 v) {
        Vec3 vec = new Vec3();
        vec.x = this.x * v.x;
        vec.y = this.y * v.y;
        vec.z = this.z * v.z;
        return vec;
    }

    /**
     * Dzielenie wektora przez skalar
     * @param t float przez ktory sie dzieli wektor
     * @return wynik dzielenia
     */
    public Vec3 div(final float t) {
        Vec3 vec = new Vec3();
        vec.x = this.x/t;
        vec.y = this.y/t;
        vec.z = this.z/t;
        return vec;
    }

    /**
     * Oblicza dlugosc wektora
     * @return dlugosc wektora
     */
    public final float length() {
        return (float) Math.sqrt(length_squared());
    }

    /**
     * Oblicza kwadradrat dlugosci wektora
     * @return kwadradrat dlugosci wektora
     */
    public final float length_squared() {
        return x*x + y*y + z*z;
    }

    /**
     * oblicza iloczyn skalarny 2 wektorow
     * @param v wektor
     * @return iloczyn skalarny
     */
    public float dot(final Vec3 v) {
        return this.x * v.x
                + this.y * v.y
                + this.z * v.z;
    }

    /**
     * oblicza iloczyn wektorowy 2 wektorow
     * @param v wektor
     * @return iloczyn wektorowy
     */
    public Vec3 cross(final Vec3 v) {
        return new Vec3(this.y*v.z - this.z*v.y,
                this.z*v.x - this.x*v.z,
                this.x*v.y - this.y*v.x);
    }

    /**
     * Zwacaca wektor jednostkowy
     * @return wektor jednostkowy
     */
    public Vec3 unit_vector() {
        return this.div(this.length());
    }

    /**
     * @return wartosc X wektora
     */
    public float getX() {
        return x;
    }

    /**
     * @return wartosc Y wektora
     */
    public float getY() {
        return y;
    }

    /**
     * @return wartosc Z wektora
     */
    public float getZ() {
        return z;
    }

    /**
     * Wyswietla wartosc wektora
     */
    public void print() {
        System.out.println("[" + x + "," + y + "," + z + "]");
    }

    /**
     * oblicza wektor odbity
     * @param v wektor
     * @param n wektor normalny
     * @return wektor odbity
     */
    public static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.sub(n.mul(2*v.dot(n)));
    }

    /**
     * oblicza wektor zalamany
     * @param uv wektor
     * @param n wektor normalny
     * @param etaiOverEtat stosunek wspolczynnikow zalamania
     * @return wektor zalamany
     */
    public static Vec3 refract(Vec3 uv, Vec3 n, float etaiOverEtat) {
        float cos_theta = uv.inverse().dot(n);
        Vec3 rOutParallel = (uv.add(n.mul(cos_theta))).mul(etaiOverEtat);
        Vec3 rOutPerp = n.mul((float) -Math.sqrt(1.0-rOutParallel.length_squared()));
        return  rOutParallel.add(rOutPerp);
    }

    public static float clamp(float x, float min, float max) {
        if(x < min) return min;
        if(x > max) return max;
        return x;
    }

    /**
     * Skaluje kolor pixela biorac pod uwage ilosc probek, a nastepnie dokonuje
     * korekcji gammy podnoszac wynik do potegi 1/2.
     * @param samples liczba probek na pixel
     */
    public void scale(int samples) {
        float scale = 1.0f/samples;
        x = (clamp((float) Math.sqrt(scale * x),0,1));
        y = (clamp((float) Math.sqrt(scale * y),0,1));
        z = (clamp((float) Math.sqrt(scale * z),0,1));
    }
}

