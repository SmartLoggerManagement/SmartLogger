import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestGetWeights {
   @Test
   private Analyser analy;

   private void newAnaly() {
     analy = new Analyser();
   }

   public void testReceivedType() {
      assertTrue(true, newAnaly().getWeights() instanceof Collection<E>);
   }
}




