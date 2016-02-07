import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;              
import java.awt.event.*;  
import java.io.*;

public class Window extends JFrame implements ActionListener
{
    private JLabel motifLabel, sequenceLabel;
    private JTextField helpField, motifField, sequenceField, outputField;
    private JButton findButton;
    private JFileChooser fc;
    private JMenuItem motifItem, sequenceItem;
    private String motif, sequence;
    private JComboBox motifCombo;
    
    public Window()
    {
        super("Kinase Site Finder");
        setSize (600,400);
        Container container = getContentPane();
        container.setLayout(new GridLayout(8,1));
        
        // motif label and short explaination
        motifLabel = new JLabel("Kinase Motif: ");
        container.add(motifLabel);
        helpField = new JTextField 
                ("(Use lowercase to indicate phosphoacceptor residue.)",50);
        helpField.setEditable(false);
        container.add(helpField);
        
        //JcomboBox for selecting common motifs
        String[] motif = {"Select a common kinase motif","PKA - R-X-s/t",
                          "CKI - S-X-X-s/t","CKII - s/t-X-X-E",
                          "GSK3 - s-X-X-X-S","CDC2 - s/t-P-X-R/K",
                          "CaMKII - R-X-X-s/t","MAPK - X-X-s/t-P"};
        motifCombo = new JComboBox(motif);
        motifCombo.setSelectedIndex(0);
        container.add(motifCombo);
        motifCombo.addActionListener(this);
        
        motifField = new JTextField (40);
        container.add(motifField);
        
        //sequence field displaying the chosen sequence
        sequenceLabel = new JLabel("Amino acid sequence: ");
        container.add(sequenceLabel);
        sequenceField = new JTextField (40);
        container.add(sequenceField);
        
        //output field displaying the result
        outputField = new JTextField(30);
        outputField.setEditable(false);
        container.add(outputField);
        
        findButton = new JButton("Find");
        container.add(findButton);
        findButton.addActionListener(this);
        
        // menu bar for importing files of motif and sequence
        JMenu fileMenu = new JMenu("File");
        
        motifItem = new JMenuItem("Open Motif from File");
        motifItem.addActionListener(this);
        fileMenu.add(motifItem);
        
        sequenceItem = new JMenuItem("Open AA sequence from File");
        sequenceItem.addActionListener(this);
        fileMenu.add(sequenceItem);
        
        JMenuBar mBar = new JMenuBar();
        mBar.add(fileMenu);
        setJMenuBar(mBar);
        
    }
    
    public void actionPerformed(ActionEvent e) 
    {
        // listener for find button
        if (e.getActionCommand().equals("Find"))
        {
            String motif = motifField.getText();
            
            String sequence = sequenceField.getText();
            
            Kinase kinase = new Kinase(motif);
            //Substrate substrate = new Substrate(sequence);
            
            kinase.findPhosphateSite(sequence);
            if (kinase.getFound() == true) {
                String output = "Match Found! Phosphoacceptor residue at ";
                for (int i = 0; i < kinase.getNumFound(); i++) {
                    output = output.concat(kinase.getStart()[i] + ", ");
                }
                
                outputField.setText(output);
            }
            else if (kinase.getFound() == false)
                outputField.setText("No match found");    
        }
        //listerner for open motif file
        else if (e.getActionCommand().equals("Open Motif from File"))
        {
            fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(motifItem);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File motifFile = fc.getSelectedFile();
                
                try
                {
                    BufferedReader motifBuf = new BufferedReader 
                                             (new FileReader(motifFile)); 
                    motif = motifBuf.readLine();
                    motifField.setText(motif);
                } 
                catch (IOException exception)
                {
                    outputField.setText("No motif file found!");
                }

            } 
        }
        //listener for open amino acide sequence file
        else if (e.getActionCommand().equals("Open AA sequence from File"))
        {
            fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(sequenceItem);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File sequenceFile = fc.getSelectedFile();
                try
                {
                    BufferedReader sequenceBuf = new BufferedReader 
                                                (new FileReader(sequenceFile)); 
                    sequence = sequenceBuf.readLine();
                    sequenceField.setText(sequence);
                } 
                catch (IOException exception)
                {
                    outputField.setText("No sequence file found!");
                }
                
            } 
        }
        //listener for combobox of motifs
        else if ("comboBoxChanged".equals(e.getActionCommand()))
        {
            switch(motifCombo.getSelectedIndex())
            {
                case 0: break;
                case 1: motifField.setText("R-X-s/t");break;
                case 2: motifField.setText("S-X-X-s/t");break;
                case 3: motifField.setText("s/t-X-X-E");break;
                case 4: motifField.setText("s-X-X-X-S");break;
                case 5: motifField.setText("s/t-P-X-R/K");break;
                case 6: motifField.setText("R-X-X-s/t");break;
                case 7: motifField.setText("X-X-s/t-P");break;
                default: motifField.setText("Select a motif.");
            }
        }
    }

    public static void main(String[] args) 
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        Window window = new Window();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.pack();
        window.setVisible(true);
    }
    
}
