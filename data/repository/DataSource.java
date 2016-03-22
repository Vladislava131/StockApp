package stockapp.data.repository;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource 
{    
    private String fileName;
    private static final Logger log = Logger.getLogger(DataSource.class.getName());
    
    public DataSource(String fileName)
    {
        this.fileName = fileName;
        
        FileHandler fileLogHandler = null;
        try
        {
            fileLogHandler = new FileHandler("./log/stockapp.log");
            fileLogHandler.setLevel(Level.ALL);
            this.log.addHandler(fileLogHandler);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public ArrayList<String> readData()
    {
        try
        {
            File file = new File(this.fileName);
            if(!file.exists())
                file.createNewFile();
            
            BufferedReader bufferedInputData = new BufferedReader(new FileReader(this.fileName));
            ArrayList<String> data = new ArrayList<String>();
            String line = null;
            while((line = bufferedInputData.readLine()) != null)
            {
                data.add(line);
            }
            
            bufferedInputData.close();
            
            return data;
        }
        catch(IOException ex)
        {
            this.log.severe(ex.getMessage());            
            return null;
        }
    }
    
    public void writeData(ArrayList<String> data)
    {
        try
        {
            File file = new File(this.fileName);
            if(!file.exists())
                file.createNewFile();
            
            BufferedWriter bufferedOutputData = new BufferedWriter(new FileWriter(this.fileName));
            
            for (String line : data) 
            {
                bufferedOutputData.write(line);
                bufferedOutputData.newLine();
                bufferedOutputData.flush();
            }
            
            bufferedOutputData.close();                        
        }
        catch(IOException ex)
        {
            this.log.severe(ex.getMessage());    
        }
    }
    
}