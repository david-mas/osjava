package code316.charlotte;
import java.math.BigInteger;
import java.util.List;

public interface Encoding {
    public static final char WHITE_SPACE[] = {' ', '\t'};
    String getName();
    void setName(String name);
    void addFieldDefinition(FieldDefinition fd);
    FieldDefinition getFieldDefinition(int index);
    int getLength();
    List getFieldDefinitions();    
    int getFieldCount();
    BigInteger getMaxValue();
    BigInteger extractFieldValue(int index, BigInteger bits);
    double expandFieldValue(int index, BigInteger bits);
}
