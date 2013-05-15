import junit.framework.TestCase;

public class CalculadoraTest extends TestCase {
        
        private Calculadora calculadora;
        
        protected void setUp() throws Exception {
                super.setUp();
                calculadora = new Calculadora();
        }
        
        protected void tearDown() throws Exception {
                super.tearDown();
                calculadora = null;
        }
        
        public void testSoma() {
                assertEquals(15, calculadora.soma(10, 5));
                assertEquals(5, calculadora.soma(10, -5));
                assertEquals(-5, calculadora.soma(-10, 5));
                assertEquals(-15, calculadora.soma(-10, -5));
        }
        
        public void testSubtrai() {
                assertEquals(5, calculadora.subtrai(10, 5));
                assertEquals(15, calculadora.subtrai(10, -5));
                assertEquals(-15, calculadora.subtrai(-10, 5));
                assertEquals(-5, calculadora.subtrai(-10, -5));
        }
        
        public void testMultiplica() {
                assertEquals(50, calculadora.multiplica(10, 5));
                assertEquals(-50, calculadora.multiplica(10, -5));
                assertEquals(-50, calculadora.multiplica(-10, 5));
                assertEquals(50, calculadora.multiplica(-10, -5));
        }
        
        public void testDivide() {  
                assertEquals(2.0, calculadora.divide(10, 5));
                assertEquals(-2.0, calculadora.divide(10, -5));
                assertEquals(-2.0, calculadora.divide(-10, 5));
                assertEquals(2.0, calculadora.divide(-10, -5));
        }
        
}