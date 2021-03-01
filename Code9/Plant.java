import java.util.List;
/**
 * 在这里给出对类 Grass 的描述。
 * 
 * @作者（你的名字）
 * @版本（一个版本号或者一个日期）
 */
public abstract class Plant extends Actor {

    // private static final int MAX_GROW_SIZE = 2;

    // private static final double GROWING_PROBABILITY = 0.06;

    // /**
     // * 类 Grass 的对象的构造函数
     // */
    // public Plant(boolean randomAge, Field field, Location location) {
        // super(randomAge, field, location);
    // }

    // public void act(List<Actor> newGrass) {
        // if (isActive()) {
            // growPlant(newGrass);
            // Location newLocation = getField().freeAdjacentLocation(getLocation());
            // if (newLocation != null) {
                // setLocation(newLocation);
            // } else {
                // // Overcrowding.
                // setDead();
            // }
        // }
    // }

    // protected void growPlant(List<Actor> newGrass) {
        // Field field = getField();
        // int birth = grow();
        // for (int b = 0; b < birth && freeLocationList().size() > 0; b++) {
            // if (freeLocationList().size() == 0) {
                // break;
            // }
            // Location loc = freeLocationList().remove(0);
            // Plant young = newPlant(false,field, loc);
            // newGrass.add(young);
        // }
    // }

    // Plant newPlant(boolean randomAge, Field field, Location location) {
        // return null;
    // }

    // protected double getGrowingProbability() {
        // return GROWING_PROBABILITY;
    // }

    // protected int getMaxGrowSize() {
        // return MAX_GROW_SIZE;
    // }

    // public void setDead() {
        // isActive = false;
        // if (location != null) {
            // field.clear(location);
            // field = null;
        // }
    // }

    // protected Location getLocation() {
        // return location;
    // }

    // /**
     // * @return The Actor's maximum age.
     // */
    // @Override
    // protected int getMAX_AGE() {
        // return 0;
    // }

    // /**
     // * @return How much food the Actor is worth.
     // */
    // @Override
    // protected int getFoodLevel() {
        // return 0;
    // }

    // protected void setLocation(Location newLocation) {
        // if (location != null) {
            // field.clear(location);
        // }
        // location = newLocation;
        // field.place(this, newLocation);
    // }


    // protected int grow() {
        // int births = 0;
        // if (rand.nextDouble() <= getGrowingProbability()) {
            // births = rand.nextInt(getMaxGrowSize()) + 1;
        // }
        // return births;
    // }

    // protected List<Location> freeLocationList() {
        // List<Location> free = field.getFreeAdjacentLocations(getLocation());
        // return free;
    // }
    



    /**
     * Create a new plant and place it in the field.
     * @param randomAge Whether or not this plant should start with a random age.
     * @param location The location within the field.
     * @param field The field currently occupied.
     * @param weather The weather affecting the plant.
     */
    public Plant(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
    }
    
    /**
     * Makes this plant act (spread and grow).
     * @param newPlants A list to receive newly created plants.
     */
    public void act(List<Actor> newPlants)
    {
        if (isActive()) {
            spread(newPlants);
            incrementAge();
        }
    }
    
    /**
     * Attempts to create new plants and add them to the List. New plants
     * grow in adjacent locations.
     * @param newPlants The list to attempt to add new plants to.
     */
    private void spread(List<Actor> newPlants)
    {
        // Get a list of adjacent free locations.
        Field field = getField();
        Location location = getLocation();
        List<Location> free = field.getFreeAdjacentLocations(location);
        // Check how many new plants are created.
        int seeds = pollenationSuccess();
        // Create the appropriate number of new plants.
        for(int b = 0; b < seeds && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Plant seed = buildSeed(false, field, loc);
            newPlants.add(seed);
        }
    }
    
    /**
     * Returns an initialised object of a Plant subclass which can be used by the spread method.
     * @param randomAge Whether or not this plant should start with a random age.
     * @param location The location within the field.
     * @param field The field currently occupied.
     * @param weather The weather affecting the plant.
     * @returns an object of a subclass plant as a Plant object.
     */
    abstract protected Plant buildSeed(boolean randomAge, Field field, Location loc);
        
    /**
     * Generate a number representing the new seeds that sucessfully take hold
     * if the plant is old enough to spread.
     * @return The number of successfull seeds (may be zero).
     */
    private int pollenationSuccess()
    {
        int seeds = 0;
        if(rand.nextDouble() <= getSEEDING_PROBABILITY() && age >= getSEEDING_AGE())
            seeds = rand.nextInt(getMAX_SEEDLINGS()) + 1;
        return seeds;
    }
    
    /**
     * @return true if the plant has reached its edible age.
     */
    public boolean isEdible()
    {
        return (age > getEDIBLE_AGE());
    }
    
    // methods for aquiring the constants of the subclasses.
    protected abstract int getMAX_SEEDLINGS();
    protected abstract int getSEEDING_AGE();
    protected abstract int getEDIBLE_AGE();
    protected abstract double getSEEDING_PROBABILITY();
}

