package com.certh.iti.easytv.user.preference.operand;

import java.awt.Color;

import org.json.JSONException;
import org.json.JSONObject;

public class ColorLiteral extends OperandLiteral{

	private Color color;
	
	public ColorLiteral(Object literal) {
		super(literal);
		color = Color.decode((String) literal);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!ColorLiteral.class.isInstance(obj)) return false;
		ColorLiteral other = (ColorLiteral) obj;
		
		return color.equals(other.color);
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		ColorLiteral other = (ColorLiteral) op2;

		return Math.abs(color.getRed() - other.color.getRed()) + 
				Math.abs(color.getGreen() - other.color.getGreen()) +
	              Math.abs(color.getBlue() - other.color.getBlue());
	}
	
	public double[] getPoint() {
		return new double[] {color.getRed(), color.getGreen(), color.getBlue()};
	}

	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			String obj = jsonPreference.getString(field);
			return new ColorLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
	
/*
 *  TO-DO keep or remove
 
	static Color stringToColor(String str) {
		  Color color = null;

		  if (str == null) {
		      return null;
		  }
		  if (str.length() == 0)
		    color = Color.black;
		  else if (str.startsWith("rgb(")) {
		      //color = parseRGB(str);
		  }
		  else if (str.charAt(0) == '#')
		    color = hexToColor(str);
		  else if (str.equalsIgnoreCase("Black"))
		    color = hexToColor("#000000");
		  else if(str.equalsIgnoreCase("Silver"))
		    color = hexToColor("#C0C0C0");
		  else if(str.equalsIgnoreCase("Gray"))
		    color = hexToColor("#808080");
		  else if(str.equalsIgnoreCase("White"))
		    color = hexToColor("#FFFFFF");
		  else if(str.equalsIgnoreCase("Maroon"))
		    color = hexToColor("#800000");
		  else if(str.equalsIgnoreCase("Red"))
		    color = hexToColor("#FF0000");
		  else if(str.equalsIgnoreCase("Purple"))
		    color = hexToColor("#800080");
		  else if(str.equalsIgnoreCase("Fuchsia"))
		    color = hexToColor("#FF00FF");
		  else if(str.equalsIgnoreCase("Green"))
		    color = hexToColor("#008000");
		  else if(str.equalsIgnoreCase("Lime"))
		    color = hexToColor("#00FF00");
		  else if(str.equalsIgnoreCase("Olive"))
		    color = hexToColor("#808000");
		  else if(str.equalsIgnoreCase("Yellow"))
		    color = hexToColor("#FFFF00");
		  else if(str.equalsIgnoreCase("Navy"))
		    color = hexToColor("#000080");
		  else if(str.equalsIgnoreCase("Blue"))
		    color = hexToColor("#0000FF");
		  else if(str.equalsIgnoreCase("Teal"))
		    color = hexToColor("#008080");
		  else if(str.equalsIgnoreCase("Aqua"))
		    color = hexToColor("#00FFFF");
		  else if(str.equalsIgnoreCase("Orange"))
		    color = hexToColor("#FF8000");
		  else
		      color = hexToColor(str); // sometimes get specified without leading #
		  return color;
		}
	
	static final Color hexToColor(String value) {
		String digits;
		int n = value.length();
		
		if (value.startsWith("#")) {
			digits = value.substring(1, Math.min(value.length(), 7));
		} else {
			digits = value;
		}
		
		String hstr = "0x" + digits;
		Color c;
		
		try {
			c = Color.decode(hstr);
		} catch (NumberFormatException nfe) {
			c = null;
		}
		
		return c;
	}
*/

}
