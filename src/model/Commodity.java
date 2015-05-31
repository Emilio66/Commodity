package model;

import com.jfinal.plugin.activerecord.Model;

public class Commodity extends Model<Commodity> {
	public static final Commodity dao = new Commodity();
	
	public String getCateName(){
		return Category.dao.findById(get("cate_id")).get("name");
	}
}
