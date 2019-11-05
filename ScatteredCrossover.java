import java.util.ArrayList;
import java.awt.Point;
import java.lang.Integer;
import java.util.Random;
import java.util.Collections;
import java.lang.Math;

// public class ScatteredCrossover implements Jmetal_crossover_interface
public class ScatteredCrossover
{
	public ArrayList<Integer> crossover(ArrayList<Integer> parent1, ArrayList<Integer> parent2){
		int width = 20;
		int height = 20;

		Random rand = new Random();
		boolean current_parent = rand.nextBoolean();

		//Empty child with all 400 elements set to zero
		ArrayList<Integer> child = new ArrayList<Integer>(Collections.nCopies(width*height, 0));

		for(int i = 0; i < width * height; i++){
			//Checks if there are cities present in the current tile
			boolean p1_present = (parent1.get(i) > 0);
			boolean p2_present = (parent2.get(i) > 0);
			if(p1_present && p2_present){	//Both are set
				//Choose a random parent to take city from
				if(rand.nextBoolean()){	//Take from parent 2
					if(p2_present){child.set(i, parent2.get(i));}
				}
				else{	//Take from parent 1
					if(p1_present){child.set(i, parent1.get(i));}
				}
			}
			else if(p1_present || p2_present){	//Only one of them is set
				//Depending on offset, take one from parent
				if(current_parent){
					if(p1_present){child.set(i, parent1.get(i));}
					if(p2_present){child.set(i, parent2.get(i));}
				}
				current_parent = !current_parent;
			}
			
		}

		return child;
	}
}
