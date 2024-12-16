package com.aluracursos.bookstorage.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
}
