import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a snake.
 * snakes age, move, eat rabbits, and die.
 *
 * @author David J. Barnes, Michael Kölling, Fang,Lidan and Yuxin Chen
 * @version 2021.03.01
 */
public class Snake extends Animal
{
    // Characteristics shared by all snakees (class variables).
    
    // The age at which a snake can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a snake can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a snake breeding.
    private static final double BREEDING_PROBABILITY = 0.075;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a snake can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 20;
    // The fax distinguishes male and female.
    private static final double GENDER_PROBABILITY = 1;
    
    // Individual characteristics (instance fields).
    // The snake's age.
    private int age;
    // The snake's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a snake. A snake can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the snake will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Snake(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        generateRandomGender();
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the snake does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newSnakes A list to return newly born snakees.
     */
    public void act(List<Actor> newSnakes)
    {
        incrementAge();
        incrementHunger();
        infectOthers();
        deathByInfection();
        heal();
        if(isActive()) {
            giveBirth(newSnakes);            
            // Move towards a source of food if found.
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
     * Make this snake more hungry. This could result in the snake's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = getAdjacentLocation();
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isActive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this snake is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newSnakes A list to return newly born snakees.
     */
    private void giveBirth(List<Actor> newSnakes)
    {
        // New snakes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Boolean isMeet = false;
        Field field = getField();
        List<Location> adjacent = getAdjacentLocation();
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            if(where != null)
            {
                Object surroundingAnimal = field.getObjectAt(where);
                if(surroundingAnimal != null && surroundingAnimal instanceof Snake) {
                    Snake mate = (Snake) surroundingAnimal;
                    if(this.getGender() == mate.getGender()) {
                        int birth = breed();
                        for(int b = 0; b < birth && freeLocationList().size() > 0; b ++) {
                            if(freeLocationList().size() == 0) {
                                break;
                            }
                            Location loc = freeLocationList().remove(0);
                            Snake young = new Snake(false, field, loc);
                            generateRandomGender();
                            newSnakes.add(young);
                        }
                    }
                }
            }
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
     * @return the probability to be in a gender
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
