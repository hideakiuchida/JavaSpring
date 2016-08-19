package com.valmar.ecommerce.dao;

import java.util.List;

import com.valmar.ecommerce.model.Direccion;
import com.valmar.ecommerce.model.Tienda;
import com.valmar.ecommerce.viewmodel.TiendaVMLite;

public interface TiendaDao {
	Tienda obtenerPorId(int id);	 
    int agregar(Tienda tienda);     
    void eliminar(int id);    
    List<Tienda> listarTiendas();
	void actualizar(Tienda tiendaBean);
	Tienda obtenerTiendaPorDireccion(int id);
	List<Tienda> obtenerTiendasPorNombre(String nombre);
	List<Tienda> listarPorDistrito(int id);
	List<Tienda> obtenerTiendasPorNombreDistrito(String nombre, int id);
	List<Tienda> listarPorVendedor(int id);
	List<Tienda> obtenerTiendasPorNombreDistritoUrbanizacion(String nombre, int id, int id_urbanizacion);
	List<Tienda> listarPorBodeguero(int id);
	List<TiendaVMLite> listarTodosTiendasPorCobertura();
	List<TiendaVMLite> obtenerTiendasPorNombreDistrito2(String nombre, int id);
	List<TiendaVMLite> obtenerTiendasPorNombre2(String nombre);	
}
