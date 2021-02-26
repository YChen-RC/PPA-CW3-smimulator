import java.util.Random;
/**
 * 在这里给出对类 Weather 的描述。
 * 
 * @作者（你的名字）
 * @版本（一个版本号或者一个日期）
 */
public class Weather
{
    protected static final Random rand = Randomizer.getRandom();
    
    protected boolean isRaining = false;
    
    protected double RAIN_PROBABILITY = 0.09;
    
    public Weather()
    {
    }
    
    public boolean isRaining()
    {
        return isRaining;
    }
    
    protected void changeWeather()
    {
        if(rand.nextDouble() < RAIN_PROBABILITY)
        {
            isRaining = true;
        }
        else {
            isRaining = false;
        }
    }
    
}
