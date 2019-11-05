package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * This class implements a random mutation operator for double solutions
 *
 * @author Jayden Boskell
 */
@SuppressWarnings("serial")
public class Ex4OffsetMutation implements MutationOperator<DoubleSolution> {
  private double mutationProbability ;
  private RandomGenerator<Double> randomGenerator ;

  /**  Constructor */
  public Ex4OffsetMutation(double probability) {
	  this(probability, () -> JMetalRandom.getInstance().nextDouble());
  }
  
  /**  Constructor */
  public Ex4OffsetMutation(double probability, double mutationDistributionIndex) {
	  this(probability, () -> JMetalRandom.getInstance().nextDouble());
  }

  /**  Constructor */
  public Ex4OffsetMutation(double probability, RandomGenerator<Double> randomGenerator) {
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
        // Set our variables. Note that for this one offset is regenerated each time.
        double sign_x, sign_y;
        // Generates offset_x and offset_y to be between 0 and 0.1
        double offset_x = randomGenerator.getRandomValue() / 10;
        double offset_y = randomGenerator.getRandomValue() / 10;

        // Get number of cities. 
        int number_cities = solution.getNumberOfVariables() / 3;
    
        // If random generator is lower than probability, return without doing anything.
        if (randomGenerator.getRandomValue() <= probability)
        {
            return;
        }
    
        // For each grid id.
        for (int i = 0; i < number_cities; i++)
        {
            double chance = randomGenerator.getRandomValue();
            
            // 1 / n chance of moving this city to a nearby grid cell. 
            if (chance < 1 / number_cities)
            {
                double vertical = randomGenerator.getRandomValue();
                double horizontal = randomGenerator.getRandomValue();
            
                // 50% chance of moving cell up one row, 50% chance of moving cell down one row. 
                if (vertical < 0.5)
                {
                    double result = solution.getVariableValue(i) + 20;
                    if (result > 400)
                    {
                        result = result - 400;
                    }
                    solution.setVariableValue(i, result);
                }
                else
                {
                    double result = solution.getVariableValue(i) - 20;
                    if (result < 0)
                    {
                        result = result + 400;
                    }
                    solution.setVariableValue(i, result);
                }
                
                // 50% chance of moving cell left one row, 50% chance of moving cell right one row. 
                if (horizontal < 0.5)
                {
                    double result = solution.getVariableValue(i) + 1;
                    if (result > 400)
                    {
                        result = result - 400;
                    }
                    solution.setVariableValue(i, result);
                }
                else
                {
                    double result = solution.getVariableValue(i) - 1;
                    if (result < 0)
                    {
                        result = result + 400;
                    }
                    solution.setVariableValue(i, result);
                }
            }
        }
        
        // For each pair of offsets, which start at index number of cities,
        // and end at index of the number of variables. 
        for (int i = number_cities; i < solution.getNumberOfVariables(); i+=2) 
        {
            double chance = randomGenerator.getRandomValue();
            
            // 1 on n chance of mutating this offset. 
            if (chance < 1 / number_cities)
            {
                sign_x = randomGenerator.getRandomValue();
                sign_y = randomGenerator.getRandomValue();
                
                // 50% chance of mutating offset to be further the left, 
                // 50% chance of mutating offset to be further to the right. 
                if (sign_x < 0.5) 
                {
                    sign_x = -1;
                }
                else 
                {
                    sign_x = 1;
                }
            
                // 50% chance of mutating offset to be lower,
                // 50% chance of mutating offset to be higher
                if (sign_y < 0.5) 
                {
                    sign_y = -1;
                }
                else 
                {
                    sign_y = 1;
                }
                
                // If x was pushed out of permissable range, wrap it around to the other side. 
                double adjusted_x = solution.getVariableValue(i) + (offset_x * sign_x);
                if (adjusted_x > 0.5)
                {
                    adjusted_x -= 1;
                }
                else if (adjusted_x < -0.5)
                {
                    adjusted_x += 1;
                }
                
                // If y was pushed out of permissible range, wrap it around to the other side. 
                double adjusted_y = solution.getVariableValue(i + 1) + (offset_x * sign_x);
                if (adjusted_y > 0.5)
                {
                    adjusted_y -= 1;
                }
                else if (adjusted_y < -0.5)
                {
                    adjusted_y += 1;
                }
                solution.setVariableValue(i, adjusted_x);
                solution.setVariableValue(i + 1, adjusted_y);
            }
            
        }

 
	}
}
