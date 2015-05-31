package model;

import com.jfinal.plugin.activerecord.Model;

public class Order extends Model<Order> {
	public static final Order dao = new Order();
	
	public Commodity getCommodity(){
		return Commodity.dao.findById(get("commodity_id"));
	}
}
