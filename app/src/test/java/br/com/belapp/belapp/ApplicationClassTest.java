package br.com.belapp.belapp;

import org.junit.Test;

import static br.com.belapp.belapp.utils.CalculaDistanciaKt.calculaDistancia;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApplicationClassTest {

    @Test
    public void testCalculaDistancia() {
        double lat1 = -8;
        double lon1 = -36;
        double lat2 = -9;
        double lon2 = -37;

        double resultado = calculaDistancia(lat1,lon1,lat2,lon2);

        assertThat(resultado, is(156.39104297508643));
    }
}