package modeling.sign;

import java.util.ArrayList;
import java.util.Iterator;

import model.State;
import model.Transition;

public interface JavaTransitionSignInterface {
//	public Iterator<State> stateExtract(String line);
	public Transition transitionExtract(String line,ArrayList<State> stateList);
}