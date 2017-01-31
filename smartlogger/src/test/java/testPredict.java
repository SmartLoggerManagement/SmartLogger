import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestGetWeights {
   @Test
   private Analyser analy = new Analyser();
   //Require erroneous Data
   private Collection<E> errData;
   //Require data logs for testing
   private Collection<E> data;
   //Efficiency testing
   private Collection<E> bigData;
   private Collection<E> smallData;
   @TODO
   public void testReceivedType() {
      
   }

   public void testTypeCorrespondance() {
      analy.predict(data);
   	assertTrue(true, analy.data instanceof Collection<E>);
   }

   public void testSentType() {
      analy.predict(data);
      assertTrue(true, analy.predict(data) instanceof RDD[double]);
   }

   public void testErrorData() {
      assertEquals(analy.predict)
   }

   public void testEfficiency() {
      long startTime = System.currentTimeMillis();
      analy.predict(smallData);
      long endTime = System.currentTimeMillis();
      System.out.println("Small Data patch predicting: " + (endTime - startTime) + " milliseconds");
      startTime = System.currentTimeMillis();
      analy.predict(data);
      endTime = System.currentTimeMillis();
      System.out.println("Average Data patch predicting: " + (endTime - startTime) + " milliseconds");
      startTime = System.currentTimeMillis();
      analy.predict(bigData);
      endTime = System.currentTimeMillis();
      System.out.println("Big Data patch predicting: " + (endTime - startTime) + " milliseconds");
   }
}