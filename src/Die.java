public class Die {
    private int sides;
    private int currentValue;
    public Die() {
        sides = 6;
        currentValue = roll();
    }
    public Die(int sides){
        this.sides = sides;
        currentValue = roll();
    }
    public int value(){
        return currentValue;
    }
    public int roll(){
        currentValue = randomWithRange(1,sides);
        return currentValue;
    }
    private int randomWithRange(int min, int max){
        int range = max - min + 1;
        return (int) (Math.random()*range + min);
    }
    public int numberOfSides(){
        return sides;
    }
    public boolean equals(Die aDie){
        return currentValue == aDie.value();
    }
    public String toString(){
        return ""+value();
    }
}
