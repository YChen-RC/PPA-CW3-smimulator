import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 7;
    // The rabbits distinguishes male and female.
    private static final double GENDER_PROBABILITY = 0.5;
    
    private static final int FOOD_VALUE = 60;
    
    private static final int DEFAULT_FOOD_LEVEL = 30;
    
    private int age;
   
    
    // Individual characteristics (instance fields).


    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(randomAge,field, location);
        generateRandomGender();
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits
     */
    public void act(List<Actor> newRabbits)
    {
        incrementHunger();
        incrementAge();
        
        deathByInfection();
        heal();
        if(isActive()) {
            giveBirth(newRabbits);            
            moveTo();
        }
    }
    
  
   
    
    protected Animal newAnimal(boolean randomAge, Field field, Location loc){
        return new Rabbit(randomAge, field, loc);
    }
    
    public Class<?>[]getFoodList()
    {
        Class<?>[]foodList = {Plant.class};
        return foodList;
    }
    
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
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
     
    protected int getMAX_AGE() {
        return MAX_AGE;
    }
    
     protected int getFoodLevel()
    {
        return FOOD_VALUE ;
    }
    
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    protected double getGenderProbability()
    {
        return GENDER_PROBABILITY;
    }
    
    protected List<Location> getAdjacentLocation()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        return adjacent;
    }
    
    protected int  getDEFAULT_FOOD_LEVEL() 
    {
        return DEFAULT_FOOD_LEVEL;
    }
    

}
