package org.uma.jmetal.problem.multiobjective.Assignment2;

import java.util.ArrayList;

public interface ILocalSearchOperator
{
    public void mutate(ArrayList<Integer> permutation, int first, int second);
    public String name();
}
