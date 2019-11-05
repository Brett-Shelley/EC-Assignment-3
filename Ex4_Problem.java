package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.problem.multiobjective.Assignment2.*;

import java.util.ArrayList;
import java.util.List;

public class Ex4_Problem extends AbstractDoubleProblem {
    public Ex4_Problem() {
        this(50);
    }

    public Ex4_Problem(Integer numberOfCities) {
        setNumberOfVariables(numberOfCities * 3);
        setNumberOfObjectives(2);
        setName("Ex4_Problem");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) 
        {
            if (i < numberOfCities)
            {
                // First third represents grid ids
                // So must be between 0 and 400
                lowerLimit.add(0.0);
                upperLimit.add(400.0);
            }
            else
            {
                // Second third represents offsets
                // so must be between -0.5 and 0.5
                lowerLimit.add(-0.5);
                upperLimit.add(0.5);
            }
            
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    public void evaluate(DoubleSolution solution) {
        ArrayList<Point> cities = new ArrayList<>();
        // Number of cities is one third number of variables. 
        int number_cities = getNumberOfVariables() / 3;
        for (int i = 0; i < getNumberOfVariables(); i+=2) 
        {
            // Gets the cell id. 
            double cell = solution.getVariableValue(number_cities);
            // Offset by number of cities so that it retrieves offsets. 
            double x = solution.getVariableValue(number_cities + i);
            double y = solution.getVariableValue(number_cities + i + 1);
            
            cities.add(new Point((cell + 1) * 0.5 + x, (cell + 1) * 0.5 + y));
        }

        Performance fitness = new Performance(cities, 3, 3);

        Double o1 = fitness.getFitnessPairInverAlg(1, 1) - fitness.getFitnessPairTwoOptAlg(1, 1);
        Double o2 = fitness.getFitnessPairInverTwoOp(1, 1) - fitness.getFitnessPairTwoOptAlg(1, 1);

        solution.setObjective(0, o1);
        solution.setObjective(1, o2);
    }
}
