import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestGetWeights {
   @Test
   private Analyser analy = new Analyser();

   public void testNull() {
   	   assertFalse(analy.weights == NULL);
   }

   public void testSentType() {
      assertTrue(true, analy.getWeights() instanceof Collection<E>);
   }
}




