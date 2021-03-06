package hse.java.cr.client.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class CardInfo extends Actor {
    private int index = 0;
    BitmapFont font = new BitmapFont();
    private TextureRegion curFrame;
    private final ArrayList<Animation<TextureRegion>> animation;
    private final ArrayList<String> typeOfCard;
    private final ArrayList<String> name;
    private final ArrayList<Integer> hp;
    private final ArrayList<Integer> attack;
    private final ArrayList<Integer> costs;
    private final ArrayList<TextureRegion> curFrames;
    private final ArrayList<Integer> addedCards;
    private final int maxCardsInDeck = 6;
    private final ArrayList<Float> frameDeltas;
    private float frameDelta = 0f;

    public CardInfo() {
        curFrame = new Sprite();
        animation = new ArrayList<>();
        typeOfCard = new ArrayList<>();
        name = new ArrayList<>();
        hp = new ArrayList<>();
        attack = new ArrayList<>();
        costs = new ArrayList<>();
        addedCards = new ArrayList<>();
        curFrames = new ArrayList<>();
        frameDeltas = new ArrayList<>();
        for (int i = 0; i < maxCardsInDeck; i++) {
            curFrames.add(null);
            addedCards.add(null);
            frameDeltas.add(0f);
        }
    }

    public void add(TextureAtlas atlas, String nameOfCard, String type, int hp1, int attack1, int cost) {
        Animation<TextureRegion> animation1 = new Animation<>(0.06f, atlas.getRegions());
        animation1.setPlayMode(Animation.PlayMode.LOOP);
        animation.add(animation1);
        typeOfCard.add(type);
        name.add(nameOfCard);
        hp.add(hp1);
        attack.add(attack1);
        costs.add(cost);
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return animation.size();
    }

    public void setIndex(int i) {
        index = i;
    }

    public void addCard(int index) {
        for (int i = 0; i < maxCardsInDeck; i++) {
            if (addedCards.get(i) != null &&  addedCards.get(i) == index) {
                return;
            }
        }
        for (int i = 0; i < maxCardsInDeck; i++) {
            if (addedCards.get(i) == null) {
                addedCards.set(i, index);
                return;
            }
        }
    }

    public void deleteCard(int index) {
        for (int i = 0; i < maxCardsInDeck; i++) {
            if (addedCards.get(i) != null && addedCards.get(i) == index) {
                addedCards.set(i, null);
                return;
            }
        }
    }

    private void drawAddedCards(Batch batch, int i) {
        if (addedCards.get(i) != null) {
            int currentIndex = addedCards.get(i);
            curFrames.set(i, animation.get(currentIndex).getKeyFrame(frameDeltas.get(i)));
            batch.draw(curFrames.get(i), 6.0f * Gdx.graphics.getWidth() / 10 + (i < 3 ? 0 : 1) * 2.0f * Gdx.graphics.getWidth() / 10,  7.5f * Gdx.graphics.getHeight() / 10 - (i % 3) * 2.5f * Gdx.graphics.getHeight() / 10,
                    2.0f * Gdx.graphics.getWidth() / 10, 8.0f * Gdx.graphics.getHeight() / 30);
            frameDeltas.set(i, frameDeltas.get(i) + Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        curFrame = animation.get(index).getKeyFrame(frameDelta);
        String currentString = "";
        currentString += "          " + name.get(index) + "\n";
        currentString += "Type: " + typeOfCard.get(index) + "\n";
        currentString += "Attack: " + attack.get(index) + "\n";
        currentString += "Health: " + hp.get(index) + "\n";
        currentString += "Radious: " + costs.get(index) + "\n";
        font.setColor(0, 0, 0, 1);
        font.draw(batch, currentString, 1.0f * Gdx.graphics.getWidth() / 10 , 8.0f * Gdx.graphics.getHeight() / 10);
        batch.draw(curFrame, 0,  2.0f * Gdx.graphics.getHeight() / 10,
                3.0f * Gdx.graphics.getWidth() / 10, 4.0f * Gdx.graphics.getHeight() / 10);
        frameDelta += Gdx.graphics.getDeltaTime();
        for (int i = 0; i < maxCardsInDeck; i++) {
            drawAddedCards(batch, i);
        }
    }

}