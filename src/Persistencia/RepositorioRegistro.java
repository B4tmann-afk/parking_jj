/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;


import Modelos.Registro;
import java.util.ArrayList;
import java.util.List;

public class RepositorioRegistro {
    private List<Registro> registros;

    public RepositorioRegistro() {
        this.registros = new ArrayList<>();
    }

    public synchronized void guardar(Registro registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
        registros.add(registro);
    }

    public List<Registro> listarRegistros() {
        return new ArrayList<>(registros);
    }

    public synchronized void actualizar(Registro registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }
        
        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getId() == registro.getId()) {
                registros.set(i, registro);
                return;
            }
        }
    }
}