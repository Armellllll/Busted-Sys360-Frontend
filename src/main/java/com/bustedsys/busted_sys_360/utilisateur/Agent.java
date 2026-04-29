package com.bustedsys.busted_sys_360.utilisateur;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "agents")
public class Agent extends Utilisateur {

    // Attributs spécifiques à l'Agent si besoin plus tard

}
