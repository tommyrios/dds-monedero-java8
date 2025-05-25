package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private static final double limiteDiario = 1000;
  private static final int limiteExtracciones = 3;
  private final List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    this.saldo = 0;
  }

  public void poner(double cuanto) {
    validarMontoPositivo(cuanto);
    validarLimiteExtracciones();
    validarLimiteDiario(cuanto);

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar(double cuanto) {

    validarMontoPositivo(cuanto);

    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }

    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    var movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return List.copyOf(movimientos);
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  public void validarMontoPositivo(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto+ ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarLimiteDiario(double monto) {
    var montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    var limite = limiteDiario - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + limiteDiario + " diarios, " + "límite: " + limite);
    }
  }

  public void validarLimiteExtracciones() {
    if (getMovimientos().stream()
        .filter(movimiento -> movimiento.fueDepositado(LocalDate.now()))
        .count() >= limiteExtracciones) {
        throw new MaximaCantidadDepositosException("Ya excedio los " + limiteExtracciones + " depositos diarios");
    }
  }
}
