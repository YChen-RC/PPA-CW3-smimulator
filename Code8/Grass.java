import java.util.List;
/**
 * 在这里给出对类 Grass 的描述。
 * 
 * @作者（你的名字）
 * @版本（一个版本号或者一个日期）
 */
public class Grass extends Plant
{
    
    private static final int MAX_GROW_SIZE = 2;
    
    private static final double GROWING_PROBABILITY = 0.06;
    
    /**
     * 类 Grass 的对象的构造函数
     */
    public Grass(Field field, Location location)
    {
        super(field, location);
    }
    
    public void act(List<Actor> newGrass)
    {
        if(isActive()){
            growPlant(newGrass);
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    protected void growPlant(List<Actor> newGrass)
    {
        Field field = getField();
        int birth = grow();
        for(int b = 0; b < birth && freeLocationList().size() > 0; b ++) {
            if(freeLocationList().size() == 0) {
                break;
            }
            Location loc = freeLocationList().remove(0);
            Plant young = new Grass(field, loc);
            newGrass.add(young);
        }
    }
    
    protected double getGrowingProbability()
    {
        return GROWING_PROBABILITY;
    }
    
    protected int getMaxGrowSize()
    {
        return MAX_GROW_SIZE;
    }
}
