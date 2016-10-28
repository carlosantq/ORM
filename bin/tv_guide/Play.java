//	Created by: Carlos Antonio
//	File: tv_guide/Play.java
//	Created using: Orm.java
//	Date and time of creation: 2016-10-27 21:23:35.916

import java.lang.*;
import java.util.*;

public class Play{

	String name;
	Date playDate;
	time playTime;
	int unsigned channel;
	tinyint unsigned episodeID;
	String affilate;

	public Play(){
		name = null;
		playDate = null;
		playTime = 0;
		channel = 0;
		episodeID = 0;
		affilate = null;
	}

	public String getname(){
		return name;
	}

	public Date getplayDate(){
		return playDate;
	}

	public time getplayTime(){
		return playTime;
	}

	public int unsigned getchannel(){
		return channel;
	}

	public tinyint unsigned getepisodeID(){
		return episodeID;
	}

	public String getaffilate(){
		return affilate;
	}

	public void setname (String _name){
		name = _name;
	}

	public void setplayDate (Date _playDate){
		playDate = _playDate;
	}

	public void setplayTime (time _playTime){
		playTime = _playTime;
	}

	public void setchannel (int unsigned _channel){
		channel = _channel;
	}

	public void setepisodeID (tinyint unsigned _episodeID){
		episodeID = _episodeID;
	}

	public void setaffilate (String _affilate){
		affilate = _affilate;
	}

	public String toString(){
		return "name = " + name + "playDate = " + playDate + "playTime = " + playTime + "channel = " + channel + "episodeID = " + episodeID + "affilate = " + affilate;
	}
}