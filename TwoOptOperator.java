import java.util.ArrayList;

// Class which implements the TwoOptOperator. 
public class TwoOptOperator implements ILocalSearchOperator
{
    // Implements the TwoOptOperator.
    public void mutate(ArrayList<Integer> permutation, int first, int second)
    {
        int temp;
        while (true)
        {
            if (first == second)
            {
                break;
            }
            else if (first+1 == second)
            {
                temp = permutation.get(first);
                permutation.set(first, permutation.get(second));
                permutation.set(second, temp);
                break;
            }
            else
            {
                temp = permutation.get(first);
                permutation.set(first, permutation.get(second));
                permutation.set(second, temp);
                first++;
                second--;
            }
        }
    }
    
    // Gets the name of this Local Search Operator.
    public String name()
    {
        return "Two opt";
    }
}
