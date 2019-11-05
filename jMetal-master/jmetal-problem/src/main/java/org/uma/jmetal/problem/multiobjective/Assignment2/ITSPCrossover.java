package org.uma.jmetal.problem.multiobjective.Assignment2;

import java.util.ArrayList;

public interface ITSPCrossover
{
    public ArrayList<TSP_Instance> crossover(TSP_Instance first_parent, TSP_Instance second_parent);
}
