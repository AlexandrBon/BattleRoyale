package hse.java.cr.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.Assets;

public class UIButton extends Actor {
    public enum State {
        NORMAL,
        HOVERED,
        PRESSED
    }

    private State state;
    private final Array<TextureRegion> buttonFrames = new Array<>(2);
    private final TextureAtlas buttonAtlas;
    private Sound sound;

    public UIButton(String buttonName, Assets assets) {
        state = State.NORMAL;
        sound = assets.get(Assets.clickSound);
        buttonAtlas = assets.get(Assets.uiAtlas);

        buttonFrames.add(buttonAtlas.findRegion(buttonName + ' ' + "Button"));
        buttonFrames.add(buttonAtlas.findRegion(buttonName + ' ' +  "col_Button"));

        setBounds((float)Gdx.graphics.getWidth() / 2
                - (float)buttonFrames.get(0).getRegionWidth() / 2,
                (float)Gdx.graphics.getHeight() / 2
                - (float)buttonFrames.get(0).getRegionHeight() / 2,
                buttonFrames.get(0).getRegionWidth(), buttonFrames.get(0).getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!state.equals(State.PRESSED)) {
            batch.draw(buttonFrames.get(state.ordinal()), getX(), getY());
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            if (getX() <= x && x <= getX() + getWidth()
                    && getY() <= y && y <= getY() + getHeight()) {
                setState(UIButton.State.HOVERED);
            } else {
                setState(UIButton.State.NORMAL);
            }
        }
    }

    public void playSound() {
        sound.play();
    }

    public void setSound(Sound otherSound) {
        sound = otherSound;
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
