package com.bustedsys.busted_sys_360.utilisateur;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "greffiers")
public class Greffier extends Utilisateur {

    // Attributs spécifiques au Greffier si besoin plus tard

}
