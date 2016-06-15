package com.valmar.ecommerce.dao;

import java.util.List;

import com.valmar.ecommerce.model.Tienda;

public interface TiendaDao {
	Tienda obtenerPorId(int id);	 
    void agregar(Tienda tienda);     
    void eliminar(int id);    
    List<Tienda> listarTiendas();
}