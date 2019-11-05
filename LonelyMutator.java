import java.util.ArrayList;
import java.awt.Point;
import java.lang.Integer;
import java.util.Random;
import java.util.Collections;
import java.lang.Math;

public class LonelyMutator implements Jmetal_mutator_interface
// public class LonelyMutator
{
	public ArrayList<Integer> mutator(ArrayList<Integer> list){
		int width = 20;
		int height = 20;
		int min = 8;
		int max = 0;
		ArrayList<Integer> neighbours = new ArrayList<Integer>();

		//These two will contain the indexes of all the min/max elements
		ArrayList<Integer> maxes = new ArrayList<Integer>();
		ArrayList<Integer> mins = new ArrayList<Integer>();

		for(int i = 0; i < width * height; i++){
			neighbours.add(neighbours(list, i, width, height));
			if(neighbours.get(i) < min){
				min = neighbours.get(i);
				mins = new ArrayList<Integer>();	//Clear the list
			}
			if(neighbours.get(i) == min){mins.add(i);}
			if(neighbours.get(i) > max){
				max = neighbours.get(i);
				maxes = new ArrayList<Integer>();
			}
			if(neighbours.get(i) == max){maxes.add(i);}
		}

		//Choose a random number of elements to be swapped
		Random rand = new Random();
		int max_num_of_elements_that_can_be_moved = Math.max(mins.size(), maxes.size());
		int number_of_elements_we_will_move = rand.nextInt(max_num_of_elements_that_can_be_moved + 1);

		for(int i = 0; i < number_of_elements_we_will_move; i++){
			int index_1 = maxes.get(rand.nextInt(maxes.size()));
			int index_2 = mins.get(rand.nextInt(mins.size()));

			Collections.swap(list, index_1, index_2);

			maxes.remove(index_1);
			mins.remove(index_2);
		}

		return list;
	}

	// N0 N1 N2
	// N3 S  N4
	// N5 N6 N7
	//Gets the number of tiles where city is surrounding it
	private int neighbours(ArrayList<Integer> list, int i, int width, int height){

		Point p = oneD_to_twoD_coords(i, width);
		int total = 0;

		int ret_val = 0xFF;	//All 8 sides are true
		if(p.x == 0){	//On left boarder
			ret_val &= ~(1 << 0);	//Clearing bits which can't be right
			ret_val &= ~(1 << 3);
			ret_val &= ~(1 << 5);
		}
		else if(p.x >= width - 1){
			ret_val &= ~(1 << 2);
			ret_val &= ~(1 << 4);
			ret_val &= ~(1 << 7);
		}
		if(p.y == 0){
			ret_val &= ~(1 << 0);
			ret_val &= ~(1 << 1);
			ret_val &= ~(1 << 2);
		}
		else if(p.y >= height - 1){
			ret_val &= ~(1 << 5);
			ret_val &= ~(1 << 6);
			ret_val &= ~(1 << 7);
		}

		//If the neighour contains a city. Increment the counter
		if((ret_val & (1 << 0)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x-1,p.y-1), width)) > 0){total++;}}
		if((ret_val & (1 << 1)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x,p.y-1), width)) > 0){total++;}}
		if((ret_val & (1 << 2)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x+1,p.y-1), width)) > 0){total++;}}
		if((ret_val & (1 << 3)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x-1,p.y), width)) > 0){total++;}}
		if((ret_val & (1 << 4)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x+1,p.y), width)) > 0){total++;}}
		if((ret_val & (1 << 5)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x-1,p.y+1), width)) > 0){total++;}}
		if((ret_val & (1 << 6)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x,p.y+1), width)) > 0){total++;}}
		if((ret_val & (1 << 7)) > 0){if(list.get(twoD_to_oneD_coords(new Point(p.x+1,p.y+1), width)) > 0){total++;}}

		return total;
	}

	private Point oneD_to_twoD_coords(int i, int row_length){
		return new Point(i % row_length, i / row_length);
	}

	private int twoD_to_oneD_coords(Point p, int row_length){
		return (int)p.getX() + ((int)p.getY() * row_length);
	}
}
