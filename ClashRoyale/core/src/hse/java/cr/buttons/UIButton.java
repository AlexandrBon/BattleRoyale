package hse.java.cr.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import hse.java.cr.wrappers.Assets;


public class UIButton extends Actor {
    public enum State {
        NORMAL,
        HOVERED,
        PRESSED
    }

    private State state;
    private final Array<TextureRegion> buttonFrames = new Array<>(3);
    private final TextureAtlas buttonAtlas;
    private Sound sound;

    public UIButton(String buttonName, Assets assets) {
        state = State.NORMAL;
        sound = assets.get(Assets.clickSound);
        buttonAtlas = assets.get(Assets.uiAtlas);

        buttonFrames.add(buttonAtlas.findRegion(buttonName + ' ' + "Button"));
        buttonFrames.add(buttonAtlas.findRegion(buttonName + ' ' +  "col_Button"));
        buttonFrames.add(buttonFrames.get(1));

        float buttonRegionWidth = buttonFrames.get(0).getRegionWidth();
        float buttonRegionHeight = buttonFrames.get(0).getRegionHeight();

        float expectedHeight = (float)Gdx.graphics.getHeight() / 5;
        float scale = expectedHeight / buttonRegionHeight;
        float buttonWidth = buttonRegionWidth * scale;
        float buttonHeight  = buttonRegionHeight * scale;
        setBounds((Gdx.graphics.getWidth() - buttonWidth) / 2,
                (Gdx.graphics.getHeight() - buttonHeight) / 2,
                buttonWidth,
                buttonHeight
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(buttonFrames.get(state.ordinal()), getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (!state.equals(State.PRESSED)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (getX() <= x && x <= getRight()
                    && getY() <= y && y <= getTop()) {
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
