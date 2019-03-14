package org.xg;

import io.jsonwebtoken.Jwts;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;


/**
 * Root resource (exposed at "myresource" path)
 */
@Path("zdtest")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("page")
    @Produces(MediaType.TEXT_HTML)
    public Response getPage() {
        return Response.ok("<!DOCTYPE html>\n" +
          "<html lang=\"zh-cmn-Hans\">\n" +
          "<head>\n" +
          "  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n" +
          "  <title> 在您的网站上嵌入并自定义小组件 </title>\n" +
          "<!-- Start of tonglueyun Zendesk Widget script -->\n" +
          "<script id=\"ze-snippet\" src=\"https://static.zdassets.com/ekr/snippet.js?key=7258774f-4938-48f2-969b-cfecd6dfd13f\"> </script>\n" +
          "<!-- End of tonglueyun Zendesk Widget script -->  <script type=\"text/javascript\">\n" +
          "\n" +
          "function zendeskInit(user, email) {\n" +
          "  //alert('Hello, zendesk!');\n" +
          "  console.log('in zendeskInit ...');\n" +
          "  zE('webWidget', 'setLocale', 'zh-cn');\n" +
          "  window.zESettings = {\n" +
          "    webWidget: {\n" +
          "      chat: {\n" +
          "        prechatForm: {\n" +
          "          greeting: {\n" +
          "            '*': 'Please fill out the form below to chat with us',\n" +
          "            'zh-cn': \"请输入用户名和电子邮件\"\n" +
          "          }\n" +
          "        }\n" +
          "      }\n" +
          "    }\n" +
          "  };\n" +
          "  zE(\n" +
          "    'webWidget', 'identify',\n" +
          "    {\n" +
          "      name: user,\n" +
          "      email: email,\n" +
          "      organization: 'Voltron, Inc.'\n" +
          "    }\n" +
          "  );\n" +
          "  if (typeof user === 'undefined' || typeof email === 'undefined') {\n" +
          "  }\n" +
          "  else {\n" +
          "  }\n" +
          "}\n" +
          "\n" +
          "//zendeskInit('', '');\n" +
          "zendeskInit('分公司11', 'fgs1@zgs1a.com');\n" +
          "\n" +
          "  </script>\n" +
          "  <h1> 在您的网站上嵌入并自定义小组件 </h1>\n" +
          "</head>\n"
        ).build();
    }

    @GET
    @Path("token")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getToken() {
        //byte[] keyBytes = "secret".getBytes(StandardCharsets.UTF_8);
        //Key key = Keys.hmacShaKeyFor(keyBytes); //
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
        return Response.ok(jws).build();
    }
}
