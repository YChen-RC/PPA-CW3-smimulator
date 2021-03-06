import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 
 */
public class Fox extends Animal
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a fox can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    private static final int FOOD_VALUE = 30;
    //The food value at the start
    private static final int DEFAULT_FOOD_LEVEL = 0;
    // The fax distinguishes male and female.
    private static final double GENDER_PROBABILITY = 0.5;
    
    // Individual characteristics (instance fields).
    // The fox's age.
    private int age;
   

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(randomAge,field,location);
        generateRandomGender();
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Actor> newFoxes)
    {
        incrementAge();
        incrementHunger();
        deathByInfection();
        heal();
        if(isActive()) {
            giveBirth(newFoxes);            
            // Move towards a source of food if found.  
        }
        moveTo();
    }
   
   
    
    protected Animal newAnimal(boolean randomAge, Field field, Location loc){
        return new Fox(randomAge,field,loc);
    }
    
    public Class<?>[] getFoodList()
    {
        Class<?>[]foodList = {Rabbit.class};
        return foodList;
    }
    
    protected int getFoodLevel()
    {
        return  FOOD_VALUE ;
    }
    
    protected int  getDEFAULT_FOOD_LEVEL() 
    {
        return DEFAULT_FOOD_LEVEL;
    }
    
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    protected int getMAX_AGE() {
        return MAX_AGE;
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
}
