package physics;

import java.util.ArrayList;



public class StellarBody extends Object{
    String Classification;

    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Integer size, Formula init_mv) {
        super(new Formula(init_mv.getEquation()));
        this.Classification = cls;
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(posx);
        pos.add(posy);
        this.Position = pos;
        this.Title = ttl;
        this.Mass = mass;
        this.Size = size;
    }

    public void effect_movement() {

    }
}

