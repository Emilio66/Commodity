package config;

import model.Category;
import model.Commodity;
import model.Order;
import model.User;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

import controller.CartController;
import controller.CommodityController;
import controller.MainController;
import controller.OrderController;
import controller.UserController;

public class Configuration extends JFinalConfig {

	@Override
	public void configConstant(Constants config) {
		config.setDevMode(true);
		config.setViewType(ViewType.FREE_MARKER);
	}

	@Override
	public void configPlugin(Plugins plugins) {
		loadPropertyFile("db.properties");
		C3p0Plugin c3p0 = new C3p0Plugin(getProperty("url"),getProperty("user"),getProperty("pwd"),getProperty("driver"));
		plugins.add(c3p0);
		
		ActiveRecordPlugin arPlugin=new ActiveRecordPlugin(c3p0);
		plugins.add(arPlugin);
		
		arPlugin.addMapping("user", User.class);
		arPlugin.addMapping("order","sequence",Order.class);
		arPlugin.addMapping("commodity","cate_id",Commodity.class);
		arPlugin.addMapping("category",Category.class);
	}

	@Override
	public void configRoute(Routes routes) {
		//Two method get physical path
		//String base = PathKit.getWebRootPath()+"/WEB-INF/view";
		//String base = JFinal.me().getServletContext().getRealPath("/WEB-INF");
		
		routes.add("/",MainController.class,"/WEB-INF/view");	//main page
		routes.add("/u",UserController.class,"/WEB-INF/view");	//user related
		routes.add("/g",CommodityController.class,"/WEB-INF/view");	//goods related
		routes.add("/o",OrderController.class,"/WEB-INF/view");	//order related
		routes.add("/c",CartController.class,"/WEB-INF/view");	//shopping cart related
	}

	@Override
	public void configHandler(Handlers handlers) {
		//handlers.add(new ContextPathHandler("ctx"));
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		interceptors.add(new SessionInViewInterceptor());	//for the use of session in view
	}

}
