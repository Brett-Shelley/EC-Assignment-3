package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class implements a polynomial mutation operator
 *
 * The implementation is based on the NSGA-II code available in
 * http://www.iitk.ac.in/kangal/codes.shtml
 *
 * If the lower and upper bounds of a variable are the same, no mutation is carried out and the
 * bound value is returned.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class LonelyMutator implements MutationOperator<DoubleSolution> {
  private static final double DEFAULT_PROBABILITY = 0.01 ;
  private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0 ;
  private double distributionIndex ;
  private double mutationProbability ;
  private RepairDoubleSolution solutionRepair ;

  private RandomGenerator<Double> randomGenerator ;

  /** Constructor */
  public LonelyMutator() {
    this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX) ;
  }

  /** Constructor */
  public LonelyMutator(DoubleProblem problem, double distributionIndex) {
    this(1.0/problem.getNumberOfVariables(), distributionIndex) ;
  }

  /** Constructor */
  public LonelyMutator(DoubleProblem problem,
                            double distributionIndex,
                            RandomGenerator<Double> randomGenerator) {
    this(1.0/problem.getNumberOfVariables(), distributionIndex) ;
    this.randomGenerator = randomGenerator ;
  }

  /** Constructor */
  public LonelyMutator(double mutationProbability, double distributionIndex) {
    this(mutationProbability, distributionIndex, new RepairDoubleSolutionAtBounds()) ;
  }

  /** Constructor */
  public LonelyMutator(double mutationProbability,
                            double distributionIndex,
                            RandomGenerator<Double> randomGenerator) {
    this(mutationProbability, distributionIndex, new RepairDoubleSolutionAtBounds(), randomGenerator) ;
  }

  /** Constructor */
  public LonelyMutator(double mutationProbability, double distributionIndex,
      RepairDoubleSolution solutionRepair) {
	  this(mutationProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public LonelyMutator(double mutationProbability, double distributionIndex,
      RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
    if (mutationProbability < 0) {
      throw new JMetalException("Mutation probability is negative: " + mutationProbability) ;
    } else if (distributionIndex < 0) {
      throw new JMetalException("Distribution index is negative: " + distributionIndex) ;
    }
    this.mutationProbability = mutationProbability;
    this.distributionIndex = distributionIndex;
    this.solutionRepair = solutionRepair ;

    this.randomGenerator = randomGenerator ;
  }

  /* Getters */
  public double getMutationProbability() {
    return mutationProbability;
  }

  public double getDistributionIndex() {
    return distributionIndex;
  }

  /* Setters */
  public void setMutationProbability(double probability) {
    this.mutationProbability = probability ;
  }

  public void setDistributionIndex(double distributionIndex) {
    this.distributionIndex = distributionIndex ;
  }

  /** Execute() method */
  @Override
  public DoubleSolution execute(DoubleSolution solution) throws JMetalException {
    if (null == solution) {
      throw new JMetalException("Null parameter") ;
    }

    doMutation(mutationProbability, solution);
    return solution;
  }

  /** Perform the mutation operation */
  private void doMutation(double probability, DoubleSolution solution) {
    int width = 20;
    int height = 20;
    int min = 8;
    int max = 0;
    ArrayList<Integer> neighbours = new ArrayList<Integer>();

    //These two will contain the indexes of all the min/max elements
    ArrayList<Integer> maxes = new ArrayList<Integer>();
    ArrayList<Integer> mins = new ArrayList<Integer>();

    for(int i = 0; i < width * height; i++){
      neighbours.add(neighbours(solution, i, width, height));
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
    int number_of_elements_we_will_move = rand.nextInt(max_num_of_elements_that_can_be_moved);

    for(int i = 0; i < number_of_elements_we_will_move; i++){
      int index_1 = rand.nextInt(maxes.size());
      int index_2 = rand.nextInt(mins.size());

      Double temp = solution.getVariableValue(index_1);
      solution.setVariableValue(index_1, solution.getVariableValue(index_2));
      solution.setVariableValue(index_2, temp);

      maxes.remove(index_1);
      mins.remove(index_2);
    }
  }

  // N0 N1 N2
  // N3 S  N4
  // N5 N6 N7
  //Gets the number of tiles where city is surrounding it
  private int neighbours(DoubleSolution list, int i, int width, int height){

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
    if((ret_val & (1 << 0)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x-1,p.y-1), width)) > 0){total++;}}
    if((ret_val & (1 << 1)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x,p.y-1), width)) > 0){total++;}}
    if((ret_val & (1 << 2)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x+1,p.y-1), width)) > 0){total++;}}
    if((ret_val & (1 << 3)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x-1,p.y), width)) > 0){total++;}}
    if((ret_val & (1 << 4)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x+1,p.y), width)) > 0){total++;}}
    if((ret_val & (1 << 5)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x-1,p.y+1), width)) > 0){total++;}}
    if((ret_val & (1 << 6)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x,p.y+1), width)) > 0){total++;}}
    if((ret_val & (1 << 7)) > 0){if(list.getVariableValue(twoD_to_oneD_coords(new Point(p.x+1,p.y+1), width)) > 0){total++;}}

    return total;
  }

  private Point oneD_to_twoD_coords(int i, int row_length){
    return new Point(i % row_length, i / row_length);
  }

  private int twoD_to_oneD_coords(Point p, int row_length){
    return (int)p.getX() + ((int)p.getY() * row_length);
  }
}
