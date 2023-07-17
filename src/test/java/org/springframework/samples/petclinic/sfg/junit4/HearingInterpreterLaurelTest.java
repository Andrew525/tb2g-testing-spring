package org.springframework.samples.petclinic.sfg.junit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.sfg.BaseConfig;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.LaurelConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

// junit 4
@ActiveProfiles("base-test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BaseConfig.class, LaurelConfig.class})
public class HearingInterpreterLaurelTest {

    @Autowired
    HearingInterpreter hearingInterpreter;

    // jUnit 4
//    @Before
//    public void setUp() throws Exception {
//        hearingInterpreter = new HearingInterpreter(new LaurelWordProducerImpl());
//    }

    @Test
    public void whatIHeard() {
        String word = hearingInterpreter.whatIHeard();

        assertThat(word).isEqualTo("Laurel");
    }
}