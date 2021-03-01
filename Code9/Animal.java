import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Actor
{
   
    // The animal's gender
    private Gender gender;
    
    private List<Location> adjacent = new LinkedList<>();
    
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    // The animal's age
    protected int age;
    // The shared time track for animal    
    protected boolean isDayTime = true;
    
    protected int foodLevel;
    // Chellenge Task 3 Simulate Disease
    protected boolean isInfected = false;
    protected boolean isImmune = false;
    protected int numInfected;
    protected int numInfectionDeath = 0;
    private static final int INFECTION_TIME = 200;
    protected static final double INFECT_PROBABILITY = 0.003;
    protected static final double DIE_RATE =0.04;
    protected static final double HEAL_RATE = 0.04;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(boolean randomAge, Field field, Location location)
    {
        super(randomAge,field,location);
         if(randomAge){
            foodLevel = rand.nextInt(getFoodLevel());}
        else{
            foodLevel = getDEFAULT_FOOD_LEVEL();}
    }
    
    protected void giveBirth(List<Actor> newAnimals)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Boolean isMeet = false;
        Field field = getField();
        List<Location> adjacent = field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            if(where != null) {
                Actor surroundingAnimal = field.getActorAt(where);
                if(surroundingAnimal != null && surroundingAnimal instanceof Animal) {
                    Animal mate = (Animal) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int birth = breed();
                        for(int b = 0; b < birth && freeLocationList().size() > 0; b ++) {
                            if(freeLocationList().size() == 0) {
                                break;
                            }
                            Location loc = freeLocationList().remove(0);
                            Animal young = newAnimal(false, field, loc);
                            generateRandomGender();
                            newAnimals.add(young);
                        }
                    }
                }
            }
        }
    }
    
    abstract protected Animal newAnimal(boolean randomAge, Field field, Location loc);
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = this.field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Actor food = field.getActorAt(where);
            for(Class<?> foodList :getFoodList()) {
                if (foodList.isInstance(food)){
                    if (food.isActive()){
                        food.setDead();
                        foodLevel = foodLevel+food.getFoodLevel();
                    }
            }
                
            }
        }
        return null;
    }
    
    abstract protected Class<?>[]getFoodList();
    
    protected void moveTo()
    {
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
  
    
    protected boolean canBreed(){
        return (age >= getBreedingAge() && !isMale());
    }
    
    protected boolean isMale()
    {
        return (gender == Gender.MALE);
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && (rand.nextDouble() <= getBreedingProbability())) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    protected void generateRandomGender()
    {
        if (getGenderProbability() == 1) {
            gender = Gender.UNKNOWN;
        }
        else if (rand.nextDouble() <= getGenderProbability()) {
            gender = Gender.FEMALE;
        }
    }
    
    protected Gender getGender()
    {
        return gender;
    }
    
    protected List<Location> freeLocationList()
    {
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        return free;
    }
    
    
    protected void infect()
    {
        if (rand.nextDouble()<= INFECT_PROBABILITY){
            isInfected = true;
        }
    }
    
    public boolean isInfected()
    {
        return isInfected;
    }
    
    protected void infectOthers() {  
        if (isInfected){
        List<Location> adjacent = this.field.getFreeAdjacentLocations(getLocation());
        for(Location location : adjacent) {
            Location where = location;
            if(where != null && getField().getActorAt(where) instanceof Animal) {
                Animal other = (Animal)getField().getActorAt(where);
                if(isInfected && !isImmune && rand.nextDouble() < INFECT_PROBABILITY) {
                    other.infect(); 
                    numInfected ++;
                }
            }
        }}
    }
    
    
    protected void deathByInfection()
    {
        if(isInfected && rand.nextDouble() < DIE_RATE) {
            setDead();
            numInfectionDeath ++;
        }
    }
    
    protected void heal()
    {
        if(isInfected && rand.nextDouble() < HEAL_RATE) {
            infect();
            isImmune = true;
            numInfected --;
        }
    }
    
    protected int getNumInfected()
    {
        return numInfected;
    }
    
    protected int getNumInfectionDeath()
    {
        return numInfectionDeath;
    }
    
    protected double infectProb()
    {
        return INFECT_PROBABILITY;
    }
    
    protected void incrementNumInfected()
    {
        numInfected ++;
    }
    
    abstract protected int getBreedingAge();
    
    abstract protected int getMAX_AGE();
    
    abstract protected double getBreedingProbability();
    
    abstract protected int getMaxLitterSize();
    
    abstract protected int getFoodLevel();
    
    abstract protected int  getDEFAULT_FOOD_LEVEL();
    
    abstract public void act(List<Actor> newActors);
    
    abstract protected double getGenderProbability();

}
