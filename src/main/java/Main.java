import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONArray;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.generator.UserProfileGenerator;

public class Main {
	
	// Arguments
	private static final String _ArgTotalProfiles = "-n";
	private static final String _ArgSeed = "-s";
	private static final String _ArgOutputDirectory = "-o";
	private static final String _ArgInitialProfile = "-p";
	
	// Profiles
	private static long seed = 0;
	private static File _OutputDirectory = null;
	private static int num = 1;
	private static Profile initialProfile =  new Profile();
	
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UserProfileParsingException {
		
		//Parse arguments
		int i = 0;
		while(i < args.length){
			String arg = args[i++].trim();
			if(arg.equals(_ArgOutputDirectory)) {
				_OutputDirectory = new File(args[i++].trim());
			}
			else if (arg.equals(_ArgTotalProfiles)) { 
				num = Integer.valueOf(args[i++]);
			}
			else if(arg.equals(_ArgSeed)) {
				seed = Long.valueOf(args[i++]);
			}
			else if(arg.equals(_ArgInitialProfile)) {
				initialProfile = new Profile(new File(args[i++].trim()));
			}
		}
		
		//Initialize generator
		UserProfileGenerator userProfileGenerator = new UserProfileGenerator(seed, initialProfile);
		
		if(_OutputDirectory == null) {
			
			PrintWriter writer = new PrintWriter(System.out);
			for(i = 0; i < num; i++) 
					userProfileGenerator.getNextProfile().getJSONObject().write(writer, 4, 0);
			
		} else if (_OutputDirectory.isDirectory()){
			
			for(i = 0; i < num; i++) {
				
				String fileName = String.format(_OutputDirectory.getPath() + File.separatorChar + "userProfile_%d.json", i);
				
				File file = new File(fileName);
				
				if(!file.exists())
					file.createNewFile();
				
				PrintWriter writer = new PrintWriter(file);
				userProfileGenerator.getNextProfile().getJSONObject().write(writer, 4, 0);
				writer.close();
				
				System.out.println("Profile: "+ fileName +" has been created");
			}
		} else {
			
			if(!_OutputDirectory.exists())
				_OutputDirectory.createNewFile();
			
			JSONArray profiles = new JSONArray();
			for(i = 0; i < num; i++) 
				profiles.put(userProfileGenerator.getNextProfile().getJSONObject());

			PrintWriter writer = new PrintWriter(_OutputDirectory);
			writer.write(profiles.toString(4));
			writer.close();
			
			System.out.println("Profile: has been written to "+_OutputDirectory.getPath());
			
			
		}
		
	}

}
