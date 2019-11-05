package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.problem.multiobjective.Assignment2.*;

import java.util.ArrayList;
import java.util.List;

public class Ex2_Problem extends AbstractDoubleProblem {
    public Ex2_Problem() {
        this(50);
    }

    public Ex2_Problem(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(2);
        setName("Ex2_Problem");

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

        for (int i = 0; i < (getNumberOfVariables() - 1); i++) {
            int cell = (int)Math.floor(solution.getVariableValue(i));

            int x = cell / 20;
            int y = cell % 20;

            cities.add(new Point(x, y));
        }

        Performance fitness = new Performance(cities, 3, 3);

        Double o1 = fitness.getFitnessPairInverAlg(1, 1) - fitness.getFitnessPairTwoOptAlg(1, 1);
        Double o2 = fitness.getFitnessPairInverTwoOp(1, 1) - fitness.getFitnessPairTwoOptAlg(1, 1);

        solution.setObjective(0, o1);
        solution.setObjective(1, o2);
    }
}