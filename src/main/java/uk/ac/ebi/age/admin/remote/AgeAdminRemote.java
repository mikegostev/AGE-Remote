package uk.ac.ebi.age.admin.remote;

import java.net.CookieHandler;
import java.net.CookieManager;

import uk.ac.ebi.age.admin.client.AgeAdminService;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


public class AgeAdminRemote
{
 private static CookieManager coockieMngr = new CookieManager();
 
 static
 {
  CookieHandler.setDefault(coockieMngr);
 }
 
 public static AgeAdminService getInstance( String webAppURL  )
 {
  return (AgeAdminService)SyncProxy.newProxyInstance(AgeAdminService.class,
    webAppURL+"/admin/", AgeAdminService.class.getAnnotation(RemoteServiceRelativePath.class).value(),coockieMngr);
 }

}
