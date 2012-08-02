package uk.ac.ebi.age.admin.remote;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class RMICall
{
 public static final String fabricMethodName = "newInstance";
 
 private static Map<Class<?>, String2ValueConverter> directConverterMap = new HashMap<Class<?>, String2ValueConverter>();
 private static Map<Class<?>, OutputFormatter> formatters = new HashMap<Class<?>, OutputFormatter>();

 static
 {
  directConverterMap.put(int.class, new IntStr2ValConverter());
 }
 
 public static String call(Object instance, String... input) throws AmbiguousMethodCallException, MethodNotExistException, ArgumentConversionException,
   InvocationTargetException, MethodInvocationException
 {
  
  String methodName = input[0];
  
  Method method = null;
  
  for( Method mth : instance.getClass().getMethods() )
  {
   
   if( mth.getName().equals(methodName) && mth.getParameterTypes().length == input.length-1 )
   {
    if( method != null )
     throw new AmbiguousMethodCallException();
    
    method = mth;
   }
   
  }
  
  if( method == null )
   throw new MethodNotExistException();
  
  Object[] params = new Object[ input.length - 1 ];
  
  int i=-1;
  
  for( Class<?> cls : method.getParameterTypes() )
  {
   i++;
   
   if( cls == String.class )
    params[i] = input[i+1];
   else
   {
    String2ValueConverter conv = directConverterMap.get(cls);
    
    if( conv != null )
    {
     try
     {
      params[i] = conv.convert(input[i+1]);
      continue;
     }
     catch(ConvertionException e)
     {
      throw new ArgumentConversionException("Argument #"+i+" conversion error. Target class: "+cls.getName(), i);
     }
    }
    
    Method fabMeth = null;
    
    try
    {
     fabMeth = cls.getMethod(fabricMethodName, String.class);
     
     if( ! Modifier.isStatic( fabMeth.getModifiers() ) || ! cls.isAssignableFrom(fabMeth.getReturnType()) )
      fabMeth = null;
    }
    catch(NoSuchMethodException | SecurityException e1)
    {
    }
    
    if( fabMeth != null )
    {
     try
     {
      params[i] = fabMeth.invoke(null, input[i+1]) ;
      continue;
     }
     catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
     {
      throw new ArgumentConversionException("Argument #"+i+" conversion error. (Fabric method call error: "+e1.getMessage()+") Target class: "+cls.getName(), i);
     }
    }
    
    Constructor<?> ctor = null;

    try
    {
     ctor = cls.getConstructor(String.class);
    }
    catch(NoSuchMethodException | SecurityException e)
    {
    }

    if( ctor != null )
    {
     try
     {
      params[i] = ctor.newInstance(input[i+1]);
      continue;
     }
     catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
     {
      throw new ArgumentConversionException("Argument #"+i+" conversion error. (Constructor call error: "+e.getMessage()+") Target class: "+cls.getName(), i);
     }
    }
    
    throw new ArgumentConversionException("Argument #"+i+" conversion error. No corresponding converter. Target class: "+cls.getName(), i);

   }
  }
  
  Object val = null;
  
  try
  {
   val = method.invoke(instance, params);
  }
  catch(IllegalAccessException | IllegalArgumentException e)
  {
   throw new MethodInvocationException("Invocation error",e);
  }
  
  if( val == null )
   return null;
 
  OutputFormatter fmt = formatters.get( method.getReturnType() );
 
  if( fmt != null )
   return fmt.format(val);
  
  return val.toString();
 }
 
}
