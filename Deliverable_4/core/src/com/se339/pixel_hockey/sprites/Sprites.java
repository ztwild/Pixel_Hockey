package com.se339.pixel_hockey.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.se339.fileUtilities.FileList;
import com.se339.log.Log;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.world.ContactBits;

/**
 * Created by mweem_000 on 12/4/2016.
 */

public abstract class Sprites{

    Log log;

    public World world;
    public Body body;

    protected GameScreen screen;
    protected Sprite sprite;
    protected Texture texture;

    public float posX;
    public float posY;
    public boolean dynamic;

    protected FixtureDef fdef;
    protected float size;

    public Sprites(GameScreen screen) {
        this.screen = screen;
        texture = null;
        sprite = null;
        init();
    }

    public Sprites(GameScreen screen, String image) {

        this.screen = screen;
        texture = new Texture(image);
        sprite = new Sprite(texture);
        init();
    }

    private void init(){
        this.world = screen.getWorld();

        log = new Log("Sprites");

        body = null;

        posX = 0;
        posY = 0;
        dynamic = false;
    }

    protected void setSprite(String image){
        texture = new Texture(image);
        sprite = new Sprite(texture);
    }

    protected void initSprite(float pX, float pY, float size, boolean dynamic){
        this.posX = pX;
        this.posY = pY;
        this.size = size;
        this.dynamic = dynamic;
        sprite.setPosition(posX, posY);
        sprite.setSize(size, size);
    }

    public void setPosition(float x, float y){
        posX = x;
        posY = y;
        sprite.setPosition(x, y);
    }

    public abstract void defineFixture();

    protected void defineBody(){
        //log.g(x,y,"posX", "posY", "defineBody() Called");

        BodyDef bdef = new BodyDef();
        bdef.position.set(posX, posY);
        if (dynamic)
            bdef.type = BodyDef.BodyType.DynamicBody;
        else
            bdef.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(bdef);
    }

    public void update(float dt){
//        if (screen.getHud().isTimeUp() && !isDead()) {
//            die();
//        }

        // update sprite with the current position
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);

        // update sprite with the current texture
        //sprite.setTexture(texture);
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }

    public void applyLinearImpulse(float x, float y){
        body.applyLinearImpulse(new Vector2(x, y), body.getWorldCenter(), true);
    }

    public void setVelocity(float x, float y){
        body.setLinearVelocity(x, y);
    }

    public void setVelocity(Vector2 v){
        body.setLinearVelocity(v);
    }

    public void applyForce(float x, float y){
        body.applyForce(new Vector2(x,y), body.getWorldCenter(), true);
    }

    public Sprite getSprite(){
        return sprite;
    }


}
