package demo;
import com.jfinal.core.Controller;
public class Hello extends Controller{
	public void index(){
		renderHtml("<h1>Hello, Stupid Human Being!</h1>");
	}

}
