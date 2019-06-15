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
	
	private static final String _ArgAge = "--age";
	private static final String _ArgGender = "--gender";

	private static final String _ArgVisualAcutiy = "--acuity";
	private static final String _ArgContrast = "--contrast";
	private static final String _ArgColor = "--color";
	private static final String _ArgQuarterk = "--quarterk";
	private static final String _ArgHalfk = "--halfk";
	private static final String _ArgOnek = "--onek";
	private static final String _ArgTwok = "--twok";
	private static final String _ArgFourk = "--fourk";
	private static final String _ArgEifhtk = "--eightk";

	
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
					throw new IllegalArgumentException("The path is not a directory: " + _OutputDirectory.getPath());
			}
			else if (arg.equals(_ArgTotalProfiles)) 
				profiles = Integer.valueOf(args[i++]);
			
			//General
			else if (arg.equals(_ArgAge)) 
				userProfileGenerator.setAgeRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgGender)) 
				userProfileGenerator.setGenderRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			
			//Visual
			else if (arg.equals(_ArgVisualAcutiy)) 
				userProfileGenerator.setVisualAcuityRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgContrast)) 
				userProfileGenerator.setContrastSensitivityRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgColor)) 
				userProfileGenerator.setColorBlindnessRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			
			//Auditory
			else if (arg.equals(_ArgQuarterk)) 
				userProfileGenerator.setQuarterKRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgHalfk)) 
				userProfileGenerator.setHalfKRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgOnek)) 
				userProfileGenerator.setOneKRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgTwok)) 
				userProfileGenerator.setTwoKRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgFourk)) 
				userProfileGenerator.setFourKRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			else if (arg.equals(_ArgEifhtk)) 
				userProfileGenerator.setEightKRange(new int[] {Integer.valueOf(args[i++]), Integer.valueOf(args[i++])});
			
		}
		
		//Generate profiles
		actualProfiles = userProfileGenerator.generate(profiles);
		
		if(_OutputDirectory == null) {
			for(i = 0; i < actualProfiles.size(); i++) 
					System.out.println(actualProfiles.get(i).getJSONObject().toString(4));

		} else {
			for(i = 0; i < actualProfiles.size(); i++) {
				File file = new File(_OutputDirectory.getPath() + File.separatorChar + "userProfile_" + i);
				
				if(!file.exists())
					file.createNewFile();
				
				PrintWriter writer = new PrintWriter(file);
				writer.write(actualProfiles.get(i).getJSONObject().toString(4));
				writer.close();
			}
		}
		
	}

}
