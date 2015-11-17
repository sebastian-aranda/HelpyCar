package cl.osare.helpycar;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class Local{
	
	private int id;
	private String nombre;
	private String localizacion;
	private String direccion;
	private String telefono;
	private String horario;
	private String mail;
	private String logo;
	private String photo;
	private String descripcion;

	private int premium;
	private Marker marker;
	
	public Local(){}
	
	public Local(int id, String nombre, String localizacion, String direccion, String telefono, String horario,
			String mail, String logo, String photo, String descripcion, int premium){
		this.id = id;
		this.nombre = nombre;
		this.localizacion = localizacion;
		this.direccion = direccion;
		this.telefono = telefono;
		this.horario = horario;
		this.mail = mail;
		this.logo = logo;
		this.photo = photo;
		this.descripcion = descripcion;
		this.premium = premium;
	}

	//GETTERS
	public int getId(){
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getHorario() {
		return horario;
	}

	public String getMail() {
		return mail;
	}

	public String getLogo() {
		return logo;
	}

	public String getPhoto() {
		return photo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public int getPremium() {
		return premium;
	}
	
	public Marker getMarker(){
		return marker;
	}

	//SETTERS
	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setPremium(int premium) {
		this.premium = premium;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public void setMarkerVisible(boolean value){
		this.marker.setVisible(value);
	}
	
}
