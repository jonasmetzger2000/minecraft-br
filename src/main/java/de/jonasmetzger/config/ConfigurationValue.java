package de.jonasmetzger.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationValue {
    @BsonId
    public String key;
    public String value;
}
