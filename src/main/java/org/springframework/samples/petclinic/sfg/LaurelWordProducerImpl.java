package org.springframework.samples.petclinic.sfg;

import org.springframework.stereotype.Component;

@Component
public class LaurelWordProducerImpl implements WordProducer {
    @Override
    public String getWord() {
        return "Laurel";
    }
}
