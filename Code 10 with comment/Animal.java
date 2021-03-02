import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kölling, Fang,Lidan and Yuxin Chen
 * @version 2021.03.01
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
    
    private List<Location> adjacent = new LinkedList<>();
    
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    // The animal's age
    protected int age;
    // The shared time track for animal    
    protected boolean isDayTime = true;
    
    // Challenge Task 3 Simulate Disease
    //The animal is infected by disease
    private boolean isInfected = false;
    //The animal is immune to the disease
    private boolean isImmune = false;
    //Number of animal that are infected
    private int numInfected;
    //Number of animal that are dead after infected
    private int numInfectionDeath = 0;
    //Probability that will be infect
    private double INFECT_PROBABILITY = 0.003;
    private double LETHALITY_RATE =0.04;
    //Heal rate after infected the disease
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

    /**
     * Check can the animal breed
     * @return true if the animal can breed
     */
    protected boolean canBreed(){

        return (age >= getBreedingAge() && !isMale());
    }

    /**
     * Check is the animal male or female
     * @return true if the animal is male
     */
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

    /**
     * Generate random gender to the animal
     */
    protected void generateRandomGender()
    {
        if (getGenderProbability() == 1) {
            gender = Gender.UNKNOWN;
        }
        else if (rand.nextDouble() <= getGenderProbability()) {
            gender = Gender.FEMALE;
        }
    }

    /**
     * Getter Method
     * @return gender of an animal
     */
    protected Gender getGender()
    {
        return gender;
    }

    /**
     * @return A list of free location in the field
     */
    protected List<Location> freeLocationList()
    {
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        return free;
    }

    /**
     * Change the isInfected state of an animal to true
     * when the animal is infected
     */
    protected void infect()
    {
        isInfected = true;
    }

    /**
     * Spread the Disease infect other animal
     */
    protected void infectOthers() {  
        if(isActive()) {
            Field field = getField();
            List<Location> adjacent = getAdjacentLocation();
            for(Location location : adjacent) {
                Location where = location;
                if(where != null && getField().getObjectAt(where) instanceof Animal) {
                    Animal other = (Animal)getField().getObjectAt(where);
                    if(isInfected && !other.isImmune() && rand.nextDouble() < INFECT_PROBABILITY) {
                        other.infect(); 
                        numInfected ++;
                    }
                }
            } 
        }   
    }

    /**
     *
     * @return is an animal immune to the disease or not
     */
    protected boolean isImmune()
    {
        return isImmune;
    }

    /**
     *The animal set to dead if infected and under other condition
     */
    protected void deathByInfection()
    {
        if(isInfected && rand.nextDouble() < LETHALITY_RATE) {
            setDead();
            numInfectionDeath ++;
        }
    }

    /**
     * Let the animal to heal from disease
     */
    protected void heal()
    {
        if(active && isInfected && rand.nextDouble() < HEAL_RATE) {
            infect();
            isImmune = true;
            numInfected --;
        }
    }

    /**
     * @return The number of animals that are infected
     */
    protected int getNumInfected()
    {
        return numInfected;
    }

    /**
     * @return The number of animals that died after infected
     */
    protected int getNumInfectionDeath()
    {
        return numInfectionDeath;
    }

    /**
     * @return The infection probability
     */
    protected double infectProb()
    {
        return INFECT_PROBABILITY;
    }
    
    protected void incrementNumInfected()
    {
        numInfected ++;
    }

    abstract protected int getBreedingAge();
    
    abstract protected int getMaxAge();
    
    abstract protected double getBreedingProbability();
    
    abstract protected int getMaxLitterSize();
    
    abstract public void act(List<Actor> newActors);
    
    abstract protected double getGenderProbability();

    abstract protected List<Location> getAdjacentLocation();
}
