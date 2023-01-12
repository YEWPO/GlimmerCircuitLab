import chisel3._

class Top extends Module {
  val io = IO(new Bundle {
    val x = Input(Vec(4, UInt(2.W)))
    val y = Input(UInt(2.W))
    val f = Output(UInt(2.W))
  })

  io.f := io.x(io.y)
}

object Top extends App {
  emitVerilog(new Top, Array("--target-dir", "generated"))
}
