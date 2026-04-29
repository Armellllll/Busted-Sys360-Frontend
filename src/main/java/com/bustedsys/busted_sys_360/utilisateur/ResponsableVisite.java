package com.bustedsys.busted_sys_360.utilisateur;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "responsables_visite")
public class ResponsableVisite extends Utilisateur {

    // Attributs spécifiques au ResponsableVisite si besoin plus tard

}
