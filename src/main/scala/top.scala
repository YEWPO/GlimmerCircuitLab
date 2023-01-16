import chisel3._
import chisel3.util._

object top extends App {
  emitVerilog(new top, Array("--target-dir", "generated"))
}

class top extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(8.W))
  })

  io.out := RegNext(Cat(io.out(4) ^ io.out(3) ^ io.out(2) ^ io.out(0), io.out(7, 1)), 1.U(8.W))
}
