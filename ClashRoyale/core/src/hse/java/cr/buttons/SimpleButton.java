package hse.java.cr.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.Assets;

public class SimpleButton extends Actor {
    public enum State {
        NORMAL,
        HOVERED,
        PRESSED
    }

    private State state;
    private final Array<TextureRegion> buttonFrames = new Array<>(2);
    private final TextureAtlas buttonAtlas;
    private final Sound clickSound;

    public SimpleButton(String buttonName, Assets assets) {
        state = State.NORMAL;
        clickSound = assets.get(Assets.clickSound);
        buttonAtlas = assets.get(Assets.uiAtlas);

        char space = ' ';
        buttonFrames.add(buttonAtlas.findRegion(buttonName + space + "Button"));
        buttonFrames.add(buttonAtlas.findRegion(buttonName + space +  "col_Button"));

        this.setBounds((float)Gdx.graphics.getWidth() / 2
                - (float)buttonFrames.get(0).getRegionWidth() / 2,
                (float)Gdx.graphics.getHeight() / 2
                - (float)buttonFrames.get(0).getRegionHeight() / 2,
                buttonFrames.get(0).getRegionWidth(), buttonFrames.get(0).getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!state.equals(State.PRESSED)) {
            batch.draw(buttonFrames.get(state.ordinal()), this.getX(), this.getY());
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            if (getX() <= x && x <= getX() + getWidth()
                    && getY() <= y && y <= getY() + getHeight()) {
                setState(SimpleButton.State.HOVERED);
            } else {
                setState(SimpleButton.State.NORMAL);
            }
        }
    }

    public void playClickSound() {
        clickSound.play();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void dispose() {
        buttonAtlas.dispose();
    }
}
