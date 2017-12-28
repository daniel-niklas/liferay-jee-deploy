package orgl.liferayext.lr7.jsfsample;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class HelloWorldBean {

	private String name = "Developer";
	private String message;

	public String onSayHello() {
		message = "Hallo " + name + "!";

		// return "" or null will cause "java.lang.NullPointerException:
		// in case of non-ajax request
		// Argumentfehler: Parameter text ist null"
		return "hello-world";
	}

	// getter / setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

}