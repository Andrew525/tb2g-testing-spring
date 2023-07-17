package org.springframework.samples.petclinic.sfg.junit5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("externalized")
@TestPropertySource("classpath:yanny.properties")
@SpringJUnitConfig({PropertiesYannyTest.TestConfig.class})
class PropertiesYannyTest {


    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig { }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    public void whatIHeard() {
        String word = hearingInterpreter.whatIHeard();

        assertThat(word).isEqualTo("YaNNy");
        assertThat(word).isEqualToIgnoringCase("yanny");
    }
}