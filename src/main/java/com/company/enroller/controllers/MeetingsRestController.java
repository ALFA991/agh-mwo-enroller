package com.company.enroller.controllers;

import java.security.DrbgParameters;
import java.util.Collection;
import java.util.Optional;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import java.util.Optional;

@RestController
@RequestMapping("/meetings")

public class MeetingsRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;





    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting){
        Meeting existMeeting = meetingService.findById(meeting.getId());
        if (existMeeting != null) {
            return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
        }else{
            meetingService.createNew(meeting);
            return new ResponseEntity<>("Meeting " + meeting.getTitle() + " created.", HttpStatus.CREATED);
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id){
        Meeting existMeeting = meetingService.findById(id);
        if (existMeeting == null) {
            return new ResponseEntity("Unable to delete. A meeting with id " + id + " doesn't exist.", HttpStatus.CONFLICT);
        }else{
            meetingService.delete(existMeeting);
            return new ResponseEntity<>("Meeting " + id + " deleted.", HttpStatus.CREATED);
        }
    }
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting){
        Meeting existMeeting = meetingService.findById(meeting.getId());
        if (existMeeting == null) {
            return new ResponseEntity("Unable to delete. A meeting with id " + meeting.getId() + " don't exist.", HttpStatus.CONFLICT);
        }else{
            meetingService.update(meeting);
            return new ResponseEntity<>("Meeting " + meeting.getId() + " updated.", HttpStatus.CREATED);
        }
    }
    @RequestMapping(value = "/{id}/{login}", method = RequestMethod.PUT)
    public ResponseEntity<?> addParticipant(@PathVariable long id,@PathVariable String login){
        Meeting existMeeting = meetingService.findById(id);
        Participant extPart = participantService.findByLogin(login);
        meetingService.addParticipant(existMeeting,extPart);
        if ((existMeeting == null) || (login == null)){
            return new ResponseEntity("Cannot add participant to the meeeting", HttpStatus.CONFLICT);
        }else{
            meetingService.addParticipant(existMeeting,extPart);
            return new ResponseEntity<>("Participant "+login+" added into the meeting " + existMeeting.getTitle(), HttpStatus.CREATED);
        }
    }
    @RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable long id,@PathVariable String login){
        Meeting existMeeting = meetingService.findById(id);
        Participant extPart = participantService.findByLogin(login);
        meetingService.addParticipant(existMeeting,extPart);

        if ((existMeeting != null) || (login != null)) {
            meetingService.deleteParticipant(existMeeting,extPart);
            return new ResponseEntity<>("Participant "+login+" deleted from the meeting " + existMeeting.getTitle(), HttpStatus.CREATED);
        }else{
            return new ResponseEntity("Cannot delete participant from the meeting.", HttpStatus.CONFLICT);
        }



    }

    @RequestMapping(value = "/getMP/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting != null) {
            return new ResponseEntity<>(meeting.getParticipants(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

}
