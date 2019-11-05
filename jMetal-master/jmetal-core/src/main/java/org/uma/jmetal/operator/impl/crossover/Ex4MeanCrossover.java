package org.uma.jmetal.operator.impl.crossover;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows to apply a SBX crossover operator using two parent solutions (Double encoding).
 * A {@link RepairDoubleSolution} object is used to decide the strategy to apply when a value is out
 * of range.
 *
 * The implementation is based on the NSGA-II code available in
 * <a href="http://www.iitk.ac.in/kangal/codes.shtml">http://www.iitk.ac.in/kangal/codes.shtml</a>
 *
 * @author Jayden Boskell
 */
@SuppressWarnings("serial")
public class Ex4MeanCrossover implements CrossoverOperator<DoubleSolution> {

  private double distributionIndex ;
  private double crossoverProbability  ;

  private RandomGenerator<Double> randomGenerator ;

  /** Constructor */
  public Ex4MeanCrossover(double crossoverProbability, double distributionIndex) {
    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds()) ;
  }

  /** Constructor */
  public Ex4MeanCrossover(double crossoverProbability, double distributionIndex, RandomGenerator<Double> randomGenerator) {
    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds(), randomGenerator) ;
  }

  /** Constructor */
  public Ex4MeanCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair) {
	  this(crossoverProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public Ex4MeanCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
    if (crossoverProbability < 0) {
      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
    } else if (distributionIndex < 0) {
      throw new JMetalException("Distribution index is negative: " + distributionIndex);
    }

    this.crossoverProbability = crossoverProbability ;
    this.distributionIndex = distributionIndex ;
    this.randomGenerator = randomGenerator ;
  }

  /* Getters */
  public double getCrossoverProbability() {
    return crossoverProbability;
  }

  public double getDistributionIndex() {
    return distributionIndex;
  }

  /* Setters */
  public void setCrossoverProbability(double probability) {
    this.crossoverProbability = probability ;
  }

  public void setDistributionIndex(double distributionIndex) {
    this.distributionIndex = distributionIndex ;
  }

  /** Execute() method */
  @Override
  public List<DoubleSolution> execute(List<DoubleSolution> solutions) {
    if (null == solutions) {
      throw new JMetalException("Null parameter") ;
    } 

    int size = solutions.size();
    List<DoubleSolution> population = new ArrayList<DoubleSolution>(size);

    for(int i = 0; i < size; i+=2) {

        List<DoubleSolution> temp = doCrossover(crossoverProbability,solutions.get(i),solutions.get(i+1));
        population.set(i, temp.get(0));
        population.set(i+1, temp.get(1));
    }

    return population;
  }

  /** doCrossover method */
  public List<DoubleSolution> doCrossover(
      double probability, DoubleSolution parent1, DoubleSolution parent2) {
    List<DoubleSolution> offspring = new ArrayList<DoubleSolution>(2);

    offspring.add((DoubleSolution) parent1.copy()) ;
    offspring.add((DoubleSolution) parent2.copy()) ;


    double y1, y2, x1, x2;
    double ny1, ny2, nx1, nx2;
    double alpha, beta;

    if (randomGenerator.getRandomValue() <= probability) 
    {
      int number_cities = parent1.getNumberOfVariables() / 3;
      // For each grid value.
      for (int j = 0; j < number_cities; j++)
      {
        // 50% chance child 1 gets parent 1's grid value and child 2 gets parent 2's grid value.
        // 50% chance child 1 gets parent 2's grid value and child 2 gets parent 1's grid value. 
        double chance = randomGenerator.getRandomValue();
        if (chance < 0.5)
        {
            offspring.get(0).setVariableValue(i,parent1.getVariableValue(i));
            offspring.get(1).setVariableValue(i,parent2.getVariableValue(i));
        }
        else
        {
            offspring.get(0).setVariableValue(i,parent2.getVariableValue(i));
            offspring.get(1).setVariableValue(i,parent1.getVariableValue(i));   
        }
      }
      
      // For each set of x, y coordinates which start at number_cities and end
      // at the end of the variables. 
      for (int i = number_cities; i < parent1.getNumberOfVariables(); i+=2) 
      {
        // Determine alpha and beta. 
        alpha = randomGenerator.getRandomValue();
        beta = 1 - alpha;

        // Generate the points.
        x1 = parent1.getVariableValue(i);
        x2 = parent2.getVariableValue(i);
        y1 = parent1.getVariableValue(i + 1);
        y2 = parent2.getVariableValue(i + 1);

        nx1 = alpha*x1 + beta*x2;
        nx2 = beta*x1 + alpha*x2;

        ny1 = alpha*y1 + beta*y2;
        ny2 = beta*y1 + alpha*y2;

        // Set the points.
        offspring.get(0).setVariableValue(i,nx1);
        offspring.get(0).setVariableValue(i + 1,ny1);
        offspring.get(1).setVariableValue(i,nx2);
        offspring.get(1).setVariableValue(i + 1,ny2);

      }
    }

    return offspring;
  }

  @Override
  public int getNumberOfRequiredParents() {
    return 2 ;
  }

  @Override
  public int getNumberOfGeneratedChildren() {
	// TODO Auto-generated method stub
	return 0;
  }
}
