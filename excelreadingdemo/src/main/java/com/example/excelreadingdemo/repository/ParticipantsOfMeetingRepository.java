
package com.example.excelreadingdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.excelreadingdemo.modal.ParticipantOfMeeting;


@Repository
public interface ParticipantsOfMeetingRepository extends JpaRepository<ParticipantOfMeeting, Long> {

}
