package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant){
		Participant existPart = participantService.findByLogin(participant.getLogin());
		if (existPart != null) {
			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}else{
			participantService.createNew(participant);
			return new ResponseEntity<>("User " + participant.getLogin() + " created", HttpStatus.CREATED);
		}
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login){
		Participant existPart = participantService.findByLogin(login);
		if (existPart == null) {
			return new ResponseEntity("Unable to delete. A participant with login " + login + " don't exist.", HttpStatus.CONFLICT);
		}else{
			participantService.delete(existPart);
			return new ResponseEntity<>("User " + login + " deleted", HttpStatus.CREATED);
		}
	}
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@RequestBody Participant participant){
		Participant existPart = participantService.findByLogin(participant.getLogin());
		if (existPart == null) {
			return new ResponseEntity("Unable to update. A participant with login " + participant.getLogin() + " don't exist.", HttpStatus.CONFLICT);
		}else{
			participantService.updatePart(participant);
			return new ResponseEntity<>("User " + participant.getLogin() + " updated", HttpStatus.CREATED);
		}
	}


}
