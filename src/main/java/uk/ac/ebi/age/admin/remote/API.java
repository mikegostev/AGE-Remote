package uk.ac.ebi.age.admin.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class API
{
 private Log log = LogFactory.getLog(API.class);
 
 private HttpClient httpclient;

 public String login(String host, String login, String pass)
 {
  boolean ok = false;

  try
  {
   String sessionKey = null;

   if( httpclient == null )
    httpclient = new DefaultHttpClient();
   
   HttpPost httpost = new HttpPost(host + "Login");

   List<NameValuePair> nvps = new ArrayList<NameValuePair>();
   nvps.add(new BasicNameValuePair("username", login));
   nvps.add(new BasicNameValuePair("password", pass != null ? pass : ""));

   httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

   HttpResponse response = httpclient.execute(httpost);

   if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
   {
    log.error("Login failed. Server response code is: " + response.getStatusLine().getStatusCode());
    return null;
   }

   HttpEntity ent = response.getEntity();

   String respStr = EntityUtils.toString(ent).trim();

   if(respStr.startsWith("OK:"))
   {
    sessionKey = respStr.substring(3);
   }
   else
   {
    log.error("Login failed: " + respStr);
    return null;
   }

   EntityUtils.consume(ent);

   ok = true;

   if( log.isDebugEnabled() )
    log.debug("Login for user '"+login+"' successfull. Session key: "+sessionKey);
   
   return sessionKey;
  }
  catch(Throwable e)
  {
   log.error("ERROR: Login failed: " + e.getMessage() + " (" + e.getClass().getName() + ")");
  }
  finally
  {
   if(!ok)
   {
    httpclient.getConnectionManager().shutdown();
    return null;
   }
  }

  return null;
 }

}
