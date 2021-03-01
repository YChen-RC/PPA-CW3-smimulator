import java.util.List;
public interface Actor
{
    /**
     * Perform the actor's regular behaviour
     * @param newActor A listfor receiving newly created actors
     */
    void act(List<Actor> newActor);
    
    /**
     * Is the actor still active?
     * @return true if still active, false if not.
     */
    boolean isActive();
}
