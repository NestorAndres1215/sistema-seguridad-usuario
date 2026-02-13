package com.example.backend_security.utils;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

public final  class ValidacionUtil {

    private static final Pattern TELEFONO_PATTERN = Pattern.compile("\\d{9}");
    private static final Pattern CORREO_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");


    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && TELEFONO_PATTERN.matcher(telefono).matches();
    }

    public static boolean esCorreoValido(String correo) {
        return correo != null && CORREO_PATTERN.matcher(correo).matches();
    }

    public static boolean esNombreValido(String nombre) {
        return nombre != null && NOMBRE_PATTERN.matcher(nombre).matches();
    }

    public static String convertirArchivoABase64(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o es nulo");
        }

        byte[] contenido = archivo.getBytes();
        return Base64.getEncoder().encodeToString(contenido);
    }
}
