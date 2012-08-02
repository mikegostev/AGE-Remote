package uk.ac.ebi.age.admin.remote.test;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.remote.AgeAdminRemote;
import uk.ac.ebi.age.ext.user.exception.NotAuthorizedException;
import uk.ac.ebi.age.ext.user.exception.UserAuthException;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public class RemoteTest
{

 /**
  * @param args
  * @throws UserAuthException 
  * @throws NotAuthorizedException 
  */
 public static void main(String[] args) throws UserAuthException, NotAuthorizedException
 {
  AgeAdminService svc = AgeAdminRemote.getInstance("http://wwwdev.ebi.ac.uk/biosamples/");
  System.out.println(  AgeAdminService.class.getAnnotation(RemoteServiceRelativePath.class).value() );
  System.out.println( svc.login("mike", "mikegostev") );
  System.out.println( svc.getModelImprint() );
 }

}
