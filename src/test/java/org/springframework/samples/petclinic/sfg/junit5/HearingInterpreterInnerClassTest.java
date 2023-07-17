package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.LaurelWordProducerImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("inner-class")
@SpringJUnitConfig({HearingInterpreterInnerClassTest.TestConfig.class})
class HearingInterpreterInnerClassTest {

    @Profile("inner-class")
    @Configuration // it will work as well without this annotation
    static class TestConfig {
        @Bean
        HearingInterpreter hearingInterpreter() {
            return new HearingInterpreter(new LaurelWordProducerImpl());
        }
    }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    public void whatIHeard() {
        String word = hearingInterpreter.whatIHeard();

        assertThat(word).isEqualTo("Laurel");
    }
}