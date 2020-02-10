public class DicePair {
    private Die die1;
    private Die die2;
    private int total = 0;
    private int maxSum = 0;
    public DicePair(){
        die1 = new Die();
        die2 = new Die();
        maxSum = die1.numberOfSides() + die2.numberOfSides();
    }
    public int roll(){
        total=die1.roll()+die2.roll();
        return total;
    }
    public boolean equal(){
        return die1.equals(die2);
    }
    public int diePairTotal(){
        return total;
    }
    public int pairMaxSum(){
        return maxSum;
    }
    public String toString(){
        return ("Die 1: "+die1.value()+" Die2: "+die2.value());
    }
}
