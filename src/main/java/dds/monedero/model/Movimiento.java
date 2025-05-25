package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private final LocalDate fecha;
  // Nota: En ningún lenguaje de programación usen jamás doubles (es decir, números con punto flotante) para modelar dinero en el mundo real.
  // En su lugar siempre usen numeros de precision arbitraria o punto fijo, como BigDecimal en Java y similares
  // De todas formas, NO es necesario modificar ésto como parte de este ejercicio. 
  private final double monto;
  private final boolean esDeposito;

  public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
    this.fecha = fecha;
    this.monto = monto;
    this.esDeposito = esDeposito;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean isDeposito() {
    return esDeposito;
  }

  public boolean esDelTipoYFecha(boolean tipoDeposito, LocalDate fecha) {
    return this.esDeposito == tipoDeposito && this.fecha.equals(fecha);
  }

  public boolean fueDepositado(LocalDate fecha) {
    return esDelTipoYFecha(true, fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return esDelTipoYFecha(false, fecha);
  }

}
