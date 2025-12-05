import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class FileOperations {
	public static void main(String[] args) {
		String fname="input.txt";
		int cnt=0;
		try {
			BufferedReader br=new BufferedReader(new FileReader(fname));
			String line;
			while((line=br.readLine())!=null) {
				String lower=line.toLowerCase();
				String[] w=lower.split("[ ,.!?;:\"()]+");
				for (String word:w) {
					if(word.equals("india")) {
						cnt++;
					}
				}
			}
			br.close();
			System.out.println("No.of Times: "+cnt);
		}catch(IOException e) {
			System.out.println("Error: "+e.getMessage());
		}
	}
}
