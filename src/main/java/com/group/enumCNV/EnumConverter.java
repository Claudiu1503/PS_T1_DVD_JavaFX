package com.group.enumCNV;

// Generic Interface for Enum Conversion
public interface EnumConverter<T extends Enum<T>> {
    String toString(T enumValue);
    T fromString(String value);
}

