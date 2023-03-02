package starter;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

import global.GlobalInfo;

public class HandlerDashboard implements Handler<RoutingContext>{
	@Override
	public void handle(RoutingContext request) {
		 HttpServerResponse response = request.response();
	     response.putHeader("content-type", "text/html");
	     
         // Generate HTML table dynamically
         StringBuilder htmlBuilder = new StringBuilder();
         htmlBuilder.append("<html>\n")
                 .append("<head>\n")
                 .append("<script src=\"https://unpkg.com/axios/dist/axios.min.js\"></script>")
                 .append("<title>Table Example</title>\n")
                 .append("</head>\n")
                 .append("<style>table, th, td{border:1px solid black}</style>")
                 .append("<body>\n")
                 .append("<table>\n")
                 .append("<thead>\n")
                 .append("<tr>\n")
                 .append("<th>Starting Time</th>\n")
                 .append("<th>Hours</th>\n")
                 .append("</tr>\n")
                 .append("</thead>\n")
                 .append("<tbody>\n");
         
         for (Map.Entry<String, Long> e : GlobalInfo.getDurationLight().entrySet()) {
             htmlBuilder.append("<tr>\n")
                     .append("<td>").append(e.getKey()).append("</td>\n")
                     .append("<td>").append(e.getValue()).append("</td>\n")
                     .append("</tr>\n");
         }

         htmlBuilder.append("</tbody>\n")
                 .append("</table>\n")
                 .append("<button class='control'>Take control</button>")
                 .append("<button class='interrupt'>Give control</button>")
                 .append("<p>Lighting Subsystem:</p>\n")
                 .append("<label><input type=\"radio\" name=\"option\" value=\"on\"> ON</label><br>\n")
                 .append("<label><input type=\"radio\" name=\"option\" value=\"off\"> OFF</label><br>\n")
                 .append("<input type=\"range\" min=\"0\" max=\"180\" value=\"0\" class=\"slider\" name=\"slider\">\n")
                 .append("<br><br>\n")
                 .append("<script>var btn = document.querySelector(\".control\")\n"
                 		+ "var btn2 = document.querySelector(\".interrupt\")\n"
                 		+ "var slider = document.querySelector(\"input[type=range]\")\n"
                 		+ "var radios = document.querySelectorAll(\"input[type=radio]\")\n"
                 		+ "\n"
                 		+ "btn.addEventListener(\"click\", e=>{\n"
                 		+ "	e.preventDefault()\n"
                 		+ "	axios.post(\"/control\", {control:true})\n"
                 		+ "	.then(res=>{console.log(res)})\n"
                 		+ "})\n"
                 		+ "\n"
                 		+ "btn2.addEventListener(\"click\", e=>{\n"
                 		+ "	e.preventDefault()\n"
                 		+ "	axios.post(\"/control\", {control:false})\n"
                 		+ "	.then(res=>{console.log(\"prova\")})\n"
                 		+ "})\n"
                 		+ "\n"
                 		+ "slider.addEventListener(\"change\", e=>{\n"
                 		+ "	axios.post(\"/setAlpha\", {alpha:slider.value})\n"
                 		+ "})\n"
                 		+ "\n"
                 		+ "radios.forEach(radio=>{\n"
                 		+ "	radio.addEventListener(\"click\", e=>{\n"
                 		+ "		axios.post(\"/setLight\", {light:radio.value})\n"
                 		+ "	})\n"
                 		+ "})</script>")
                 .append("</body>\n")
                 .append("</html>");
	     //response.end(htmlBuilder.toString());
	     response.sendFile("../room-dashboard/starter/index.html");
	}
}
