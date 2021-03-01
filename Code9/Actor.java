import java.util.List;
import java.util.Random;

/**
 * 
 */
public abstract class Actor
{
    protected static final Random rand = Randomizer.getRandom();
    
    // Whether the entity is alive or not.
    protected boolean isActive = true;
    // The entity's field.
    protected Field field;
    // The entity's position in the field.
    protected Location location;
    // The weather affecting the entity.
    //protected Weather weather;
    // The entity's age.
    protected int age;
    
    /**
     * Create a new entity and place it in the field.
     * @param location The location within the field.
     * @param field The field currently occupied.
     */
    public Actor(boolean randomAge,Field field,Location location) 
    {
        this.field = field;
        //this.weather = weather;
        setLocation(location);
         if(randomAge){
            age = rand.nextInt(getMAX_AGE());}
        else{
            age = 0;
    }
}
    
    /**
     * Places the entity at the new location in the given field.
     * @param newLocation The entity's new location.
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
     * Makes this entity act.
     * @param newEntities A list to receive newly made entities.
     */
    abstract public void act(List<Actor> newEntities);
    
    /**
     * Increase the entity's age. This could result in its death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMAX_AGE()) {
            setDead();
        }
    }
    
    /**
     * Indicates that the entity is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        // System.out.println("Killed: "+super.toString()); 
        isActive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * @return true if the entity is still alive.
     */
    protected boolean isActive()
    {
        return isActive;
    }
   

    /**
     * @return The entity's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * @return The entity's field.
     */
    protected Field getField()
    {
        return field;
    }
     
    // Methods for ensuring access to the MAX_AGE and FOOD_VALUE constants of the subclasses.
    /**
     * @return The entity's maximum age.
     */
    abstract protected int getMAX_AGE();
    
    /**
     * @return How much food the entity is worth.
     */
    abstract protected int getFoodLevel();
}
