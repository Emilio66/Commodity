package controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Cart;
import model.Order;
import model.User;

import com.jfinal.core.Controller;
/**
 * 购物车
 * 列表，加入购物车，购买，删除
 *
 */
public class CartController extends Controller {
	//main page of cart
	public void index(){
		User user = getSessionAttr("user");
		if(user == null){
			forwardAction("/u");
		}else{
			int userId = user.getInt("id");
			List<Cart> list = Cart.dao.find("select * from cart where user_id = "+userId);
			setAttr("cart",list);
			render("mycart.html");
		}
	}
	
	//add goods to cart
	public void add(){
		User user = getSessionAttr("user");
		if(user == null){
			forwardAction("/u");
		}else{
			int userId = user.getInt("id");
			int comId = getParaToInt("commodity_id");
			int quantity = getParaToInt("quantity");
			double price = Double.parseDouble(getPara("price"));
			double sum = price * quantity;
			long time = System.currentTimeMillis();
			
			Cart cart = new Cart();
			cart.set("user_id",userId);
			cart.set("commodity_id",comId);
			cart.set("quantity",quantity);
			cart.set("sum",sum);
			cart.set("time",new Timestamp(time));
			cart.save();
		}
	}
	
	//pay one thing in cart
	public void buy(){
		Cart.dao.findById(getPara("id")).delete();	//delete record in cart
		forwardAction("/o/buy");
	}
	
	//pay all things in cart
	public void payAll(){
		User user = getSessionAttr("user");
		int id = user.getInt("id");
		List<Cart> carts = Cart.dao.find("select * from cart where user_id="+id);
		List<String> seqList = new ArrayList<String>();

		for (int i = 0; i < carts.size(); i++) {
			Cart one = carts.get(i);
			//remove this record from cart and generate an order
			Order order = new Order();
			long time = System.currentTimeMillis();
			order.set("user_id",id);
			order.set("sequence",id+"-"+time);
			order.set("commodity_id",one.get("commodity_id"));
			order.set("quantity",one.get("quantity"));
			order.set("sum",one.getDouble("sum"));
			order.set("time",new Timestamp(time));
			order.save();
			
			seqList.add(order.getStr("sequence"));
			one.delete();
		}

		setAttr("seqList",seqList);
		forwardAction("/o/payOff");
	}
	
	public void delete(){
		int id = getParaToInt("id");
		Cart.dao.findById(id).delete();
		
		forwardAction("/c");	//show cart again
	}
}
