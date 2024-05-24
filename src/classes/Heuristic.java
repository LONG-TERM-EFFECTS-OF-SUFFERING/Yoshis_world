package src.classes;

import src.classes.GameState.Player;

abstract public class Heuristic {
	public Game game;
	public Player maximized_player;


	public Heuristic(Game game, Player maximized_player) {
		this.game = game;
		this.maximized_player = maximized_player;
	}

	abstract public float get_score(GameState game_state);
}
