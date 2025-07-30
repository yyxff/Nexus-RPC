package com.github.yyxff.nexusrpc.core.serializers;

import com.github.yyxff.nexusrpc.core.Serializer;

import java.io.*;

public class SerializerJDK implements Serializer {

    /**
     * |baos| is a stream like a bytes buffer
     * |oos| can convert an object into a stream in bytes
     * So we need to wrap |baos| by |oos|
     * @param obj: the object to be serialized
     * @return serialized object in bytes
     * @param <T>
     */
    @Override
    public <T> byte[] serialize(T obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(obj);
            return baos.toByteArray();
        }catch (IOException e){
            throw new RuntimeException("SerializerJDK error when serializing: ", e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            return clazz.cast(ois.readObject());
        }catch (IOException | ClassNotFoundException e){
            throw new RuntimeException("SerializerJDK error when deserializing: ", e);
        }
    }
}
