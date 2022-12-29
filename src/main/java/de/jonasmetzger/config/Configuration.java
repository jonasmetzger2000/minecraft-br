package de.jonasmetzger.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    public String key;
    public Map<String, Object> value;
}
