package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.SeqNumber;
import com.microservice.serviceB.repository.SeqRepository;
import com.microservice.serviceB.service.SequenceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    SeqRepository seqRepository;

    @Override
    public synchronized String getSequenceNumber(String seqPrefix) {
        Optional<SeqNumber> seqNumber = seqRepository.findById(seqPrefix);
        if(seqNumber.isPresent()) {
            int nextSeqNumber = seqNumber.get().getNextSeq() + 1;
            SeqNumber updateSeqNumber = seqNumber.get();
            updateSeqNumber.setNextSeq(nextSeqNumber);
            seqRepository.save(updateSeqNumber);
            return seqPrefix + StringUtils
                    .leftPad(String.valueOf(nextSeqNumber),
                            4, '0');
        }else {
            SeqNumber newSeqNumber = new SeqNumber();
            newSeqNumber.setSeqPrefix(seqPrefix);
            newSeqNumber.setNextSeq(1);
            seqRepository.save(newSeqNumber);
            return seqPrefix + StringUtils
                    .leftPad(String.valueOf(1),
                            4, '0');
        }
    }
}
