import java.util.regex.*;

public class Substrate
{
    private String name;
    private String sequence;
    
    public Substrate() 
    {
        String name = "Unknown substrate";
        sequence = "";
    }
    
    public Substrate(String givenSequence)
    {
        sequence = givenSequence;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getSequence()
    {
        return sequence;
    }
    
}
