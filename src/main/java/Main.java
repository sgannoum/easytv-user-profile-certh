import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.generator.UserProfileGenerator;

public class Main {
	
	// Arguments
	private static final String _ArgTotalProfiles = "-n";
	private static final String _ArgSeed = "-s";
	private static final String _ArgOutputDirectory = "-d";

	
	// Profiles
	private static long seed = 0;
	private static File _OutputDirectory = null;
	private static int num = 1;

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UserProfileParsingException {
		

		//Parse arguments
		int i = 0;
		while(i < args.length){
			String arg = args[i++].trim();
			if(arg.equals(_ArgOutputDirectory)) {
				_OutputDirectory = new File(args[i++].trim());
				
				if(!_OutputDirectory.isDirectory())
					throw new IllegalArgumentException("The path: " + _OutputDirectory.getPath()+" is not a directory");
			}
			else if (arg.equals(_ArgTotalProfiles)) 
				num = Integer.valueOf(args[i++]);
			else if(arg.equals(_ArgSeed))
				seed = Long.valueOf(args[i++]);
		}
		
		
		//Initialize generator
		UserProfileGenerator userProfileGenerator = new UserProfileGenerator(seed);

		
		if(_OutputDirectory == null) {
			
			PrintWriter writer = new PrintWriter(System.out);
			for(i = 0; i < num; i++) 
					userProfileGenerator.getNextProfile().getJSONObject().write(writer, 4, 0);
			
		} else {
			
			for(i = 0; i < num; i++) {
				
				String fileName = String.format(_OutputDirectory.getPath() + File.separatorChar + "userProfile_%d.json", i);
				
				File file = new File(fileName);
				
				if(!file.exists())
					file.createNewFile();
				
				PrintWriter writer = new PrintWriter(file);
				userProfileGenerator.getNextProfile().getJSONObject().write(writer, 4, 0);
				writer.close();
				
				System.out.println("User profile: "+ fileName +" has been created");
			}
		}
		
	}

}
