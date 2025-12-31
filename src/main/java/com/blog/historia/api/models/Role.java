package com.blog.historia.api.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, // control total
    USER,  // usuario consulta 
	PJ,	   // 
	PJADM, // usuario adm JUdicial
	PJJUD, // usuario judicial
	PJINF, // usuario informatica
	PJTRA, // usuario modulo de transportes
	PJSEG, // usuario modulo de seguridad y resguardo
	PJMAU, // usuario uso de MAU
	ML,	   // Medicina Legal
	DP,	   // Defensa Publica
	MP,	   // Ministerio Publico
	PNP;   // Policina Nacional

	@Override
	public String getAuthority() {
		return name();
	}  
}