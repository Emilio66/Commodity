package controller;

import java.sql.Timestamp;
import java.util.List;

import model.Commodity;
import model.Order;
import model.User;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 订单
 * 购买，确认订单，一键支付 , 历史记录
 *
 */
public class OrderController extends Controller{
	//user are prepared to buy something
	public void buy(){
		User user = getSessionAttr("user");
		if(user == null){
			forwardAction("/u");
		}else{
			int userId = user.getInt("id");
			System.out.println("Size: "+getParaMap().size()+getParaMap().toString());
			int comId = getParaToInt("commodity_id");
			int quantity = getParaToInt("quantity");
			double sum = 0;
			String string="";
			if((string = getPara("sum") )== null){
				double price = Double.parseDouble(getPara("price"));
				 sum = price * quantity;	//record the total cost of this order
			}else{
				sum = Double.parseDouble(string);
			}
			long time = System.currentTimeMillis();
			String sequence = userId+"-"+time;
			
			Order order = new Order();
			order.set("sequence",sequence);
			order.set("commodity_id",comId);
			order.set("user_id",userId);
			order.set("quantity",quantity);
			order.set("sum",sum);
			order.set("time",new Timestamp(System.currentTimeMillis()));
			
			if(order.save()){
				order = Order.dao.findById(sequence);
				Commodity commodity = order.getCommodity();
				setAttr("order",order);
				setAttr("commodity",commodity);
				
				render("buy.html");
			}else {
				//setAttr("error","");
				//render("error.html");
				renderText("Error in Ordering");
			}
		}
		
	}
	
	//confirm the order
	public void confirm(){
		//update status of order
		Order.dao.findById(getPara("sequence")).set("status",1).update();
		
		//Update repository and sales number
		int commoId = getParaToInt("commodity_id");
		int respository = getParaToInt("respository");
		int quantity = getParaToInt("quantity");
		int saleNum = getParaToInt("sales_num");
		
		Commodity.dao.findById(commoId).set("quantity",respository-quantity).set("sales_num",saleNum+quantity).update();
		
		setAttr("msg","下单成功");
		render("success.html");
	}
	
	//pay all the order in cart
	public void payOff(){
		List<String> seqList = getAttr("seqList");
		
		for (String sequence : seqList) {
			Order.dao.findById(sequence).set("status",1).update();
		}
		setAttr("msg","支付成功");
		render("success.html");
	}
	
	//list all shopping orders & sale orders
	public void index(){
		User user = getSessionAttr("user");
		if(user == null){
			forwardAction("/u");
		}else{
			int id = user.getInt("id");
			List<Order> list = Order.dao.find("select * from `order` where user_id="+id);
			List<Record> sales = Db.find("select id,sequence,name,order.quantity as q, order.sum as s," +
					"order.status as st, order.time as t " +
					"from `order` join commodity on `order`.commodity_id=commodity.id where owner="+id);
			
			setAttr("sales",sales);
			setAttr("orders",list);
			render("orders.html");
		}
	}
	
	public void send(){
		Order.dao.findById(getPara("seq")).set("status",2).update();
		forwardAction("/o");
	}
	
	public void delete(){
		Order.dao.findById(getPara("seq")).delete();
		forwardAction("/o");
	}
}
