import java.util.List;
/**
 * A simple model of Grass.
 * Grass age, breed, grow and die.
 *
 * @author David J. Barnes, Michael Kölling, Fang,Lidan and Yuxin Chen
 * @version 2021.03.01
 */
public class Grass extends Plant
{
    //The maximun grow size a plant can have
    private static final int MAX_GROW_SIZE = 2;

    //The probability that a plant can grow
    private static final double GROWING_PROBABILITY = 0.05;
    
    /**
     * 类 Grass 的对象的构造函数
     */
    public Grass(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * This is plant behaviour and what they do most of the time
     * @param newGrass A list to return all the new born grass
     */
    public void act(List<Actor> newGrass)
    {
        if(isActive()){
            growPlant(newGrass);
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Grows a new plant when this method is called
     * @param newGrass A list to return all the new born grass
     */
    protected void growPlant(List<Actor> newGrass)
    {
        Field field = getField();
        int birth = grow();
        for(int b = 0; b < birth && freeLocationList().size() > 0; b ++) {
            if(freeLocationList().size() == 0) {
                break;
            }
            Location loc = freeLocationList().remove(0);
            Plant young = new Grass(field, loc);
            newGrass.add(young);
        }
    }

    /**
     * Getter method
     * Returns the growing probability
     */
    protected double getGrowingProbability()
    {
        return GROWING_PROBABILITY;
    }
    /**
     * Getter method
     * Returns the Max growing size
     */
    protected int getMaxGrowSize()
    {
        return MAX_GROW_SIZE;
    }
}
