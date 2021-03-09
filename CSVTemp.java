
/**
 * Write a description of CSVTemp here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
public class CSVTemp {
    public CSVRecord hottestOrColdestHourInFile(CSVParser parser,int option){
        CSVRecord largestSoFar = null;
        for(CSVRecord currentRow : parser){
           largestSoFar = getLargestOfTwo(currentRow,largestSoFar,option);
        }
        return largestSoFar;
    }
    public StorageResource fileWithHottestOrColdestTemperature(int option){
        StorageResource sr = new StorageResource();
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser();
            CSVRecord record = hottestOrColdestHourInFile(parser,option);
            sr.add(record.get("DateUTC") + " " + record.get("TemperatureF"));
        }
        return sr;
    }
    public void testBetweenDays(int option){
        StorageResource sr = fileWithHottestOrColdestTemperature(option);
        for(String s : sr.data()){
            System.out.println(s);
        }
    }
    public void testHottestInDay(int option){
        FileResource fr = new FileResource();
        CSVRecord largest = hottestOrColdestHourInFile(fr.getCSVParser(),option);
        System.out.println("hottest temperature was " + largest.get("TemperatureF") + " at " 
                            + largest.get("TimeEDT"));
    }
    public CSVRecord  hottestOrColdestInManyDays(int option){
       CSVRecord largestSoFar = null;
       DirectoryResource dr = new DirectoryResource();
       for(File f : dr.selectedFiles()){
           FileResource fr = new FileResource(f);
           CSVParser parser = fr.getCSVParser();
           CSVRecord record = hottestOrColdestHourInFile(parser,option);
           largestSoFar = getLargestOfTwo(record,largestSoFar,option);
       }
       return largestSoFar;
    }
    public CSVRecord getLargestOfTwo(CSVRecord currentRow, CSVRecord largestSoFar,int option){
         if(largestSoFar == null){
                largestSoFar = currentRow;
            }else{
                Double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
                Double desTemp = Double.parseDouble(largestSoFar.get("TemperatureF"));
                if(option == 1 && currentTemp > desTemp){
                    largestSoFar = currentRow;
                }
                if(option == 2 && currentTemp < desTemp){
                    largestSoFar = currentRow;
                }
                
         }
         return largestSoFar;
    }
    public void testOfManyDays(int option){
        CSVRecord hottestOrColdestDayOfTheYear = hottestOrColdestInManyDays(option);
        if(option == 1){
            System.out.println("Hottest temperature was " + hottestOrColdestDayOfTheYear.get("TemperatureF") +
                            " at " + hottestOrColdestDayOfTheYear.get("TimeEDT") + "\nDate: " 
                            + hottestOrColdestDayOfTheYear.get("DateUTC"));
        }else if(option == 2){
            System.out.println("Coldest temperature was " + hottestOrColdestDayOfTheYear.get("TemperatureF") +
                            " at " + hottestOrColdestDayOfTheYear.get("TimeEST") + "\nDate: " 
                            + hottestOrColdestDayOfTheYear.get("DateUTC"));
        }else{
            System.out.println("Please enter a valid value\nFor the hottest day of the year = 1\nFor the coldest day of the year = 2");
        }
        
    }
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowHumidity = null;
        
        for(CSVRecord currentRow : parser){
            if(lowHumidity == null){
                if(currentRow.get("Humidity") != "N/A"){
                   lowHumidity = currentRow;
                }
                
            }
            else{
                
                if(currentRow.get("Humidity") != "N/A"){
                    int currentHumidity = Integer.parseInt(currentRow.get("Humidity"));
                    int iLowHumidity = Integer.parseInt(lowHumidity.get("Humidity"));
                    if(currentHumidity < iLowHumidity){
                        lowHumidity = currentRow;
                    }
                }
                 
                
            }
            
        }
        return lowHumidity;
    }
    public void  testLowestHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord lowHumidity = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity was " + lowHumidity.get("Humidity") +
                            " at " + lowHumidity.get("DateUTC"));
    }
    public double averageTemperature(CSVParser parser){
        double averageTemp = 0;
        double totalTemp = 0;
        int counter = 0;
        for(CSVRecord cr : parser){
            totalTemp += Double.parseDouble(cr.get("TemperatureF"));
            counter++;
        }
        averageTemp = totalTemp / counter;
        
        return averageTemp;  
    }
    public void testAverageTemperatureInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        System.out.println("Average temperature in file is " + averageTemperature(parser));
    } 
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser,int value){
        double averageTemp = 0;
        double totalTemp = 0;
        int counter = 0;
        for(CSVRecord cr : parser){
            
            if(cr.get("Humidity") != "N/A" && Integer.parseInt(cr.get("Humidity")) > value){
                 totalTemp += Double.parseDouble(cr.get("TemperatureF"));
                 counter++;
                 System.out.println(totalTemp + " " + counter);
                 
             }
               
            
           
        }
        
        averageTemp = totalTemp / counter;
        
        
        return averageTemp;
    }
    public void testAverageTemperatureWithHighHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double db = averageTemperatureWithHighHumidityInFile(parser,80);
        if(db == 0){
            System.out.println("No temperatures with that humidity");
        }else{
            System.out.println("Average Temp when high Humidity is " + db);
        }
        
    }
    public void testLowestHumidity(){
       int lowest = 0;
       String date = "";
        
       CSVRecord largestSoFar = null;
       DirectoryResource dr = new DirectoryResource();
       for(File f : dr.selectedFiles()){
           FileResource fr = new FileResource(f);
           CSVParser parser = fr.getCSVParser();
           
           try{
               CSVRecord record = lowestHumidityInFile(parser);
                if(lowest == 0){
               lowest = Integer.parseInt(record.get("Humidity"));
           }else{
               
              int curr = Integer.parseInt(record.get("Humidity"));
              if(curr < lowest){
                 lowest = curr;
                 date = record.get("DateUTC");
              }
               
           }
            }catch(Exception e){
                
            }
           
          
           
       
    }
    System.out.println(lowest);
    System.out.println(date);
    
    
   }
}
