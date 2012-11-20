package cs424.windblows.gui;

import cs424.windblows.application.Variable;

public class Playback extends Sketch {

	Button play, next, prev, first, last;
	
	public Playback(Variable firstData, Variable prevData, Variable playData,
			Variable nextData, Variable lastData) {
		super(new Variable());
		first = new Button(firstData, "<<");
		prev = new Button(prevData, "<");
		play = new Button(playData, "P");
		next = new Button(nextData, ">");
		last = new Button(lastData, ">>");
	}

	@Override
	protected void draw() {
		first.draw();
		prev.draw();
		play.draw();
		next.draw();
		last.draw();
	}
	
}
