import chisel3._
import chisel3.util._

class top extends Module {
  val io = IO(new Bundle {
    val a = Input(SInt(4.W))
    val b = Input(SInt(4.W))
    val op = Input(UInt(3.W))
    val out = Output(SInt(4.W))
    val cf = Output(Bool())
    val zf = Output(Bool())
    val of = Output(Bool())
  })

  io.out := 0.S
  io.cf := false.B
  io.of := false.B
  io.zf := io.out === 0.S

  switch (io.op) {
    is (0.U) {
      val d = io.a +& io.b
      io.out := d
      io.cf := (io.a(3) ^ io.b(3) ^ d(4)) === 1.U
      io.of := (d(4) ^ d(3)) === 1.U
    }
    is (1.U) {
      val d = io.a -& io.b
      io.out := d
      io.cf := (io.a(3) ^ io.b(3) ^ d(4)) === 1.U
      io.of := (d(4) ^ d(3)) === 1.U
    }
    is (2.U) {
      io.out := ~io.a
    }
    is (3.U) {
      io.out := io.a & io.b
    }
    is (4.U) {
      io.out := io.a | io.b
    }
    is (5.U) {
      io.out := io.a ^ io.b
    }
    is (6.U) {
      val d = io.a -& io.b
      val f = d(4) === 1.U
      when (f) {
        io.out := 1.S
      }
    }
    is (7.U) {
      val d = (io.a ^ io.b) === 0.S
      when (d) {
        io.out := 1.S
      }
    }
  }
}

object top extends App {
  emitVerilog(new top, Array("--target-dir", "generated"))
}
