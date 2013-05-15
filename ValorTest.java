import static org.junit.Assert.*;
import org.junit.Test;

public class ValorTest {

    @Test
    public void testAdiciona() {
        Valor v1 = new Valor();
        v1.setValor(1);
        v1.adiciona(2);
        assertEquals(3, v1.getValor());
    }
}