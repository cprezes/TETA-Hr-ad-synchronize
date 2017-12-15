import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class FileWrite {
	
 private static	FileWriter fstream;
 private static BufferedWriter out ;

	

	public FileWrite(String fileName) throws IOException {

			fstream = new FileWriter(fileName);
			 out = new BufferedWriter(fstream);

	}
	
	public void write(String line) throws IOException {
		
		        out.write(line);
		        out.newLine();
		
	}
	
	public void  closeFile() throws IOException {
				
		out.close();
	}
	
}
