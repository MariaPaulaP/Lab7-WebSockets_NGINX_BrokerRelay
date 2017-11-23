/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.restcontrollers;

import edu.eci.arsw.collabhangman.persistence.PersistenceException;
import edu.eci.arsw.collabhangman.services.GameServices;
import edu.eci.arsw.collabhangman.services.GameServicesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping("/users")
public class UsersResourceController {

    @Autowired
    GameServices gameServices;

    @RequestMapping(path = "/{idUsuario}", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentWord(@PathVariable Integer idUsuario) throws PersistenceException {
        try {
            gameServices.getPuntajeMaximo(idUsuario);
            return new ResponseEntity<>(gameServices.loadUserData(idUsuario), HttpStatus.ACCEPTED);
        } catch (GameServicesException ex) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/score/{score}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserWithScoreMoreThan(@PathVariable Integer score) {
        try {
            return new ResponseEntity<>(gameServices.getUsuarioConPuntajeMas(score), HttpStatus.ACCEPTED);
        } catch (PersistenceException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
