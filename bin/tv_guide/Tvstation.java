//	Created by: Carlos Antonio
//	File: tv_guide/Tvstation.java
//	Created using: Orm.java
//	Date and time of creation: 2016-10-27 21:23:35.924

import java.lang.*;
import java.util.*;

public class Tvstation{

	int unsigned channel;
	String callLetters;
	String city;
	String st;
	String phone;
	String email;
	String url;

	public Tvstation(){
		channel = 0;
		callLetters = null;
		city = null;
		st = null;
		phone = null;
		email = null;
		url = null;
	}

	public int unsigned getchannel(){
		return channel;
	}

	public String getcallLetters(){
		return callLetters;
	}

	public String getcity(){
		return city;
	}

	public String getst(){
		return st;
	}

	public String getphone(){
		return phone;
	}

	public String getemail(){
		return email;
	}

	public String geturl(){
		return url;
	}

	public void setchannel (int unsigned _channel){
		channel = _channel;
	}

	public void setcallLetters (String _callLetters){
		callLetters = _callLetters;
	}

	public void setcity (String _city){
		city = _city;
	}

	public void setst (String _st){
		st = _st;
	}

	public void setphone (String _phone){
		phone = _phone;
	}

	public void setemail (String _email){
		email = _email;
	}

	public void seturl (String _url){
		url = _url;
	}

	public String toString(){
		return "channel = " + channel + "callLetters = " + callLetters + "city = " + city + "st = " + st + "phone = " + phone + "email = " + email + "url = " + url;
	}
}