import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.03;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.01;  
    
    private static final double SNAKE_CREATION_PROBABILITY = 0.03;
    
    private static final double TIGER_CREATION_PROBABILITY = 0.03;
    
    private static final double GRASS_CREATION_PROBABILITY = 0.02;  

    // List of animals in the field.
    private final List<Actor> actors;
    // The current state of the field.
    private final Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;
    // The current hour track of the simulation
    private int hourTrack;
    // The current day track of the simulation
    private boolean isNightTime;
    
    protected boolean isRaining = false;
    
    protected double RAIN_PROBABILITY = 0.7;
    
    protected static final Random rand = Randomizer.getRandom();
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        actors = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Snake.class, Color.YELLOW);
        view.setColor(Tiger.class, Color.RED);
        view.setColor(Grass.class, Color.GREEN);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            // delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step ++;
        
        changeWeather();

        // Provide space for newborn animals.
        List<Actor> newActors = new ArrayList<>();
        // Let all rabbits act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            Actor actors = it.next();
            if(!isAtNight() && actors instanceof Animal) {
                actors.act(newActors);
            }
            if(!isRaining() && actors instanceof Plant) {
                actors.act(newActors);
            }
            if(! actors.isActive()) {
                    it.remove();
                }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        actors.addAll(newActors);

        view.showStatus(step, field, isAtNight(), isRaining());
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        actors.clear();
        populate();
        changeWeather();
        // Show the starting state in the view.
        view.showStatus(step, field, isAtNight(), isRaining);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        
        int randRow = rand.nextInt(field.getDepth());
        int randCol = rand.nextInt(field.getWidth());
        Location randLocation = new Location(randRow, randCol);
        Fox illFox = new Fox(true, field, randLocation);
        illFox.infect();
        actors.add(illFox);
        
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(field.getObjectAt(row, col) == null) {
                    if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Fox fox = new Fox(true, field, location);
                        actors.add(fox);
                    }
                    else if(rand.nextDouble() <= SNAKE_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Snake snake = new Snake(true, field, location);
                        actors.add(snake);
                    }
                    else if(rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Tiger tiger = new Tiger(true, field, location);
                        actors.add(tiger);
                    }
                    else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Rabbit rabbit = new Rabbit(true, field, location);
                        actors.add(rabbit);
                    }
                    else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                        Location location = new Location(row, col);
                        Grass grass = new Grass(field, location);
                        actors.add(grass);
                    }
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
    private boolean isAtNight()
    {
        if(step % 24 <= 14) {
            isNightTime = false;
        }
        else {
            isNightTime = true;
        }
        return isNightTime;
    }
    
    private boolean isRaining()
    {
        return isRaining;
    }
    
    private void changeWeather()
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