package uk.ac.ebi.age.admin.remote;

import java.util.ArrayList;
import java.util.List;

public class ArrayConverter implements String2ValueConverter
{
 public static char SEPARATOR = ';';
 public static char ESC = '\\';
 
 private static ArrayConverter instance = new ArrayConverter();
 
 public static ArrayConverter getInstance()
 {
  return instance;
 }

 @Override
 public Object convert(String val, Class< ? > targetClass) throws ConvertionException
 {
  // TODO Auto-generated method stub
  return null;
 }

 protected static List<String> splitString( String val )
 {
  int ptr = 0;
  int len = val.length();
  
  ArrayList< String > res = new ArrayList<>();
  
  String part = null;
  
  while( ptr < len )
  {
   int pos = val.indexOf(SEPARATOR,ptr);
   
   if( pos == -1 )
   {
    if( ptr == 0 )
     res.add( val );
    else
    {
     if( part != null )
      part += val.substring(ptr);
     else
      part = val.substring(ptr);
      
     res.add( part );
    }
    
    break;
   }
   
   if( pos >= 2 && val.charAt(pos-1) == ESC && val.charAt(pos-2) == ESC )
   {
    if( part != null )
     part += val.substring(ptr,pos-1);
    else
     part = val.substring(ptr,pos-1);

    res.add( part );
    ptr = pos+1;
    part = null;
   }
   else if( pos >= 1 && val.charAt(pos-1) == ESC )
   {
    if( part != null )
     part += val.substring(ptr,pos-1)+";";
    else
     part = val.substring(ptr,pos-1)+";";
    
    ptr = pos+1;
   }
   else
   {
    if( part != null )
     part += val.substring(ptr,pos-1);
    else
     part = val.substring(ptr,pos-1);
    
    res.add( part );
    ptr = pos+1;
    part = null;
   }
  }
  
  return res;
 }
}
