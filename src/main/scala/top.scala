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
    val enable = Input(Bool())
    val out = Output(UInt(8.W))
  })

  io.out := "hff".U
  when (io.enable) {
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
}

class Transplay extends Module {
  val io = IO(new Bundle {
    val mcode = Input(UInt(8.W))
    val ascii = Input(UInt(8.W))
    val count = Input(UInt(8.W))
    val enable = Input(Bool())
    val out = Output(Vec(8, UInt(8.W)))
  })
  
  val mcodel = Module(new HexEncoder)
  val mcodeh = Module(new HexEncoder)
  mcodel.io.in := io.mcode(3, 0)
  mcodeh.io.in := io.mcode(7, 4)
  mcodel.io.enable := io.enable
  mcodeh.io.enable := io.enable
  io.out(0) := mcodel.io.out
  io.out(1) := mcodeh.io.out

  io.out(2) := "hff".U

  val asciil = Module(new HexEncoder)
  val asciih = Module(new HexEncoder)
  asciil.io.in := io.ascii(3, 0)
  asciih.io.in := io.ascii(7, 4)
  asciil.io.enable := io.enable
  asciih.io.enable := io.enable
  io.out(3) := asciil.io.out
  io.out(4) := asciih.io.out

  io.out(5) := "hff".U

  val countl = Module(new HexEncoder)
  val counth = Module(new HexEncoder)
  countl.io.in := io.count(3, 0)
  counth.io.in := io.count(7, 4)
  countl.io.enable := true.B
  counth.io.enable := true.B
  io.out(6) := countl.io.out
  io.out(7) := counth.io.out
}

class PS2K extends Module {
  val io = IO(new Bundle {
    val ps2_clk = Input(UInt(1.W))
    val ps2_data = Input(UInt(1.W))
    val out_data = Output(UInt(8.W))
    val out_total = Output(UInt(8.W))
    val out_enable = Output(Bool())
  })

  val ps2_clk_sync = RegInit(0.U(3.W))
  val buffer = RegInit(0.U(10.W))
  val count = RegInit(0.U(4.W))
  val total = RegInit(0.U(8.W))
  val data_buffer = RegInit(VecInit(Seq.fill(3)(0.U(8.W))))
  val enable = RegInit(false.B);

  ps2_clk_sync := Cat(ps2_clk_sync(1, 0), io.ps2_clk)

  val sampling = ps2_clk_sync(2) & (~ps2_clk_sync(1))

  when (sampling === 1.U) {
    when (count === 10.U) {
      when (buffer(0) === 0.U && io.ps2_data === 1.U && buffer(9, 1).xorR) {
        when (data_buffer(0) =/= "hf0".U && buffer(8, 1) =/= "hf0".U) {
          enable := true.B
        }.otherwise {
          enable := false.B
        }
        when (buffer(8, 1) === "hf0".U) {
          total := total + 1.U
        }
        data_buffer(2) := data_buffer(1)
        data_buffer(1) := data_buffer(0)
        data_buffer(0) := buffer(8, 1)
      }
      count := 0.U
      buffer := 0.U
    }.otherwise {
      buffer := buffer | (io.ps2_data << count)
      count := count + 1.U
    }
  }

  io.out_enable := enable
  io.out_data := data_buffer(0)
  io.out_total := total
}

class Top extends Module {
  val io = IO(new Bundle {
    val ps2_clk = Input(UInt(1.W))
    val ps2_data = Input(UInt(1.W))
    val out = Output(Vec(8, UInt(8.W)))
  })

  val input_module = Module(new PS2K)
  val rom = Module(new M2A)
  val dis = Module(new Transplay)

  input_module.io.ps2_clk := io.ps2_clk
  input_module.io.ps2_data := io.ps2_data

  rom.io.in := input_module.io.out_data

  dis.io.mcode := input_module.io.out_data
  dis.io.ascii := rom.io.out
  dis.io.count := input_module.io.out_total
  dis.io.enable := input_module.io.out_enable

  io.out := dis.io.out
}
