package uk.ac.ebi.age.admin.remote;

public class IntStr2ValConverter implements String2ValueConverter
{

 @Override
 public Object convert(String val) throws ConvertionException
 {
  try
  {
   return Integer.parseInt(val);
  }
  catch(Exception e)
  {
   throw new ConvertionException();
  }
 }

}
