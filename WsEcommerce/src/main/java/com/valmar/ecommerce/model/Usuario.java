package com.valmar.ecommerce.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USUARIO")
public class Usuario {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE", length = 200, unique = true)
	@NotNull
	@Size(min = 4, max = 200)
	private String nombre;
	
	@Column(name = "APELLIDO", length = 100, unique = true)
	@NotNull
	@Size(min = 4, max = 100)
	private String apellido;
	
	@Column(name = "CORREO", length = 100, unique = true)
	@NotNull
	@Size(min = 4, max = 100)
	private String correo;
	
	@Column(name = "LOGIN", length = 100, unique = true)
	@NotNull
	@Size(min = 4, max = 100)
	private String login;
	
	@Column(name = "PASSWORD", length = 100, unique = true)
	@NotNull
	@Size(min = 4, max = 100)
	private String password;
	
	@Column(name = "ESTADO", length = 100, unique = true)
	@NotNull
	@Size(min = 1, max = 1)
	private int estado;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
}
