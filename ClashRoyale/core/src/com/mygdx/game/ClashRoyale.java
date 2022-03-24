package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.lang.invoke.MutableCallSite;


public class ClashRoyale extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Music startSound;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("clash_royale.png");
		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		startSound = Gdx.audio.newMusic(Gdx.files.internal("startSounnd.mp3"));
		startSound.play();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		startSound.dispose();
	}
}
