package com.valmar.ecommerce.controller;

import java.util.ArrayList;
import java.util.Comparator;
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
import com.valmar.ecommerce.model.Banco;
import com.valmar.ecommerce.model.Direccion;
import com.valmar.ecommerce.model.Distrito;
import com.valmar.ecommerce.model.Envio;
import com.valmar.ecommerce.model.Horarios;
import com.valmar.ecommerce.model.ImagenTienda;
import com.valmar.ecommerce.model.MetodoPago;
import com.valmar.ecommerce.model.Tienda;
import com.valmar.ecommerce.model.TipoTienda;
import com.valmar.ecommerce.model.Usuario;
import com.valmar.ecommerce.services.BancoService;
import com.valmar.ecommerce.services.DireccionService;
import com.valmar.ecommerce.services.HorarioService;
import com.valmar.ecommerce.services.ImagenTiendaService;
import com.valmar.ecommerce.services.TiendaService;
import com.valmar.ecommerce.services.TipoTiendaService;
import com.valmar.ecommerce.util.DistanceCalculatorUtil;
import com.valmar.ecommerce.viewmodel.HorarioVM;
import com.valmar.ecommerce.viewmodel.ImagenProductoVM;
import com.valmar.ecommerce.viewmodel.TiendaVM;
import com.valmar.ecommerce.viewmodel.TiendaVMDetalle;
import com.valmar.ecommerce.viewmodel.TiendaVMLite;

@CrossOrigin
@RestController
@RequestMapping("/tienda")
public class TiendaRestController {

	@Autowired
	TiendaService service;
	@Autowired
	DireccionService direccionService;
	@Autowired
	ImagenTiendaService imagenTiendaService;
	@Autowired
	TipoTiendaService tipoTiendaService;
	@Autowired
	BancoService bancoService;
	@Autowired
	HorarioService horarioService;

	private List<TiendaVMDetalle> clonarTiendasVMDetalle(List<Tienda> tiendas) {
		List<TiendaVMDetalle> tiendasLite = new ArrayList<>();
		for (Tienda item : tiendas) {
			tiendasLite.add(copyTiendaToTiendaVMDetalle(item));
		}
		return tiendasLite;
	}

