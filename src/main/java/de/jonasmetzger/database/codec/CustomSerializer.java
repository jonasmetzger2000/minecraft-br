package de.jonasmetzger.database.codec;

public interface CustomSerializer<T> {

    String serialize(T obj);
    T deserialize(String str);

}
