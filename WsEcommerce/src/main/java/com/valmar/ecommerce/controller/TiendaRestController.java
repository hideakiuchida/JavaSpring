package com.valmar.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.valmar.ecommerce.enums.TipoEstado;
import com.valmar.ecommerce.model.MetodoPago;
import com.valmar.ecommerce.model.Tienda;
import com.valmar.ecommerce.model.Usuario;
import com.valmar.ecommerce.services.TiendaService;
import com.valmar.ecommerce.viewmodel.TiendaVM;
import com.valmar.ecommerce.viewmodel.TiendaVMLite;

@CrossOrigin
@RestController
@RequestMapping("/tienda")
public class TiendaRestController {
	
	@Autowired
    TiendaService service;

    @RequestMapping(value = { "/listar" }, method = RequestMethod.GET)
    public ResponseEntity<List<Tienda>> listarTiendas() {
        List<Tienda> tiendas = service.listarTiendas();
        if(tiendas.isEmpty()){
            return new ResponseEntity<List<Tienda>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Tienda>>(tiendas, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/obtenerPorId", params = {"id"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tienda> obtenerPorId(@RequestParam("id") int id) {
    	Tienda tienda = service.obtenerPorId(id);
        if (tienda == null) {
            return new ResponseEntity<Tienda>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Tienda>(tienda, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/obtenerTiendasPorNombre", params = {"nombre"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TiendaVMLite>> obtenerTiendasPorNombre(@RequestParam("nombre") String nombre) {
    	 List<Tienda> tiendas = service.obtenerTiendasPorNombre(nombre);
    	 List<TiendaVMLite> tiendasLite = new ArrayList<>();
    	 for(Tienda item : tiendas){
    		 TiendaVMLite _tienda = new TiendaVMLite();
    		 _tienda.setId(item.getId());
    		 _tienda.setNombre(item.getNombre());
    		 tiendasLite.add(_tienda);
    	 }
         if(tiendasLite.isEmpty()){
             return new ResponseEntity<List<TiendaVMLite>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
         }
         return new ResponseEntity<List<TiendaVMLite>>(tiendasLite, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/agregar", method = RequestMethod.POST)
    public ResponseEntity<Void> agregar(@RequestBody TiendaVM tienda,  UriComponentsBuilder ucBuilder) {
    	 
    	Usuario usuario = service.obtenerUsuario(tienda.getId_usuario());
    	
    	if(usuario==null){
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	}
    	Tienda tiendaBean = new Tienda();
    	tiendaBean.setNombre(tienda.getNombre());
    	tiendaBean.setRuc(tienda.getRuc());
    	tiendaBean.setTelefono_local(tienda.getTelefono_local());
    	tiendaBean.setTelefono_movil(tienda.getTelefono_movil());
    	tiendaBean.setAfiliacion(tienda.getAfiliacion());
    	tiendaBean.setAfiliacion_valor(tienda.getAfiliacion_valor());
    	tiendaBean.setCostoMinimo(tienda.getCostoMinimo());
    	tiendaBean.setEstado(TipoEstado.HABILITADO.getValue());
    	tiendaBean.setEstadoAbierto(tienda.getEstadoAbierto());
    	tiendaBean.setUsuario(usuario);
    	tiendaBean.setFechaRegistro(new Date());
    	tiendaBean.setFechaModificacion(new Date());
    	
    	List<MetodoPago> metodoPagos = new ArrayList<>();
    	for(int id_metodoPago : tienda.getMetodoPagos()){
    		MetodoPago metodoPago = service.obtenerMetodoPago(id_metodoPago);
    		metodoPagos.add(metodoPago);
    	}
    	tiendaBean.setMetodoPagos(new HashSet<MetodoPago>(metodoPagos));
    	
        service.agregar(tiendaBean); 
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/tienda/{nombre}").buildAndExpand(tienda.getNombre()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<Void> actualizar(@RequestBody TiendaVM tienda,  UriComponentsBuilder ucBuilder) {
    	 
    	Usuario usuario = service.obtenerUsuario(tienda.getId_usuario());
    	
    	if(usuario==null){
    		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    	}
    	Tienda tiendaBean = new Tienda();
    	tiendaBean.setId(tienda.getId());
    	tiendaBean.setNombre(tienda.getNombre());
    	tiendaBean.setRuc(tienda.getRuc());
    	tiendaBean.setTelefono_local(tienda.getTelefono_local());
    	tiendaBean.setTelefono_movil(tienda.getTelefono_movil());
    	tiendaBean.setAfiliacion(tienda.getAfiliacion());
    	tiendaBean.setAfiliacion_valor(tienda.getAfiliacion_valor());
    	tiendaBean.setCostoMinimo(tienda.getCostoMinimo());
    	tiendaBean.setEstado(TipoEstado.HABILITADO.getValue());
    	tiendaBean.setEstadoAbierto(tienda.getEstadoAbierto());
    	tiendaBean.setUsuario(usuario);
    	tiendaBean.setFechaModificacion(new Date());
    	
    	List<MetodoPago> metodoPagos = new ArrayList<>();
    	for(int id_metodoPago : tienda.getMetodoPagos()){
    		MetodoPago metodoPago = service.obtenerMetodoPago(id_metodoPago);
    		metodoPagos.add(metodoPago);
    	}
    	tiendaBean.setMetodoPagos(new HashSet<MetodoPago>(metodoPagos));
    	
        service.actulizar(tiendaBean); 
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/tienda/{nombre}").buildAndExpand(tienda.getNombre()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/eliminar", params = {"id"}, method = RequestMethod.DELETE)
    public ResponseEntity<Tienda> eliminar(@RequestParam("id") int id) {
    	Tienda tienda = service.obtenerPorId(id);
        if (tienda == null) {
            return new ResponseEntity<Tienda>(HttpStatus.NOT_FOUND);
        } 
        service.eliminar(id);
        return new ResponseEntity<Tienda>(HttpStatus.NO_CONTENT);
    }

}
