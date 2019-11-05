package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.problem.multiobjective.Assignment2.*;

import java.util.ArrayList;
import java.util.List;

public class Ex3_Problem extends AbstractDoubleProblem {
    public Ex3_Problem() {
        this(50);
    }

    public Ex3_Problem(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables * 2);
        setNumberOfObjectives(2);
        setName("Ex3_Problem");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(400.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    public void evaluate(DoubleSolution solution) {
        ArrayList<Point> cities = new ArrayList<>();

        for (int i = 0; i < getNumberOfVariables(); i+=2) {
            double x = solution.getVariableValue(i);
            double y = solution.getVariableValue(i+1);

            cities.add(new Point(x, y));
        }

        Performance fitness = new Performance(cities, 3, 3);

        Double o1 = fitness.getFitnessPairInverAlg(1, 1) - fitness.getFitnessPairTwoOptAlg(1, 1);
        Double o2 = fitness.getFitnessPairInverTwoOp(1, 1) - fitness.getFitnessPairTwoOptAlg(1, 1);

        solution.setObjective(0, o1);
        solution.setObjective(1, o2);
    }
}