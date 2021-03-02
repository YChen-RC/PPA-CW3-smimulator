import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of Giraffe.
 * Giraffe age, move, breed, eat and die.
 * 
 * @author David J. Barnes, Michael Kölling, Fang,Lidan and Yuxin Chen
 * @version 2021.03.01
 */
public class Giraffe extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a Giraffe can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a Giraffe can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a Giraffe breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The Giraffes distinguishes male and female.
    private static final double GENDER_PROBABILITY = 0.5;
    // The food value an animal will add after they ate a plant
    private static final int PLANT_FOOD_VALUE = 60;
    
    private int age;
    
    private int foodLevel;
    
    // Individual characteristics (instance fields).


    /**
     * Create a new rabbit. A Giraffe may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Giraffe(boolean randomAge, Field field, Location location)
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
     * This is what the giraffe does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newGiraffe A list to return newly born giraffe
     */
    public void act(List<Actor> newGiraffe)
    {
        incrementHunger();
        incrementAge();
        infectOthers();
        deathByInfection();
        heal();
        if(isActive()) {
            giveBirth(newGiraffe);            
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
     * @param newGiraffe A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newGiraffe)
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
                if(surroundingAnimal != null && surroundingAnimal instanceof Giraffe) {
                    Giraffe mate = (Giraffe) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int birth = breed();
                        for(int b = 0; b < birth && freeLocationList().size() > 0; b ++) {
                            if(freeLocationList().size() == 0) {
                                break;
                            }
                            Location loc = freeLocationList().remove(0);
                            Giraffe young = new Giraffe(false, field, loc);
                            generateRandomGender();
                            newGiraffe.add(young);
                        }
                    }
                }
            }
        }
    }

    /**
     * Look for Giraffe adjacent to the current location.
     * Only the first live giraffe is eaten.
     * @return Where food was found, or null if it wasn't.
     */
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

    /**
     * Increase the animal's hunger level when the method is called
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Getter method
     * @return food Level of the animal
     */
    protected int getFoodLevel()
    {
        return foodLevel;
    }

    /**
     * Getter method
     * @return the age the animal can breed
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Getter method
     * @return Maximun age a animal can be
     */
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * Getter method
     * @return breeding probability
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * Getter method
     * @return MaxLitterSize
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * Getter method
     * @return the propability to be in a gender
     */
    protected double getGenderProbability()
    {
        return GENDER_PROBABILITY;
    }

    /**
     * Get the adjacent location
     * @return the location
     */
    protected List<Location> getAdjacentLocation()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        return adjacent;
    }
    
}
