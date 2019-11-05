package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * This class implements a random mutation operator for double solutions
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class OffsetMutation implements MutationOperator<DoubleSolution> {
  private double mutationProbability ;
  private RandomGenerator<Double> randomGenerator ;

  /**  Constructor */
  public OffsetMutation(double probability) {
	  this(probability, () -> JMetalRandom.getInstance().nextDouble());
  }

  /**  Constructor */
  public OffsetMutation(double probability, RandomGenerator<Double> randomGenerator) {
    if (probability < 0) {
      throw new JMetalException("Mutation probability is negative: " + mutationProbability) ;
    }

  	this.mutationProbability = probability ;
    this.randomGenerator = randomGenerator ;
  }

  /* Getters */
  public double getMutationProbability() {
    return mutationProbability;
  }

  /* Setters */
  public void setMutationProbability(double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  /** Execute() method */
	@Override
  public DoubleSolution execute(DoubleSolution solution) throws JMetalException {
    if (null == solution) {
      throw new JMetalException("Null parameter") ;
    }

    doMutation(mutationProbability, solution) ;
    
    return solution;
  }

  /** Implements the mutation operation */
	private void doMutation(double probability, DoubleSolution solution) {
        // Set our variables in needed. Offset only gained once
        double sign_x, sign_y;
        double offset_x = randomGenerator.getRandomValue();
        double offset_y = randomGenerator.getRandomValue();

    for (int i = 0; i < solution.getNumberOfVariables(); i+=2) {
      if (randomGenerator.getRandomValue() <= probability) {
        sign_x = randomGenerator.getRandomValue();
        sign_y = randomGenerator.getRandomValue();

        if(sign_x < 0.5) {sign_x = 0;}
        else {sign_x = 0;}
        if(sign_y < 0.5) {sign_x = 0;}
        else {sign_y = 0;}

        solution.setVariableValue(i, solution.getVariableValue(i) + (offset_x * sign_x));
        solution.setVariableValue(i + 1, solution.getVariableValue(i+1) + (offset_y * sign_y));
          
      }
    }

 
	}
}
