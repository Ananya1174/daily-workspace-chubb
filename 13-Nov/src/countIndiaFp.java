import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class countIndiaFp {
    public static void main(String[] args) {
        String fileName = "input.txt";   
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            long count = br.lines().map(String::toLowerCase).flatMap(line ->Arrays.stream(line.split("[ ,.!?;:\"()]+"))
                    		).filter(word -> word.equals("india")).count();                            

            System.out.println("Number of times 'India' appears: " + count);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}