package model;

import com.jfinal.plugin.activerecord.Model;

public class Cart extends Model<Cart> {
	public static final Cart dao = new Cart();
	
	public Commodity getCommodity(){
		return Commodity.dao.findById(get("commodity_id"));
	}
}
