package de.jonasmetzger.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.RawBsonDocument;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

@RequiredArgsConstructor
public class CustomJacksonCodec<T> implements Codec<T> {

    private final CodecRegistry codecRegistry;
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    @Override
    @SneakyThrows
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        RawBsonDocument document = codecRegistry.get(RawBsonDocument.class).decode(reader, decoderContext);
        String json = document.toJson();
        return objectMapper.readValue(json, clazz);
    }

    @Override
    @SneakyThrows
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        String json = objectMapper.writeValueAsString(value);
        codecRegistry.get(RawBsonDocument.class).encode(writer, RawBsonDocument.parse(json), encoderContext);
    }

    @Override
    public Class<T> getEncoderClass() {
        return null;
    }
}
