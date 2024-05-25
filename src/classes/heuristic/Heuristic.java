package src.classes.heuristic;

import src.classes.Game;
import src.classes.GameState;
import src.classes.GameState.Player;


abstract public class Heuristic {
	public Game game;
	public Player maximized_player;
	public Player minimized_player;


	public Heuristic(Game game, Player maximized_player) {
		this.game = game;
		this.maximized_player = maximized_player;
		minimized_player = maximized_player == Player.GREEN ? Player.RED : Player.GREEN;
	}


	abstract public float get_score(GameState game_state);
}
