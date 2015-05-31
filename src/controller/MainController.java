package controller;

import java.util.List;

import model.Category;
import model.Commodity;
import model.User;

import com.jfinal.core.Controller;

public class MainController extends Controller {
	//main page, show goods, user and categories
	public void index(){
		User user = getSessionAttr("user");
		if(user != null)
			setAttr("user",user);
		
		String sql ="select * from commodity";
		
		List<Commodity> goods = Commodity.dao.find(sql);
		//System.out.println(goods.get(0).toJson());
		setAttr("goods",goods);
		List<Category> categories = Category.dao.find("select * from category");
		setAttr("categories",categories);
		
		render("home.html")	;
	}
	
	//show goods of different category
	public void cate(){
		User user = getSessionAttr("user");
		if(user != null)
			setAttr("user",user);
		
		String sql ="select * from commodity";
		if(getPara("id") != null){
			sql += " where cate_id="+getPara("id");
		}
		System.out.println(sql);
		List<Commodity> goods = Commodity.dao.find(sql);
		//System.out.println(goods.get(0).toJson());
		setAttr("goods",goods);
		List<Category> categories = Category.dao.find("select * from category");
		setAttr("categories",categories);
		
		render("home.html")	;
	}
	
	//search by name
	public void search(){
		User user = getSessionAttr("user");
		if(user != null)
			setAttr("user",user);
		
		String sql ="select * from commodity";
		String aim = getPara("name");
		if(aim != null){
			sql += " where name like '%"+aim+"%'";
		}
		
		List<Commodity> goods = Commodity.dao.find(sql);
		//System.out.println(goods.get(0).toJson());
		setAttr("goods",goods);
		List<Category> categories = Category.dao.find("select * from category");
		setAttr("categories",categories);
		
		render("home.html")	;
		
	}
	
}
