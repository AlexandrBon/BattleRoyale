package hse.java.cr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.model.Golem;

class Card extends Actor {
    private final TextureRegion cardTexture;

    public Card(Assets assets, String heroName) {
        cardTexture = assets.get(Assets.cardTextures).findRegion(heroName);
        setBounds(0, 0, cardTexture.getRegionWidth(), cardTexture.getRegionHeight());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(cardTexture, getX(), getY(), 100, 100);
    }
}

public class Cards {
    private Stage cardStage;
    private Array<Card> cardConveyor;
    private Stage gameStage;

    public Cards(Assets assets, Stage gameStage) {
        cardStage = new Stage();
        this.gameStage = gameStage;
        cardConveyor = new Array<>(4);
        // TODO need to init from User's Card Deck
        cardConveyor.add(new Card(assets, "brownGolem"));
        cardConveyor.add(new Card(assets, "grayGolem"));
        cardConveyor.add(new Card(assets, "greenGoblin"));
        cardConveyor.add(new Card(assets, "greenGolem"));

        float width = cardConveyor.get(0).getWidth();
        for (int i = 0; i < 4; i++) {
            cardConveyor.get(i).setPosition(0 + i * 300, 0);
            cardStage.addActor(cardConveyor.get(i));
        }
        cardStage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 0; i < 4; i++) {
                    if (cardConveyor.get(i).getX()
                            + cardConveyor.get(i).getWidth() <= x) {
                        gameStage.addActor(new Golem(assets.get(Assets.golemAnimation_2)));
                    }
                }
            }
        });

        Gdx.input.setInputProcessor(cardStage);
    }

    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < 4; i++) {
            cardConveyor.get(i).draw(batch, parentAlpha);
        }
    }
}
