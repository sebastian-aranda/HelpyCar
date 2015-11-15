package cl.osare.helpycar;

import com.google.android.gms.maps.model.Marker;

public class Local{
	
	private int id;
	private String nombre;
	private String localizacion;
	private int tipo;
	private String direccion;
	private String telefono;
	private String horario;
	private String mail;
	private String descripcion;
	
	private Marker marker;
	
	public Local(){}
	
	public Local(int id, String nombre, String localizacion, int tipo, String direccion, String telefono, String horario, 
			String mail, String descripcion){
		this.id = id;
		this.nombre = nombre;
		this.localizacion = localizacion;
		this.tipo = tipo; 
		this.direccion = direccion;
		this.telefono = telefono;
		this.horario = horario;
		this.mail = mail;
		this.descripcion = descripcion;
	}
	
	public int getId(){
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getLocalizacion() {
		return localizacion;
	}
	
	public int getTipo(){
		return tipo;
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

	public String getDescripcion() {
		return descripcion;
	}
	
	public Marker getMarker(){
		return marker;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
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

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public void setMarkerVisible(boolean value){
		this.marker.setVisible(value);
	}
	
}
