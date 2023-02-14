import chisel3._
import chisel3.util._

object Top extends App {
  emitVerilog(new Top, Array("--target-dir", "generated"))
}

class M2A extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  io.out := 0.U(8.W)
  switch (io.in) {
    is ("h16".U) { io.out := "h31".U }
    is ("h1e".U) { io.out := "h32".U }
    is ("h26".U) { io.out := "h33".U }
    is ("h25".U) { io.out := "h34".U }
    is ("h2e".U) { io.out := "h35".U }
    is ("h36".U) { io.out := "h36".U }
    is ("h3d".U) { io.out := "h37".U }
    is ("h3e".U) { io.out := "h38".U }
    is ("h46".U) { io.out := "h39".U }
    is ("h45".U) { io.out := "h30".U }
    is ("h1c".U) { io.out := "h61".U }
    is ("h32".U) { io.out := "h62".U }
    is ("h21".U) { io.out := "h63".U }
    is ("h23".U) { io.out := "h64".U }
    is ("h24".U) { io.out := "h65".U }
    is ("h2b".U) { io.out := "h66".U }
    is ("h34".U) { io.out := "h67".U }
    is ("h33".U) { io.out := "h68".U }
    is ("h43".U) { io.out := "h69".U }
    is ("h3b".U) { io.out := "h6a".U }
    is ("h42".U) { io.out := "h6b".U }
    is ("h4b".U) { io.out := "h6c".U }
    is ("h3a".U) { io.out := "h6d".U }
    is ("h31".U) { io.out := "h6e".U }
    is ("h44".U) { io.out := "h6f".U }
    is ("h4d".U) { io.out := "h70".U }
    is ("h15".U) { io.out := "h71".U }
    is ("h2d".U) { io.out := "h72".U }
    is ("h1b".U) { io.out := "h73".U }
    is ("h2c".U) { io.out := "h74".U }
    is ("h3c".U) { io.out := "h75".U }
    is ("h2a".U) { io.out := "h76".U }
    is ("h1d".U) { io.out := "h77".U }
    is ("h22".U) { io.out := "h78".U }
    is ("h35".U) { io.out := "h79".U }
    is ("h1a".U) { io.out := "h7a".U }
    is ("hf0".U) { io.out := "hf0".U }
  }
}

class HexEncoder extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(4.W))
    val out = Output(UInt(8.W))
  })

  io.out := "hff".U
  switch (io.in) {
    is (0.U) { io.out := "hc0".U }
    is (1.U) { io.out := "hf9".U }
    is (2.U) { io.out := "ha4".U }
    is (3.U) { io.out := "hb0".U }
    is (4.U) { io.out := "h99".U }
    is (5.U) { io.out := "h92".U }
    is (6.U) { io.out := "h82".U }
    is (7.U) { io.out := "hf8".U }
    is (8.U) { io.out := "h80".U }
    is (9.U) { io.out := "h90".U }
    is (10.U) { io.out := "h88".U }
    is (11.U) { io.out := "h83".U }
    is (12.U) { io.out := "hc6".U }
    is (13.U) { io.out := "ha1".U }
    is (14.U) { io.out := "h86".U }
    is (15.U) { io.out := "h8e".U }
  }
}

class Transplay extends Module {
  val io = IO(new Bundle {
    val mcode = Input(UInt(8.W))
    val ascii = Input(UInt(8.W))
    val count = Input(UInt(8.W))
    val out = Output(Vec(8, UInt(8.W)))
  })
}

class Top extends Module {
}
