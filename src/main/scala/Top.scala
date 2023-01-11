import chisel3._

class Top extends Module {
  val io = IO(new Bundle {
  })
}

object Top extends App {
  emitVerilog(new Top, Array("--target-dir", "generated"))
}
