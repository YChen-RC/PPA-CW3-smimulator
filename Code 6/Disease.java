import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * 在这里给出对类 Disease 的描述。
 * 
 * @作者（你的名字）
 * @版本（一个版本号或者一个日期）
 */
public class Disease
{
    private Animal animal;
    
    protected static final Random rand = Randomizer.getRandom();
    
    private boolean isInfected = false;
    private boolean isImmune = false;
    private int numInfected;
    private int numInfectionDeath = 0;
    private static final int INFECTION_TIME = 200;
    private double INFECT_PROBABILITY = 0.003;
    private double LETHALITY_RATE =0.04;
    private double HEAL_RATE = 0.04;
    
    /**
     * Constructor
     */
    public Disease()
    {
        
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
            Animal other = (Animal)animal.getField().getObjectAt(where);
            if(other instanceof Animal && rand.nextDouble() < INFECT_PROBABILITY) {
                other.infect();
                numInfected ++;
            }
        }
    }
    
    protected List<Location> getAdjacentLocations() {
        Boolean isMeet = false;
        Field field = animal.getField();
        List<Location> adjacent = field.adjacentLocations(animal.getLocation());
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
        if(animal.isInfected() && rand.nextDouble() < LETHALITY_RATE) {
            animal.setDead();
            numInfectionDeath ++;
        }
    }
    
    protected void heal()
    {
        if(animal.isActive() && animal.isInfected() && rand.nextDouble() < HEAL_RATE) {
            animal.infect();
            numInfected --;
        }
    }
    
    public void incrementNumInfected()
    {
        numInfected ++;
    }
    
    protected int getNumInfected()
    {
        return numInfected;
    }
    
    protected int getNumInfectionDeath()
    {
        return numInfectionDeath;
    }
}
