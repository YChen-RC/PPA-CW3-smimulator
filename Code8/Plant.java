import java.util.List;
import java.util.Random;
/**
 * A class representing plants
 * 
 *
 */
public abstract class Plant implements Actor
{
    private boolean active;
    
    private Field field;
    
    private Location location;
    
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
     * 
     * 
     */
    public void setDead()
    {
        active = false;
        if(location != null) {
            field.clear(location);
            field = null;
        }
    }
    
    protected Location getLocation()
    {
        return location;
    }
    
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
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
    
    protected int grow()
    {
        int births = 0;
        if(rand.nextDouble() <= getGrowingProbability()) {
            births = rand.nextInt(getMaxGrowSize()) + 1;
        }
        return births;
    }

    protected List<Location> freeLocationList()
    {
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        return free;
    }
    
    abstract public void act(List<Actor> newActors);
    
    abstract protected int getMaxGrowSize();
    
    abstract protected double getGrowingProbability();
}
