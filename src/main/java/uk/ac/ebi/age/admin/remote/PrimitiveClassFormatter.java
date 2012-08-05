package uk.ac.ebi.age.admin.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrimitiveClassFormatter implements OutputFormatter
{

 @Override
 public String format(Object obj, Class<?> prClass) throws FormatterException
 {
  try
  {
   Method valueOfMeth = String.class.getMethod("valueOf",prClass);
   return (String) valueOfMeth.invoke(null, obj);
  }
  catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
  {
   throw new FormatterException("Can't invoke String.valueOf method for arg: " + prClass.getName(), e);
  }
 }

}
