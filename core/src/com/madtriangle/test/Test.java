package com.madtriangle.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class Test implements ApplicationListener {

    Texture lookup;
    OrthographicCamera cam;
    SpriteBatch batch;
    private float gameWidth, gameHeight;
    private ArrayList<Color> colors = new ArrayList<Color>();

    float percent;
    ImmediateModeRenderer renderer;

    float p = 0f;
    float v = 1f;
    Color c;
    int i = 0;

    @Override
    public void create() {
        cam = new OrthographicCamera();
        batch = new SpriteBatch();
        lookup = new Texture("bar.png");
        lookup.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        renderer = new ImmediateModeRenderer20(false, true, 1);
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();
        colors.add(FlatColors.RED);
        colors.add(FlatColors.GREEN);
        colors.add(FlatColors.YELLOW);
        colors.add(FlatColors.BLUE);
        colors.add(FlatColors.PURPLE);
        colors.add(FlatColors.SEA);
        colors.add(FlatColors.ORANGE);
        c = colors.get(0);

    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false);
        batch.setProjectionMatrix(cam.combined);
    }


    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1,1,1,1);

        float r = (gameWidth/2) - gameWidth/20;
        float cx = gameWidth / 2;
        float cy = gameHeight / 2;
        float thickness = gameWidth/25;
        //increase percentage
        p += v * 0.15f * Gdx.graphics.getDeltaTime();
        if (p > 1f) {
            p = 1f;
            v *= -1f;
            c = getColor();
        } else if (p < 0f) {
            p = 0f;
            v *= -1f;
            c = getColor();
        }
        //progress(cx, cy, r, thickness, 1f, Color.GRAY, lookup);
        progress(cx, cy, r, thickness, p, c, lookup);
        //progress(cx, cy, r-(thickness/2), 5, 1f, FlatColors.BLACK, lookup);
        //progress(cx, cy, r+(thickness/2), 5, 1f, FlatColors.BLACK, lookup);


    }

    public Color getColor() {
        i++;
        if (i >= colors.size()) {
            i = 0;
        }
        return colors.get(i);

    }

    public void progress(float cx, float cy, float r, float thickness, float amt, Color c,
                         Texture lookup) {
        //start and end angles
        float start = 0f;
        float end = amt * 360f;

        lookup.bind();
        renderer.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        int segs = (int) (100 * Math.cbrt(r));
        end += 90f;
        start += 90f;
        float halfThick = thickness / 2f;
        float step = 360f / segs;
        for (float angle = start; angle < (end + step); angle += step) {
            float tc = 0.5f;
            if (angle == start)
                tc = 0f;
            else if (angle >= end)
                tc = 1f;

            float fx = MathUtils.cosDeg(angle);
            float fy = MathUtils.sinDeg(angle);

            float z = 0f;
            renderer.color(c.r, c.g, c.b, c.a);
            renderer.texCoord(tc, 1f);
            renderer.vertex(cx + fx * (r + halfThick), cy + fy * (r + halfThick), z);

            renderer.color(c.r, c.g, c.b, c.a);
            renderer.texCoord(tc, 0f);
            renderer.vertex(cx + fx * (r + -halfThick), cy + fy * (r + -halfThick), z);
        }
        renderer.end();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}