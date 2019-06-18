import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.generator.UserProfileGenerator;

public class Main {
	
	// Arguments
	private static final String _ArgTotalProfiles = "-n";
	private static final String _ArgOutputDirectory = "-d";

	
	// Profiles
	private static File _OutputDirectory = null;
	private static int profiles = 1;

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		List<UserProfile> actualProfiles;
		UserProfileGenerator userProfileGenerator = new UserProfileGenerator();
		
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
				profiles = Integer.valueOf(args[i++]);
			
		}
		
		//Generate profiles
		actualProfiles = userProfileGenerator.generate(profiles);
		
		if(_OutputDirectory == null) {
			for(i = 0; i < actualProfiles.size(); i++) 
					System.out.println(actualProfiles.get(i).getJSONObject().toString(4));

		} else {
			for(i = 0; i < actualProfiles.size(); i++) {
				String fileName = _OutputDirectory.getPath() + File.separatorChar + "userProfile_" + i + ".json";
				File file = new File(fileName);
				
				if(!file.exists())
					file.createNewFile();
				
				PrintWriter writer = new PrintWriter(file);
				writer.write(actualProfiles.get(i).getJSONObject().toString(4));
				writer.close();
				
				System.out.println("User profile: "+ fileName +" has been created");
			}
		}
		
	}

}
