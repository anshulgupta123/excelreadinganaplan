
package com.example.excelreadingdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.excelreadingdemo.modal.MeetingDetails;

@Repository
public interface MeetingDetailsRepository extends JpaRepository<MeetingDetails, String> {

}
