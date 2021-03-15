import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class App {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
             Writer writer = new OutputStreamWriter(new FileOutputStream("output.txt"), "utf-8")) {
            String line = reader.readLine();
            int c = 0;
            while (line != null) {
                if (LineValidator.isValidLine(line)) {
                    c++;
                    writer.write(line + "\n");
                }
                line = reader.readLine();
            }
            writer.write("The total number of lines that are valid is: " + c);
        } catch (FileNotFoundException e) {
            System.out.println("The file does not exist");
        } catch (IOException e) {
            System.out.println("Something wrong when reading/writing the file");
        }
    }
}
