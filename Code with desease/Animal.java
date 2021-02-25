import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal implements Actor
{
    // Whether the animal is alive or not.
    private boolean active;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The animal's gender
    private Gender gender;
    
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    // The animal's age
    protected int age;
    // The shared time track for animal    
    protected boolean isDayTime = true;
    
    // Chellenge Task 3 Simulate Disease
    private boolean isInfected = false;
    private boolean isImmune = false;
    private int numInfected = 0;
    private int numInfectionDeath = 0;
    private static final int INFECTION_TIME = 200;
    private double INFECT_PROBABILITY = 0.003;
    private double LETHALITY_RATE =0.04;
    private double HEAL_RATE = 0.04;
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        active = true;
        gender = Gender.MALE;
        this.field = field;
        setLocation(location);
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        active = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
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
     * Return the animal's field.
     * @return The animal's field.
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
     * Increase the age.
     * This could result in the animal's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
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
        isInfected = true;
    }
    
    /**
     * Chack whether animals are touching anoher person. If they are, infect them.
     */
    protected void infectOthers()
    {
        List<Location> intersecting = getAdjacentLocations();
        Iterator<Location> it = intersecting.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Animal other = (Animal)field.getObjectAt(where);
            if(other instanceof Animal && rand.nextDouble() < INFECT_PROBABILITY) {
                other.infect();
                numInfected ++;
            }
        }
    }
    
    protected List<Location> getAdjacentLocations() {
        Boolean isMeet = false;
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            if(where != null) {
                Object surroundingAnimal = (Animal)field.getObjectAt(where);
                if(surroundingAnimal != null) {
                    adjacent.add(where);
                }
            }
        }
        return adjacent;
    }
    
    protected void deathByInfection()
    {
        if(isInfected && rand.nextDouble() < LETHALITY_RATE) {
            setDead();
            numInfectionDeath ++;
        }
    }
    
    protected void heal()
    {
        if(active && isInfected && rand.nextDouble() < HEAL_RATE) {
            isInfected = false;
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
    
    abstract protected int getBreedingAge();
    
    abstract protected int getMaxAge();
    
    abstract protected double getBreedingProbability();
    
    abstract protected int getMaxLitterSize();
    
    abstract public void act(List<Actor> newActors);
    
    abstract protected double getGenderProbability();

}
