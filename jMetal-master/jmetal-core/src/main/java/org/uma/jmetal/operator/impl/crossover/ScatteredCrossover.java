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
import java.util.Random;

/**
 * This class allows to apply a SBX crossover operator using two parent solutions (Double encoding).
 * A {@link RepairDoubleSolution} object is used to decide the strategy to apply when a value is out
 * of range.
 *
 * The implementation is based on the NSGA-II code available in
 * <a href="http://www.iitk.ac.in/kangal/codes.shtml">http://www.iitk.ac.in/kangal/codes.shtml</a>
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class ScatteredCrossover implements CrossoverOperator<DoubleSolution> {
  /** EPS defines the minimum difference allowed between real values */
  private static final double EPS = 1.0e-14;

  private double distributionIndex ;
  private double crossoverProbability  ;
  private RepairDoubleSolution solutionRepair ;

  private RandomGenerator<Double> randomGenerator ;

  /** Constructor */
  public ScatteredCrossover(double crossoverProbability, double distributionIndex) {
    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds()) ;
  }

  /** Constructor */
  public ScatteredCrossover(double crossoverProbability, double distributionIndex, RandomGenerator<Double> randomGenerator) {
    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionAtBounds(), randomGenerator) ;
  }

  /** Constructor */
  public ScatteredCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair) {
	  this(crossoverProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public ScatteredCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
    if (crossoverProbability < 0) {
      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
    } else if (distributionIndex < 0) {
      throw new JMetalException("Distribution index is negative: " + distributionIndex);
    }

    this.crossoverProbability = crossoverProbability ;
    this.distributionIndex = distributionIndex ;
    this.solutionRepair = solutionRepair ;

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
    } else if (solutions.size() != 2) {
      throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
    }

    return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
  }

  /** doCrossover method */
  public List<DoubleSolution> doCrossover(
      double probability, DoubleSolution parent1, DoubleSolution parent2) {
    Random rand;
    double temp;
    DoubleSolution p1Cpy = (DoubleSolution) parent1.copy();
    DoubleSolution p2Cpy = (DoubleSolution) parent2.copy();

    rand=new Random();
    for(int i=0;i<50;i++){
      if(rand.nextDouble()<probability){
        temp=p1Cpy.getVariableValue(i);
        p1Cpy.setVariableValue(i,p2Cpy.getVariableValue(i));
        p2Cpy.setVariableValue(i,temp);
      }
    }
    List<DoubleSolution> offspring = new ArrayList<DoubleSolution>(2);
    offspring.add(p1Cpy);
    offspring.add(p2Cpy);
    return offspring;
  }

  @Override
  public int getNumberOfRequiredParents() {
    return 2 ;
  }

  @Override
  public int getNumberOfGeneratedChildren() {
    return 2;
  }
}
