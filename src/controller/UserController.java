package controller;

import model.User;

import com.jfinal.core.Controller;

/**
 * 与用户相关的操作
 * 登陆，注册，用户列表，删除用户
 *
 */
public class UserController extends Controller {
	public void index(){
		render("login.html");
	}
	public void login(){
		String account = getPara("account");
		String pwd = getPara("pwd");
		User user = User.dao.findFirst("select * from user where account='"+account+"' and pwd='"+pwd+"' and status=1");
		if(user != null){
			setSessionAttr("user",user);
			forwardAction("/");
		}else{
			setAttr("error",true);
			render("login.html");
		}
	}
	
	public void out(){
		setSessionAttr("user",null);
		forwardAction("/");
	}
	
	public void register(){
		String account = getPara("account");
		if(account == null){
			render("register.html");	//first access
			return;
		}
		String name = getPara("name");
		String pwd = getPara("pwd");
		
		User user = User.dao.findFirst("select * from user where account=?",account);
		//account exist
		if(user != null){
			setAttr("error",true);
			render("register.html");
		}else{
			user = new User().set("account",account).set("name",name).set("pwd",pwd);
			user.save();
			setSessionAttr("user",user);
			forwardAction("/");	//login directly
		}
	}
}
