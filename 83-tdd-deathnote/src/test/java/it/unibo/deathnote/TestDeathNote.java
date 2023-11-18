package it.unibo.deathnote;

import static java.lang.Thread.sleep;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.SimpleDeathNote;

class TestDeathNote {

    private static final int NEGATIVE_NUMBER = -1;
    private static final int ZERO = 0;
    private static final String FIRST_VICTIM = "Harry Potter";
    private static final String SECOND_VICTIM = "Gianmarcus Puonds";
    private static final String EMPTY_STRING = "";
    private static final String RANDOM_DEATH_CAUSE = "Eaten by a dog";
    private static final String RANDOM_DEATH_DETAILS = "Triying to escape fromd dogs while cycling in Ciola Araldi";
    private static final long CAUSE_OF_DEATH_INVALID_TIME = 100L;

    DeathNote kirasDeathnote;
    /**
     * Configuration step, inizialization of a new SimpleDeathnote 
     * implementing the DeathNote interface
     */
    @BeforeEach
    public void Inizialization(){
        kirasDeathnote = new SimpleDeathNote();
    }

    /**
     * Rule number 0 and negative rules do not exist in the DeathNote rules
     */
    @Test
    public void testBadIndexRule() {
        try{
            kirasDeathnote.getRule(ZERO);
            Assertions.fail();
        }catch(IllegalArgumentException e){
            Assertions.assertEquals("Rule 0 doesn't exist, Rule's list starts from 1",e.getMessage());
        } 

        try{
            kirasDeathnote.getRule(NEGATIVE_NUMBER);
            Assertions.fail();
        }catch(IllegalArgumentException e){
            Assertions.assertEquals("Rules number can't be a negative number or zero, Rule's list starts from 1",e.getMessage());
        } 
    }

    /**
     * No rule is empty or null in the DeathNote rules.
     */
    @Test
    public void testRules(){
        for(String rule : DeathNote.RULES){
            Assertions.assertNotNull(rule);
            Assertions.assertFalse(rule.isBlank());
        }
    }

    /**
     * The human whose name is written in the DeathNote will eventually die.
     */
    @Test
    public void testDeath(){
        Assertions.assertFalse(kirasDeathnote.isNameWritten(FIRST_VICTIM));
        kirasDeathnote.writeName(FIRST_VICTIM);
        Assertions.assertTrue(kirasDeathnote.isNameWritten(FIRST_VICTIM));
        Assertions.assertFalse(kirasDeathnote.isNameWritten(SECOND_VICTIM));
        Assertions.assertFalse(kirasDeathnote.isNameWritten(EMPTY_STRING));
    }

    /** 
     * Only if the cause of death is written within the next 40 milliseconds 
     * of writing the person's name, it will happen. 
     * @throws InterruptedException
     * */
    @Test public void testDeathCause() throws InterruptedException{
        try {
            kirasDeathnote.writeDeathCause(RANDOM_DEATH_CAUSE);
            Assertions.fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("No name written in the deathnote yet!", e.getMessage());
        }

        kirasDeathnote.writeName(FIRST_VICTIM);
        Assertions.assertEquals("heart attack",kirasDeathnote.getDeathCause(FIRST_VICTIM));
        kirasDeathnote.writeName(SECOND_VICTIM);
        Assertions.assertTrue(kirasDeathnote.writeDeathCause("karting incident"));
        Assertions.assertTrue(kirasDeathnote.isNameWritten(SECOND_VICTIM));
        Assertions.assertEquals(kirasDeathnote.getDeathCause(SECOND_VICTIM),"karting incident");

        Thread.sleep(CAUSE_OF_DEATH_INVALID_TIME);

        Assertions.assertFalse(kirasDeathnote.writeDeathCause(RANDOM_DEATH_CAUSE));
        Assertions.assertEquals(kirasDeathnote.getDeathCause(SECOND_VICTIM),"karting incident");
    }

    /**
     * Only if the cause of death is written within the next 6 seconds and 40 milliseconds
     *  of writing the death's details, it will happen.
     * @throws InterruptedException
     */

    @Test public void testDeathDetails() throws InterruptedException{
        try {
            kirasDeathnote.writeDetails(RANDOM_DEATH_DETAILS);
            Assertions.fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("No name written in the deathnote yet!", e.getMessage());
        }
        kirasDeathnote.writeName(FIRST_VICTIM);
        Assertions.assertEquals(EMPTY_STRING,kirasDeathnote.getDeathDetails(FIRST_VICTIM));
        Assertions.assertTrue(kirasDeathnote.writeDetails("ran for too long"));
        Assertions.assertEquals("ran for too long",kirasDeathnote.getDeathDetails(FIRST_VICTIM));
        kirasDeathnote.writeName(SECOND_VICTIM);
        sleep(6100L);
        Assertions.assertFalse(kirasDeathnote.writeDetails(RANDOM_DEATH_DETAILS));
        Assertions.assertEquals(EMPTY_STRING, kirasDeathnote.getDeathDetails(SECOND_VICTIM));
    }
}