	private TiendaVMDetalle copyTiendaToTiendaVMDetalle(Tienda item) {
		TiendaVMDetalle _tienda = new TiendaVMDetalle();
		_tienda.setId(item.getId());

		String nombre = item.getNombre() != null ? item.getNombre() : "";
		String horarioAtencion = item.getHorarioAtencion() != null ? item.getHorarioAtencion() : "";
		String telFijo = item.getTelefono_local() != null ? item.getTelefono_local() : "";
		String telfMovil = item.getTelefono_movil() != null ? item.getTelefono_movil() : "";
		String estado = (item.getEstado() != 1 || item.getEstado() != 2) ? ""
				: ((item.getEstado() == 1) ? "Abierto" : "Cerrado");
		String tarjeta = (item.getTarjeta() != null) ? ((item.getTarjeta() == 1) ? "Si" : "No") : "";
		String web = (item.getPaginaweb() != null) ? item.getPaginaweb() : "";

		_tienda.setNombre(nombre);
		_tienda.setHorarioAtencion(horarioAtencion);
		_tienda.setTelefonoFijo(telFijo);
		_tienda.setTelefonoMovil(telfMovil);
		_tienda.setEstado(estado);
		_tienda.setTarjeta(tarjeta);
		_tienda.setWeb(web);

		ImagenTienda imagen = imagenTiendaService.obtenerImagenPorDefectoTienda(item.getId());
		if (imagen != null)
			_tienda.setImagen(imagen.getImagen());
		if (item.getDistancia() != null)
			_tienda.setDistancia(Double.parseDouble(item.getDistancia()));
		List<Direccion> direcciones = direccionService.listarPorTienda(item.getId());
		if (direcciones != null) {
			for (Direccion direccion : direcciones) {
				Distrito _distrito = direccion.getDistrito();
				String domicilio = direccion.getDomicilio() != null ? direccion.getDomicilio() : "";
				String numero = direccion.getNumero() != null ? direccion.getNumero() : "";
				String distrito = _distrito.getNombre() != null ? _distrito.getNombre() : "";
				String latitud = direccion.getLatitud() != null ? direccion.getLatitud() : "";
				String longitud = direccion.getLongitud() != null ? direccion.getLongitud() : "";
				String direccionFormatted = String.format("%s %s, %s ", domicilio, numero, distrito);
				_tienda.setDireccion(direccionFormatted);
				_tienda.setLatitud(latitud);
				_tienda.setLongitud(longitud);
				_tienda.setDomicilio(domicilio);
				_tienda.setNumero(numero);
				_tienda.setDistrito(distrito);
				break;
			}
		}

		List<TipoTienda> tipoTiendas = tipoTiendaService.listarPorTienda(item.getId());
		if (tipoTiendas != null) {
			StringBuilder strCategoria = new StringBuilder();
			for (int i = 0; i < tipoTiendas.size(); i++) {
				if (i == (tipoTiendas.size() - 1))
					strCategoria.append(tipoTiendas.get(i).getDescripcion());
				else
					strCategoria.append(tipoTiendas.get(i).getDescripcion() + ", ");
			}
			_tienda.setCategoria(strCategoria.toString());
		}

		List<Banco> bancos = bancoService.listarPorTienda(item.getId());
		if (bancos != null) {
			StringBuilder strBanco = new StringBuilder();
			for (int i = 0; i < bancos.size(); i++) {
				if (i == (bancos.size() - 1))
					strBanco.append(bancos.get(i).getNombre());
				else
					strBanco.append(bancos.get(i).getNombre() + ", ");
			}
			_tienda.setAgente(strBanco.toString());
		}

		List<Horarios> horarios = horarioService.obtenerHorariosPorTienda(item.getId());
		if (horarios != null) {
			List<HorarioVM> _horarios = new ArrayList<>();
			for (Horarios horario : horarios) {
				HorarioVM horarioVM = new HorarioVM();
				horarioVM.setDia_inicial(obtenerDia(horario.getDia_inicial()));
				horarioVM.setDia_final(obtenerDia(horario.getDia_final()));
				horarioVM.setHora_inicial(obtenerHora(horario.getHora_inicial()));
				horarioVM.setHora_final(obtenerHora(horario.getHora_final()));
				horarioVM.setHorario(String.format("%s - %s %s - %s", horarioVM.getDia_inicial(),
						horarioVM.getDia_final(), horarioVM.getHora_inicial(), horarioVM.getHora_final()));
				_horarios.add(horarioVM);
			}
			_tienda.setHorarios(_horarios);
		}
		return _tienda;
	}

	private String obtenerDia(int dia) {
		String diaString = "";
		switch (dia) {
		case 1:
			diaString = "Lunes";
			break;
		case 2:
			diaString = "Martes";
			break;
		case 3:
			diaString = "Miercoles";
			break;
		case 4:
			diaString = "Jueves";
			break;
		case 5:
			diaString = "Viernes";
			break;
		case 6:
			diaString = "Sabado";
			break;
		case 7:
			diaString = "Domingo";
			break;
		default:
			diaString = "";
		}
		return diaString;
	}

	private String obtenerHora(int hora) {
		String horaString = "";
		switch (hora) {
		case 1:
			horaString = "01:00 AM";
			break;
		case 2:
			horaString = "02:00 AM";
			break;
		case 3:
			horaString = "03:00 AM";
			break;
		case 4:
			horaString = "04:00 AM";
			break;
		case 5:
			horaString = "05:00 AM";
			break;
		case 6:
			horaString = "06:00 AM";
			break;
		case 7:
			horaString = "07:00 AM";
			break;
		case 8:
			horaString = "08:00 AM";
			break;
		case 9:
			horaString = "09:00 AM";
			break;
		case 10:
			horaString = "10:00 AM";
			break;
		case 11:
			horaString = "11:00 AM";
			break;
		case 12:
			horaString = "12:00 PM";
			break;
		case 13:
			horaString = "01:00 PM";
			break;
		case 14:
			horaString = "02:00 PM";
			break;
		case 15:
			horaString = "03:00 PM";
			break;
		case 16:
			horaString = "04:00 PM";
			break;
		case 17:
			horaString = "05:00 PM";
			break;
		case 18:
			horaString = "06:00 PM";
			break;
		case 19:
			horaString = "07:00 PM";
			break;
		case 20:
			horaString = "08:00 PM";
			break;
		case 21:
			horaString = "09:00 PM";
			break;
		case 22:
			horaString = "10:00 PM";
			break;
		case 23:
			horaString = "11:00 PM";
			break;
		case 24:
			horaString = "12:00 AM";
			break;
		default:
			horaString = "";
		}
		return horaString;
	}

