package com.group.mvp.model.enum_converter;

import com.group.mvp.model.FilmType;

public class FilmTypeConverter implements EnumConverter<FilmType> {
    @Override
    public String toString(FilmType enumValue) {
        return enumValue.name();
    }

    @Override
    public FilmType fromString(String value) {
        try {
            return FilmType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            System.err.println("Invalid FilmType: " + value);
            return FilmType.UNDEFINED; // Default fallback
        }
    }
}
