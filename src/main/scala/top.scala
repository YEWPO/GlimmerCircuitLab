import chisel3._
import chisel3.util._

object Top extends App {
  emitVerilog(new Top, Array("--target-dir", "generated"))
}

class Top extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
    val c = Input(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  val m1 = Module(new Bit4)
  val m2 = Module(new Bit4)
  val m3 = Module(new Bit4)
  val m4 = Module(new Bit4)

  m1.io.a := io.a(3, 0)
  m1.io.b := io.b(3, 0)
  m2.io.a := io.a(7, 4)
  m2.io.b := io.b(7, 4)
  m3.io.a := io.a(11, 8)
  m3.io.b := io.b(11, 8)
  m4.io.a := io.a(15, 12)
  m4.io.b := io.b(15, 12)

  val g = m4.io.g | (m4.io.p & m3.io.g) | (m4.io.p & m3.io.p & m2.io.g) | (m4.io.p & m3.io.p & m2.io.p & m1.io.g)
  val p = m4.io.p & m3.io.p & m2.io.p & m1.io.p

  io.cout := g | (p & io.c)
}

class Bit4 extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(4.W))
    val b = Input(UInt(4.W))
    val g = Output(UInt(1.W))
    val p = Output(UInt(1.W))
  })

  val m1 = Module(new Bit)
  val m2 = Module(new Bit)
  val m3 = Module(new Bit)
  val m4 = Module(new Bit)

  m1.io.a := io.a(0)
  m1.io.b := io.b(0)
  m2.io.a := io.a(1)
  m2.io.b := io.b(1)
  m3.io.a := io.a(2)
  m3.io.b := io.b(2)
  m4.io.a := io.a(3)
  m4.io.b := io.b(3)

  io.g := m4.io.g | (m4.io.p & m3.io.g) | (m4.io.p & m3.io.p & m2.io.g) | (m4.io.p & m3.io.p & m2.io.p & m1.io.g)
  io.p := m4.io.p & m3.io.p & m2.io.p & m1.io.p
}

class Bit extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val g = Output(UInt(1.W))
    val p = Output(UInt(1.W))
  })

  io.g := io.a & io.b
  io.p := io.a | io.b
}
