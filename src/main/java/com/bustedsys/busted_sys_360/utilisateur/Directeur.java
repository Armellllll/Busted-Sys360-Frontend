package com.bustedsys.busted_sys_360.utilisateur;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "directeurs")
public class Directeur extends Utilisateur {

    // Attributs spécifiques au Directeur si besoin plus tard

}