/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author oscar
 */
@PreMatching
@Provider
public class CorsResponseFilter implements ContainerResponseFilter {
 @Override
 public void filter( ContainerRequestContext requestCtx, ContainerResponseContext res )
   throws IOException {
   res.getHeaders().add("Access-Control-Allow-Origin", "*" );
   res.getHeaders().add("Access-Control-Allow-Credentials", "true" );
   res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
   res.getHeaders().add("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, Authorization,x-access-token");
 }
}