	@RequestMapping(value = { "/listar" }, method = RequestMethod.GET)
	public ResponseEntity<List<Tienda>> listarTiendas() {
		List<Tienda> tiendas = service.listarTiendas();
		if (tiendas.isEmpty()) {
			return new ResponseEntity<List<Tienda>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Tienda>>(tiendas, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/obtenerTiendasPorNombre2", params = {
	"nombre" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TiendaVMLite>> obtenerTiendasPorNombre2(@RequestParam("nombre") String nombre) {
		List<TiendaVMLite> tiendas = service.obtenerTiendasPorNombre2(nombre);
		if ((tiendas == null) || (tiendas.isEmpty())) {
			return new ResponseEntity<List<TiendaVMLite>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<TiendaVMLite>>(tiendas, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/obtenerTiendasPorNombreDistrito2", params = { "nombre",
	"id" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TiendaVMLite>> obtenerTiendasPorNombreDistrito2(@RequestParam("nombre") String nombre,
			@RequestParam("id") int id) {
		List<TiendaVMLite> tiendas = service.obtenerTiendasPorNombreDistrito2(nombre, id);
		if ((tiendas == null) || (tiendas.isEmpty())) {
			return new ResponseEntity<List<TiendaVMLite>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<TiendaVMLite>>(tiendas, HttpStatus.OK);
	}

	@RequestMapping(value = { "/listarTiendasPorCobertura2" }, method = RequestMethod.GET)
	public ResponseEntity<List<TiendaVMLite>> listarTiendasPorCobertura2(@RequestParam String latitud,
			@RequestParam String longitud) {
		List<TiendaVMLite> tiendasLite = service.listarTodosTiendasPorCobertura();
		if (tiendasLite.isEmpty()) {
			return new ResponseEntity<List<TiendaVMLite>>(HttpStatus.NO_CONTENT);
		}
		double latitudCliente = Double.parseDouble(latitud);
		double longitudCliente = Double.parseDouble(longitud);
		List<TiendaVMLite> tiendasLiteFinal = new ArrayList<>();
		for (TiendaVMLite item : tiendasLite) {
			if ((item.getLatitud() != null && !item.getLatitud().isEmpty())
					&& (item.getLongitud() != null && !item.getLongitud().isEmpty())) {
				double latitudTienda = Double.parseDouble(item.getLatitud());
				double longitudTienda = Double.parseDouble(item.getLongitud());
				double distancia = DistanceCalculatorUtil.distance(latitudCliente, longitudCliente, latitudTienda,
						longitudTienda, "K");
				if (distancia < DistanceCalculatorUtil.RADIO_USUARIO && (item != null)) {
					distancia = distancia * 1000;
					item.setDistancia(distancia);
					tiendasLiteFinal.add(item);
				}
			}
		}

		tiendasLiteFinal.sort(Comparator.comparing(TiendaVMLite::getDistancia));

		return new ResponseEntity<List<TiendaVMLite>>(tiendasLiteFinal, HttpStatus.OK);
	}

	@RequestMapping(value = { "/listarPorBodeguero" }, params = { "id" }, method = RequestMethod.GET)
	public ResponseEntity<List<Tienda>> listarPorBodeguero(@RequestParam int id) {
		List<Tienda> tiendas = service.listarPorBodeguero(id);
		if (tiendas.isEmpty()) {
			return new ResponseEntity<List<Tienda>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Tienda>>(tiendas, HttpStatus.OK);
	}

	@RequestMapping(value = { "/listarTiendasPorCobertura" }, method = RequestMethod.GET)
	public ResponseEntity<List<TiendaVMDetalle>> listarTiendasPorCobertura(@RequestParam String latitud,
			@RequestParam String longitud) {
		List<Tienda> tiendas = new ArrayList<>();
		List<Direccion> direcciones = direccionService.listarDirecciones();
		double latitudCliente = Double.parseDouble(latitud);
		double longitudCliente = Double.parseDouble(longitud);
		for (Direccion item : direcciones) {
			if ((item.getLatitud() != null && !item.getLatitud().isEmpty())
					|| (item.getLongitud() != null && !item.getLongitud().isEmpty())) {
				double latitudTienda = Double.parseDouble(item.getLatitud());
				double longitudTienda = Double.parseDouble(item.getLongitud());
				double distancia = DistanceCalculatorUtil.distance(latitudCliente, longitudCliente, latitudTienda,
						longitudTienda, "K");
				if (distancia < DistanceCalculatorUtil.RADIO_USUARIO) {
					Tienda tienda = service.obtenerTiendaPorDireccion(item.getId());
					if (tienda != null) {
						distancia = distancia * 1000;
						tienda.setDistancia(Double.toString(distancia));
						tiendas.add(tienda);
					}
				}
			}
		}
		List<TiendaVMDetalle> tiendasLite = clonarTiendasVMDetalle(tiendas);

		tiendasLite.sort(Comparator.comparing(TiendaVMDetalle::getDistancia));

		if (tiendasLite.isEmpty()) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<TiendaVMDetalle>>(tiendasLite, HttpStatus.OK);
	}

	@RequestMapping(value = { "/listarPorVendedor" }, params = { "id" }, method = RequestMethod.GET)
	public ResponseEntity<List<Tienda>> listarPorVendedor(int id) {
		List<Tienda> tiendas = service.listarPorVendedor(id);
		if (tiendas.isEmpty()) {
			return new ResponseEntity<List<Tienda>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Tienda>>(tiendas, HttpStatus.OK);
	}

	@RequestMapping(value = { "/listarPorDistrito" }, params = { "id" }, method = RequestMethod.GET)
	public ResponseEntity<List<TiendaVMDetalle>> listarPorDistrito(@RequestParam int id) {
		List<Tienda> tiendas = service.listarPorDistrito(id);
		if ((tiendas == null) || (tiendas.isEmpty())) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}

		List<TiendaVMDetalle> tiendasLite = clonarTiendasVMDetalle(tiendas);

		return new ResponseEntity<List<TiendaVMDetalle>>(tiendasLite, HttpStatus.OK);
	}

	@RequestMapping(value = "/obtenerPorId", params = {
			"id" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TiendaVMDetalle> obtenerPorId(@RequestParam("id") int id) {
		Tienda tienda = service.obtenerPorId(id);
		if (tienda == null) {
			return new ResponseEntity<TiendaVMDetalle>(HttpStatus.NO_CONTENT);
		}
		TiendaVMDetalle _tienda = copyTiendaToTiendaVMDetalle(tienda);
		return new ResponseEntity<TiendaVMDetalle>(_tienda, HttpStatus.OK);
	}

	@RequestMapping(value = "/obtenerTiendasPorNombre", params = {
			"nombre" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TiendaVMDetalle>> obtenerTiendasPorNombre(@RequestParam("nombre") String nombre) {
		List<Tienda> tiendas = service.obtenerTiendasPorNombre(nombre);
		if ((tiendas == null) || (tiendas.isEmpty())) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}

		List<TiendaVMDetalle> tiendasLite = clonarTiendasVMDetalle(tiendas);

		if (tiendasLite.isEmpty()) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<TiendaVMDetalle>>(tiendasLite, HttpStatus.OK);
	}

	@RequestMapping(value = "/obtenerTiendasPorNombreDistrito", params = { "nombre",
			"id" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TiendaVMDetalle>> obtenerTiendasPorNombreDistrito(@RequestParam("nombre") String nombre,
			@RequestParam("id") int id) {
		List<Tienda> tiendas = service.obtenerTiendasPorNombreDistrito(nombre, id);
		if ((tiendas == null) || (tiendas.isEmpty())) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}

		List<TiendaVMDetalle> tiendasLite = clonarTiendasVMDetalle(tiendas);

		if (tiendasLite.isEmpty()) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<TiendaVMDetalle>>(tiendasLite, HttpStatus.OK);
	}	

	@RequestMapping(value = "/obtenerTiendasPorNombreDistritoUrbanizacion", params = { "nombre", "id",
			"id_urbanizacion" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TiendaVMDetalle>> obtenerTiendasPorNombreDistritoUrbanizacion(
			@RequestParam("nombre") String nombre, @RequestParam("id") int id,
			@RequestParam("id_urbanizacion") int id_urbanizacion) {
		List<Tienda> tiendas = service.obtenerTiendasPorNombreDistritoUrbanizacion(nombre, id, id_urbanizacion);
		if ((tiendas == null) || (tiendas.isEmpty())) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}

		List<TiendaVMDetalle> tiendasLite = clonarTiendasVMDetalle(tiendas);

		if (tiendasLite.isEmpty()) {
			return new ResponseEntity<List<TiendaVMDetalle>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<TiendaVMDetalle>>(tiendasLite, HttpStatus.OK);
	}

	@RequestMapping(value = "/agregar", method = RequestMethod.POST)
	public ResponseEntity<Integer> agregar(@RequestBody TiendaVM tienda, UriComponentsBuilder ucBuilder) {

		Tienda tiendaBean = new Tienda();
		tiendaBean.setNombre(tienda.getNombre());
		tiendaBean.setRuc(tienda.getRuc());
		tiendaBean.setRazonSocial(tienda.getRazon_social());
		tiendaBean.setTelefono_local(tienda.getTelefono_local());
		tiendaBean.setTelefono_movil(tienda.getTelefono_movil());
		tiendaBean.setAfiliacion(tienda.getAfiliacion());
		tiendaBean.setAfiliacion_valor(tienda.getAfiliacion_valor());
		tiendaBean.setCostoMinimo(tienda.getCostoMinimo());
		tiendaBean.setHorarioAtencion(tienda.getHorarioAtencion());
		tiendaBean.setPaginaweb(tienda.getPaginaweb());
		tiendaBean.setTarjeta(tienda.getTarjeta());
		tiendaBean.setEstado(TipoEstado.HABILITADO.getValue());
		tiendaBean.setEstadoAbierto(tienda.getEstadoAbierto());
		tiendaBean.setFechaRegistro(new Date());
		tiendaBean.setFechaModificacion(new Date());

		if (tienda.getId_banco() != null) {
			List<Banco> bancos = new ArrayList<>();
			for (int id : tienda.getId_banco()) {
				Banco banco = bancoService.obtenerPorId(id);
				if (banco != null)
					bancos.add(banco);
			}
			tiendaBean.setBancos(new HashSet<Banco>(bancos));
		}

		if (tienda.getId_tipo_tienda() != null) {
			List<TipoTienda> tipoTiendas = new ArrayList<>();
			for (int id_tienda : tienda.getId_tipo_tienda()) {
				TipoTienda tipoTienda = tipoTiendaService.obtenerPorId(id_tienda);
				if (tipoTienda != null)
					tipoTiendas.add(tipoTienda);
			}
			tiendaBean.setTipoTiendas(new HashSet<TipoTienda>(tipoTiendas));
		}

		if (tienda.getId_usuarios() != null) {
			List<Usuario> usuarios = new ArrayList<>();
			for (int id_usuario : tienda.getId_usuarios()) {
				Usuario usuario = service.obtenerUsuario(id_usuario);
				if (usuario != null)
					usuarios.add(usuario);
			}
			tiendaBean.setUsuarios(new HashSet<Usuario>(usuarios));
		}

		if (tienda.getMetodoPagos() != null) {
			List<MetodoPago> metodoPagos = new ArrayList<>();
			for (int indice : tienda.getMetodoPagos()) {
				MetodoPago metodoPago = service.obtenerMetodoPago(indice);
				if (metodoPago != null)
					metodoPagos.add(metodoPago);
			}
			tiendaBean.setMetodoPagos(new HashSet<MetodoPago>(metodoPagos));
		}
		if (tienda.getEnvios() != null) {
			List<Envio> envios = new ArrayList<>();
			for (int indice : tienda.getEnvios()) {
				Envio envio = service.obtenerEnvio(indice);
				if (envio != null)
					envios.add(envio);
			}
			tiendaBean.setEnvios(new HashSet<Envio>(envios));
		}

		if (tienda.getHorarios() != null) {
			List<Horarios> horarios = new ArrayList<>();
			for (Horarios horario : tienda.getHorarios()) {
				horario.setTienda(tiendaBean);
				horarios.add(horario);
			}
			tiendaBean.setHorarios(new HashSet<Horarios>(horarios));
		}

		int id = service.agregar(tiendaBean);

		return new ResponseEntity<Integer>(id, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/actualizar", method = RequestMethod.PUT)
	public ResponseEntity<Void> actualizar(@RequestBody TiendaVM tienda, UriComponentsBuilder ucBuilder) {

		Tienda tiendaBean = new Tienda();
		tiendaBean.setId(tienda.getId());
		tiendaBean.setNombre(tienda.getNombre());
		tiendaBean.setRuc(tienda.getRuc());
		tiendaBean.setRazonSocial(tienda.getRazon_social());
		tiendaBean.setTelefono_local(tienda.getTelefono_local());
		tiendaBean.setTelefono_movil(tienda.getTelefono_movil());
		tiendaBean.setAfiliacion(tienda.getAfiliacion());
		tiendaBean.setAfiliacion_valor(tienda.getAfiliacion_valor());
		tiendaBean.setCostoMinimo(tienda.getCostoMinimo());
		tiendaBean.setHorarioAtencion(tienda.getHorarioAtencion());
		tiendaBean.setPaginaweb(tienda.getPaginaweb());
		tiendaBean.setTarjeta(tienda.getTarjeta());
		tiendaBean.setEstado(TipoEstado.HABILITADO.getValue());
		tiendaBean.setEstadoAbierto(tienda.getEstadoAbierto());
		tiendaBean.setFechaModificacion(new Date());

		if (tienda.getId_banco() != null) {
			List<Banco> bancos = new ArrayList<>();
			for (int id : tienda.getId_banco()) {
				Banco banco = bancoService.obtenerPorId(id);
				if (banco != null)
					bancos.add(banco);
			}
			tiendaBean.setBancos(new HashSet<Banco>(bancos));
		}

		if (tienda.getId_tipo_tienda() != null) {
			List<TipoTienda> tipoTiendas = new ArrayList<>();
			for (int id_tienda : tienda.getId_tipo_tienda()) {
				TipoTienda tipoTienda = tipoTiendaService.obtenerPorId(id_tienda);
				if (tipoTienda != null)
					tipoTiendas.add(tipoTienda);
			}
			tiendaBean.setTipoTiendas(new HashSet<TipoTienda>(tipoTiendas));
		}

		if (tienda.getId_usuarios() != null) {
			List<Usuario> usuarios = new ArrayList<>();
			for (int id_usuario : tienda.getId_usuarios()) {
				Usuario usuario = service.obtenerUsuario(id_usuario);
				if (usuario != null)
					usuarios.add(usuario);
			}
			tiendaBean.setUsuarios(new HashSet<Usuario>(usuarios));
		}

		if (tienda.getMetodoPagos() != null) {
			List<MetodoPago> metodoPagos = new ArrayList<>();
			for (int indice : tienda.getMetodoPagos()) {
				MetodoPago metodoPago = service.obtenerMetodoPago(indice);
				if (metodoPago != null)
					metodoPagos.add(metodoPago);
			}
			tiendaBean.setMetodoPagos(new HashSet<MetodoPago>(metodoPagos));
		}
		if (tienda.getEnvios() != null) {
			List<Envio> envios = new ArrayList<>();
			for (int indice : tienda.getEnvios()) {
				Envio envio = service.obtenerEnvio(indice);
				if (envio != null)
					envios.add(envio);
			}
			tiendaBean.setEnvios(new HashSet<Envio>(envios));
		}

		if (tienda.getHorarios() != null) {
			List<Horarios> horarios = new ArrayList<>();
			for (Horarios horario : tienda.getHorarios()) {
				horario.setTienda(tiendaBean);
				horarios.add(horario);
			}
			tiendaBean.setHorarios(new HashSet<Horarios>(horarios));
		}

		service.actualizar(tiendaBean);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/tienda/{nombre}").buildAndExpand(tienda.getNombre()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/eliminar", params = { "id" }, method = RequestMethod.DELETE)
	public ResponseEntity<Tienda> eliminar(@RequestParam("id") int id) {
		Tienda tienda = service.obtenerPorId(id);
		if (tienda == null) {
			return new ResponseEntity<Tienda>(HttpStatus.NO_CONTENT);
		}
		service.eliminar(id);
		return new ResponseEntity<Tienda>(HttpStatus.NO_CONTENT);
	}

	/************************************************************************************************
	 ********************************** Imagenes de Producto*****************************************
	 ************************************************************************************************/

	@RequestMapping(value = { "/imagen/listar" }, method = RequestMethod.GET)
	public ResponseEntity<List<ImagenTienda>> listarImagenes() {
		List<ImagenTienda> imagenes = imagenTiendaService.listarImagenes();
		if (imagenes.isEmpty()) {
			return new ResponseEntity<List<ImagenTienda>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ImagenTienda>>(imagenes, HttpStatus.OK);
	}

	@RequestMapping(value = "/imagen/obtenerPorId", params = {
			"id" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ImagenTienda> obtenerImagenPorId(@RequestParam("id") int id) {
		ImagenTienda imagen = imagenTiendaService.obtenerImagenPorId(id);
		if (imagen == null) {
			return new ResponseEntity<ImagenTienda>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ImagenTienda>(imagen, HttpStatus.OK);
	}

	@RequestMapping(value = "/imagen/agregar", method = RequestMethod.POST)
	public ResponseEntity<Void> agregarImagen(@RequestBody ImagenProductoVM imagen, UriComponentsBuilder ucBuilder) {
		ImagenTienda imagenBean = new ImagenTienda();
		Tienda tienda = service.obtenerPorId(imagen.getId_tienda());

		if (tienda == null) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		imagenBean.setNombre(imagen.getNombre());
		imagenBean.setTienda(tienda);
		imagenBean.setImagen(imagen.getImagen());
		imagenBean.setDefecto(imagen.getDefecto());

		imagenTiendaService.agregarImagen(imagenBean);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/imagen/{nombre}").buildAndExpand(tienda.getNombre()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/imagen/actualizar", method = RequestMethod.PUT)
	public ResponseEntity<Void> actualizarImagen(@RequestBody ImagenProductoVM imagen, UriComponentsBuilder ucBuilder) {
		ImagenTienda imagenBean = new ImagenTienda();
		Tienda tienda = service.obtenerPorId(imagen.getId_tienda());

		if (tienda == null) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		imagenBean.setId(imagen.getId());
		imagenBean.setNombre(imagen.getNombre());
		imagenBean.setTienda(tienda);
		imagenBean.setImagen(imagen.getImagen());
		imagenBean.setDefecto(imagen.getDefecto());

		imagenTiendaService.actualizarImagen(imagenBean);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/imagen/{nombre}").buildAndExpand(tienda.getNombre()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/imagen/eliminar", params = { "id" }, method = RequestMethod.DELETE)
	public ResponseEntity<ImagenTienda> eliminarImagen(@RequestParam("id") int id) {
		ImagenTienda tienda = imagenTiendaService.obtenerImagenPorId(id);
		if (tienda == null) {
			return new ResponseEntity<ImagenTienda>(HttpStatus.NO_CONTENT);
		}
		imagenTiendaService.eliminarImagen(id);

		return new ResponseEntity<ImagenTienda>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = { "/imagen/listarImagenesPorTienda" }, method = RequestMethod.GET)
	public ResponseEntity<List<ImagenTienda>> listarImagenesPorTienda(int id) {
		List<ImagenTienda> imagenes = imagenTiendaService.listarImagenesPorTienda(id);
		if (imagenes.isEmpty()) {
			return new ResponseEntity<List<ImagenTienda>>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<ImagenTienda>>(imagenes, HttpStatus.OK);
	}

}
