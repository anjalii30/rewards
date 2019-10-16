package com.rar.service.impl;

import com.rar.exception.InvalidUserException;
import com.rar.model.*;
import com.rar.repository.EvidencesRepository;
import com.rar.repository.ManagerRepository;
import com.rar.repository.NominationsRepository;
import com.rar.service.NominationsService;
//import com.rar.utils.CheckDisable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service
@Transactional
public class NominationsServiceImpl implements NominationsService {

    @Autowired
    NominationsRepository nominationsRepository;
    @Autowired
    EvidencesRepository evidencesRepository;

    @Autowired
    private ManagerRepository managerRepository;
    @Override
    public ResponseEntity<?> nominationSave(NominationPojo nominationPojo) {
        Nominations nominations = new Nominations();
        nominations.setUserID(nominationPojo.getUserId());
        nominations.setFrequency(nominationPojo.getFrequency());
        nominations.setRewardID(nominationPojo.getRewardId());
        nominations.setEnd_date(nominationPojo.getEnd_date());
        nominations.setStart_date(nominationPojo.getStart_date());
        nominations.setProject_name(nominationPojo.getProject_name());
        nominations.setSelected(nominationPojo.isSelected());
    //    nominations.setReward_name(nominationPojo.getReward_name());
        nominations.setEmployee_name(nominationPojo.getEmployee_name());
        nominations.setDisable(nominationPojo.isDisable());

        nominations = nominationsRepository.save(nominations);

        long nominationID = nominations.getNominationID();


        Evidences evidences = new Evidences();
        System.out.println(nominationPojo.getEvidencesPojoList().size());

        for (int i = 0; i < nominationPojo.getEvidencesPojoList().size(); i++) {
            evidences = new Evidences();

            evidences.setNominationID(nominationID);
            evidences.setCriteria_desc(nominationPojo.getEvidencesPojoList().get(i).getCriteria_desc());
            evidences.setEvidences(nominationPojo.getEvidencesPojoList().get(i).getEvidences());
            evidences.setText_evidence(nominationPojo.getEvidencesPojoList().get(i).getText_evidence());

            evidencesRepository.save(evidences);
        }


        HashMap<String, Object> s = new HashMap<>();
        s.put("evidences", evidences);
        s.put("nominations", nominations);
        Object returnValue = s;

        return ResponseEntity.ok(s);
    }

    @Override
    public List<Nominations> GetData(Long rewardID) {

        return  nominationsRepository.GetData(rewardID);
    }

    @Override
    public List<Nominations> showToManager(String manager_email) throws Exception {

        Long manager_id = managerRepository.findByEmail(manager_email);
        Long[] members = nominationsRepository.getMembers(manager_id);

            List<Nominations> getNominations = null;

                for (int i = 0; i < members.length; i++) {
                    System.out.println(members[i]);
                    getNominations=(nominationsRepository.getNominations(members[i]));
                }
        return getNominations;

    }
}
