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
    
    private static final int PLANT_FOOD_VALUE = 60;
    
    private int age;
    
    private int foodLevel;
    
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
        super(field, location);
        generateRandomGender();
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
        }
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
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Boolean isMeet = false;
        Field field = getField();
        List<Location> adjacent = getAdjacentLocation();
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            if(where != null) {
                Object surroundingAnimal = field.getObjectAt(where);
                if(surroundingAnimal != null && surroundingAnimal instanceof Rabbit) {
                    Rabbit mate = (Rabbit) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int birth = breed();
                        for(int b = 0; b < birth && freeLocationList().size() > 0; b ++) {
                            if(freeLocationList().size() == 0) {
                                break;
                            }
                            Location loc = freeLocationList().remove(0);
                            Rabbit young = new Rabbit(false, field, loc);
                            generateRandomGender();
                            newRabbits.add(young);
                        }
                    }
                }
            }
        }
    }
    
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = getAdjacentLocation();
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            if(plant instanceof Grass) {
                Grass grass = (Grass) plant;
                if(grass.isActive()) { 
                    grass.setDead();
                    foodLevel = PLANT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    protected int getFoodLevel()
    {
        return foodLevel;
    }
    
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    protected int getMaxAge() {
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
