import java.util.List;
import java.util.Random;
/**
 * A class representing share characteristic of plants
 *
 *@author David J. Barnes, Michael Kölling, Fang,Lidan and Yuxin Chen
 *@version 2021.03.01
 */
public abstract class Plant implements Actor
{
    //The active state of a plant
    private boolean active;
    //The plant's field
    private Field field;
    //The plant's location in the field
    private Location location;
    //Generate random number
    protected static final Random rand = Randomizer.getRandom();
    
    /**
     * 类 Plant 的对象的构造函数
     */
    public Plant(Field field, Location location)
    {
        active =true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Set the statues of a plant to dead
     */
    public void setDead()
    {
        active = false;
        if(location != null) {
            field.clear(location);
            field = null;
        }
    }

    /**
     * Getter method
     * @return location
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * In a condition, set the object to at a new place
     * @param newLocation the new location
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Getter Method
     * @return field
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Grow the plant under the condition and
     * growing probability
     * @return births
     */
    protected int grow()
    {
        int births = 0;
        if(rand.nextDouble() <= getGrowingProbability()) {
            births = rand.nextInt(getMaxGrowSize()) + 1;
        }
        return births;
    }

    /**
     * Get the list of free location in the field
     * @return list of location
     */
    protected List<Location> freeLocationList()
    {
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        return free;
    }

    abstract public void act(List<Actor> newActors);
    
    abstract protected int getMaxGrowSize();
    
    abstract protected double getGrowingProbability();
}
