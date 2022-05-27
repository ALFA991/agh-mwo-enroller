package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Optional;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component("meetingService")
public class MeetingService {
	ParticipantService participantService;
	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}
	public Meeting findById(long id){
		String hql = "FROM Meeting m WHERE m.id ='"+id+"'";
		Query<Meeting> query = connector.getSession().createQuery(hql);
		return query.uniqueResult();
	}
	public Meeting findByTitle(String title){
		String hql = "FROM Meeting m WHERE m.title ='"+title+"'";
		Query<Meeting> query = connector.getSession().createQuery(hql);
		return query.uniqueResult();
	}
	public void createNew(Meeting meeting){
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}
	public void delete(Meeting meeting){
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}
	public void update(Meeting meeting){

		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}
	public void addParticipant(Meeting meeting,Participant participant){
		meeting.getParticipants().add(participant);
		update(meeting);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}
	public void deleteParticipant(Meeting meeting,Participant participant){
		meeting.getParticipants().remove(participant);
		update(meeting);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}


}
