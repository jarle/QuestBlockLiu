package menus;

import gamestate.GameStateControl;
import gamestate.MenuState;
import tiles.Background;

import java.awt.*;


/**
 * Level select menu for the game. Associated with a game state controller
 */
@SuppressWarnings("RefusedBequest") //Override methods from superclass on purpose
public class LevelSelectMenu extends MenuState {

    /**
     *
     * @param gameStateControl1 game state controller for the menu
     */
    public LevelSelectMenu(GameStateControl gameStateControl1) {
        super(gameStateControl1);
        init();
    }

    @Override
    public void init(){
        final int fontsize = 24;
        this.currentChoice = 0;
        background = new Background("/lvl1background.png");
        this.font = new Font("Century Gothic", Font.PLAIN, fontsize);
        this.options = new String[] {"Level 1","Level 2","Randomizer(beta)","Main Menu"};
    }

    @Override
    public void update() {
        background.update();
    }



    @Override
    public void select() {
        final int level1 = 0;
        final int level2 = 1;
        final int randomizer = 2;
        final int mainMenu = 3;

        switch (currentChoice){
            case level1:
                gameStateControl.setGameState(GameStateControl.LEVEL1STATE);
                break;
            case level2:
                gameStateControl.setGameState(GameStateControl.LEVEL2STATE);
                break;
            case randomizer:
                gameStateControl.setGameState(GameStateControl.RANDOMIZERSTATE);
                break;
            case mainMenu:
                gameStateControl.setGameState(GameStateControl.MAINMENUSTATE);
                break;
            default:
                gameStateControl.setGameState(GameStateControl.MAINMENUSTATE);
                break;
        }
    }
}
