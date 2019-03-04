package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.Answer;
import com.pa.march.paquestserver.domain.Congratulation;
import com.pa.march.paquestserver.message.resource.AnswerResource;
import com.pa.march.paquestserver.message.resource.CongratulationResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CongratuationToResourceConverter extends ConversionServiceAwareConverter<Congratulation, CongratulationResource> {

    @Override
    public CongratulationResource convert(Congratulation congratulation) {
        log.debug("congratulation={}", congratulation);
        CongratulationResource congratulationResource = new CongratulationResource();
        congratulationResource.setId(congratulation.getId());
        congratulationResource.setVideoId(congratulation.getVideoId());

        return congratulationResource;
    }
}
