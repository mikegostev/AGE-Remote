package uk.ac.ebi.age.admin.remote;

public interface String2ValueConverter
{
 Object convert( String val, Class<?> targetClass ) throws ConvertionException;
}
