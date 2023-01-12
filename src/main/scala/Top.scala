import chisel3._
import chisel3.util._

class Top extends Module {
  val io = IO(new Bundle {
    val enable = Input(Bool())
    val in = Input(Vec(8, Bool()))
    val out = Output(UInt(3.W))
    val hex = Output(UInt(8.W))
  })

  val aribter = Module(new Aribter())
  val encoder = Module(new Encoder())
  val hexEncoder = Module(new HexEncoder())

  aribter.io.enable := io.enable
  aribter.io.in := io.in

  encoder.io.in := aribter.io.out
  
  hexEncoder.io.in := encoder.io.out
  hexEncoder.io.valid := aribter.io.valid

  io.out := encoder.io.out
  io.hex := hexEncoder.io.out
}

class Aribter extends Module {
  val io = IO(new Bundle {
    val enable = Input(Bool())
    val in = Input(Vec(8, Bool()))
    val out = Output(Vec(8, Bool()))
    val valid = Output(Bool())
  })

  val grant = VecInit.fill(8)(false.B)
  val notGrant = VecInit.fill(8)(false.B)

  when (io.enable) {
    grant(7) := io.in(7)
    notGrant(7) := !grant(7)
    for (i <- 6 to 0 by -1) {
      grant(i) := io.in(i) && notGrant(i + 1)
      notGrant(i) := !grant(i) && notGrant(i + 1)
    }
  }
  
  for (i <- 0 to 7) {
    io.out(i) := grant(i)
  }

  io.valid := !notGrant(0) && io.enable
}

class Encoder extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(8, Bool()))
    val out = Output(UInt(3.W))
  })

  val v = Wire(Vec(8, UInt(3.W)))
  v(0) := 0.U
  for (i <- 1 to 7) {
    v(i) := Mux(io.in(i), i.U, 0.U) | v(i - 1)
  }
  io.out := v(7)
}

class HexEncoder extends Module {
  val io = IO(new Bundle {
    val valid = Input(Bool())
    val in = Input(UInt(3.W))
    val out = Output(UInt(8.W))
  })

  io.out := "hbf".U
  when (io.valid) {
    switch (io.in) {
      is (0.U) {io.out := "hc0".U}
      is (1.U) {io.out := "hf9".U}
      is (2.U) {io.out := "ha4".U}
      is (3.U) {io.out := "hb0".U}
      is (4.U) {io.out := "h99".U}
      is (5.U) {io.out := "h92".U}
      is (6.U) {io.out := "h82".U}
      is (7.U) {io.out := "hf8".U}
    }
  }
}

object Top extends App {
  emitVerilog(new Top, Array("--target-dir", "generated"))
}
