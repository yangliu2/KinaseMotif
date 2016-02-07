import java.util.regex.*;

public class Kinase
{
    private String name;
    private String motifString;
    private char [] motifChar;
    private boolean found = false;
    private int[] start = new int[100];
    private int numFound = 0;
    private int motifLatency = 0;

    public Kinase()
    {
        name = "Unknown Kinase";
    }
    
    public Kinase(String givenMotif)
    {
        motifString = givenMotif;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getMotif()
    {
        return motifString;
    }
    
    public boolean getFound()
    {
        return found;
    }
    
    public int[] getStart()
    {
        return start;
    }
    
    public int getNumFound()
    {
        return numFound;
    }
    
    public void processMotif()
    {
        StringBuffer buf;
        
        // chop motifString in to a array
        motifChar = motifString.toCharArray();
        
        // take out '-' in the motif
        int length = motifChar.length;
        for (int i = 0; i < length; i++)
        {
            if (motifChar[i] == '-')
            {
                for (int j = i+1; j < length; j++) 
                {
                    motifChar[j-1] = motifChar[j];
                }
                length--;
            }
        }
        char[] modMotifChar = new char[length];
        for (int i = 0; i < modMotifChar.length; i++)
        {
            modMotifChar[i] = motifChar[i];
        }
        motifString = new String(modMotifChar);
        findLatency(); // find latency
        
        //change S/T to [ST]
        char[] mod2MotifChar = new char[modMotifChar.length];
        for (int i = 0; i < modMotifChar.length; i++)
        {
            mod2MotifChar[i] = modMotifChar[i];
        }
        
        length = mod2MotifChar.length;
        motifString = new String(mod2MotifChar);
        
        for (int i = 0; i < mod2MotifChar.length; i++) 
        {
            if (mod2MotifChar[i] == '/') 
            {
                mod2MotifChar[i] = mod2MotifChar[i-1];
                mod2MotifChar[i - 1] = '[';
                
                // insert i+2 element to i+3 position
                motifString = new String(mod2MotifChar);
                buf = new StringBuffer(motifString);
                
                if (i <(length-2)) 
                {
                    buf = buf.insert((i + 2),']');
                    motifString = buf.toString();
                    mod2MotifChar = motifString.toCharArray();
                }
                else 
                {
                    buf = buf.append(']');
                    motifString = buf.toString();
                    mod2MotifChar = motifString.toCharArray();
                }
            }
        }
        
        //convert all letters in motifSting into lowercase
        motifString = motifString.toLowerCase();
        // replace x with .(. means any letter)
        motifString = motifString.replace('x','.');
        
    }
    
    public void findLatency()
    {
        Pattern p = Pattern.compile("[a-z]");
        Matcher m = p.matcher(motifString);
        if (m.find()) 
        {
            motifLatency = m.start();
            
            char [] copy = motifString.toCharArray();
            int length = m.start();
            for (int i = 0; i < length; i++) 
            {
                if (copy[i] == '/' ) 
                {
                    motifLatency = motifLatency - 2;
                    if (copy [i+2] == '/')
                        motifLatency++;
                }
            }
        }
    }
    
    public boolean findPhosphateSite(String sequence)
    {
        
        //convert all letter in sequence to lower case
        sequence = sequence.toLowerCase();
        
        char[] sequenceChar = new char[1000];
        sequenceChar = sequence.toCharArray();
        
        processMotif(); //process motif before matching
        
        // match sequence with motifString
        Pattern p = Pattern.compile(motifString);
        Matcher m = p.matcher(sequence);
        
        while (m.find()) 
        {
            start[numFound] = m.start() + motifLatency + 1;
            found = true;
            numFound++;
        }
            
        return found;
    }
}
