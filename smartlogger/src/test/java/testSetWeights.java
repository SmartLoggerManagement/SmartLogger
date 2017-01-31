import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestGetWeights {
   @Test
   private Analyser analy = new Analyser();
   //Require data logs for testing
   private Collection<E> data;
   @TODO

   public void testTypeCorrespondance() {
   	  analy.setWeigths(data);
   	  if (analy.weights == NULL) {
   	  	assertTrue(true, analy.weights instanceof Collection<E>);	
   	  }
   }

   public void testNullData() {
   	  analy.setWeights(NULL);
   	  assertFalse(analy.weights == NULL);
   }

   public void testSetChange() {
   	  analy.setWeights(data);
   	  assertEquals(data, analy.weights);
   }
}