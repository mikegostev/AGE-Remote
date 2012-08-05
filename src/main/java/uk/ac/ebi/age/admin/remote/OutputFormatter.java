package uk.ac.ebi.age.admin.remote;

public interface OutputFormatter
{
 String format( Object obj, Class<?> sourceClass ) throws FormatterException;
}
