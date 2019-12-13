package com.certh.iti.easytv.user.preference.attributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class TimeAttribute extends Attribute{
	
	private Date time;
	//Known format
	private static Pattern iso_8601 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z");  
	private static Pattern simpleFormat = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");  
	private static Pattern miniSimpleFormat = Pattern.compile("\\d{2}:\\d{2}"); 

	public TimeAttribute() {
		super(new double[] {0.0, Instant.parse("1970-01-01T00:00:00Z").toEpochMilli()});
	}
	
	public TimeAttribute(double[] range) {
		super(range);
	}
	
	public TimeAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	@Override
	protected void init() {
		bins = new Bin[0];
		binSize = 0;
		binsNum = 0;
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		return String.format("%02d:%02d:%02d", rand.nextInt(12), rand.nextInt(60), rand.nextInt(60));	
	}
	
	public static Date convertDate(String timeStr) throws ParseException {
		Date time = null;
		
		if(iso_8601.matcher(timeStr).matches()) {
			time = Date.from( Instant.parse(timeStr));
		} else {
			
			if(miniSimpleFormat.matcher(timeStr).matches())  
				timeStr +=":00";
			
			if(simpleFormat.matcher(timeStr).matches()) 
				time = new SimpleDateFormat("HH:mm:ss").parse(timeStr);
		}
		
		return time;
	}
	
	@Override
	public double[] getPoints(Object literal) {
		
		if(literal == null) 
			return new double[] {missingValue};
		
		String timeStr =  (String) literal;
		
		try {
			time = TimeAttribute.convertDate(timeStr);
		} catch (ParseException e) {
			return new double[] {missingValue};
		}
		
		//long ms = Instant.parse( (CharSequence) literal).toEpochMilli();
		return new double[] {time.getTime()};
	}

	@Override
	public int code(Object literal) {		
		//TODO handle to item for time attribute
		return 0;
	}

	@Override
	public Object decode(int itemId) {
		//TODO handle to value for time attribute
		return null;
	}

}
