package com.certh.iti.easytv.user.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.certh.iti.easytv.user.Profile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class UserProfileGenerator {
	
	private int userId = 0;
	private Random rand;
	
	public UserProfileGenerator() {
		rand = new Random();
	}
	
	public UserProfileGenerator(long seed) {
		rand = new Random(seed);
	}
	
	public void setSeed(long seed) {
		rand.setSeed(seed);
	}
	
	public Profile getNextProfile() throws UserProfileParsingException, IOException{
		return new Profile(userId++, rand);
	}
	
	/**
	 * Generate profiles to output directory
	 * 	
	 * @param num
	 * @param outDir
	 * @throws UserProfileParsingException
	 * @throws IOException
	 */
	public void generateProfiles(int num, File outDir) throws UserProfileParsingException, IOException{
		
		for(int i = 0; i < num; i++) {
			
			Profile userProfile = getNextProfile();
			
			String fileName = outDir.getPath() + File.separatorChar + "userProfile_" + i + ".json";
			File file = new File(fileName);
			
			if(!file.exists())
				file.createNewFile();
			
			PrintWriter writer = new PrintWriter(file);
			userProfile.getJSONObject().write(writer, 4, 0);
			writer.close();
			
			System.out.println("User profile: "+ fileName +" has been created");
		}
	}
	
	
	/**
	 * Generate randomly initiated set of user profiles. 
	 * 
	 * @param num
	 * @return
	 * @throws UserProfileParsingException 
	 * @throws IOException 
	 */
	public List<Profile> getProfiles(int num) throws UserProfileParsingException, IOException{
		
		List<Profile> profiles =  new ArrayList<Profile>(num);	
		for(int i = 0; i < num; i++) {
				
			//Create profile
			Profile profile = getNextProfile();
			
			profiles.add(profile);

		}
		
		return profiles;
	}

}
