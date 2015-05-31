package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import model.Category;
import model.Commodity;
import model.User;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

/**
 * 商品操作类
 * 上架，修改，已上架商品列表,下架
 *
 */
public class CommodityController extends Controller{
	//prepare the data needed in adding a new product
	public void index(){
		User user = getSessionAttr("user");
		
		if(user == null){
			forwardAction("/u");	//login first
		}else{
			List<Category> categories = Category.dao.find("select * from category");
			setAttr("categories",categories);
			render("add.html");
		}
	}
	
	//add a new commodity
	public void add() {
		UploadFile file = getFile("image","/images");
		String name = getPara("name");
		String cate_id = getPara("cate_id");
		String introduction = getPara("introduction");
		double price = Double.parseDouble(getPara("price"));
		double discount = Double.parseDouble(getPara("discount"));
		int quantity = Integer.parseInt(getPara("quantity"));
		
		String image = file.getFileName();
		String extension = image.substring(image.indexOf('.'));
		long now =  System.currentTimeMillis();
		image = now + extension;//only need the extension
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(file.getFile());

			FileOutputStream fos = new FileOutputStream(PathKit.getWebRootPath()+"/images/"+image);
			byte[] bytes =new byte[512];
			while( fis.read(bytes) !=-1){
				fos.write(bytes);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		User user = getSessionAttr("user");
		int id = user.getInt("id");
		
		Commodity commodity = new Commodity();
		commodity.set("owner",id);
		commodity.set("name",name);
		commodity.set("cate_id",cate_id);
		commodity.set("introduction",introduction);
		commodity.set("price",price);
		commodity.set("discount",discount);
		commodity.set("quantity",quantity);
		commodity.set("image",image);
		commodity.set("up_time",new Timestamp(now));
		
		if(!commodity.save()){
			setAttr("error",true);
		}else {
			setAttr("error",false);
		}
		
		render("add.html");
	}
	
	//update a commodity
	public void udpate(){
		int id = getParaToInt("id");
		Commodity commodity = Commodity.dao.findById(id);
		UploadFile file = getFile("image","/images");
		String image = "/"+file.getFileName();
		String name = getPara("name");
		String cate_id = getPara("cate_id");
		String introduction = getPara("introduction");
		double price = Double.parseDouble(getPara("price"));
		double discount = Double.parseDouble(getPara("discount"));
		int quantity = Integer.parseInt(getPara("quantity"));
		
		commodity.set("name",name);
		commodity.set("cate_id",cate_id);
		commodity.set("introductioin",introduction);
		commodity.set("price",price);
		commodity.set("discount",discount);
		commodity.set("quantity",quantity);
		commodity.set("image",image);
		
		if(!commodity.update()){
			setAttr("error",true);
		}else {
			setAttr("error",false);
		}
		
		render("add.html");
	}
	
	//show details of a specific commodity
	public void one(){
		int id = getParaToInt("id");
		System.out.println(" ID is : "+id);
		Commodity commo = Commodity.dao.findById(id);
		System.out.println(commo.toString());
		setAttr("commodity",commo);
		render("details.html");
	}
	
	//list my sales 
	public void list(){
		User user = getSessionAttr("user");
		int id = user.getInt("id");
		
		List<Commodity> list = Commodity.dao.find("select * from commodity where owner="+id);
		
		setAttr("myGoods",list);
		render("mysale.html");
	}
	
	//invalidate commodity 
	public void down(){
		int commoId = getParaToInt("id");
		Commodity.dao.findById(commoId).set("status",0).update();	
		
		list();
	}
	
	//search for sth
	public void search(){
		String aim = getPara("name");
		List<Commodity> list = Commodity.dao.find("select * from commodity where name like '%"+aim+"%'");
		
		setAttr("result",list);
		render("result.html");
	}
}
