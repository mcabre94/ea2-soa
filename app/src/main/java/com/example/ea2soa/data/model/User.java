package com.example.ea2soa.data.model;

import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {
    private String nombre;
    private String apellido;
    private Integer dni;
    private Integer comision;
    private String username;
    private String password;
    private String password_repeat;
    private HashMap<String,String> errors;

    public User(String nombre, String apellido, Integer dni, Integer comision, String username, String  password, String password_repeat) {
        System.out.println("q ondaaaaa1");
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.comision = comision;
        this.username = username;
        this.password = password;
        this.password_repeat = password_repeat;
    }

    public User(String nombre, String apellido, String dni, String comision, String username, String  password, String password_repeat) {
        this.errors = new HashMap<>();

        System.out.println("q ondaaaaa1");
        this.nombre = nombre;
        this.apellido = apellido;
        try{
            this.dni = Integer.parseInt(dni);
        }catch (Exception e ){errors.put("dni","Ingrese un valor numérico de hasta 10 dígitos");}
        try{
            this.comision = Integer.parseInt(comision);
        }catch (Exception e){errors.put("comision","Ingrese un valor numérico de hasta 10 dígitos");}
        this.username = username;
        this.password = password;
        this.password_repeat = password_repeat;

        System.out.println("q ondaaaaa2");
    }

    public User(String nombre, String apellido, Integer dni, Integer comision, String username) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.comision = comision;
        this.username = username;
    }

    public User(String username,String password) {
        this.username = username;
        this.password = password;
    }

    private void validateUser(){
        if(this.errors == null)
            this.errors = new HashMap<>();

        if(nombre.isEmpty()){
            errors.put("nombre","El campo nombre es obligatorio");
        }else if(!nombre.matches("[a-zA-Z áéíóúñÑÁÉÍÓÚ,']+")){
            errors.put("nombre","El nombre solo puede contener letras, apóstrofes, comas y espacios");
        }
        if(apellido.isEmpty()){
            errors.put("apellido","El campo apellido es obligatorio");
        }else if(!apellido.matches("[a-zA-Z áéíóúñÑÁÉÍÓÚ,']+")){
            errors.put("apellido","El apellido solo puede contener letras, apóstrofes, comas y espacios");
        }
//        InternetAddr
//        if(!username.matches("[a-zA-Z áéíóúñÑÁÉÍÓÚ,']+")){
//            errors.put("apellido","El apellido solo puede contener letras, apóstrofes, comas y espacios");
//        }
        if(String.valueOf(dni).length() > 10){
            errors.put("dni","El dni no puede tener mas de 10 dígitos");
        }

        if(String.valueOf(comision).length() > 10){
            errors.put("dni","La comisión no puede tener mas de 10 dígitos");
        }

        if(username.isEmpty()){
            errors.put("username","El campo email es obligatorio");
        }else if(!username.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            errors.put("username","El email no es válido");
        }

        if(password.length() < 4){
            errors.put("password","La password tiene que tener un mínimo de 4 caracteres");
        }

        System.out.println(password);
        System.out.println(password_repeat);
        System.out.println(password.equals(password_repeat));
        if(!password.equals(password_repeat)){
            errors.put("password_repeat","El valor de repetición no coincide con la password");
        }
        System.out.println(errors);
    }

    public boolean isValid(){
        validateUser();
        return errors.isEmpty();
    }

    public HashMap<String,String> getErrors(){
        return errors;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public Integer getComision() {
        return comision;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
